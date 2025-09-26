package com.vnuhus.signaturerouting.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Service for making HTTP calls to external APIs
 */
@Service
public class HttpClientService {
    
    private final WebClient webClient;
    
    @Value("${spring.webflux.httpclient.connect-timeout:30000}")
    private int connectTimeout;
    
    @Value("${spring.webflux.httpclient.response-timeout:60000}")
    private int responseTimeout;
    
    public HttpClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    /**
     * Make POST request to external service
     * 
     * @param url target URL
     * @param requestBody request payload
     * @param responseType expected response type
     * @return HTTP response wrapper
     */
    public <T, R> HttpResponse<R> post(String url, T requestBody, Class<R> responseType) {
        try {
            R responseBody = webClient
                    .post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(responseType)
                    .timeout(Duration.ofMillis(responseTimeout))
                    .block();
            
            return new HttpResponse<>(HttpStatus.OK.value(), responseBody);
            
        } catch (WebClientResponseException ex) {
            // Return error response with actual HTTP status and body
            try {
                @SuppressWarnings("unchecked")
                R errorBody = (R) ex.getResponseBodyAsString();
                return new HttpResponse<>(ex.getStatusCode().value(), errorBody);
            } catch (Exception e) {
                return new HttpResponse<>(ex.getStatusCode().value(), null);
            }
            
        } catch (Exception ex) {
            // Handle other exceptions (timeout, network issues, etc.)
            return new HttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
    
    /**
     * Wrapper class for HTTP response
     */
    public static class HttpResponse<T> {
        private final int httpCode;
        private final T payload;
        
        public HttpResponse(int httpCode, T payload) {
            this.httpCode = httpCode;
            this.payload = payload;
        }
        
        public int getHttpCode() { return httpCode; }
        public T getPayload() { return payload; }
        
        public boolean isSuccessful() {
            return httpCode >= 200 && httpCode < 300;
        }
    }
}