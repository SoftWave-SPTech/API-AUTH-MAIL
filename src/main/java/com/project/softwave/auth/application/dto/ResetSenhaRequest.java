package com.project.softwave.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO para solicitação de redefinição de senha")
public class ResetSenhaRequest {

    @Schema(description = "Token de redefinição de senha recebido por email", example = "abc123def456", required = true)
    @NotBlank(message = "O token é obrigatório")
    private String token;

    @Schema(description = "Nova senha do usuário", example = "NovaSenha@123", required = true)
    @NotBlank(message = "A nova senha é obrigatória")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais")
    private String novaSenha;

    @Schema(description = "Confirmação da nova senha", example = "NovaSenha@123", required = true)
    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    private String novaSenhaConfirma;

    public ResetSenhaRequest() {
    }

    public ResetSenhaRequest(String token, String novaSenha, String novaSenhaConfirma) {
        this.token = token;
        this.novaSenha = novaSenha;
        this.novaSenhaConfirma = novaSenhaConfirma;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getNovaSenhaConfirma() {
        return novaSenhaConfirma;
    }

    public void setNovaSenhaConfirma(String novaSenhaConfirma) {
        this.novaSenhaConfirma = novaSenhaConfirma;
    }
}