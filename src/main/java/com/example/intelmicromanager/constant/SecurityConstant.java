package com.example.intelmicromanager.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 5 * 60 * 60 * 1000 * 24; //5 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt_Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String INTEL_MANAGER = "Intel Manager Plc";
    public static final String INTEL_MANAGER_ADMINISTRATION = "User management portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTION_HTTP_METHOD = "Options";
//    public static final String[] PUBLIC_URL = {"user/login", "user/register", "/user/reset-password/**", "/user/image/**"};
    public static final String[] PUBLIC_URL = {"**"};

}
