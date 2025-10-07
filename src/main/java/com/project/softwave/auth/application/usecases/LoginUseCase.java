package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioLoginDto;
import com.project.softwave.auth.application.dto.UsuarioTokenDTO;
import com.project.softwave.auth.domain.entities.*;
import com.project.softwave.auth.domain.ports.TokenService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UsuarioTokenDTO autenticar(UsuarioLoginDto usuarioLoginDto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado = usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email do usuário não cadastrado!"));

        if (!usuarioAutenticado.getAtivo()) {
            throw new RuntimeException("Usuário inativo!");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String tipoUsuario = usuarioAutenticado.getClass().getSimpleName();

        String nome = "";
        if (usuarioAutenticado instanceof UsuarioFisico) {
            nome = ((UsuarioFisico) usuarioAutenticado).getNome();
        } else if (usuarioAutenticado instanceof UsuarioJuridico) {
            nome = ((UsuarioJuridico) usuarioAutenticado).getNomeFantasia();
        } else if (usuarioAutenticado instanceof AdvogadoFisico) {
            nome = ((AdvogadoFisico) usuarioAutenticado).getNome();
        } else if (usuarioAutenticado instanceof AdvogadoJuridico) {
            nome = ((AdvogadoJuridico) usuarioAutenticado).getNomeFantasia();
        }

        final String token = tokenService.generateToken(authentication, tipoUsuario, nome, usuarioAutenticado.getId());
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        return UsuarioTokenDTO.toDTO(usuarioAutenticado, token, role, nome, usuarioAutenticado.getFoto());
    }
}