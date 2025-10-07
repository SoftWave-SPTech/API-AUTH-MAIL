package com.project.softwave.auth.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AdvogadoFisico")
public class AdvogadoFisico extends Usuario {
    
    private String nome;
    private String cpf;
    private String rg;
    private String oab;

    public AdvogadoFisico() {
        super();
    }

    public AdvogadoFisico(String email, String senha, Role role, String nome, String cpf, String oab) {
        super(email, senha, role);
        this.nome = nome;
        this.cpf = cpf;
        this.oab = oab;
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

    public String getOab() {
        return oab;
    }

    public void setOab(String oab) {
        this.oab = oab;
    }
}