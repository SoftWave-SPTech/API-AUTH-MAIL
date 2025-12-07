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
import org.springframework.security.core.context.SecurityContextHolder;
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

            // Busca novamente com o tipo correto usando o ID para garantir o carregamento do tipo concreto
            Usuario usuarioComTipoCorreto = entityManager.find(Usuario.class, usuarioAutenticado.getId());
            
            String tipoUsuario;
            String nome;
            
            // Primeiro tenta usar instanceof
            if (usuarioComTipoCorreto instanceof AdvogadoFisico) {
                tipoUsuario = "advogado_fisico";
                nome = ((AdvogadoFisico) usuarioComTipoCorreto).getNome();
            } else if (usuarioComTipoCorreto instanceof AdvogadoJuridico) {
                tipoUsuario = "advogado_juridico";
                nome = ((AdvogadoJuridico) usuarioComTipoCorreto).getNomeFantasia();
            } else if (usuarioComTipoCorreto instanceof UsuarioFisico) {
                tipoUsuario = "usuario_fisico";
                nome = ((UsuarioFisico) usuarioComTipoCorreto).getNome();
            } else if (usuarioComTipoCorreto instanceof UsuarioJuridico) {
                tipoUsuario = "usuario_juridico";
                nome = ((UsuarioJuridico) usuarioComTipoCorreto).getNomeFantasia();
            } else {
                // Fallback usando o campo tipoUsuario do banco de dados
                String tipoUsuarioDb = usuarioComTipoCorreto.getTipoUsuario();
                if (tipoUsuarioDb != null) {
                    switch (tipoUsuarioDb) {
                        case "AdvogadoFisico":
                            tipoUsuario = "advogado_fisico";
                            // Busca o objeto como UsuarioFisico para acessar o nome
                            UsuarioFisico advFisico = entityManager.find(UsuarioFisico.class, usuarioComTipoCorreto.getId());
                            nome = advFisico != null ? advFisico.getNome() : usuarioComTipoCorreto.getEmail();
                            break;
                        case "AdvogadoJuridico":
                            tipoUsuario = "advogado_juridico";
                            UsuarioJuridico advJuridico = entityManager.find(UsuarioJuridico.class, usuarioComTipoCorreto.getId());
                            nome = advJuridico != null ? advJuridico.getNomeFantasia() : usuarioComTipoCorreto.getEmail();
                            break;
                        case "usuario_fisico":
                            tipoUsuario = "usuario_fisico";
                            UsuarioFisico userFisico = entityManager.find(UsuarioFisico.class, usuarioComTipoCorreto.getId());
                            nome = userFisico != null ? userFisico.getNome() : usuarioComTipoCorreto.getEmail();
                            break;
                        case "usuario_juridico":
                            tipoUsuario = "usuario_juridico";
                            UsuarioJuridico userJuridico = entityManager.find(UsuarioJuridico.class, usuarioComTipoCorreto.getId());
                            nome = userJuridico != null ? userJuridico.getNomeFantasia() : usuarioComTipoCorreto.getEmail();
                            break;
                        default:
                            tipoUsuario = tipoUsuarioDb.toLowerCase();
                            nome = usuarioComTipoCorreto.getEmail();
                    }
                } else {
                    tipoUsuario = "fallback";
                    nome = usuarioComTipoCorreto.getEmail();
                }
            }

            final String token = tokenService.generateToken(authentication, tipoUsuario, nome, usuarioComTipoCorreto.getId(), usuarioComTipoCorreto.getFoto());
            return UsuarioTokenDTO.toDTO(usuarioComTipoCorreto, token, usuarioComTipoCorreto.getRole().toString(), nome, usuarioComTipoCorreto.getFoto());

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