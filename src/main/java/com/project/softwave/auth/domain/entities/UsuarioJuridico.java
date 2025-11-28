package com.project.softwave.auth.domain.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("usuario_juridico")
public class UsuarioJuridico extends Usuario {

    @Column(unique = true)
    private String cnpj;

    private String nomeFantasia;

    private String razaoSocial;

    private String representante;

    public UsuarioJuridico(String cnpj, String nomeFantasia, String razaoSocial, String representante) {
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.representante = representante;
    }

    public UsuarioJuridico(Integer id, String senha, String email, String cep, String logradouro, String bairro, String cidade, String complemento, String telefone, String cnpj, String nomeFantasia, String razaoSocial, String representante) {
        super(id, senha, email, cep, logradouro, bairro, cidade, complemento, telefone);
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.representante = representante;
    }

    public UsuarioJuridico(String senha, String email, String cep, String logradouro, String bairro, String cidade, String complemento, String telefone, String cnpj, String nomeFantasia, String razaoSocial, String representante) {
        super(senha, email, cep, logradouro, bairro, cidade, complemento, telefone);
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.representante = representante;
    }

    public UsuarioJuridico() {
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }
}
