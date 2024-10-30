package com.Maciel.Impressora.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import com.Maciel.Impressora.model.enums.TipoCategoria;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produtos", schema = "risco")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {


    @Id
    @Column(name = "produto_id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_barras")
    private Integer codigoProduto;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "rastreabilidade")
    private Integer rastreabilidade;

    @Column(name = "data_validade")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataValidade;

    @Column(name = "data_fabricacao")
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataProducao;

    @Column(name = "peso")
    private BigDecimal peso;


    @Column(name="tipo")
    @Enumerated(value = EnumType.STRING)
    private TipoCategoria tipo;

}
