package com.project.softwave.auth.application.services;

import com.project.softwave.auth.application.dto.UsuarioDetalhesDto;
import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuário nao encontrado!"));
        }
        return new UsuarioDetalhesDto(usuarioOpt.get());
    }
}