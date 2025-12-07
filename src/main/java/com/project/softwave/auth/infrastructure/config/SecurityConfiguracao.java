package com.project.softwave.auth.infrastructure.config;


import com.project.softwave.auth.application.services.AutenticacaoService;
import com.project.softwave.auth.infrastructure.security.AutenticacaoFilter;
import com.project.softwave.auth.infrastructure.security.GerenciadorTokenJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguracao {

    @Autowired
    private AutenticacaoService autenticacaoService;

    private static final AntPathRequestMatcher[] URLS_PERMITIDAS = {
            new AntPathRequestMatcher("/swagger-ui/**")
            ,new AntPathRequestMatcher("/swagger-ui.html")
            ,new AntPathRequestMatcher("/swagger-resources")
            ,new AntPathRequestMatcher("/swagger-resources/**")
            ,new AntPathRequestMatcher("/configuration/ui")
            ,new AntPathRequestMatcher("/configuration/security")
            ,new AntPathRequestMatcher("/api/public/**")
            ,new AntPathRequestMatcher("/api/public/authenticate")
            ,new AntPathRequestMatcher("/api/processo/**")
            ,new AntPathRequestMatcher("/webjars/**")
            ,new AntPathRequestMatcher("/v3/api-docs/**")
            ,new AntPathRequestMatcher("/actuator/*")
            ,new AntPathRequestMatcher("/auth/login/**")
            ,new AntPathRequestMatcher("/auth/primeiro-acesso/**")
            ,new AntPathRequestMatcher("/auth/cadastrar-senha/**")
            ,new AntPathRequestMatcher("/h2-console/**")
            ,new AntPathRequestMatcher("/h2-console/**/**")
            ,new AntPathRequestMatcher("/error/**")
            ,new AntPathRequestMatcher("/**")// Libera todo o acesso (apenas para desenvolvimento, remover em produção)

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(URLS_PERMITIDAS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPointBean()))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new AutenticacaoProvider(autenticacaoService, passwordEncoder()));
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AutenticacaoEntryPoint jwtAuthenticationEntryPointBean() {
        return new AutenticacaoEntryPoint();
    }

    @Bean
    public AutenticacaoFilter jwtAuthenticationFilterBean() {
        return new AutenticacaoFilter(autenticacaoService, jwtAuthenticationUtilBean());
    }

    @Bean
    public GerenciadorTokenJwt jwtAuthenticationUtilBean() {
        return new GerenciadorTokenJwt();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowCredentials(true);
    
        // Lê do env corretamente
        String allowedOriginsEnv = null;
        List<String> origins;
    
        if (allowedOriginsEnv != null && !allowedOriginsEnv.isBlank()) {
            origins = Arrays.stream(allowedOriginsEnv.split(","))
                    .map(String::trim)
                    .flatMap(orig -> {
                        if (orig.endsWith(":80")) {
                            return List.of(orig, orig.replaceFirst(":80$", "")).stream();
                        }
                        return List.of(orig).stream();
                    })
                    .distinct()
                    .toList();
        } else {
            // defaults, garanta que inclui o IP do front (sem :80)
            origins = List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://52.3.112.88",
                "http://52.3.112.88:80",
                "http://52.3.112.88:8080"
            );
        }
    
        configuracao.setAllowedOrigins(origins);
        // fallback: patterns (útil se quiser curingas), não use "*" com allowCredentials(true)
        configuracao.setAllowedOriginPatterns(List.of("http://52.3.112.88", "http://localhost:*"));
    
        configuracao.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuracao.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION, "Authorization", "Content-Type", "Set-Cookie"));
        configuracao.setMaxAge(3600L);
    
        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);
    
        // Log (stdout) para podermos ver no docker logs quais origens foram aplicadas
        System.out.println("CORS allowed origins: " + origins);
        System.out.println("CORS allowed origin patterns: " + configuracao.getAllowedOriginPatterns());
    
        return origem;
    }

}
 
