package com.project.softwave.auth.adapters.web;

import com.project.softwave.auth.application.dto.*;
import com.project.softwave.auth.application.usecases.*;
import com.project.softwave.auth.adapters.external.CookieService;
import com.project.softwave.auth.domain.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "API para gerenciamento de autenticação e autorização")
public class AuthController {

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private PrimeiroAcessoUseCase primeiroAcessoUseCase;

    @Autowired
    private CadastrarSenhaUseCase cadastrarSenhaUseCase;

    @Autowired
    private SolicitarResetSenhaUseCase solicitarResetSenhaUseCase;

    @Autowired
    private ResetarSenhaUseCase resetarSenhaUseCase;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private ReenviarTokenPrimeiroAcessoUseCase reEnviarTokenPrimeiroAcessoUseCase;

    @PostMapping("/login")
    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioTokenDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<UsuarioTokenDTO> login(
            @Parameter(description = "Credenciais do usuário", required = true)
            @Valid @RequestBody UsuarioLoginDto usuarioLoginDto, HttpServletResponse response) {
        UsuarioTokenDTO usuarioTokenDto = this.loginUseCase.autenticar(usuarioLoginDto);
        this.cookieService.addJwtCookie(response, usuarioTokenDto.getToken());
        return ResponseEntity.status(200).body(usuarioTokenDto);
    }

    @PostMapping("/primeiro-acesso")
    @Operation(summary = "Primeiro acesso", description = "Valida as credenciais para primeiro acesso do usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Primeiro acesso validado com sucesso",
            content = @Content(schema = @Schema(implementation = UsuarioLoginDto.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<UsuarioLoginDto> primeiroAcesso(
            @Parameter(description = "Credenciais do usuário", required = true)
            @Valid @RequestBody UsuarioPrimeiroAcessoDTO usuarioLoginDto) {
        UsuarioLoginDto usuarioLogado = this.primeiroAcessoUseCase.primeiroAcesso(usuarioLoginDto);
        return ResponseEntity.status(200).body(usuarioLogado);
    }

    @PatchMapping("/cadastrar-senha")
    @Operation(summary = "Cadastrar senha", description = "Cadastra uma nova senha para o usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> cadastrarSenha(
            @Parameter(description = "Dados para cadastro de senha", required = true)
            @Valid @RequestBody UsuarioSenhaDto usuarioSenhaDto) {
        this.cadastrarSenhaUseCase.cadastrarSenha(usuarioSenhaDto);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/solicitar-reset-senha")
    @Operation(summary = "Solicitar redefinição de senha", description = "Envia um email com token para redefinição de senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Email inválido"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<String> solicitarResetSenha(
            @Parameter(description = "Email do usuário", required = true)
            @Valid @RequestParam String email) {
        solicitarResetSenhaUseCase.solicitarResetSenha(email);
        return ResponseEntity.ok().body("Email enviado com sucesso");
    }

    @PostMapping("/resetar-senha")
    @Operation(summary = "Redefinir senha", description = "Redefine a senha do usuário usando o token recebido por email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    public ResponseEntity<Void> resetarSenha(
            @Parameter(description = "Dados para redefinição de senha", required = true)
            @Valid @RequestBody ResetSenhaRequest request) {
        resetarSenhaUseCase.resetarSenha(request.getToken(), request.getNovaSenha(), request.getNovaSenhaConfirma());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Realiza o logout do usuário removendo o cookie JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao realizar logout")
    })
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieService.clearJwtCookie(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reenviar-token")
    @Operation(summary = "Reenviar token primeiro acesso", description = "Gera e envia o token para primeiro acesso novamente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token reenviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Email inválido"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<String> reenviarTokenPrimeiroAcesso(
            @Parameter(description = "Email do usuário", required = true)
            @Valid @RequestParam String email) {
        reEnviarTokenPrimeiroAcessoUseCase.execute(email);
        return ResponseEntity.ok().body("Token reenviado com sucesso");
    }
}