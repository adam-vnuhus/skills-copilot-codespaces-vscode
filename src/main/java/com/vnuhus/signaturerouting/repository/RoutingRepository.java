package com.vnuhus.signaturerouting.repository;

import com.vnuhus.signaturerouting.model.Routing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing routing configurations
 */
@Repository
public interface RoutingRepository extends JpaRepository<Routing, Long> {
    
    /**
     * Find routing configuration by endpoint and CA code
     * 
     * @param routeIn the incoming route endpoint
     * @param caCode the CA provider code
     * @return routing configuration if found
     */
    @Query("SELECT r FROM Routing r WHERE r.routeIn = :routeIn AND r.caCode = :caCode AND r.active = true")
    Optional<Routing> findByRouteInAndCaCode(@Param("routeIn") String routeIn, @Param("caCode") String caCode);
}