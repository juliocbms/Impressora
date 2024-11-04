package com.Maciel.Impressora.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutosGUI {
    private String codigoProduto;
    private String nomeProduto;
    private String descricao;
    private String rastreabilidade;
    private String lote;
    private String dataProducao;
    private String dataValidade;
    private String peso;
    private String tipo;
    private String NumeroCaixa;
    private String EAN;
    /*comentario*/


}
