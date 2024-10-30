package com.Maciel.Impressora.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "usuario" , schema = "risco")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @Column(name = "usuario_id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "nome_usuario")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    @JsonIgnore
    private String senha;


    @Column(name = "permissao")
    private String permissao;
}
