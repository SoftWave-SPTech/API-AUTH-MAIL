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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Service
public class LoginUseCase {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public UsuarioTokenDTO autenticar(UsuarioLoginDto usuarioLoginDto) {
        try {
            final UsernamePasswordAuthenticationToken credentials =
                    new UsernamePasswordAuthenticationToken(usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

                Usuario usuarioAutenticado = usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Email do usuário não cadastrado!"));

            if (Boolean.FALSE.equals(usuarioAutenticado.getAtivo())) {
                throw new ForbiddenException("Usuário inativo!");
            }

            int tentativasFalhasLogin = usuarioAutenticado.getTentativasFalhasLogin() == null
                    ? 0
                    : usuarioAutenticado.getTentativasFalhasLogin();

            if (tentativasFalhasLogin >= 3) {
                throw new TooManyRequestsException("Muitas tentativas de login! Por favor, faça o reset de senha!");
            }

            final Authentication authentication = this.authenticationManager.authenticate(credentials);

            usuarioAutenticado.setTentativasFalhasLogin(0);
            usuarioRepository.save(usuarioAutenticado);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Busca novamente com o tipo correto usando o ID para garantir o carregamento do tipo concreto
            Usuario usuarioComTipoCorreto = entityManager != null
                    ? entityManager.find(Usuario.class, usuarioAutenticado.getId())
                    : null;
            if (usuarioComTipoCorreto == null) {
                usuarioComTipoCorreto = usuarioAutenticado;
            }

            String tipoUsuario;
            String nome;
            
            // Primeiro tenta usar instanceof
            if (usuarioComTipoCorreto instanceof AdvogadoFisico) {
                tipoUsuario = "advogadoFisico";
                nome = ((AdvogadoFisico) usuarioComTipoCorreto).getNome();
            } else if (usuarioComTipoCorreto instanceof AdvogadoJuridico) {
                tipoUsuario = "advogadoJuridico";
                nome = ((AdvogadoJuridico) usuarioComTipoCorreto).getNomeFantasia();
            } else if (usuarioComTipoCorreto instanceof UsuarioFisico) {
                tipoUsuario = "usuarioFisico";
                nome = ((UsuarioFisico) usuarioComTipoCorreto).getNome();
            } else if (usuarioComTipoCorreto instanceof UsuarioJuridico) {
                tipoUsuario = "usuarioJuridico";
                nome = ((UsuarioJuridico) usuarioComTipoCorreto).getNomeFantasia();
            } else {
                // Fallback usando o campo tipoUsuario do banco de dados
                String tipoUsuarioDb = usuarioComTipoCorreto.getTipoUsuario();
                if (tipoUsuarioDb != null) {
                    switch (tipoUsuarioDb) {
                        case "AdvogadoFisico":
                        case "advogado_fisico":
                        case "advogadoFisico":
                            tipoUsuario = "advogadoFisico";
                            // Busca o objeto como UsuarioFisico para acessar o nome
                            UsuarioFisico advFisico = entityManager != null ? entityManager.find(UsuarioFisico.class, usuarioComTipoCorreto.getId()) : null;
                            nome = advFisico != null ? advFisico.getNome() : usuarioComTipoCorreto.getEmail();
                            break;
                        case "AdvogadoJuridico":
                        case "advogado_juridico":
                        case "advogadoJuridico":
                            tipoUsuario = "advogadoJuridico";
                            UsuarioJuridico advJuridico = entityManager != null ? entityManager.find(UsuarioJuridico.class, usuarioComTipoCorreto.getId()) : null;
                            nome = advJuridico != null ? advJuridico.getNomeFantasia() : usuarioComTipoCorreto.getEmail();
                            break;
                        case "UsuarioFisico":
                        case "usuario_fisico":
                        case "usuarioFisico":
                            tipoUsuario = "usuarioFisico";
                            UsuarioFisico userFisico = entityManager != null ? entityManager.find(UsuarioFisico.class, usuarioComTipoCorreto.getId()) : null;
                            nome = userFisico != null ? userFisico.getNome() : usuarioComTipoCorreto.getEmail();
                            break;
                        case "UsuarioJuridico":
                        case "usuario_juridico":
                        case "usuarioJuridico":
                            tipoUsuario = "usuarioJuridico";
                            UsuarioJuridico userJuridico = entityManager != null ? entityManager.find(UsuarioJuridico.class, usuarioComTipoCorreto.getId()) : null;
                            nome = userJuridico != null ? userJuridico.getNomeFantasia() : usuarioComTipoCorreto.getEmail();
                            break;
                        default:
                            throw new IllegalStateException("Tipo de usuário não suportado para login: " + tipoUsuarioDb);
                    }
                } else {
                    throw new IllegalStateException("Tipo de usuário não identificado para login");
                }
            }

            final String token = tokenService.generateToken(authentication, tipoUsuario, nome, usuarioComTipoCorreto.getId(), usuarioComTipoCorreto.getFoto());
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(usuarioComTipoCorreto.getRole() != null ? usuarioComTipoCorreto.getRole().toString() : null);

            return UsuarioTokenDTO.toDTO(usuarioComTipoCorreto, token, tipoUsuario, role, nome, usuarioComTipoCorreto.getFoto());

        } catch (Exception e) {
            if (e instanceof AuthenticationException) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(usuarioLoginDto.getEmail());
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    int tentativasFalhasLogin = usuario.getTentativasFalhasLogin() == null ? 0 : usuario.getTentativasFalhasLogin();
                    usuario.setTentativasFalhasLogin(tentativasFalhasLogin + 1);
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