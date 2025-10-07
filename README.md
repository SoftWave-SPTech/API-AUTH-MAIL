# 🔐 API-AUTH-MAIL

Microserviço de Autenticação e Autorização desenvolvido com Spring Boot, implementando Clean Architecture e responsável pelo gerenciamento de usuários, autenticação JWT e envio de emails.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Uso](#uso)
- [Endpoints da API](#endpoints-da-api)
- [Configuração de Email](#configuração-de-email)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
- [Contribuição](#contribuição)

## 🎯 Sobre o Projeto

Este microserviço é responsável por:

- **Autenticação e Autorização**: Login seguro com JWT
- **Gerenciamento de Usuários**: Cadastro e gestão de diferentes tipos de usuários
- **Primeiro Acesso**: Fluxo completo para novos usuários
- **Reset de Senha**: Recuperação de senha via email
- **Envio de Emails**: Notificações e comunicações automáticas
- **Segurança**: Implementação de Spring Security com JWT

## 🚀 Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 3.1.5** - Framework principal
- **Maven** - Gerenciamento de dependências

### Segurança
- **Spring Security** - Segurança da aplicação
- **JWT (JSON Web Token)** - Autenticação stateless
- **BCrypt** - Criptografia de senhas

### Banco de Dados
- **MySQL** - Banco de dados principal
- **Spring Data JPA** - Abstração de acesso a dados
- **Hibernate** - ORM

### Comunicação
- **Spring Mail** - Envio de emails
- **SMTP Gmail** - Provedor de email

### Documentação e Monitoramento
- **SpringDoc OpenAPI 3** - Documentação da API
- **Swagger UI** - Interface da documentação
- **Spring Actuator** - Monitoramento da aplicação

### Testes
- **Spring Boot Test** - Framework de testes
- **H2 Database** - Banco em memória para testes
- **JUnit 5** - Testes unitários

## 🏗️ Arquitetura

O projeto segue os princípios da **Clean Architecture**:

```
src/
├── main/
│   ├── java/com/project/softwave/auth/
│   │   ├── adapters/           # Camada de Adapters
│   │   │   ├── external/       # Integrações externas (Email, Cookies)
│   │   │   ├── persistence/    # Persistência de dados
│   │   │   └── web/           # Controllers REST
│   │   ├── application/        # Camada de Aplicação
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── services/      # Serviços de aplicação
│   │   │   └── usecases/      # Casos de uso
│   │   ├── domain/            # Camada de Domínio
│   │   │   ├── entities/      # Entidades de negócio
│   │   │   └── ports/         # Interfaces/Contratos
│   │   └── infrastructure/    # Camada de Infraestrutura
│   │       ├── config/        # Configurações
│   │       └── security/      # Configurações de segurança
│   └── resources/
│       └── application.yml    # Configurações da aplicação
```

## ⚙️ Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 21** ou superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

## 🔧 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/SoftWave-SPTech/API-AUTH-MAIL.git
cd API-AUTH-MAIL
```

### 2. Configure o banco de dados

Crie um banco de dados MySQL:

```sql
CREATE DATABASE softwave;
```

### 3. Configure as variáveis de ambiente

Configure as seguintes variáveis de ambiente ou edite o arquivo `application.yml`:

**Banco de Dados:**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/softwave
    username: seu_usuario
    password: sua_senha
```

**Email (Gmail):**
```yaml
spring:
  mail:
    username: ${MAIL_USERNAME:seu_email@gmail.com}
    password: ${MAIL_PASSWORD:sua_senha_de_app}
```

### 4. Instale as dependências

```bash
mvn clean install
```

### 5. Execute a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8081`

## 📝 Uso

### Fluxo Básico de Autenticação

1. **Primeiro Acesso**: Usuário recebe token por email
2. **Cadastro de Senha**: Define senha usando o token
3. **Login**: Autentica com email e senha
4. **Acesso Protegido**: Usa JWT para acessar recursos

### Exemplo de Login

```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@email.com",
    "senha": "suaSenha123"
  }'
```

## 🛠️ Endpoints da API

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/auth/login` | Realizar login |
| `POST` | `/auth/primeiro-acesso` | Enviar token de primeiro acesso |
| `POST` | `/auth/cadastrar-senha` | Cadastrar senha inicial |
| `POST` | `/auth/solicitar-reset-senha` | Solicitar reset de senha |
| `POST` | `/auth/resetar-senha` | Resetar senha |
| `POST` | `/auth/reenviar-token-primeiro-acesso` | Reenviar token |

### Monitoramento

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/actuator/health` | Status da aplicação |
| `GET` | `/actuator/info` | Informações da aplicação |
| `GET` | `/actuator/metrics` | Métricas da aplicação |

## 📧 Configuração de Email

### Gmail (Recomendado)

1. Ative a **verificação em duas etapas** na sua conta Google
2. Gere uma **senha de app** específica
3. Configure as variáveis:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
```

### Outros Provedores

Para outros provedores SMTP, ajuste as configurações conforme necessário:

```yaml
spring:
  mail:
    host: seu.provedor.smtp
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
```

## 🧪 Testes

### Executar todos os testes

```bash
mvn test
```

### Executar testes específicos

```bash
mvn test -Dtest=AuthServiceApplicationTests
```

### Coverage Report

```bash
mvn jacoco:report
```

## 📚 Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

## 🔒 Segurança

### JWT Configuration

O token JWT é configurado com:
- **Algoritmo**: HMAC SHA-256
- **Validade**: 1 hora (3600 segundos)
- **Secret**: Configurado em Base64

### Endpoints Protegidos

Todos os endpoints, exceto login e primeiro acesso, requerem autenticação JWT no header:

```
Authorization: Bearer seu_jwt_token_aqui
```

## 🚦 Status dos Serviços

Monitore a saúde da aplicação através dos endpoints do Actuator:

```bash
# Status geral
curl http://localhost:8081/actuator/health

# Informações detalhadas
curl http://localhost:8081/actuator/info
```

## 📊 Monitoramento

A aplicação expõe métricas através do Spring Actuator:

- **Health Check**: `/actuator/health`
- **Application Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

## 🤝 Contribuição

1. **Fork** o projeto
2. Crie uma **branch** para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. Abra um **Pull Request**

### Padrões de Código

- Siga as convenções do **Clean Code**
- Mantenha a **Clean Architecture**
- Escreva **testes** para novas funcionalidades
- Documente adequadamente o código

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Equipe

**SoftWave SPTech**
- 🌐 Website: [SoftWave](https://github.com/SoftWave-SPTech)
- 📧 Email: contato@softwave.com

## 📞 Suporte

Se você encontrar algum problema ou tiver dúvidas:

1. Verifique a [documentação](#documentação-da-api)
2. Consulte as [issues abertas](https://github.com/SoftWave-SPTech/API-AUTH-MAIL/issues)
3. Abra uma [nova issue](https://github.com/SoftWave-SPTech/API-AUTH-MAIL/issues/new)

---

**Desenvolvido com ❤️ pela equipe SoftWave SPTech**