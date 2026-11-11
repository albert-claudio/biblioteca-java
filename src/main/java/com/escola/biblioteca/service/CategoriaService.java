package com.escola.biblioteca.service;

import com.escola.biblioteca.dto.CategoriaRequest;
import com.escola.biblioteca.dto.CategoriaResponse;
import com.escola.biblioteca.entity.Categoria;
import com.escola.biblioteca.exception.RecursoNaoEncontradoException;
import com.escola.biblioteca.exception.RegraDeNegocioException;
import com.escola.biblioteca.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponse cadastrar(CategoriaRequest request) {
        if (categoriaRepository.findByNome(request.nome()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma categoria com o nome " + request.nome());
        }
        return CategoriaResponse.from(
                categoriaRepository.save(new Categoria(request.nome(), request.descricao()))
        );
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(CategoriaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return CategoriaResponse.from(buscarEntidade(id));
    }

    public Categoria buscarEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com o id " + id));
    }
}
