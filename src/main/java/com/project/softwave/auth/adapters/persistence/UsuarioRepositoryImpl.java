package com.project.softwave.auth.adapters.persistence;

import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<Usuario> findByEmailEqualsAndTokenPrimeiroAcessoEquals(String email, String token) {
        return usuarioJpaRepository.findByEmailEqualsAndTokenPrimeiroAcessoEquals(email, token);
    }

    @Override
    public Optional<Usuario> findByTokenRecuperacaoSenha(String token) {
        return usuarioJpaRepository.findByTokenRecuperacaoSenha(token);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmailAndAtivoIsTrue(String email) {
        return usuarioJpaRepository.existsByEmailAndAtivoIsTrue(email);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioJpaRepository.save(usuario);
    }

    @Override
    public void updateSenhaByEmail(String senhaCriptografada, String email) {
        usuarioJpaRepository.updateSenhaByEmail(senhaCriptografada, email);
    }
}