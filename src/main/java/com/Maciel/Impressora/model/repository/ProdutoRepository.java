package com.Maciel.Impressora.model.repository;

import com.Maciel.Impressora.model.entity.Produto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByNomeProduto(String nomeProduto);

    boolean existsByCodigoProduto(Integer codigoProduto);

    Optional<Produto> findByNomeProduto(String nomeProduto);

    List<Produto> findByNomeProdutoContainingIgnoreCase(String nome);

    Optional<Produto> findByCodigoProduto(Integer codigoProduto);



}

