package com.Maciel.Impressora.controller;

import com.Maciel.Impressora.api.dto.ProdutoDTO;
import com.Maciel.Impressora.exception.RegraNegocioException;
import com.Maciel.Impressora.model.entity.Produto;
import com.Maciel.Impressora.service.ProdutoService;
import com.Maciel.Impressora.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> buscar(
            @RequestParam(value = "codigoProduto", required = false) Integer codigoProduto,
            @RequestParam(value = "nomeProduto", required = false) String nomeProduto,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "rastreabilidade", required = false) Integer rastreabilidade,
            @RequestParam(value = "dataProducao", required = false) LocalDate dataProducao,
            @RequestParam(value = "dataValidade", required = false) LocalDate dataValidade,
            @RequestParam(value = "peso", required = false) BigDecimal peso

    ) {
        Produto produtoFiltro = new Produto();
        produtoFiltro.setCodigoProduto(codigoProduto);
        produtoFiltro.setNomeProduto(nomeProduto);
        produtoFiltro.setDescricao(descricao);
        produtoFiltro.setRastreabilidade(rastreabilidade);
        produtoFiltro.setDataProducao(dataProducao);
        produtoFiltro.setDataValidade(dataValidade);
        produtoFiltro.setPeso(peso);


        List<Produto> produtos = service.buscar(produtoFiltro);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ProdutoDTO dto) {
        try {
            Produto entidade = converte(dto);
            entidade = service.salvarProduto(entidade);
            return new ResponseEntity<>(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/deletar/{codigoProduto}")
    public ResponseEntity<?> deletar(@PathVariable("codigoProduto") Integer codigoProduto) {
        return service.obterPorCodigo(codigoProduto).map(entidade -> {
            service.deletar(entidade);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>("Produto não encontrado na base de dados", HttpStatus.BAD_REQUEST));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterPorId(@PathVariable("id") Long id) {
        Optional<Produto> produto = service.obterPorId(id);

        if (!produto.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(produto.get());
    }

    @GetMapping("/buscarPorCodigo")
    public ResponseEntity<Optional<Produto>> buscarPorCodigo(@RequestParam Integer codigoProduto) {
        System.out.println("Código do produto recebido na API: " + codigoProduto); // Log para verificar o código recebido
        Optional<Produto> produto = service.obterPorCodigo(codigoProduto);
        if (produto.isPresent()) {
            return ResponseEntity.ok(produto);
        } else {
            System.out.println("Produto com código " + codigoProduto + " não encontrado."); // Log para produto não encontrado
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/atualizar/{codigoProduto}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Integer codigoProduto, @RequestBody ProdutoDTO dto) {
        try {
            // Tente encontrar o produto existente pelo código
            Optional<Produto> produtoExistente = service.obterPorCodigo(codigoProduto);

            if (!produtoExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
            }

            // Converte o DTO e mantém o ID do produto existente
            Produto entidade = converte(dto);
            entidade.setId(produtoExistente.get().getId()); // Mantém o ID do produto existente

            // Atualiza a entidade
            entidade = service.atualizar(entidade);

            return ResponseEntity.ok(entidade);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/buscarPorNome")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nomeProduto) {
        List<Produto> produtos = service.obterPorNome(nomeProduto);
        return ResponseEntity.ok(produtos);
    }
    private Produto converte(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setCodigoProduto(dto.getCodigoProduto());
        produto.setNomeProduto(dto.getNomeProduto());
        produto.setDescricao(dto.getDescricao());
        produto.setRastreabilidade(dto.getRastreabilidade());
        produto.setDataValidade(dto.getDataValidade());
        produto.setDataProducao(dto.getDataProducao());
        produto.setPeso(dto.getPeso());



        produto.setTipo(dto.getTipo());


        return produto;
    }

}