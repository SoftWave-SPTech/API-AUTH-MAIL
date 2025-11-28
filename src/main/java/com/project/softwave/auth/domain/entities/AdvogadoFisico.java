package com.project.softwave.auth.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AdvogadoFisico")
public class AdvogadoFisico extends UsuarioFisico {

        @Column(unique = true)
        private Integer oab;

        public AdvogadoFisico() {
        }

        public AdvogadoFisico(Integer oab) {
            this.oab = oab;
        }

        public AdvogadoFisico(String nome, String cpf, String rg, Integer oab) {
            super(nome, cpf, rg);
            this.oab = oab;
        }

        public AdvogadoFisico(Integer id, String senha, String email, String cep, String logradouro, String bairro, String cidade, String complemento, String telefone, String nome, String cpf, String rg, Integer oab) {
            super(id, senha, email, cep, logradouro, bairro, cidade, complemento, telefone, nome, cpf, rg);
            this.oab = oab;
        }

        public AdvogadoFisico(String senha, String email, String cep, String logradouro, String bairro, String cidade, String complemento, String telefone, String nome, String cpf, String rg, Integer oab) {
            super(senha, email, cep, logradouro, bairro, cidade, complemento, telefone, nome, cpf, rg);
            this.oab = oab;
        }

        public Integer getOab() {
            return oab;
        }

        public void setOab(Integer oab) {
            this.oab = oab;
        }
    }
