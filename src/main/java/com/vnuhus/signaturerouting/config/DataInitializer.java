package com.vnuhus.signaturerouting.config;

import com.vnuhus.signaturerouting.model.Routing;
import com.vnuhus.signaturerouting.repository.RoutingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for initializing routing data
 */
@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Bean
    public CommandLineRunner initRouting(RoutingRepository routingRepository) {
        return args -> {
            if (routingRepository.count() == 0) {
                logger.info("Initializing routing data...");
                
                // Old API routes for VIETTEL and VNPT
                routingRepository.save(new Routing(
                        "VIETTEL",
                        "/external/v1/routing/employee/signature/sign_hashs",
                        "http://old-api.viettel.com/service/v1/remote/signature/sign_hashs",
                        "old",
                        "POST",
                        30000,
                        60000,
                        true
                ));
                
                routingRepository.save(new Routing(
                        "VNPT",
                        "/external/v1/routing/employee/signature/sign_hashs",
                        "http://old-api.vnpt.com/service/v1/remote/signature/sign_hashs",
                        "old",
                        "POST",
                        30000,
                        60000,
                        true
                ));
                
                // New API route for SAVIS
                routingRepository.save(new Routing(
                        "SAVIS",
                        "/external/v1/routing/employee/signature/sign_hashs",
                        "http://new-api.savis.com/internal/v1/employee/signature/tx/sign_hashs",
                        "new",
                        "POST",
                        30000,
                        60000,
                        true
                ));
                
                logger.info("Routing data initialized successfully");
            } else {
                logger.info("Routing data already exists, skipping initialization");
            }
        };
    }
}