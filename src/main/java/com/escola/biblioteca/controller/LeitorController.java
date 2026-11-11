package com.escola.biblioteca.controller;

import com.escola.biblioteca.dto.EmprestimoResponse;
import com.escola.biblioteca.dto.LeitorRequest;
import com.escola.biblioteca.dto.LeitorResponse;
import com.escola.biblioteca.service.LeitorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leitores")
public class LeitorController {

    private final LeitorService leitorService;

    public LeitorController(LeitorService leitorService) {
        this.leitorService = leitorService;
    }

    @PostMapping
    public ResponseEntity<LeitorResponse> cadastrar(@Valid @RequestBody LeitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leitorService.cadastrar(request));
    }

    @GetMapping
    public List<LeitorResponse> listar() {
        return leitorService.listar();
    }

    @GetMapping("/{id}")
    public LeitorResponse buscarPorId(@PathVariable Long id) {
        return leitorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public LeitorResponse atualizar(@PathVariable Long id, @Valid @RequestBody LeitorRequest request) {
        return leitorService.atualizar(id, request);
    }

    /** DESAFIO 2 - lista os empréstimos de um leitor. */
    @GetMapping("/{id}/emprestimos")
    public List<EmprestimoResponse> listarEmprestimos(@PathVariable Long id) {
        return leitorService.listarEmprestimos(id);
    }
}
