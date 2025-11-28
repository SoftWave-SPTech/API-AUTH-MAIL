package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.EmailService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import com.project.softwave.auth.infrastructure.exceptions.EntidadeNaoEncontradaException;
import com.project.softwave.auth.infrastructure.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitarResetSenhaUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void solicitarResetSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado!"));
        if(!usuario.getAtivo()){
            throw new ForbiddenException("Usuário inativo!, realize o primeiro acesso ou verifique com o administrador.");
        }
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        usuario.setTokenRecuperacaoSenha(token);
        usuario.setDataCriacaoTokenRecuperacaoSenha(LocalDateTime.now());
        usuario.setDataExpiracaoTokenRecuperacaoSenha(LocalDateTime.now().plusMinutes(5));
        usuarioRepository.save(usuario);

        emailService.enviarEmailResetSenha(usuario.getEmail(), token);
    }
}