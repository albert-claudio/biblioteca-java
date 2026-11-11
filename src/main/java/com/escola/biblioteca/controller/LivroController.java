package com.escola.biblioteca.controller;

import com.escola.biblioteca.dto.LivroRequest;
import com.escola.biblioteca.dto.LivroResponse;
import com.escola.biblioteca.service.LivroService;
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

/** DESAFIO 3 - endpoints dos livros e suas categorias (ManyToMany). */
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody LivroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.cadastrar(request));
    }

    @GetMapping
    public List<LivroResponse> listar() {
        return livroService.listar();
    }

    @GetMapping("/{id}")
    public LivroResponse buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<LivroResponse> listarPorCategoria(@PathVariable Long categoriaId) {
        return livroService.listarPorCategoria(categoriaId);
    }

    @PutMapping("/{id}/categorias")
    public LivroResponse atualizarCategorias(@PathVariable Long id, @RequestBody List<Long> categoriaIds) {
        return livroService.atualizarCategorias(id, categoriaIds);
    }
}
