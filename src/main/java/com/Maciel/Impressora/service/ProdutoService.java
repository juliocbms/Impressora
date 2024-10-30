package com.Maciel.Impressora.service;

import com.Maciel.Impressora.model.entity.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    Produto salvarProduto(Produto produto);

    void deletar(Produto produto);

    Produto atualizar(Produto lancamento);

    List<Produto> buscar(Produto podutroFiltro);

    void validarCodigoProduto(Integer codigoProduto);

    void validarNome(String nomeProduto);

    void validar(Produto produto);

    Optional<Produto> obterPorId(Long id);



    List<Produto> obterPorNome(String nomeProduto);

    Optional<Produto> obterPorCodigo(Integer codigoProduto);
}
