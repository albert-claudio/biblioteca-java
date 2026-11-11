package com.escola.biblioteca.service;

import com.escola.biblioteca.dto.CarteirinhaRequest;
import com.escola.biblioteca.dto.CarteirinhaResponse;
import com.escola.biblioteca.entity.Carteirinha;
import com.escola.biblioteca.entity.Leitor;
import com.escola.biblioteca.exception.RecursoNaoEncontradoException;
import com.escola.biblioteca.exception.RegraDeNegocioException;
import com.escola.biblioteca.repository.CarteirinhaRepository;
import com.escola.biblioteca.repository.LeitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DESAFIO 1 - emissão/consulta de carteirinha.
 */
@Service
public class CarteirinhaService {

    private final CarteirinhaRepository carteirinhaRepository;
    private final LeitorRepository leitorRepository;

    public CarteirinhaService(CarteirinhaRepository carteirinhaRepository, LeitorRepository leitorRepository) {
        this.carteirinhaRepository = carteirinhaRepository;
        this.leitorRepository = leitorRepository;
    }

    @Transactional
    public CarteirinhaResponse emitir(Long leitorId, CarteirinhaRequest request) {
        Leitor leitor = leitorRepository.findById(leitorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitor não encontrado com o id " + leitorId));

        if (leitor.getCarteirinha() != null) {
            throw new RegraDeNegocioException("O leitor já possui uma carteirinha emitida");
        }
        if (carteirinhaRepository.findByNumero(request.numero()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma carteirinha com o número " + request.numero());
        }

        Carteirinha carteirinha = new Carteirinha(
                request.numero(), request.dataEmissao(), request.dataValidade(), request.tipo());
        carteirinha.setLeitor(leitor);
        leitor.setCarteirinha(carteirinha);

        return CarteirinhaResponse.from(carteirinhaRepository.save(carteirinha));
    }

    @Transactional(readOnly = true)
    public CarteirinhaResponse buscarPorLeitor(Long leitorId) {
        return carteirinhaRepository.findByLeitorId(leitorId)
                .map(CarteirinhaResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("O leitor " + leitorId + " não possui carteirinha"));
    }
}
