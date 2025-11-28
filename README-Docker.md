# Docker Setup - Auth Service

Este diretório contém a configuração Docker para o microserviço de autenticação.

## Arquivos Docker

- `Dockerfile`: Configuração da imagem da aplicação
- `docker-compose.yml`: Orquestração da aplicação com MySQL
- `.dockerignore`: Arquivos ignorados no build
- `application-docker.yml`: Configuração específica para ambiente Docker

## Como usar

### 1. Build e execução com Docker Compose (recomendado)

```bash
# Build e start dos serviços
docker-compose up --build

# Executar em background
docker-compose up -d --build

# Parar os serviços
docker-compose down

# Remover volumes (cuidado: apaga dados do banco)
docker-compose down -v
```

### 2. Build manual da imagem

```bash
# Build da imagem
docker build -t softwave/auth-service:latest .

# Executar apenas a aplicação (precisa de MySQL externo)
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/softwave" \
  -e SPRING_DATASOURCE_USERNAME="root" \
  -e SPRING_DATASOURCE_PASSWORD="09241724" \
  softwave/auth-service:latest
```

### 3. Variáveis de ambiente

Você pode customizar as configurações através de variáveis de ambiente:

```bash
# Banco de dados
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/softwave
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=09241724

# Email
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_app

# Perfil Spring
SPRING_PROFILES_ACTIVE=docker
```

## Endpoints disponíveis

Após iniciar a aplicação:

- **Aplicação**: http://localhost:8081
- **Health Check**: http://localhost:8081/actuator/health
- **Swagger UI**: http://localhost:8081/swagger-ui.html (se configurado)
- **MySQL**: localhost:3306

## Troubleshooting

### 1. Problema de conectividade com MySQL
Aguarde alguns segundos para o MySQL inicializar completamente antes da aplicação tentar conectar.

### 2. Porta já em uso
Altere as portas no `docker-compose.yml` se necessário:
```yaml
ports:
  - "8082:8081"  # Usar porta 8082 no host
```

### 3. Logs da aplicação
```bash
# Ver logs da aplicação
docker-compose logs auth-service

# Seguir logs em tempo real
docker-compose logs -f auth-service
```

### 4. Acessar container
```bash
# Entrar no container da aplicação
docker-compose exec auth-service sh

# Entrar no container do MySQL
docker-compose exec mysql mysql -u root -p
```

## Otimizações de produção

Para ambiente de produção, considere:

1. **Usar secrets** para senhas sensíveis
2. **Configurar volumes persistentes** para logs
3. **Usar registry privado** para imagens
4. **Configurar monitoring** (Prometheus, Grafana)
5. **Usar reverse proxy** (Nginx, Traefik)

## Build otimizado

O Dockerfile usa multi-stage build para:
- ✅ Reduzir tamanho da imagem final
- ✅ Separar dependências de build do runtime
- ✅ Usar JRE em vez de JDK completo
- ✅ Executar como usuário não-root
- ✅ Incluir health check