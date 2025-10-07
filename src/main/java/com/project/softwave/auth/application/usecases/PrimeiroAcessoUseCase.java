package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioLoginDto;
import com.project.softwave.auth.application.dto.UsuarioPrimeiroAcessoDTO;
import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrimeiroAcessoUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioLoginDto primeiroAcesso(UsuarioPrimeiroAcessoDTO usuario) {
        if (usuario.getEmail() == null || usuario.getTokenPrimeiroAcesso() == null) {
            throw new RuntimeException("Email e chave de acesso não podem ser nulos");
        }

        Optional<Usuario> possivelUsuario =
                usuarioRepository.findByEmailEqualsAndTokenPrimeiroAcessoEquals(
                        usuario.getEmail(), usuario.getTokenPrimeiroAcesso());
        
        if (possivelUsuario.isEmpty()) {
            throw new RuntimeException("Email ou chave de acesso inválido");
        }
        
        UsuarioLoginDto primeiroAcesso = new UsuarioLoginDto(
                possivelUsuario.get().getEmail(),
                possivelUsuario.get().getSenha());

        Boolean usuarioAtivo = usuarioRepository.existsByEmailAndAtivoIsTrue(usuario.getEmail());
        if (usuarioAtivo) {
            throw new RuntimeException("Usuário Já Ativo!");
        }

        return primeiroAcesso;
    }
}