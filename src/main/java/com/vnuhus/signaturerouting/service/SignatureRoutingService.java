package com.vnuhus.signaturerouting.service;

import com.vnuhus.signaturerouting.dto.*;
import com.vnuhus.signaturerouting.model.Routing;
import com.vnuhus.signaturerouting.repository.RoutingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for handling signature routing logic
 */
@Service
@Transactional
public class SignatureRoutingService {
    
    private static final Logger logger = LoggerFactory.getLogger(SignatureRoutingService.class);
    
    private final RoutingRepository routingRepository;
    private final HttpClientService httpClientService;
    
    public SignatureRoutingService(RoutingRepository routingRepository, HttpClientService httpClientService) {
        this.routingRepository = routingRepository;
        this.httpClientService = httpClientService;
    }
    
    /**
     * Process signature hash request based on routing configuration
     * 
     * @param request incoming signature hash request
     * @return processed response
     */
    public HttpClientService.HttpResponse<SignatureHashResponse> processSignatureHash(SignatureHashRequest request) {
        String endpoint = "/external/v1/routing/employee/signature/sign_hashs";
        String channel = request.getMetaData().getChannel();
        
        logger.debug("Processing signature hash request for channel: {}", channel);
        
        // Step 1: Get routing data from database
        Optional<Routing> routingOpt = routingRepository.findByRouteInAndCaCode(endpoint, channel);
        
        if (routingOpt.isEmpty()) {
            logger.error("No routing configuration found for endpoint: {} and channel: {}", endpoint, channel);
            return createErrorResponse("1", "Không tìm thấy cấu hình routing cho channel: " + channel);
        }
        
        Routing routing = routingOpt.get();
        logger.debug("Found routing configuration: {} -> {} (version: {})", 
                routing.getRouteIn(), routing.getRouteOut(), routing.getRouteVersion());
        
        // Step 2: Process based on routing version
        if ("old".equals(routing.getRouteVersion())) {
            return processOldApiCall(request, routing);
        } else if ("new".equals(routing.getRouteVersion())) {
            return processNewApiCall(request, routing);
        } else {
            logger.error("Unknown route version: {}", routing.getRouteVersion());
            return createErrorResponse("1", "Phiên bản routing không hợp lệ: " + routing.getRouteVersion());
        }
    }
    
    /**
     * Process call to old API (for VIETTEL, VNPT)
     */
    private HttpClientService.HttpResponse<SignatureHashResponse> processOldApiCall(
            SignatureHashRequest request, Routing routing) {
        
        logger.debug("Processing old API call to: {}", routing.getRouteOut());
        
        // Forward request as-is to old API
        HttpClientService.HttpResponse<SignatureHashResponse> response = 
                httpClientService.post(routing.getRouteOut(), request, SignatureHashResponse.class);
        
        logger.debug("Old API response - HTTP: {}", response.getHttpCode());
        return response;
    }
    
    /**
     * Process call to new API (for SAVIS) with message transformation
     */
    private HttpClientService.HttpResponse<SignatureHashResponse> processNewApiCall(
            SignatureHashRequest request, Routing routing) {
        
        logger.debug("Processing new API call to: {}", routing.getRouteOut());
        
        // Transform request to new API format
        NewApiRequest newApiRequest = transformToNewApiRequest(request);
        
        // Call new API
        HttpClientService.HttpResponse<NewApiResponse> apiResponse = 
                httpClientService.post(routing.getRouteOut(), newApiRequest, NewApiResponse.class);
        
        // Transform response back to standard format
        SignatureHashResponse standardResponse = transformToStandardResponse(apiResponse);
        
        logger.debug("New API response - HTTP: {}, Success: {}", 
                apiResponse.getHttpCode(), 
                apiResponse.getPayload() != null ? apiResponse.getPayload().getSuccess() : null);
        
        return new HttpClientService.HttpResponse<>(apiResponse.getHttpCode(), standardResponse);
    }
    
    /**
     * Transform incoming request to new API format
     */
    private NewApiRequest transformToNewApiRequest(SignatureHashRequest request) {
        NewApiRequest newRequest = new NewApiRequest();
        
        // Basic fields mapping
        newRequest.setRequestId(request.getMetaData().getRequestId());
        newRequest.setUserId(request.getData().getUserId());
        newRequest.setAppCode(request.getMetaData().getAppCode());
        newRequest.setUsingCertDefault(true);
        newRequest.setTranId(null);
        newRequest.setUserCertDefault(true);
        newRequest.setCert(null);
        
        // Credential mapping
        NewApiRequest.Credential credential = new NewApiRequest.Credential();
        credential.setAccessToken(request.getData().getAccessToken());
        newRequest.setCredential(credential);
        
        // Hash data mapping - preserve order
        List<NewApiRequest.HashData> newHashList = new ArrayList<>();
        for (SignatureHashRequest.Data.HashData hash : request.getData().getHashs()) {
            NewApiRequest.HashData newHash = new NewApiRequest.HashData();
            newHash.setHashId(hash.getHashId());
            newHash.setHashName(hash.getHashName());
            newHash.setHashValue(hash.getHashValue());
            newHashList.add(newHash);
        }
        newRequest.setHashs(newHashList);
        
        // Callback configuration
        if (request.getData().getWebhook() != null) {
            NewApiRequest.CallbackSignResult callback = new NewApiRequest.CallbackSignResult();
            callback.setActiveCallback("yes");
            callback.setHttpEndpoint(request.getData().getWebhook());
            callback.setHttpMethod("POST");
            newRequest.setCallbackSignResult(callback);
        }
        
        return newRequest;
    }
    
    /**
     * Transform new API response to standard format
     */
    private SignatureHashResponse transformToStandardResponse(HttpClientService.HttpResponse<NewApiResponse> apiResponse) {
        SignatureHashResponse response = new SignatureHashResponse();
        
        if (apiResponse.getPayload() != null && Boolean.TRUE.equals(apiResponse.getPayload().getSuccess())) {
            // Success case
            response.setErrorCode("0");
            response.setErrorDesc("Thành công");
            
            SignatureHashResponse.ResponseData data = new SignatureHashResponse.ResponseData();
            NewApiResponse payload = apiResponse.getPayload();
            
            if (payload.getTranInfo() != null) {
                data.setTraceId(payload.getTranInfo().getEsignTransId());
                data.setTransactionId(payload.getTranInfo().getCaTraceId());
                data.setWaitingTimeConfirm(payload.getTranInfo().getExpiresIn());
            }
            
            if (payload.getCustodyInfo() != null) {
                data.setChannel(payload.getCustodyInfo().getCode());
                if (payload.getCustodyInfo().getAuthConfig() != null) {
                    data.setClientId(payload.getCustodyInfo().getAuthConfig().getClientId());
                }
            }
            
            response.setData(data);
        } else {
            // Error case
            response.setErrorCode("1");
            response.setErrorDesc("Xảy ra lỗi khi lấy thông tin chứng thư số");
        }
        
        return response;
    }
    
    /**
     * Create error response
     */
    private HttpClientService.HttpResponse<SignatureHashResponse> createErrorResponse(String errorCode, String errorDesc) {
        SignatureHashResponse response = new SignatureHashResponse();
        response.setErrorCode(errorCode);
        response.setErrorDesc(errorDesc);
        
        return new HttpClientService.HttpResponse<>(400, response);
    }
}