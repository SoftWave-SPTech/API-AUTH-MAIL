package com.project.softwave.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para primeiro acesso do usuário")
public class UsuarioPrimeiroAcessoDTO {

    @Schema(description = "Email do usuário", example = "usuario@exemplo.com", required = true)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Token de primeiro acesso", example = "12387590", required = true)
    @NotBlank(message = "Token é obrigatório")
    private String tokenPrimeiroAcesso;

    public UsuarioPrimeiroAcessoDTO() {
    }

    public UsuarioPrimeiroAcessoDTO(String email, String tokenPrimeiroAcesso) {
        this.email = email;
        this.tokenPrimeiroAcesso = tokenPrimeiroAcesso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenPrimeiroAcesso() {
        return tokenPrimeiroAcesso;
    }

    public void setTokenPrimeiroAcesso(String tokenPrimeiroAcesso) {
        this.tokenPrimeiroAcesso = tokenPrimeiroAcesso;
    }
}