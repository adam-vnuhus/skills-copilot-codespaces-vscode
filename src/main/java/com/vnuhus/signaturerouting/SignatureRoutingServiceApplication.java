package com.vnuhus.signaturerouting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Digital Signature Routing Service Application
 * 
 * This application provides routing functionality for digital signature services,
 * routing requests to different CA providers based on channel configuration.
 */
@SpringBootApplication
public class SignatureRoutingServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SignatureRoutingServiceApplication.class, args);
    }
}