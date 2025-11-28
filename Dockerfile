# Dockerfile para Microserviço de Autenticação Spring Boot
# Multi-stage build para otimização de tamanho

# Estágio 1: Build da aplicação
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

# Definir diretório de trabalho
WORKDIR /app

# Copiar apenas os arquivos de configuração primeiro (para cache do Docker)
COPY pom.xml .
COPY src ./src

# Fazer o build da aplicação (skip tests para build mais rápido)
RUN mvn clean package -DskipTests

# Estágio 2: Runtime da aplicação
FROM eclipse-temurin:21-jre-alpine

# Metadados da imagem
LABEL maintainer="SoftWave Team"
LABEL description="Microserviço de Autenticação e Autorização"
LABEL version="1.0.0"

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

# Instalar dependências necessárias
RUN apk add --no-cache curl

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação do estágio de build
COPY --from=builder /app/target/auth-service-*.jar app.jar

# Alterar proprietário dos arquivos para o usuário spring
RUN chown -R spring:spring /app

# Mudar para usuário não-root
USER spring

# Expor a porta da aplicação
EXPOSE 8083

# Configurar JVM options para container
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8083/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]