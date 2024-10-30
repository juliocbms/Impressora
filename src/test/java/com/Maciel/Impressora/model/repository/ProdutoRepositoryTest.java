package com.Maciel.Impressora.model.repository;

import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Produto;
import com.Maciel.Impressora.model.enums.TipoCategoria;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProdutoRepositoryTest {

    @Autowired
    ProdutoRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    public void deveSalvarUmProduto() {
        Produto produto = criarProduto();
        produto = repository.save(produto);

        // Verificando se o ID do produto não é nulo após a persistência
        Assertions.assertNotNull(produto.getId());
    }

    @Test
    @Transactional
    public void deveDeletarUmProduto() {
        Produto produto = criarEPersistirUmProduto();

        // Deletando o produto
        repository.delete(produto);

        // Verificando se o produto foi deletado (busca por ID retornando null)
        Produto produtoInexistente = entityManager.find(Produto.class, produto.getId());
        Assertions.assertNull(produtoInexistente);
    }

    @Test
    @Transactional
    public void deveAtualizarUmProduto() {
        Produto produto = criarEPersistirUmProduto();

        // Alterando atributos do produto
        produto.setNomeProduto("Produto Atualizado");
        produto.setPeso(BigDecimal.valueOf(20));
        produto.setTipo(TipoCategoria.EXTERNA);

        repository.save(produto);

        // Verificando se as alterações foram persistidas
        Produto produtoAtualizado = entityManager.find(Produto.class, produto.getId());
        Assertions.assertEquals("Produto Atualizado", produtoAtualizado.getNomeProduto());
        Assertions.assertEquals(BigDecimal.valueOf(20), produtoAtualizado.getPeso());
        Assertions.assertEquals(TipoCategoria.EXTERNA, produtoAtualizado.getTipo());
    }

    @Test
    @Transactional
    public void deveBuscarUmProdutoPorId() {
        Produto produto = criarEPersistirUmProduto();

        Optional<Produto> produtoEncontrado = repository.findById(produto.getId());

        // Verificando se o produto foi encontrado
        Assertions.assertTrue(produtoEncontrado.isPresent());
    }

    // Método auxiliar para criar e persistir um produto no banco
    private Produto criarEPersistirUmProduto() {
        Produto produto = criarProduto();
        entityManager.persist(produto);
        entityManager.flush();
        return produto;
    }

    // Método estático para criar uma nova instância de Produto
    public static Produto criarProduto() {
        return Produto.builder()
                .codigoProduto(1234)
                .nomeProduto("Costela de Boi")
                .peso(BigDecimal.valueOf(15))
                .dataValidade(LocalDate.parse("2024-10-29"))
                .dataProducao(LocalDate.parse("2024-09-29"))
                .tipo(TipoCategoria.INTERNA)
                .build();
    }

    @Test
    @Transactional
    public void naoDeveSalvarProdutoComNomeDuplicado() {
        // Criando e salvando o primeiro produto com nome duplicado
        Produto produto1 = criarProduto();
        produto1.setNomeProduto("Produto Duplicado");
        repository.save(produto1);

        // Criando um segundo produto com o mesmo nome
        Produto produto2 = Produto.builder()
                .codigoProduto(1235) // Use um código diferente para evitar conflito
                .nomeProduto("Produto Duplicado")
                .peso(BigDecimal.valueOf(10))
                .dataValidade(LocalDate.parse("2024-10-30"))
                .dataProducao(LocalDate.parse("2024-09-30"))
                .tipo(TipoCategoria.EXTERNA)
                .build();

        // Verificando se o produto duplicado não pode ser salvo
        boolean salvoComSucesso = false;
        try {
            repository.save(produto2);
            salvoComSucesso = true; // Se chegar aqui, significa que não lançou a exceção
        } catch (RegraNegocioException e) {
            // Exceção esperada
        }

        // Verificando se o produto duplicado não foi salvo
        Assertions.assertTrue(salvoComSucesso, "Produto com nome duplicado deveria ter lançado uma exceção.");
    }

}
