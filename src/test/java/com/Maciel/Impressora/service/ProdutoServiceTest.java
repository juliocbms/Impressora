package com.Maciel.Impressora.service;


import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Produto;
import com.Maciel.Impressora.model.enums.TipoCategoria;
import com.Maciel.Impressora.model.repository.ProdutoRepository;
import com.Maciel.Impressora.model.repository.ProdutoRepositoryTest;
import com.Maciel.Impressora.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ProdutoServiceTest {

    @SpyBean
    ProdutoServiceImpl service;
    @MockBean
    ProdutoRepository repository;

    @Test
    public void deveSalvarUmLancamento() {
        // cenário
        Produto produtoASalvar = ProdutoRepositoryTest.criarProduto();
        doNothing().when(service).validar(produtoASalvar);

        Produto produtoSalvo = ProdutoRepositoryTest.criarProduto();
        produtoSalvo.setId(1L);
        produtoSalvo.setCodigoProduto(1234);
        when(repository.save(produtoASalvar)).thenReturn(produtoSalvo);

        // execução
        Produto produto = service.salvarProduto(produtoASalvar);

        // verificação
        Assertions.assertEquals(produtoSalvo.getId(), produto.getId());
        Assertions.assertEquals(produtoSalvo.getCodigoProduto(), produto.getCodigoProduto());
    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
        // cenário
        Produto produtoASalvar = ProdutoRepositoryTest.criarProduto();
        doThrow(RegraNegocioException.class).when(service).validar(produtoASalvar);

        // execução e verificação
        assertThrows(RegraNegocioException.class, () -> service.salvarProduto(produtoASalvar));
        verify(repository, never()).save(produtoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento() {
        // cenário
        Produto produtoSalvo = ProdutoRepositoryTest.criarProduto();
        produtoSalvo.setId(1L);
        produtoSalvo.setTipo(TipoCategoria.INTERNA);

        doNothing().when(service).validar(produtoSalvo);

        when(repository.save(produtoSalvo)).thenReturn(produtoSalvo);

        // execução
        service.atualizar(produtoSalvo);

        // verificação
        verify(repository, times(1)).save(produtoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
        // cenário
        Produto produto = ProdutoRepositoryTest.criarProduto();

        // execução e verificação
        assertThrows(NullPointerException.class, () -> service.atualizar(produto));
        verify(repository, never()).save(produto);
    }

    @Test
    public void deveDeletarUmLancamento() {
        // cenário
        Produto produto = ProdutoRepositoryTest.criarProduto();
        produto.setId(1L);

        // execução
        service.deletar(produto);

        // verificação
        verify(repository).delete(produto);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
        // cenário
        Produto produto = ProdutoRepositoryTest.criarProduto();

        // execução e verificação
        assertThrows(NullPointerException.class, () -> service.deletar(produto));
        verify(repository, never()).delete(produto);
    }

    @Test
    public void deveFiltrarLancamentos() {
        // cenário
        Produto produto = ProdutoRepositoryTest.criarProduto();
        produto.setId(1L);

        List<Produto> lista = Arrays.asList(produto);
        when(repository.findAll(any(Example.class))).thenReturn(lista);

        // execução
        List<Produto> resultado = service.buscar(produto);

        // verificação
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(produto, resultado.getFirst());
    }

    @Test
    public void deveAtualizarOStatusDeUmProduto() {
        // cenário
        Produto produto = ProdutoRepositoryTest.criarProduto();
        produto.setId(1L);
        produto.setTipo(TipoCategoria.INTERNA);

        TipoCategoria novoTipo = TipoCategoria.EXTERNA;

        // Simula a atualização no repositório
        when(repository.save(produto)).thenReturn(produto);

        // execução
        produto = service.atualizarTipoProduto(produto, novoTipo); // Agora chamamos o método correto

        // verificação
        Assertions.assertEquals(novoTipo, produto.getTipo()); // O tipo deve ter sido alterado
        verify(repository, times(1)).save(produto); // Verifica se o save foi chamado
    }


    @Test
    public void deveObterUmLancamentoPorCodigo() {
        // cenário
        Long id = 1L;

        Produto produto = ProdutoRepositoryTest.criarProduto();
        produto.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(produto));

        // execução
        Optional<Produto> resultado = service.obterPorId(id);

        // verificação
        Assertions.assertTrue(resultado.isPresent());
    }

    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
        // cenário
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        // execução
        Optional<Produto> resultado = service.obterPorId(id);

        // verificação
        assertFalse(resultado.isPresent());
    }

    @Test
    public void deveLancarErrosAoValidarUmProduto() {
        Produto produto = new Produto();

        RegraNegocioException erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe uma descrição válida.", erro.getMessage());

        produto.setDescricao("Costela bovina");

        erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe um código de produto válido.", erro.getMessage());

        produto.setCodigoProduto(1234);

        produto.setNomeProduto("");
        erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe um nome de produto válido.", erro.getMessage());

        produto.setNomeProduto("Costela de Boi");

        produto.setPeso(BigDecimal.ZERO);
        erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe um peso válido para o produto.", erro.getMessage());

        produto.setPeso(BigDecimal.valueOf(15));

        produto.setDataProducao(null);
        erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe uma data de produção válida.", erro.getMessage());

        produto.setDataProducao(LocalDate.now().minusDays(10));

        produto.setTipo(null);
        erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe um tipo de categoria.", erro.getMessage());

        produto.setTipo(TipoCategoria.INTERNA);

        produto.setDataValidade(null);
        erro = assertThrows(RegraNegocioException.class, () -> service.validar(produto));
        Assertions.assertEquals("Informe uma data de validade válida.", erro.getMessage());

        produto.setDataValidade(LocalDate.now().plusDays(50));

        assertDoesNotThrow(() -> service.validar(produto));
    }







    @Test
    public void deveObterProdutoPorCodigo() {
        // cenário
        Integer codigoProduto = 1234;
        Produto produto = Produto.builder()
                .codigoProduto(codigoProduto)
                .nomeProduto("Costela de Boi")
                .peso(BigDecimal.valueOf(15))
                .dataValidade(LocalDate.now().plusDays(10))
                .dataProducao(LocalDate.now().minusDays(10))
                .tipo(TipoCategoria.INTERNA)
                .build();

        when(repository.findByCodigoProduto(codigoProduto)).thenReturn(Optional.of(produto));

        // execução
        Optional<Produto> produtoEncontrado = service.obterPorCodigo(codigoProduto);

        // verificação
        Assertions.assertTrue(produtoEncontrado.isPresent());
        Assertions.assertEquals(produtoEncontrado.get().getCodigoProduto(), codigoProduto);
        Assertions.assertEquals(produtoEncontrado.get().getNomeProduto(), "Costela de Boi");
    }


}
