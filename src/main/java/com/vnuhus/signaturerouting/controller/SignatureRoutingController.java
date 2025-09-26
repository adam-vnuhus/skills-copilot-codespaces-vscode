package com.vnuhus.signaturerouting.controller;

import com.vnuhus.signaturerouting.dto.SignatureHashRequest;
import com.vnuhus.signaturerouting.dto.SignatureHashResponse;
import com.vnuhus.signaturerouting.service.HttpClientService;
import com.vnuhus.signaturerouting.service.SignatureRoutingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for signature routing endpoints
 */
@RestController
@RequestMapping("/external/v1/routing/employee/signature")
public class SignatureRoutingController {
    
    private static final Logger logger = LoggerFactory.getLogger(SignatureRoutingController.class);
    
    private final SignatureRoutingService signatureRoutingService;
    
    public SignatureRoutingController(SignatureRoutingService signatureRoutingService) {
        this.signatureRoutingService = signatureRoutingService;
    }
    
    /**
     * Endpoint for signature hash signing routing
     * 
     * @param request signature hash request
     * @return routing response
     */
    @PostMapping("/sign_hashs")
    public ResponseEntity<SignatureHashResponse> signHashs(@Valid @RequestBody SignatureHashRequest request) {
        
        logger.info("Received signature hash request for channel: {}, user: {}", 
                request.getMetaData().getChannel(), 
                request.getData().getUserId());
        
        try {
            HttpClientService.HttpResponse<SignatureHashResponse> result = 
                    signatureRoutingService.processSignatureHash(request);
            
            logger.info("Signature hash processing completed with HTTP code: {}", result.getHttpCode());
            
            return ResponseEntity.status(result.getHttpCode())
                    .body(result.getPayload());
                    
        } catch (Exception ex) {
            logger.error("Error processing signature hash request", ex);
            
            SignatureHashResponse errorResponse = new SignatureHashResponse();
            errorResponse.setErrorCode("1");
            errorResponse.setErrorDesc("Lỗi hệ thống: " + ex.getMessage());
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Signature Routing Service is running");
    }
}