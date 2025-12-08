package com.project.softwave.auth.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Lê origens permitidas do env CORS_ALLOWED_ORIGINS (formato: "http://a,http://b")
        String allowedOriginsEnv = null;
        List<String> allowedOrigins;

        if (allowedOriginsEnv != null && !allowedOriginsEnv.isBlank()) {
            allowedOrigins = Arrays.stream(allowedOriginsEnv.split(","))
                    .map(String::trim)
                    .flatMap(orig -> {
                        if (orig.endsWith(":80")) {
                            // adiciona variante sem :80
                            return List.of(orig, orig.replaceFirst(":80$", "")).stream();
                        }
                        return List.of(orig).stream();
                    })
                    .distinct()
                    .toList();
        } else {
            allowedOrigins = List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:8080",
                "http://52.3.112.88",
                "http://52.3.112.88:80",
                "http://52.3.112.88:8080"
            );
        }

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name()
                )
                .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "X-Requested-With",
                        "Accept",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
                .exposedHeaders(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "Authorization",
                        "Content-Type",
                        "Set-Cookie"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}
