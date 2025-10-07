package com.project.softwave.auth.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("UsuarioJuridico")
public class UsuarioJuridico extends Usuario {
    
    private String nomeFantasia;
    private String cnpj;

    public UsuarioJuridico() {
        super();
    }

    public UsuarioJuridico(String email, String senha, Role role, String nomeFantasia, String cnpj) {
        super(email, senha, role);
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}