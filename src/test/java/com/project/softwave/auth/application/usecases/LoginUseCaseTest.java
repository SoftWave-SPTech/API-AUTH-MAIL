package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioLoginDto;
import com.project.softwave.auth.application.dto.UsuarioTokenDTO;
import com.project.softwave.auth.domain.entities.*;
import com.project.softwave.auth.domain.ports.TokenService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    @Test
    void autenticar_shouldReturnTokenDTO_forUsuarioFisico() {
        UsuarioLoginDto loginDto = new UsuarioLoginDto("email@test.com", "senha123");
        UsuarioFisico usuario = new UsuarioFisico();
        usuario.setId(1);
        usuario.setEmail("email@test.com");
        usuario.setNome("Nome Teste");
        usuario.setAtivo(true);
        usuario.setFoto("foto.png");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("email@test.com")).thenReturn(Optional.of(usuario));
        when(tokenService.generateToken(any(), anyString(), anyString(), anyInt(), anyString())).thenReturn("token123");
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))).when(authentication).getAuthorities();

        UsuarioTokenDTO result = loginUseCase.autenticar(loginDto);

        assertEquals("token123", result.getToken());
        assertEquals("ROLE_USER", result.getRole());
        assertEquals("Nome Teste", result.getNome());
        assertEquals("foto.png", result.getFoto());
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    void autenticar_shouldReturnTokenDTO_forUsuarioJuridico() {
        UsuarioLoginDto loginDto = new UsuarioLoginDto("juridico@test.com", "senha123");
        UsuarioJuridico usuario = new UsuarioJuridico();
        usuario.setId(2);
        usuario.setEmail("juridico@test.com");
        usuario.setNomeFantasia("Empresa XYZ");
        usuario.setAtivo(true);
        usuario.setFoto("foto2.png");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("juridico@test.com")).thenReturn(Optional.of(usuario));
        when(tokenService.generateToken(any(), anyString(), anyString(), anyInt(), anyString())).thenReturn("token456");
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        UsuarioTokenDTO result = loginUseCase.autenticar(loginDto);

        assertEquals("token456", result.getToken());
        assertEquals("ROLE_ADMIN", result.getRole());
        assertEquals("Empresa XYZ", result.getNome());
        assertEquals("foto2.png", result.getFoto());
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    void autenticar_shouldThrowException_whenEmailNotFound() {
        UsuarioLoginDto loginDto = new UsuarioLoginDto("notfound@test.com", "senha123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> loginUseCase.autenticar(loginDto));
        assertEquals("Email do usuário não cadastrado!", ex.getMessage());
    }

    @Test
    void autenticar_shouldThrowException_whenUsuarioInativo() {
        UsuarioLoginDto loginDto = new UsuarioLoginDto("inativo@test.com", "senha123");
        UsuarioFisico usuario = new UsuarioFisico();
        usuario.setId(3);
        usuario.setEmail("inativo@test.com");
        usuario.setNome("Inativo");
        usuario.setAtivo(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("inativo@test.com")).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> loginUseCase.autenticar(loginDto));
        assertEquals("Usuário inativo!", ex.getMessage());
    }

    @Test
    void autenticar_shouldReturnTokenDTO_forAdvogadoFisico() {
        UsuarioLoginDto loginDto = new UsuarioLoginDto("advfisico@test.com", "senha123");
        AdvogadoFisico usuario = new AdvogadoFisico();
        usuario.setId(4);
        usuario.setEmail("advfisico@test.com");
        usuario.setNome("Advogado Físico");
        usuario.setAtivo(true);
        usuario.setFoto("foto3.png");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("advfisico@test.com")).thenReturn(Optional.of(usuario));
        when(tokenService.generateToken(any(), anyString(), anyString(), anyInt(), anyString())).thenReturn("token789");
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADV"))).when(authentication).getAuthorities();

        UsuarioTokenDTO result = loginUseCase.autenticar(loginDto);

        assertEquals("token789", result.getToken());
        assertEquals("ROLE_ADV", result.getRole());
        assertEquals("Advogado Físico", result.getNome());
        assertEquals("foto3.png", result.getFoto());
        assertEquals(usuario.getId(), result.getId());
    }

    @Test
    void autenticar_shouldReturnTokenDTO_forAdvogadoJuridico() {
        UsuarioLoginDto loginDto = new UsuarioLoginDto("advjuridico@test.com", "senha123");
        AdvogadoJuridico usuario = new AdvogadoJuridico();
        usuario.setId(5);
        usuario.setEmail("advjuridico@test.com");
        usuario.setNomeFantasia("Advogado Jurídico");
        usuario.setAtivo(true);
        usuario.setFoto("foto4.png");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(usuarioRepository.findByEmail("advjuridico@test.com")).thenReturn(Optional.of(usuario));
        when(tokenService.generateToken(any(), anyString(), anyString(), anyInt(), anyString())).thenReturn("token101");
        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADVJ"))).when(authentication).getAuthorities();

        UsuarioTokenDTO result = loginUseCase.autenticar(loginDto);

        assertEquals("token101", result.getToken());
        assertEquals("ROLE_ADVJ", result.getRole());
        assertEquals("Advogado Jurídico", result.getNome());
        assertEquals("foto4.png", result.getFoto());
        assertEquals(usuario.getId(), result.getId());
    }
}
