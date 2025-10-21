package com.project.softwave.auth.domain.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_usuario", insertable = false, updatable = false)
    private String tipoUsuario;

    @Column(nullable = false)
    private String senha;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    private String telefone;
    private String foto;
    private Boolean ativo;

    private Integer tentativasFalhasLogin;

    // Tokens de autenticação
    private String tokenRecuperacaoSenha;
    private String tokenPrimeiroAcesso;

    // Datas dos tokens
    private LocalDateTime dataCriacaoTokenRecuperacaoSenha;
    private LocalDateTime dataCriacaoTokenPrimeiroAcesso;
    private LocalDateTime dataExpiracaoTokenRecuperacaoSenha;
    private LocalDateTime dataExpiracaoTokenPrimeiroAcesso;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Usuario() {}

    public Usuario(String email, String senha, Role role) {
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.ativo = false;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getTokenRecuperacaoSenha() {
        return tokenRecuperacaoSenha;
    }

    public void setTokenRecuperacaoSenha(String tokenRecuperacaoSenha) {
        this.tokenRecuperacaoSenha = tokenRecuperacaoSenha;
    }

    public Integer getTentativasFalhasLogin() {
        return tentativasFalhasLogin;
    }

    public void setTentativasFalhasLogin(Integer tentativasFalhasLogin) {
        this.tentativasFalhasLogin = tentativasFalhasLogin;
    }

    public String getTokenPrimeiroAcesso() {
        return tokenPrimeiroAcesso;
    }

    public void setTokenPrimeiroAcesso(String tokenPrimeiroAcesso) {
        this.tokenPrimeiroAcesso = tokenPrimeiroAcesso;
    }

    public LocalDateTime getDataCriacaoTokenRecuperacaoSenha() {
        return dataCriacaoTokenRecuperacaoSenha;
    }

    public void setDataCriacaoTokenRecuperacaoSenha(LocalDateTime dataCriacaoTokenRecuperacaoSenha) {
        this.dataCriacaoTokenRecuperacaoSenha = dataCriacaoTokenRecuperacaoSenha;
    }

    public LocalDateTime getDataCriacaoTokenPrimeiroAcesso() {
        return dataCriacaoTokenPrimeiroAcesso;
    }

    public void setDataCriacaoTokenPrimeiroAcesso(LocalDateTime dataCriacaoTokenPrimeiroAcesso) {
        this.dataCriacaoTokenPrimeiroAcesso = dataCriacaoTokenPrimeiroAcesso;
    }

    public LocalDateTime getDataExpiracaoTokenRecuperacaoSenha() {
        return dataExpiracaoTokenRecuperacaoSenha;
    }

    public void setDataExpiracaoTokenRecuperacaoSenha(LocalDateTime dataExpiracaoTokenRecuperacaoSenha) {
        this.dataExpiracaoTokenRecuperacaoSenha = dataExpiracaoTokenRecuperacaoSenha;
    }

    public LocalDateTime getDataExpiracaoTokenPrimeiroAcesso() {
        return dataExpiracaoTokenPrimeiroAcesso;
    }

    public void setDataExpiracaoTokenPrimeiroAcesso(LocalDateTime dataExpiracaoTokenPrimeiroAcesso) {
        this.dataExpiracaoTokenPrimeiroAcesso = dataExpiracaoTokenPrimeiroAcesso;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}