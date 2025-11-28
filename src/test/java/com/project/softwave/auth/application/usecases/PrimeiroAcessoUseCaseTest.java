package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioLoginDto;
import com.project.softwave.auth.application.dto.UsuarioPrimeiroAcessoDTO;
import com.project.softwave.auth.application.usecases.PrimeiroAcessoUseCase;
import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrimeiroAcessoUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    
    @InjectMocks
    private PrimeiroAcessoUseCase primeiroAcessoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarUsuarioLoginDtoQuandoPrimeiroAcessoValido() {
        UsuarioPrimeiroAcessoDTO dto = new UsuarioPrimeiroAcessoDTO();
        dto.setEmail("test@email.com");
        dto.setTokenPrimeiroAcesso("token123");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@email.com");
        usuario.setSenha("senha123");

        when(usuarioRepository.findByEmailEqualsAndTokenPrimeiroAcessoEquals("test@email.com", "token123"))
                .thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndAtivoIsTrue("test@email.com")).thenReturn(false);

        UsuarioLoginDto result = primeiroAcessoUseCase.primeiroAcesso(dto);

        assertEquals("test@email.com", result.getEmail());
        assertEquals("senha123", result.getSenha());
    }

    @Test
    void deveLancarExcecaoQuandoEmailENulo() {
        UsuarioPrimeiroAcessoDTO dto = new UsuarioPrimeiroAcessoDTO();
        dto.setEmail(null);
        dto.setTokenPrimeiroAcesso("token123");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            primeiroAcessoUseCase.primeiroAcesso(dto);
        });

        assertEquals("Email e chave de acesso não podem ser nulos", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTokenPrimeiroAcessoENulo() {
        UsuarioPrimeiroAcessoDTO dto = new UsuarioPrimeiroAcessoDTO();
        dto.setEmail("test@email.com");
        dto.setTokenPrimeiroAcesso(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            primeiroAcessoUseCase.primeiroAcesso(dto);
        });

        assertEquals("Email e chave de acesso não podem ser nulos", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        UsuarioPrimeiroAcessoDTO dto = new UsuarioPrimeiroAcessoDTO();
        dto.setEmail("test@email.com");
        dto.setTokenPrimeiroAcesso("token123");

        when(usuarioRepository.findByEmailEqualsAndTokenPrimeiroAcessoEquals("test@email.com", "token123"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            primeiroAcessoUseCase.primeiroAcesso(dto);
        });

        assertEquals("Email ou chave de acesso inválido", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioJaAtivo() {
        UsuarioPrimeiroAcessoDTO dto = new UsuarioPrimeiroAcessoDTO();
        dto.setEmail("test@email.com");
        dto.setTokenPrimeiroAcesso("token123");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@email.com");
        usuario.setSenha("senha123");

        when(usuarioRepository.findByEmailEqualsAndTokenPrimeiroAcessoEquals("test@email.com", "token123"))
                .thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmailAndAtivoIsTrue("test@email.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            primeiroAcessoUseCase.primeiroAcesso(dto);
        });

        assertEquals("Usuário Já Ativo!", exception.getMessage());
    }
}