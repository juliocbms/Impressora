package com.Maciel.Impressora.model.repository;

import com.Maciel.Impressora.model.entity.Usuario;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    public void deveVerificarAExistenciaDeUmEmail() {
        //cenário
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //ação/ execução
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    public void deveRetornarfalsoQUandoNaoHouverUsuarioCadastradoEmail() {
        //ação
        boolean result = repository.existsByEmail("usuario@email.com");

        //verificação
        Assertions.assertFalse(result);
    }

    @Test
    @Transactional
    public void devePersistirUmUsuarioNaBaseDeDados() {
        //cenario
        Usuario usuario = criarUsuario();
        //ação
        Usuario usuarioSalvo = repository.save(usuario);
        //verificação
        Assertions.assertNotNull(usuarioSalvo.getId());
    }

    @Test
    @Transactional
    public void deveBuscarUmUsuarioPorEmail() {
        //cenario
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        //verificacao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        //ação
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @Transactional
    public void deveRetornarVazioaoBuscarUsuarioProEmailQUandoNaoExisteNaBase() {
        //verificacao
        Optional<Usuario> result = repository.findByEmail("usuario@email.com");

        //ação
        Assertions.assertFalse(result.isPresent());
    }

    public static Usuario criarUsuario() {
        return Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }
}
