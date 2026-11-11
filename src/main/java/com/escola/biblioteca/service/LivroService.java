package com.escola.biblioteca.service;

import com.escola.biblioteca.dto.LivroRequest;
import com.escola.biblioteca.dto.LivroResponse;
import com.escola.biblioteca.entity.Categoria;
import com.escola.biblioteca.entity.Livro;
import com.escola.biblioteca.exception.RecursoNaoEncontradoException;
import com.escola.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * DESAFIO 3 - cadastro de livros vinculados a várias categorias (ManyToMany).
 */
@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final CategoriaService categoriaService;

    public LivroService(LivroRepository livroRepository, CategoriaService categoriaService) {
        this.livroRepository = livroRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional
    public LivroResponse cadastrar(LivroRequest request) {
        Livro livro = new Livro(
                request.titulo(),
                request.isbn(),
                request.anoPublicacao(),
                request.quantidadeExemplares() != null ? request.quantidadeExemplares() : 1
        );
        livro.setCategorias(resolverCategorias(request.categoriaIds()));
        return LivroResponse.from(livroRepository.save(livro));
    }

    @Transactional
    public LivroResponse atualizarCategorias(Long id, List<Long> categoriaIds) {
        Livro livro = buscarEntidade(id);
        Set<Categoria> categorias = resolverCategorias(categoriaIds);
        livro.getCategorias().clear();
        livro.getCategorias().addAll(categorias);
        return LivroResponse.from(livroRepository.save(livro));
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listar() {
        return livroRepository.findAll().stream().map(LivroResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listarPorCategoria(Long categoriaId) {
        categoriaService.buscarEntidade(categoriaId);
        return livroRepository.findByCategorias_Id(categoriaId).stream().map(LivroResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscarPorId(Long id) {
        return LivroResponse.from(buscarEntidade(id));
    }

    private Set<Categoria> resolverCategorias(List<Long> categoriaIds) {
        Set<Categoria> categorias = new LinkedHashSet<>();
        if (categoriaIds == null) {
            return categorias;
        }
        for (Long categoriaId : categoriaIds) {
            categorias.add(categoriaService.buscarEntidade(categoriaId));
        }
        return categorias;
    }

    private Livro buscarEntidade(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o id " + id));
    }
}
