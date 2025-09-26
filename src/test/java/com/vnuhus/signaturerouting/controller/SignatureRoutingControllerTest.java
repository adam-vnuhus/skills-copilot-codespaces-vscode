package com.vnuhus.signaturerouting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnuhus.signaturerouting.dto.SignatureHashRequest;
import com.vnuhus.signaturerouting.dto.SignatureHashResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SignatureRoutingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/external/v1/routing/employee/signature/health"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testSignHashsEndpoint_WithInvalidChannel() throws Exception {
        // Create test request with invalid channel
        SignatureHashRequest request = createTestRequest("INVALID_CHANNEL");
        
        String requestJson = objectMapper.writeValueAsString(request);
        
        MvcResult result = mockMvc.perform(post("/external/v1/routing/employee/signature/sign_hashs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        String responseJson = result.getResponse().getContentAsString();
        SignatureHashResponse response = objectMapper.readValue(responseJson, SignatureHashResponse.class);
        
        assertEquals("1", response.getErrorCode());
        assertTrue(response.getErrorDesc().contains("channel"));
    }
    
    @Test
    void testSignHashsEndpoint_WithValidationError() throws Exception {
        // Create request with missing required fields
        SignatureHashRequest request = new SignatureHashRequest();
        
        String requestJson = objectMapper.writeValueAsString(request);
        
        mockMvc.perform(post("/external/v1/routing/employee/signature/sign_hashs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
    
    private SignatureHashRequest createTestRequest(String channel) {
        SignatureHashRequest request = new SignatureHashRequest();
        
        SignatureHashRequest.MetaData metaData = new SignatureHashRequest.MetaData();
        metaData.setRequestId("test-request-123");
        metaData.setAppCode("TEST_APP");
        metaData.setChannel(channel);
        metaData.setSignPack("test-pack");
        
        SignatureHashRequest.Data data = new SignatureHashRequest.Data();
        data.setUserId("test-user");
        data.setAccessToken(true);
        data.setCredentialId("test-credential");
        data.setWebhook("http://test-webhook.com/callback");
        
        SignatureHashRequest.Data.HashData hashData = new SignatureHashRequest.Data.HashData();
        hashData.setHashId("hash-1");
        hashData.setHashName("test-hash");
        hashData.setHashValue("abcd1234");
        
        data.setHashs(Arrays.asList(hashData));
        
        request.setMetaData(metaData);
        request.setData(data);
        
        return request;
    }
}