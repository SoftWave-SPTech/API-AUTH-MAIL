package com.project.softwave.auth.application.usecases;

import com.project.softwave.auth.application.dto.UsuarioLoginDto;
import com.project.softwave.auth.application.dto.UsuarioTokenDTO;
import com.project.softwave.auth.domain.entities.*;
import com.project.softwave.auth.domain.ports.TokenService;
import com.project.softwave.auth.domain.ports.UsuarioRepository;
import com.project.softwave.auth.infrastructure.exceptions.EntidadeNaoEncontradaException;
import com.project.softwave.auth.infrastructure.exceptions.ForbiddenException;
import com.project.softwave.auth.infrastructure.exceptions.LoginIncorretoException;
import com.project.softwave.auth.infrastructure.exceptions.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UsuarioTokenDTO autenticar(UsuarioLoginDto usuarioLoginDto) {
        try {
            final UsernamePasswordAuthenticationToken credentials =
                    new UsernamePasswordAuthenticationToken(usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

            Usuario usuarioAutenticado = usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Email do usuário não cadastrado! verifique com o administrador."));

            if (!usuarioAutenticado.getAtivo()) {
                throw new ForbiddenException("Usuário inativo!, realize o primeiro acesso ou verifique com o administrador.");
            }

            if(usuarioAutenticado.getTentativasFalhasLogin() >= 3){
                throw new TooManyRequestsException("Muitas tentativas de login! Por favor, faça o reset de senha!");
            }

            final Authentication authentication = this.authenticationManager.authenticate(credentials);

            usuarioAutenticado.setTentativasFalhasLogin(0);
            usuarioRepository.save(usuarioAutenticado);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String tipoUsuario = usuarioAutenticado.getClass().getSimpleName();
            String nome;
            
            if (usuarioAutenticado instanceof UsuarioFisico) {
                nome = ((UsuarioFisico) usuarioAutenticado).getNome();
            } else if (usuarioAutenticado instanceof UsuarioJuridico) {
                nome = ((UsuarioJuridico) usuarioAutenticado).getNomeFantasia();
            } else {
                nome = usuarioAutenticado.getEmail(); // Fallback para email
            }

            final String token = tokenService.generateToken(authentication, tipoUsuario, nome, usuarioAutenticado.getId());
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            return UsuarioTokenDTO.toDTO(usuarioAutenticado, token, role, nome, usuarioAutenticado.getFoto());

        } catch (Exception e) {
            if(e.getClass().equals(AuthenticationException.class) || e.getClass().equals(BadCredentialsException.class)) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(usuarioLoginDto.getEmail());
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    usuario.setTentativasFalhasLogin(usuario.getTentativasFalhasLogin() + 1);
                    usuarioRepository.save(usuario);
                }
                System.out.println("Deu erro dentro do if:" + e);
                System.out.println("Tipo do erro dentro do if:" + e.getClass());
                throw new LoginIncorretoException("Email ou senha inválidos!");
            }
            System.out.println("Deu erro" + e);
            System.out.println("Tipo do erro" + e.getClass());
            throw e;
        }
    }
}