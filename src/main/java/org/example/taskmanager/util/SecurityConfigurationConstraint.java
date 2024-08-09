package org.example.taskmanager.util;

public class SecurityConfigurationConstraint {

    public static final String AUTH = "/auth/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/*";
    public static final String SWAGGER_DOC_API = "/v3/api-docs/**";

    public static final String[] PERMITTED_ALL = {
            AUTH,
            SWAGGER_UI,
            SWAGGER_RESOURCES,
            SWAGGER_DOC_API
    };
}
