package com.example.ScienceStationProject.security;

public class SecurityParams {

    public static final String jwtHeader = "Authorization";
    public static final String jwtSecret = "123-secret-456";
    //30 minutes will change after I finish the app
    public static final String jwtExpiration = "1814400";
    public static final String jwtAuthenticationPath = "/auth";
    public static final String jwtAuthenticationRefresh = "/refresh";
}