package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.usecases.ReenviarTokenPrimeiroAcessoUseCase;
import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.EmailService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ReenviarTokenPrimeiroAcessoUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private EmailService emailService;
    private ReenviarTokenPrimeiroAcessoUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        emailService = mock(EmailService.class);
        useCase = new ReenviarTokenPrimeiroAcessoUseCase(usuarioRepository, emailService);
    }

    @Test
    void shouldThrowExceptionWhenUsuarioNotFound() {
        String email = "test@example.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.execute(email));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUsuarioAlreadyActive() {
        String email = "active@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuario.getAtivo()).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.execute(email));
        assertEquals("Usuário já está ativo", exception.getMessage());
    }

    @Test
    void shouldGenerateNewTokenAndSendEmailWhenUsuarioInactive() {
        String email = "inactive@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuario.getAtivo()).thenReturn(false);

        useCase.execute(email);

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(usuario).setTokenPrimeiroAcesso(tokenCaptor.capture());
        String generatedToken = tokenCaptor.getValue();
        assertNotNull(generatedToken);
        assertEquals(8, generatedToken.length());

        verify(usuarioRepository).save(usuario);
        verify(emailService).enviarEmailPrimeiroAcesso(email, generatedToken);
    }
}