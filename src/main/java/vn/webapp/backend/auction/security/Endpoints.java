package vn.webapp.backend.auction.security;

public class Endpoints {
    public static final String front_end_host = "http://localhost:3000";

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/v1/jewelry/**", "/api/v1/jewelry-category/**", "/**"
    };

    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/v1/auth/**"
    };

    public static final String[] MANAGER_DELETE_ENDPOINTS = {
            "/api/v1/jewelry/**"
    };

    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/api/v1/user/**"
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/api/v1/user/**"
    };
}
