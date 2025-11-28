package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.usecases.SolicitarResetSenhaUseCase;
import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.EmailService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class SolicitarResetSenhaUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private EmailService emailService;
    private SolicitarResetSenhaUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        emailService = mock(EmailService.class);
        useCase = new SolicitarResetSenhaUseCase();
        // Inject mocks via reflection since fields are private and @Autowired
        try {
            var repoField = SolicitarResetSenhaUseCase.class.getDeclaredField("usuarioRepository");
            repoField.setAccessible(true);
            repoField.set(useCase, usuarioRepository);

            var emailField = SolicitarResetSenhaUseCase.class.getDeclaredField("emailService");
            emailField.setAccessible(true);
            emailField.set(useCase, emailService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void solicitarResetSenha_usuarioExistente_salvaTokenEEnviaEmail() {
        String email = "test@example.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        useCase.solicitarResetSenha(email);

        assertNotNull(usuario.getTokenRecuperacaoSenha());
        assertEquals(8, usuario.getTokenRecuperacaoSenha().length());
        assertNotNull(usuario.getDataCriacaoTokenRecuperacaoSenha());
        assertNotNull(usuario.getDataExpiracaoTokenRecuperacaoSenha());
        assertTrue(usuario.getDataExpiracaoTokenRecuperacaoSenha()
                .isAfter(usuario.getDataCriacaoTokenRecuperacaoSenha()));

        verify(usuarioRepository).save(usuario);
        verify(emailService).enviarEmailResetSenha(eq(email), eq(usuario.getTokenRecuperacaoSenha()));
    }

    @Test
    void solicitarResetSenha_usuarioNaoEncontrado_lancaExcecao() {
        String email = "notfound@example.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            useCase.solicitarResetSenha(email);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
        verify(emailService, never()).enviarEmailResetSenha(anyString(), anyString());
    }
}