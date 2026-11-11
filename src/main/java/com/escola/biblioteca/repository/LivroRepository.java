package com.escola.biblioteca.repository;

import com.escola.biblioteca.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByCategorias_Id(Long categoriaId);

    @Query("select l from Livro l join l.categorias c where c.nome like %:nome%")
    List<Livro> findByCategoriaNome(@Param("nome") String nome);
}
