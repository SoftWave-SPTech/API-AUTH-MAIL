package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioSenhaDto;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarSenhaUseCaseTest {

    @Mock
    private UsuarioRepository mockUsuarioRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private CadastrarSenhaUseCase cadastrarSenhaUseCaseUnderTest;

    @Test
    void testCadastrarSenha() {
        // Setup
        final UsuarioSenhaDto usuarioSenhaDto = new UsuarioSenhaDto("email", "senha", "senha");
        when(mockPasswordEncoder.encode("senha")).thenReturn("senhaCriptografada");

        // Run the test
        cadastrarSenhaUseCaseUnderTest.cadastrarSenha(usuarioSenhaDto);

        // Verify the results
        verify(mockUsuarioRepository).updateSenhaByEmail("senhaCriptografada", "email");
    }

    @Test
    void testCadastrarSenhaThrowsWhenSenhaIsNull() {
        // Setup
        final UsuarioSenhaDto usuarioSenhaDto = new UsuarioSenhaDto("email", null, "senha");

        // Run & Verify
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
            cadastrarSenhaUseCaseUnderTest.cadastrarSenha(usuarioSenhaDto),
            "Senha e confirmação de senha não podem ser nulas!"
        );
    }

    @Test
    void testCadastrarSenhaThrowsWhenConfirmaSenhaIsNull() {
        // Setup
        final UsuarioSenhaDto usuarioSenhaDto = new UsuarioSenhaDto("email", "senha", null);

        // Run & Verify
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
            cadastrarSenhaUseCaseUnderTest.cadastrarSenha(usuarioSenhaDto),
            "Senha e confirmação de senha não podem ser nulas!"
        );
    }

    @Test
    void testCadastrarSenhaThrowsWhenSenhasNaoCoincidem() {
        // Setup
        final UsuarioSenhaDto usuarioSenhaDto = new UsuarioSenhaDto("email", "senha1", "senha2");

        // Run & Verify
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
            cadastrarSenhaUseCaseUnderTest.cadastrarSenha(usuarioSenhaDto),
            "As senhas não coincidem!"
        );
    }
}
