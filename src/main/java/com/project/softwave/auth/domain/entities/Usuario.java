package com.project.softwave.auth.domain.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    //    @Enumerated(EnumType.STRING)
    private Role role;

    private String cep;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String complemento;

    private String numero;

    private String telefone;

    private String foto;

    private Boolean ativo;

    private Integer tentativasFalhasLogin;

    private String tokenRecuperacaoSenha;
    private String tokenPrimeiroAcesso;

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

    public Usuario() {
    }

    public Usuario(Integer id, String senha, String email, String cep, String logradouro, String bairro, String cidade, String complemento, String telefone) {
        this.id = id;
        this.senha = senha;
        this.email = email;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.complemento = complemento;
        this.telefone = telefone;
    }

    public Usuario(String senha, String email, String cep, String logradouro, String bairro, String cidade, String complemento, String telefone) {
        this.senha = senha;
        this.email = email;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.complemento = complemento;
        this.telefone = telefone;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {return foto; }

    public void setFoto(String foto) {this.foto = foto; }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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

    public Integer getTentativasFalhasLogin() {
        return tentativasFalhasLogin;
    }

    public void setTentativasFalhasLogin(Integer tentativasFalhasLogin) {
        this.tentativasFalhasLogin = tentativasFalhasLogin;
    }
}