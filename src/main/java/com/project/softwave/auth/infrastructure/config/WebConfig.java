package com.project.softwave.auth.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

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

    /**
     * Configuração de CORS permissiva para desenvolvimento.
     * ATENÇÃO: usar apenas em ambiente de desenvolvimento/testes.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Usar allowedOriginPatterns para suportar "*" com allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("*")   // permite todos os métodos
                .allowedHeaders("*")   // permite todos os headers
                .allowCredentials(true)
                // expõe headers úteis pro front — não tem wildcard confiável em todos os browsers,
                // então listamos os mais comuns
                .exposedHeaders("Set-Cookie", "Authorization", "Content-Disposition")
                .maxAge(3600);
    }
}
