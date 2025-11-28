package com.project.softwave.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para autenticação de usuário")
public class UsuarioLoginDto {

    @Schema(description = "Email do usuário", example = "usuario@exemplo.com", required = true)
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Schema(description = "Senha do usuário", example = "Senha@123", required = true)
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public UsuarioLoginDto() {
    }

    public UsuarioLoginDto(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}