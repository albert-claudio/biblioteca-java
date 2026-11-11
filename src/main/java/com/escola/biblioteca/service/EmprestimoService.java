package com.escola.biblioteca.service;

import com.escola.biblioteca.dto.EmprestimoRequest;
import com.escola.biblioteca.dto.EmprestimoResponse;
import com.escola.biblioteca.entity.Emprestimo;
import com.escola.biblioteca.entity.Leitor;
import com.escola.biblioteca.entity.Livro;
import com.escola.biblioteca.entity.StatusEmprestimo;
import com.escola.biblioteca.exception.RecursoNaoEncontradoException;
import com.escola.biblioteca.exception.RegraDeNegocioException;
import com.escola.biblioteca.repository.EmprestimoRepository;
import com.escola.biblioteca.repository.LeitorRepository;
import com.escola.biblioteca.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * DESAFIO 2 - registro e acompanhamento dos empréstimos de um leitor.
 */
@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LeitorRepository leitorRepository;
    private final LivroRepository livroRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LeitorRepository leitorRepository,
                             LivroRepository livroRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.leitorRepository = leitorRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional
    public EmprestimoResponse registrar(EmprestimoRequest request) {
        Leitor leitor = leitorRepository.findById(request.leitorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitor não encontrado com o id " + request.leitorId()));

        if (!leitor.isAtivo()) {
            throw new RegraDeNegocioException("O leitor " + leitor.getNome() + " está inativo e não pode retirar livros");
        }

        if (leitor.getCarteirinha() == null) {
            throw new RegraDeNegocioException("O leitor " + leitor.getNome() + " não possui carteirinha válida");
        }

        Livro livro = livroRepository.findById(request.livroId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com o id " + request.livroId()));

        long emprestimosAtivos = emprestimoRepository
                .findByLeitorIdAndStatus(leitor.getId(), StatusEmprestimo.ATIVO).size();
        if (emprestimosAtivos >= 3) {
            throw new RegraDeNegocioException("O leitor já possui 3 empréstimos ativos (limite máximo permitido)");
        }

        LocalDate dataEmprestimo = request.dataEmprestimo() != null ? request.dataEmprestimo() : LocalDate.now();
        LocalDate devolucaoPrevista = request.dataDevolucaoPrevista() != null
                ? request.dataDevolucaoPrevista()
                : dataEmprestimo.plusDays(14);

        Emprestimo emprestimo = new Emprestimo(dataEmprestimo, devolucaoPrevista, leitor, livro);
        // Mantém a coleção do leitor consistente com o novo empréstimo (OneToMany).
        leitor.getEmprestimos().add(emprestimo);

        return EmprestimoResponse.from(emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public EmprestimoResponse registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado com o id " + emprestimoId));

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        return EmprestimoResponse.from(emprestimoRepository.save(emprestimo));
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarPorLeitor(Long leitorId) {
        if (!leitorRepository.existsById(leitorId)) {
            throw new RecursoNaoEncontradoException("Leitor não encontrado com o id " + leitorId);
        }
        return emprestimoRepository.findByLeitorIdOrderByDataEmprestimoDesc(leitorId)
                .stream().map(EmprestimoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoRepository.findAll().stream().map(EmprestimoResponse::from).toList();
    }
}
