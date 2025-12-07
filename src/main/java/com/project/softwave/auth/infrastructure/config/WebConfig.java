package com.project.softwave.auth.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.PASTA_DOCUMENTOS_PROCESSOS}")
    private String pastaDocumentosProcessos;

    @Value("${file.PASTA_DOCUMENTOS_PESSOAIS}")
    private String pastaDocumentosPessoais;

    @Value("${file.PASTA_FOTOS_PERFIS}")
    private String pastaFotosPerfis;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Acesso para Documentos de Processo
        registry.addResourceHandler("/ArquivosSistemaUsuarios/DocumentosProcessos/**")
                .addResourceLocations("file:" + pastaDocumentosProcessos + "/");

        // Acesso para Documentos Pessoais
        registry.addResourceHandler("/ArquivosSistemaUsuarios/DocumentosPessoais/**")
                .addResourceLocations("file:" + pastaDocumentosPessoais + "/");

        // Acesso para Fotos de Perfil
        registry.addResourceHandler("/ArquivosSistemaUsuarios/FotosPerfis/**")
                .addResourceLocations("file:" + pastaFotosPerfis + "/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Tente pegar origem via env (CORS_ALLOWED_ORIGINS como "http://a,http://b")
        String env = System.getenv("CORS_ALLOWED_ORIGINS");
        List<String> allowed;
        if (env != null && !env.isBlank()) {
            allowed = Arrays.stream(env.split(","))
                            .map(String::trim)
                            .flatMap(orig -> {
                                // se veio com :80 adiciona variação sem :80
                                if (orig.endsWith(":80")) {
                                    return List.of(orig, orig.replaceFirst(":80$", "")).stream();
                                }
                                return List.of(orig).stream();
                            })
                            .distinct()
                            .toList();
        } else {
            // defaults: inclui localhost e o IP do front (com e sem :80)
            allowed = List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://52.3.112.88",
                "http://52.3.112.88:80",
                "http://52.3.112.88:8080"
            );
        }

        registry.addMapping("/**")
                .allowedOrigins(allowed.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Set-Cookie", "Authorization", "Content-Type");
    }
}
