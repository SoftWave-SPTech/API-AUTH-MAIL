package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioSenhaDto;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import com.project.softwave.auth.infrastructure.exceptions.CorpoRequisicaoVazioException;
import com.project.softwave.auth.infrastructure.exceptions.DadosInvalidosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastrarSenhaUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void cadastrarSenha(UsuarioSenhaDto usuarioSenhaDto) {
        String email = usuarioSenhaDto.getEmail();
        String senha = usuarioSenhaDto.getSenha();
        String confirmaSenha = usuarioSenhaDto.getConfirmaSenha();

        if (senha == null || confirmaSenha == null) {
            throw new CorpoRequisicaoVazioException("Senha e confirmação de senha não podem ser nulas!");
        }

        if (!senha.equals(confirmaSenha)) {
            throw new DadosInvalidosException("As senhas não coincidem!");
        }

        String senhaCriptografada = passwordEncoder.encode(senha);
        usuarioRepository.updateSenhaByEmail(senhaCriptografada, email);
    }
}