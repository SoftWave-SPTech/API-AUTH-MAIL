package com.project.softwave.auth.adapters.persistence;

import com.project.softwave.auth.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Integer> {
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Optional<Usuario> findByEmail(@Param("email") String email);
    
    Optional<Usuario> findByEmailEqualsAndTokenPrimeiroAcessoEquals(String email, String token);
    
    Optional<Usuario> findByTokenRecuperacaoSenha(String token);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndAtivoIsTrue(String email);
    
    @Modifying
    @Query("UPDATE Usuario u SET u.senha = :senha, u.ativo = true, u.tokenPrimeiroAcesso = null WHERE u.email = :email")
    void updateSenhaByEmail(@Param("senha") String senhaCriptografada, @Param("email") String email);
}