package com.escola.biblioteca.repository;

import com.escola.biblioteca.entity.Emprestimo;
import com.escola.biblioteca.entity.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByLeitorIdOrderByDataEmprestimoDesc(Long leitorId);

    List<Emprestimo> findByStatus(StatusEmprestimo status);

    List<Emprestimo> findByLeitorIdAndStatus(Long leitorId, StatusEmprestimo status);
}
