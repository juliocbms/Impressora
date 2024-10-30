package com.Maciel.Impressora.model.repository;

import com.Maciel.Impressora.model.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
}
