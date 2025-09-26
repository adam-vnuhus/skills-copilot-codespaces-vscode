package com.vnuhus.signaturerouting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request DTO for new API format (SAVIS)
 */
public class NewApiRequest {
    
    @JsonProperty("request_id")
    private String requestId;
    
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("app_code")
    private String appCode;
    
    @JsonProperty("using_cert_default")
    private Boolean usingCertDefault;
    
    @JsonProperty("tran_id")
    private String tranId;
    
    @JsonProperty("user_cert_default")
    private Boolean userCertDefault;
    
    private Object cert;
    
    private Credential credential;
    
    private List<HashData> hashs;
    
    @JsonProperty("callback_sign_result")
    private CallbackSignResult callbackSignResult;
    
    public static class Credential {
        @JsonProperty("access_token")
        private Boolean accessToken;
        
        // Getters and Setters
        public Boolean getAccessToken() { return accessToken; }
        public void setAccessToken(Boolean accessToken) { this.accessToken = accessToken; }
    }
    
    public static class HashData {
        @JsonProperty("hash_id")
        private String hashId;
        
        @JsonProperty("hash_name")
        private String hashName;
        
        @JsonProperty("hash_value")
        private String hashValue;
        
        // Getters and Setters
        public String getHashId() { return hashId; }
        public void setHashId(String hashId) { this.hashId = hashId; }
        
        public String getHashName() { return hashName; }
        public void setHashName(String hashName) { this.hashName = hashName; }
        
        public String getHashValue() { return hashValue; }
        public void setHashValue(String hashValue) { this.hashValue = hashValue; }
    }
    
    public static class CallbackSignResult {
        @JsonProperty("active_callback")
        private String activeCallback;
        
        @JsonProperty("http_endpoint")
        private String httpEndpoint;
        
        @JsonProperty("http_method")
        private String httpMethod;
        
        // Getters and Setters
        public String getActiveCallback() { return activeCallback; }
        public void setActiveCallback(String activeCallback) { this.activeCallback = activeCallback; }
        
        public String getHttpEndpoint() { return httpEndpoint; }
        public void setHttpEndpoint(String httpEndpoint) { this.httpEndpoint = httpEndpoint; }
        
        public String getHttpMethod() { return httpMethod; }
        public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    }
    
    // Main getters and setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getAppCode() { return appCode; }
    public void setAppCode(String appCode) { this.appCode = appCode; }
    
    public Boolean getUsingCertDefault() { return usingCertDefault; }
    public void setUsingCertDefault(Boolean usingCertDefault) { this.usingCertDefault = usingCertDefault; }
    
    public String getTranId() { return tranId; }
    public void setTranId(String tranId) { this.tranId = tranId; }
    
    public Boolean getUserCertDefault() { return userCertDefault; }
    public void setUserCertDefault(Boolean userCertDefault) { this.userCertDefault = userCertDefault; }
    
    public Object getCert() { return cert; }
    public void setCert(Object cert) { this.cert = cert; }
    
    public Credential getCredential() { return credential; }
    public void setCredential(Credential credential) { this.credential = credential; }
    
    public List<HashData> getHashs() { return hashs; }
    public void setHashs(List<HashData> hashs) { this.hashs = hashs; }
    
    public CallbackSignResult getCallbackSignResult() { return callbackSignResult; }
    public void setCallbackSignResult(CallbackSignResult callbackSignResult) { this.callbackSignResult = callbackSignResult; }
}