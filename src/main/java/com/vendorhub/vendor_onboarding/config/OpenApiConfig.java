package com.vendorhub.vendor_onboarding.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI: http://localhost:8080/swagger-ui.html
 * Log in with /api/auth/login (or /api/customer/auth/login), copy the token,
 * click "Authorize" and paste it. Every request you try from the page then sends it.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "VendorHub API", version = "v1", description = "Vendor onboarding and customer accounts"),
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {
}
