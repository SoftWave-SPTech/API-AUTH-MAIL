package com.project.softwave.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO para cadastro e atualização de senha")
public class UsuarioSenhaDto {

    @Email(message = "Email inválido")
    @Schema(description = "Email do usuário", example = "usuario@exemplo.com")
    @NotBlank(message = "O email é obrigatório")
    private String email;

    @Schema(description = "Nova senha do usuário", example = "NovaSenha@123")
    @NotBlank(message = "A nova senha é obrigatória")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais")
    private String senha;

    @Schema(description = "Confirmação da nova senha", example = "NovaSenha@123")
    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    private String confirmaSenha;

    public UsuarioSenhaDto() {
    }

    public UsuarioSenhaDto(String email, String senha, String confirmaSenha) {
        this.email = email;
        this.senha = senha;
        this.confirmaSenha = confirmaSenha;
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

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }
}