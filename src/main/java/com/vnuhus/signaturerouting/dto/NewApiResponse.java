package com.vnuhus.signaturerouting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for new API format (SAVIS)
 */
public class NewApiResponse {
    
    private Boolean success;
    
    @JsonProperty("tran_info")
    private TranInfo tranInfo;
    
    @JsonProperty("custody_info")
    private CustodyInfo custodyInfo;
    
    public static class TranInfo {
        @JsonProperty("esign_trans_id")
        private String esignTransId;
        
        @JsonProperty("ca_trace_id")
        private String caTraceId;
        
        @JsonProperty("expires_in")
        private String expiresIn;
        
        // Getters and Setters
        public String getEsignTransId() { return esignTransId; }
        public void setEsignTransId(String esignTransId) { this.esignTransId = esignTransId; }
        
        public String getCaTraceId() { return caTraceId; }
        public void setCaTraceId(String caTraceId) { this.caTraceId = caTraceId; }
        
        public String getExpiresIn() { return expiresIn; }
        public void setExpiresIn(String expiresIn) { this.expiresIn = expiresIn; }
    }
    
    public static class CustodyInfo {
        private String code;
        
        @JsonProperty("auth_config")
        private AuthConfig authConfig;
        
        public static class AuthConfig {
            @JsonProperty("clientId")
            private String clientId;
            
            // Getters and Setters
            public String getClientId() { return clientId; }
            public void setClientId(String clientId) { this.clientId = clientId; }
        }
        
        // Getters and Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        
        public AuthConfig getAuthConfig() { return authConfig; }
        public void setAuthConfig(AuthConfig authConfig) { this.authConfig = authConfig; }
    }
    
    // Main getters and setters
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    
    public TranInfo getTranInfo() { return tranInfo; }
    public void setTranInfo(TranInfo tranInfo) { this.tranInfo = tranInfo; }
    
    public CustodyInfo getCustodyInfo() { return custodyInfo; }
    public void setCustodyInfo(CustodyInfo custodyInfo) { this.custodyInfo = custodyInfo; }
}