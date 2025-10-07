package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ResetarSenhaUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void resetarSenha(String token, String novaSenha, String novaSenhaConfirma) {
        Usuario usuario = usuarioRepository.findByTokenRecuperacaoSenha(token)
                .orElseThrow(() -> new RuntimeException("Token inválido!"));

        if (novaSenha == null || novaSenhaConfirma == null) {
            throw new RuntimeException("Senha e confirmação de senha não podem ser nulas!");
        }

        if (!novaSenha.equals(novaSenhaConfirma)) {
            throw new RuntimeException("As senhas não coincidem!");
        }

        if (LocalDateTime.now().isAfter(usuario.getDataExpiracaoTokenRecuperacaoSenha())) {
            throw new RuntimeException("Token expirado!");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setTokenRecuperacaoSenha(null);
        usuario.setDataExpiracaoTokenRecuperacaoSenha(null);
        usuarioRepository.save(usuario);
    }
}