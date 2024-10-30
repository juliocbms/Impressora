package com.Maciel.Impressora.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;

@Entity
@Table(name = "etiquetas", schema = "risco")
@Builder
@Data
public class Etiqueta {

    @Id
    @Column(name = "id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "codigo_Produto")
    private Produto codigoProduto;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "rastreabilidade")
    private Integer rastreabilidade;

    @Column(name = "numero_layout")
    private Integer numeroLayout;

    @Column(name = "data_producao")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataProducao;

}
