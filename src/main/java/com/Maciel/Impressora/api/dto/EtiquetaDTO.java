package com.Maciel.Impressora.api.dto;

import com.Maciel.Impressora.model.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtiquetaDTO {

    private Produto codigoProduto;
    private String nomeProduto;
    private String descricao;
    private Integer rastreabilidade;
    private Integer numeroLayout;
    private LocalDate dataProducao;
}
