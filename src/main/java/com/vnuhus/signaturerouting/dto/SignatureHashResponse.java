package com.vnuhus.signaturerouting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for signature hash signing
 */
public class SignatureHashResponse {
    
    @JsonProperty("errorCode")
    private String errorCode;
    
    @JsonProperty("errorDesc")
    private String errorDesc;
    
    private ResponseData data;
    
    public static class ResponseData {
        @JsonProperty("traceId")
        private String traceId;
        
        @JsonProperty("transactionId")
        private String transactionId;
        
        private String channel;
        
        @JsonProperty("clientId")
        private String clientId;
        
        @JsonProperty("waiting_time_confirm")
        private String waitingTimeConfirm;
        
        // Getters and Setters
        public String getTraceId() { return traceId; }
        public void setTraceId(String traceId) { this.traceId = traceId; }
        
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        
        public String getChannel() { return channel; }
        public void setChannel(String channel) { this.channel = channel; }
        
        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }
        
        public String getWaitingTimeConfirm() { return waitingTimeConfirm; }
        public void setWaitingTimeConfirm(String waitingTimeConfirm) { this.waitingTimeConfirm = waitingTimeConfirm; }
    }
    
    // Main getters and setters
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getErrorDesc() { return errorDesc; }
    public void setErrorDesc(String errorDesc) { this.errorDesc = errorDesc; }
    
    public ResponseData getData() { return data; }
    public void setData(ResponseData data) { this.data = data; }
}