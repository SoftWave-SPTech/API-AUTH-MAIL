package com.project.softwave.auth.domain.ports;

import com.project.softwave.auth.domain.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByEmailEqualsAndTokenPrimeiroAcessoEquals(String email, String token);
    
    Optional<Usuario> findByTokenRecuperacaoSenha(String token);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndAtivoIsTrue(String email);
    
    Usuario save(Usuario usuario);
    
    void updateSenhaByEmail(String senhaCriptografada, String email);
}