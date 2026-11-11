package com.escola.biblioteca.controller;

import com.escola.biblioteca.dto.CarteirinhaRequest;
import com.escola.biblioteca.dto.CarteirinhaResponse;
import com.escola.biblioteca.service.CarteirinhaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** DESAFIO 1 - endpoints da carteirinha (OneToOne com Leitor). */
@RestController
@RequestMapping("/api/carteirinhas")
public class CarteirinhaController {

    private final CarteirinhaService carteirinhaService;

    public CarteirinhaController(CarteirinhaService carteirinhaService) {
        this.carteirinhaService = carteirinhaService;
    }

    @PostMapping("/leitor/{leitorId}")
    public ResponseEntity<CarteirinhaResponse> emitir(@PathVariable Long leitorId,
                                                      @Valid @RequestBody CarteirinhaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carteirinhaService.emitir(leitorId, request));
    }

    @GetMapping("/leitor/{leitorId}")
    public CarteirinhaResponse buscarPorLeitor(@PathVariable Long leitorId) {
        return carteirinhaService.buscarPorLeitor(leitorId);
    }
}
