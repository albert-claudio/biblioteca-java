package com.escola.biblioteca.repository;

import com.escola.biblioteca.entity.Carteirinha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteirinhaRepository extends JpaRepository<Carteirinha, Long> {

    Optional<Carteirinha> findByNumero(String numero);

    Optional<Carteirinha> findByLeitorId(Long leitorId);
}
