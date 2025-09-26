package com.vnuhus.signaturerouting.model;

import jakarta.persistence.*;

/**
 * Entity representing routing configuration for different CA providers
 */
@Entity
@Table(name = "routing")
public class Routing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;
    
    @Column(name = "ca_code", nullable = false)
    private String caCode;
    
    @Column(name = "route_in", nullable = false)
    private String routeIn;
    
    @Column(name = "route_out", nullable = false)
    private String routeOut;
    
    @Column(name = "route_version", nullable = false)
    private String routeVersion; // "old" or "new"
    
    @Column(name = "http_method", nullable = false)
    private String httpMethod;
    
    @Column(name = "connect_timeout")
    private Integer connectTimeout;
    
    @Column(name = "read_timeout")
    private Integer readTimeout;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    // Constructors
    public Routing() {}
    
    public Routing(String caCode, String routeIn, String routeOut, String routeVersion, 
                   String httpMethod, Integer connectTimeout, Integer readTimeout, Boolean active) {
        this.caCode = caCode;
        this.routeIn = routeIn;
        this.routeOut = routeOut;
        this.routeVersion = routeVersion;
        this.httpMethod = httpMethod;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.active = active;
    }
    
    // Getters and Setters
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    
    public String getCaCode() { return caCode; }
    public void setCaCode(String caCode) { this.caCode = caCode; }
    
    public String getRouteIn() { return routeIn; }
    public void setRouteIn(String routeIn) { this.routeIn = routeIn; }
    
    public String getRouteOut() { return routeOut; }
    public void setRouteOut(String routeOut) { this.routeOut = routeOut; }
    
    public String getRouteVersion() { return routeVersion; }
    public void setRouteVersion(String routeVersion) { this.routeVersion = routeVersion; }
    
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
    
    public Integer getConnectTimeout() { return connectTimeout; }
    public void setConnectTimeout(Integer connectTimeout) { this.connectTimeout = connectTimeout; }
    
    public Integer getReadTimeout() { return readTimeout; }
    public void setReadTimeout(Integer readTimeout) { this.readTimeout = readTimeout; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}