package com.project.softwave.auth.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguracao {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AutenticacaoEntryPoint autenticacaoEntryPoint;

    private static final AntPathRequestMatcher[] URLS_PERMITIDAS = {
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/swagger-resources"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/ui"),
            new AntPathRequestMatcher("/configuration/security"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/api/public/**"),
            new AntPathRequestMatcher("/auth/**"),
            new AntPathRequestMatcher("/actuator/**"),
            new AntPathRequestMatcher("/h2-console/**")
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URLS_PERMITIDAS).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(autenticacaoEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new AutenticacaoProvider(autenticacaoService, passwordEncoder));
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AutenticacaoFilter jwtAuthenticationFilterBean() {
        return new AutenticacaoFilter(autenticacaoService, gerenciadorTokenJwt);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        
        // Permite credenciais (cookies, headers de autenticação)
        configuracao.setAllowCredentials(true);
        
        // Lê origens permitidas de variável de ambiente ou usa valores padrão para desenvolvimento
//        String allowedOriginsEnv = System.getenv("CORS_ALLOWED_ORIGINS");
        String allowedOriginsEnv = null; // Desabilitado para teste local
        if (allowedOriginsEnv != null && !allowedOriginsEnv.isEmpty()) {
            // Suporta múltiplas origens separadas por vírgula
            List<String> origins = Arrays.asList(allowedOriginsEnv.split(","));
            configuracao.setAllowedOrigins(origins);
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
