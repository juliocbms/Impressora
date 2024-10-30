package com.Maciel.Impressora.api.dto;

import com.Maciel.Impressora.model.entity.Categoria;
import com.Maciel.Impressora.model.enums.TipoCategoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Integer codigoProduto;
    private String nomeProduto;
    private String descricao;
    private Integer rastreabilidade;
    private LocalDate dataValidade;
    private LocalDate dataProducao;
    private BigDecimal peso;
    private Categoria categoria;
    private TipoCategoria tipo;
}
