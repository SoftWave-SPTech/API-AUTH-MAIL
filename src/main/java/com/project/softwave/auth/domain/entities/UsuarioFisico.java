package com.project.softwave.auth.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("UsuarioFisico")
public class UsuarioFisico extends Usuario {
    
    private String nome;
    private String cpf;
    private String rg;

    public UsuarioFisico() {
        super();
    }

    public UsuarioFisico(String email, String senha, Role role, String nome, String cpf) {
        super(email, senha, role);
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }
}