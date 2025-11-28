package com.project.softwave.auth.application.dto;

import com.project.softwave.auth.domain.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de autenticação com token JWT")
public class UsuarioTokenDTO {

    @Schema(description = "Token JWT de autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;

    @Schema(description = "ID do usuário autenticado", example = "1", required = true)
    private Integer id;

    @Schema(description = "Nome do usuário autenticado", example = "João Silva", required = true)
    private String nome;

    @Schema(description = "Email do usuário autenticado", example = "joao.silva@exemplo.com", required = true)
    private String email;

    @Schema(description = "Tipo de usuário (ADVOGADO ou CLIENTE)", example = "ADVOGADO", required = true)
    private String tipoUsuario;

    @Schema(description = "Role do usuário autenticado", example = "ROLE_USER", required = true)
    private String role;

    @Schema(description = "Foto do usuário autenticado", example = "https://example.com/photo.jpg")
    private String foto;

    public UsuarioTokenDTO() {
    }

    public UsuarioTokenDTO(Integer id, String email, String token, String tipoUsuario, String role, String nome, String foto) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.tipoUsuario = tipoUsuario;
        this.role = role;
        this.nome = nome;
        this.foto = foto;
    }

    public static UsuarioTokenDTO toDTO(Usuario usuario, String token, String role, String nome, String foto) {
        return new UsuarioTokenDTO(usuario.getId(), usuario.getEmail(), token, usuario.getClass().getSimpleName(), role, nome, foto);
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}