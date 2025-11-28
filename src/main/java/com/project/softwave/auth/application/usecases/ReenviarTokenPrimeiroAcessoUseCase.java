package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.EmailService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import com.project.softwave.auth.infrastructure.exceptions.EntidadeNaoEncontradaException;
import com.project.softwave.auth.infrastructure.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

@Service
public class ReenviarTokenPrimeiroAcessoUseCase {

    private final UsuarioRepository usuarioRepository;

    private final EmailService emailService;

    public ReenviarTokenPrimeiroAcessoUseCase(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    public void execute(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));

        if (usuario.getAtivo()) {
            throw new ForbiddenException("Usuário já está ativo");
        }

        String novoToken = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        usuario.setTokenPrimeiroAcesso(novoToken);
        usuarioRepository.save(usuario);

        emailService.enviarEmailPrimeiroAcesso(email, novoToken);
    }
}
