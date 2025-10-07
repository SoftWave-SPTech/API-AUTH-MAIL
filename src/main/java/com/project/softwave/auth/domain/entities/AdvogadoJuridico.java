package com.project.softwave.auth.domain.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AdvogadoJuridico")
public class AdvogadoJuridico extends Usuario {
    
    private String nomeFantasia;
    private String cnpj;
    private String oab;

    public AdvogadoJuridico() {
        super();
    }

    public AdvogadoJuridico(String email, String senha, Role role, String nomeFantasia, String cnpj, String oab) {
        super(email, senha, role);
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.oab = oab;
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

    public String getOab() {
        return oab;
    }

    public void setOab(String oab) {
        this.oab = oab;
    }
}