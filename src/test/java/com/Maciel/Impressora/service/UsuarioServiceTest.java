package com.Maciel.Impressora.service;

import com.Maciel.Impressora.exception.ErroAutenticacao;
import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Usuario;
import com.Maciel.Impressora.model.repository.UsuarioRepository;
import com.Maciel.Impressora.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        // Cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder()
                .email(email)
                .senha(senha)
                .id(1L)
                .build();

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Ação
        Usuario result = service.autenticar(email, senha);

        // Verificação
        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(email, result.getEmail(), "O email retornado não corresponde ao esperado");
        Assertions.assertEquals(senha, result.getSenha(), "A senha retornada não corresponde ao esperado");
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
        // Cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        // Ação e verificação
        Assertions.assertThrows(ErroAutenticacao.class, () -> {
            service.autenticar("email@email.com", "senha");
        });
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        // Cenário
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        // Ação
        Assertions.assertThrows(ErroAutenticacao.class, () -> {
            service.autenticar("email@email.com", "123");
        });
    }

    @Test
    public void deveSalvarUmUsuario() {
        // Cenário
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("nome")
                .email("email@email.com")
                .senha("senha")
                .build();
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        // Ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        // Verificação
        Assertions.assertNotNull(usuarioSalvo);
        Assertions.assertEquals(1L, usuarioSalvo.getId());
        Assertions.assertEquals("nome", usuarioSalvo.getNome());
        Assertions.assertEquals("email@email.com", usuarioSalvo.getEmail());
        Assertions.assertEquals("senha", usuarioSalvo.getSenha());
    }

    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
        // Cenário
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email("email@email.com").build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        // Ação
        Assertions.assertThrows(RegraNegocioException.class, () -> {
            service.salvarUsuario(usuario);
        });

        // Verificação
        Mockito.verify(repository, Mockito.never()).save(usuario);
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        // Cenário
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        // Ação e verificação
        Assertions.assertThrows(RegraNegocioException.class, () -> {
            service.validarEmail("email@email.com");
        });
    }
}

