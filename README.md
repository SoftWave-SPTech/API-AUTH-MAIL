# API Auth-Mail - Microserviço de Autenticação

Microserviço especializado em autenticação, autorização e envio de emails do sistema SoftWave, desenvolvido com Spring Boot e Spring Security.

## Tecnologias Utilizadas

![Spring Boot](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

### Dependências Principais

- **Spring Boot 3.1.5** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Spring Boot Mail** - Envio de emails
- **MySQL Connector J** - Driver do banco de dados
- **JWT (jjwt) 0.11.5** - Tokens de autenticação
- **Spring Boot Validation** - Validação de dados
- **Swagger/OpenAPI 2.2.0** - Documentação da API

## Requisitos do Sistema

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

- **Java** >= 21
- **Maven** >= 3.8.0
- **MySQL** >= 8.0
- **Conta Gmail** (para envio de emails)

## Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone <repository-url>
cd API-AUTH-MAIL
```

### 2. Configuração do Banco de Dados

O serviço de autenticação utiliza o mesmo banco de dados principal do projeto:

```sql
-- O banco softwave_db já deve estar criado pelo backend principal
-- Caso não esteja, execute:
CREATE DATABASE IF NOT EXISTS softwave_db;
CREATE USER IF NOT EXISTS 'softwave'@'localhost' IDENTIFIED BY 'softwave123';
GRANT ALL PRIVILEGES ON softwave_db.* TO 'softwave'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configuração de Ambiente

Crie um arquivo `application-local.yml` em `src/main/resources/`:

```yaml
spring:
  application:
    name: auth-service
  
  datasource:
    url: jdbc:mysql://localhost:3306/softwave_db
    username: softwave
    password: softwave123
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
    show-sql: true
  
  # Configuração de Email
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME:seu-email@gmail.com}
    password: ${MAIL_PASSWORD:sua-senha-app}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000

# Configurações JWT
jwt:
  secret: ${JWT_SECRET:auth-service-super-secret-key-2025}
  validity: ${JWT_VALIDITY:3600} # 1 hora em segundos
  refresh-validity: ${JWT_REFRESH_VALIDITY:604800} # 7 dias em segundos

# Servidor
server:
  port: 8083

# CORS
cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:8080}
  allowed-methods: GET,POST,PUT,DELETE,OPTIONS
  allowed-headers: "*"
  allow-credentials: true

# Email Templates
email:
  templates:
    recovery-password: "Olá {nome}, para redefinir sua senha, clique no link: {link}"
    welcome: "Bem-vindo(a) {nome} ao sistema SoftWave!"
  from: ${EMAIL_FROM:noreply@softwave.com}
```

### 4. Configuração do Gmail

Para usar o Gmail como provedor de email:

1. Ative a verificação em 2 etapas na sua conta Google
2. Gere uma senha de app específica:
   - Vá para [Gerenciar conta Google](https://myaccount.google.com/)
   - Segurança → Verificação em duas etapas → Senhas de app
   - Gere uma senha para "Mail"

### 5. Variáveis de Ambiente

Configure as seguintes variáveis:

```bash
# JWT
export JWT_SECRET=seu-jwt-secret-super-seguro-auth-service
export JWT_VALIDITY=3600
export JWT_REFRESH_VALIDITY=604800

# Email
export MAIL_USERNAME=seu-email@gmail.com
export MAIL_PASSWORD=sua-senha-app-gmail

# CORS
export CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:8080

# Email Templates
export EMAIL_FROM=noreply@softwave.com
```

### 6. Instalação das Dependências

```bash
mvn clean install
```

### 7. Executar a Aplicação

#### Modo Desenvolvimento

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### Build e Execução

```bash
mvn clean package
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

#### Docker (Opcional)

```bash
docker build -t softwave/auth-service .
docker run -p 8083:8083 softwave/auth-service
```

A aplicação estará disponível em: http://localhost:8083

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/project/softwave/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java      # Configuração Spring Security
│   │   │   ├── JwtConfig.java          # Configuração JWT
│   │   │   ├── CorsConfig.java         # Configuração CORS
│   │   │   └── MailConfig.java         # Configuração Email
│   │   ├── controller/
│   │   │   ├── AuthController.java     # Endpoints de autenticação
│   │   │   └── MailController.java     # Endpoints de email
│   │   ├── dto/
│   │   │   ├── LoginRequestDto.java    # DTO de login
│   │   │   ├── LoginResponseDto.java   # DTO de resposta login
│   │   │   ├── RegisterRequestDto.java # DTO de cadastro
│   │   │   └── EmailRequestDto.java    # DTO de email
│   │   ├── entity/
│   │   │   ├── User.java              # Entidade usuário
│   │   │   └── RefreshToken.java      # Entidade token refresh
│   │   ├── repository/
│   │   │   ├── UserRepository.java    # Repositório usuário
│   │   │   └── RefreshTokenRepository.java
│   │   ├── service/
│   │   │   ├── AuthService.java       # Serviço de autenticação
│   │   │   ├── JwtService.java        # Serviço JWT
│   │   │   ├── MailService.java       # Serviço de email
│   │   │   └── UserService.java       # Serviço de usuário
│   │   ├── security/
│   │   │   ├── JwtAuthFilter.java     # Filtro JWT
│   │   │   └── CustomUserDetails.java # UserDetails customizado
│   │   └── exception/
│   │       ├── AuthException.java     # Exceções de auth
│   │       └── GlobalExceptionHandler.java
│   └── resources/
│       ├── application.yml            # Configuração principal
│       ├── application-docker.yml     # Configuração Docker
│       └── templates/                 # Templates de email
└── test/                             # Testes
```

## Endpoints da API

### Autenticação

#### POST /auth/login
Realiza login do usuário

**Request:**
```json
{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "usuario@example.com",
    "name": "Nome Usuario",
    "role": "USER"
  }
}
```

#### POST /auth/register
Cadastra novo usuário

**Request:**
```json
{
  "name": "Novo Usuario",
  "email": "novo@example.com",
  "password": "senha123",
  "confirmPassword": "senha123",
  "role": "USER"
}
```

#### POST /auth/refresh
Renova token de acesso

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### POST /auth/logout
Invalida tokens do usuário

#### POST /auth/forgot-password
Solicita recuperação de senha

**Request:**
```json
{
  "email": "usuario@example.com"
}
```

#### POST /auth/reset-password
Redefine senha com token

**Request:**
```json
{
  "token": "reset-token",
  "newPassword": "novaSenha123",
  "confirmPassword": "novaSenha123"
}
```

### Email

#### POST /mail/send
Envia email personalizado

**Request:**
```json
{
  "to": "destinatario@example.com",
  "subject": "Assunto do Email",
  "content": "Conteúdo do email",
  "template": "welcome"
}
```

#### POST /mail/send-recovery
Envia email de recuperação de senha

**Request:**
```json
{
  "email": "usuario@example.com"
}
```

## Documentação da API

A documentação completa está disponível via Swagger UI:

- **Desenvolvimento**: http://localhost:8083/swagger-ui.html
- **JSON**: http://localhost:8083/v3/api-docs

## Segurança

### Configurações JWT

- **Algoritmo**: HMAC-SHA256
- **Validade Access Token**: 1 hora (configurável)
- **Validade Refresh Token**: 7 dias (configurável)
- **Header**: Authorization: Bearer {token}

### Senha

- **Codificação**: BCrypt com força 12
- **Validações**: 
  - Mínimo 8 caracteres
  - Pelo menos 1 letra maiúscula
  - Pelo menos 1 número
  - Pelo menos 1 caractere especial

### CORS

Configurado para aceitar requisições dos domínios autorizados.

## Testes

### Executar Testes

```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=AuthControllerTest

# Testes de integração
mvn test -Dtest=**/*IntegrationTest
```

### Testes de Email

Para testar emails em desenvolvimento, use:

```yaml
# application-test.yml
spring:
  mail:
    host: smtp.mailtrap.io  # Ou outro provedor de teste
    port: 587
    username: test-user
    password: test-pass
```

## Monitoramento

### Health Check

```bash
curl http://localhost:8083/actuator/health
```

### Métricas

```bash
curl http://localhost:8083/actuator/metrics
```

## Troubleshooting

### Problemas Comuns

1. **Erro de autenticação Gmail**: Verifique se a senha de app está correta
2. **JWT inválido**: Confirme se o secret está configurado corretamente
3. **CORS Error**: Verifique se o frontend está nas origins permitidas
4. **Conexão MySQL**: Confirme se o banco está rodando e acessível

### Logs Úteis

```yaml
logging:
  level:
    com.project.softwave.service.MailService: DEBUG
    org.springframework.security: DEBUG
    org.springframework.mail: DEBUG
```

## Integração com Sistema Principal

Este microserviço integra com o backend principal através de:

- **Headers JWT**: Validação de tokens em requisições
- **Endpoints de validação**: `/auth/validate`
- **Sincronização de usuários**: Via API REST

## Contribuição

1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/auth-improvement`)
3. Commit suas mudanças (`git commit -m 'Melhora autenticação'`)
4. Push para a branch (`git push origin feature/auth-improvement`)
5. Abra um Pull Request

## Licença

Este projeto é propriedade da SoftWave SPTech e destina-se ao uso exclusivo do escritório Lauriano & Leão Sociedade de Advogados.

---

**Desenvolvido por:** SoftWave SPTech  
**Versão:** 0.0.1-SNAPSHOT  
**Data:** 2025