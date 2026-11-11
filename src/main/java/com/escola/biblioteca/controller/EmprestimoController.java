package com.escola.biblioteca.controller;

import com.escola.biblioteca.dto.EmprestimoRequest;
import com.escola.biblioteca.dto.EmprestimoResponse;
import com.escola.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** DESAFIO 2 - endpoints dos empréstimos (OneToMany com Leitor). */
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> registrar(@Valid @RequestBody EmprestimoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.registrar(request));
    }

    @GetMapping
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoService.listarTodos();
    }

    @GetMapping("/leitor/{leitorId}")
    public List<EmprestimoResponse> listarPorLeitor(@PathVariable Long leitorId) {
        return emprestimoService.listarPorLeitor(leitorId);
    }

    @PostMapping("/{id}/devolucao")
    public EmprestimoResponse registrarDevolucao(@PathVariable Long id) {
        return emprestimoService.registrarDevolucao(id);
    }
}
