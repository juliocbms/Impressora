package com.Maciel.Impressora.service.impl;

import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Produto;
import com.Maciel.Impressora.model.enums.TipoCategoria;
import com.Maciel.Impressora.model.repository.ProdutoRepository;
import com.Maciel.Impressora.service.ProdutoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public  class ProdutoServiceImpl implements ProdutoService {

    private ProdutoRepository repository;

    public ProdutoServiceImpl(ProdutoRepository repository){
        super();
        this.repository = repository;
    }

    @Override
    @Transactional
    public Produto salvarProduto(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Override
    public void validarCodigoProduto(Integer codigoProduto) {
        boolean existe = repository.existsByCodigoProduto(codigoProduto);

    }

    @Override
    public void validarNome(String nomeProduto) {
        boolean existe = repository.existsByNomeProduto(nomeProduto);
        if(existe) {
            throw new RegraNegocioException("Já existe um produto cadastrado com este nome.");
        }
    }


    @Override
    @Transactional
    public void deletar(Produto produto) {
        Objects.requireNonNull(produto.getId());
        repository.delete(produto);

    }

    @Override
    public Produto atualizar(Produto produto) {
        Objects.requireNonNull(produto.getId(), "O ID do produto não pode ser nulo");
        validar(produto); // Certifique-se de que essa linha está sendo executada
        return repository.save(produto); // Deve invocar o método save
    }

    public Produto atualizarTipoProduto(Produto produto, TipoCategoria novoTipo) {
        produto.setTipo(novoTipo);
        return repository.save(produto); // Salva a mudança no repositório
    }


    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscar(Produto produtoFiltro) {
        Example example = Example.of(produtoFiltro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void validar(Produto produto) {
        // Validação da descrição do produto
        if (produto.getDescricao() == null || produto.getDescricao().trim().isEmpty()) {
            throw new RegraNegocioException("Informe uma descrição válida.");
        }

        // Validação do código do produto
        if (produto.getCodigoProduto() == null || produto.getCodigoProduto() <= 0) {
            throw new RegraNegocioException("Informe um código de produto válido.");
        }

        // Validação do nome do produto
        if (produto.getNomeProduto() == null || produto.getNomeProduto().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um nome de produto válido.");
        }

        // Validação do peso
        if (produto.getPeso() == null || produto.getPeso().compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Informe um peso válido para o produto.");
        }

        // Validação da data de produção
        if (produto.getDataProducao() == null || produto.getDataProducao().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Informe uma data de produção válida.");
        }

        // Validação do tipo de categoria
        if (produto.getTipo() == null) {
            throw new RegraNegocioException("Informe um tipo de categoria.");
        }

        // Validação da data de validade
        if (produto.getDataValidade() == null) {
            throw new RegraNegocioException("Informe uma data de validade válida.");
        }

        if (produto.getDataValidade().isBefore(produto.getDataProducao().plusDays(30))) {
            throw new RegraNegocioException("A data de validade deve ser pelo menos 30 dias após a data de produção.");
        }
    }

    public List<Produto> obterPorNome(String nomePrdouto) {
        return repository.findByNomeProdutoContainingIgnoreCase(nomePrdouto);
    }

    @Override
    public Optional<Produto> obterPorCodigo(Integer codigoProduto){
        return  repository.findByCodigoProduto(codigoProduto);
    }


    @Override
    public Optional<Produto> obterPorId(Long id) {
        return repository.findById(id);
    }
}
