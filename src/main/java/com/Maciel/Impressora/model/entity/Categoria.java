package com.Maciel.Impressora.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "categorias", schema = "risco")
@Builder
@Data
public class Categoria {


    @Id
    @Column(name = "categoria_id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_categoria")
    private String categoria;
}
