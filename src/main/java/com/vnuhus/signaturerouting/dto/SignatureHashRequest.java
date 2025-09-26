package com.vnuhus.signaturerouting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for signature hash signing
 */
public class SignatureHashRequest {
    
    @Valid
    @NotNull
    @JsonProperty("meta_data")
    private MetaData metaData;
    
    @Valid
    @NotNull
    private Data data;
    
    public static class MetaData {
        @NotBlank
        @JsonProperty("request_id")
        private String requestId;
        
        @NotBlank
        @JsonProperty("app_code")
        private String appCode;
        
        @NotBlank
        private String channel;
        
        @JsonProperty("sign_pack")
        private String signPack;
        
        // Getters and Setters
        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
        
        public String getAppCode() { return appCode; }
        public void setAppCode(String appCode) { this.appCode = appCode; }
        
        public String getChannel() { return channel; }
        public void setChannel(String channel) { this.channel = channel; }
        
        public String getSignPack() { return signPack; }
        public void setSignPack(String signPack) { this.signPack = signPack; }
    }
    
    public static class Data {
        @NotBlank
        @JsonProperty("user_id")
        private String userId;
        
        @JsonProperty("access_token")
        private Boolean accessToken;
        
        @JsonProperty("credential_id")
        private String credentialId;
        
        @NotNull
        private List<HashData> hashs;
        
        private String webhook;
        
        public static class HashData {
            @JsonProperty("hash_value")
            private String hashValue;
            
            @JsonProperty("hash_id")
            private String hashId;
            
            @JsonProperty("hash_name")
            private String hashName;
            
            // Getters and Setters
            public String getHashValue() { return hashValue; }
            public void setHashValue(String hashValue) { this.hashValue = hashValue; }
            
            public String getHashId() { return hashId; }
            public void setHashId(String hashId) { this.hashId = hashId; }
            
            public String getHashName() { return hashName; }
            public void setHashName(String hashName) { this.hashName = hashName; }
        }
        
        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public Boolean getAccessToken() { return accessToken; }
        public void setAccessToken(Boolean accessToken) { this.accessToken = accessToken; }
        
        public String getCredentialId() { return credentialId; }
        public void setCredentialId(String credentialId) { this.credentialId = credentialId; }
        
        public List<HashData> getHashs() { return hashs; }
        public void setHashs(List<HashData> hashs) { this.hashs = hashs; }
        
        public String getWebhook() { return webhook; }
        public void setWebhook(String webhook) { this.webhook = webhook; }
    }
    
    // Main getters and setters
    public MetaData getMetaData() { return metaData; }
    public void setMetaData(MetaData metaData) { this.metaData = metaData; }
    
    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }
}