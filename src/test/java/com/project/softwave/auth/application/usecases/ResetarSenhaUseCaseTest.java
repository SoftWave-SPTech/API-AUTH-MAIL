package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.usecases.ResetarSenhaUseCase;
import com.project.softwave.auth.domain.entities.Usuario;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResetarSenhaUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private ResetarSenhaUseCase resetarSenhaUseCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        resetarSenhaUseCase = new ResetarSenhaUseCase();
        // Inject mocks via reflection since fields are private and @Autowired
        try {
            var repoField = ResetarSenhaUseCase.class.getDeclaredField("usuarioRepository");
            repoField.setAccessible(true);
            repoField.set(resetarSenhaUseCase, usuarioRepository);

            var encoderField = ResetarSenhaUseCase.class.getDeclaredField("passwordEncoder");
            encoderField.setAccessible(true);
            encoderField.set(resetarSenhaUseCase, passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void resetarSenha_sucesso() {
        String token = "valid-token";
        String novaSenha = "novaSenha123";
        String novaSenhaConfirma = "novaSenha123";
        Usuario usuario = new Usuario();
        usuario.setDataExpiracaoTokenRecuperacaoSenha(LocalDateTime.now().plusMinutes(10));
        usuario.setTokenRecuperacaoSenha(token);

        when(usuarioRepository.findByTokenRecuperacaoSenha(token)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(novaSenha)).thenReturn("encodedSenha");

        resetarSenhaUseCase.resetarSenha(token, novaSenha, novaSenhaConfirma);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario savedUsuario = captor.getValue();

        assertEquals("encodedSenha", savedUsuario.getSenha());
        assertNull(savedUsuario.getTokenRecuperacaoSenha());
        assertNull(savedUsuario.getDataExpiracaoTokenRecuperacaoSenha());
    }

    @Test
    void resetarSenha_tokenInvalido_lancaExcecao() {
        String token = "invalid-token";
        when(usuarioRepository.findByTokenRecuperacaoSenha(token)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                resetarSenhaUseCase.resetarSenha(token, "senha", "senha")
        );
        assertEquals("Token inválido!", ex.getMessage());
    }

    @Test
    void resetarSenha_senhasNulas_lancaExcecao() {
        String token = "valid-token";
        Usuario usuario = new Usuario();
        usuario.setDataExpiracaoTokenRecuperacaoSenha(LocalDateTime.now().plusMinutes(10));
        when(usuarioRepository.findByTokenRecuperacaoSenha(token)).thenReturn(Optional.of(usuario));

        RuntimeException ex1 = assertThrows(RuntimeException.class, () ->
                resetarSenhaUseCase.resetarSenha(token, null, "senha")
        );
        assertEquals("Senha e confirmação de senha não podem ser nulas!", ex1.getMessage());

        RuntimeException ex2 = assertThrows(RuntimeException.class, () ->
                resetarSenhaUseCase.resetarSenha(token, "senha", null)
        );
        assertEquals("Senha e confirmação de senha não podem ser nulas!", ex2.getMessage());
    }

    @Test
    void resetarSenha_senhasNaoCoincidem_lancaExcecao() {
        String token = "valid-token";
        Usuario usuario = new Usuario();
        usuario.setDataExpiracaoTokenRecuperacaoSenha(LocalDateTime.now().plusMinutes(10));
        when(usuarioRepository.findByTokenRecuperacaoSenha(token)).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                resetarSenhaUseCase.resetarSenha(token, "senha1", "senha2")
        );
        assertEquals("As senhas não coincidem!", ex.getMessage());
    }

    @Test
    void resetarSenha_tokenExpirado_lancaExcecao() {
        String token = "valid-token";
        Usuario usuario = new Usuario();
        usuario.setDataExpiracaoTokenRecuperacaoSenha(LocalDateTime.now().minusMinutes(1));
        when(usuarioRepository.findByTokenRecuperacaoSenha(token)).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                resetarSenhaUseCase.resetarSenha(token, "senha", "senha")
        );
        assertEquals("Token expirado!", ex.getMessage());
    }
}