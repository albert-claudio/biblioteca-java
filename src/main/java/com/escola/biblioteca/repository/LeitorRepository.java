package com.escola.biblioteca.repository;

import com.escola.biblioteca.entity.Leitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeitorRepository extends JpaRepository<Leitor, Long> {

    Optional<Leitor> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * Consulta os empréstimos de um leitor (DESAFIO 2).
     */
    @Query("select e from Emprestimo e where e.leitor.id = :leitorId order by e.dataEmprestimo desc")
    List<Object[]> findEmprestimosDoLeitor(@Param("leitorId") Long leitorId);
}
