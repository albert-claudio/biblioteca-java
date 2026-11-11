package com.escola.biblioteca.service;

import com.escola.biblioteca.dto.CarteirinhaRequest;
import com.escola.biblioteca.dto.EmprestimoResponse;
import com.escola.biblioteca.dto.LeitorRequest;
import com.escola.biblioteca.dto.LeitorResponse;
import com.escola.biblioteca.entity.Carteirinha;
import com.escola.biblioteca.entity.Leitor;
import com.escola.biblioteca.exception.RecursoNaoEncontradoException;
import com.escola.biblioteca.exception.RegraDeNegocioException;
import com.escola.biblioteca.repository.CarteirinhaRepository;
import com.escola.biblioteca.repository.EmprestimoRepository;
import com.escola.biblioteca.repository.LeitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DESAFIO 1 - cadastro do leitor junto com a sua carteirinha (OneToOne).
 * DESAFIO 2 - consulta dos empréstimos do leitor (OneToMany).
 */
@Service
public class LeitorService {

    private final LeitorRepository leitorRepository;
    private final CarteirinhaRepository carteirinhaRepository;
    private final EmprestimoRepository emprestimoRepository;

    public LeitorService(LeitorRepository leitorRepository,
                         CarteirinhaRepository carteirinhaRepository,
                         EmprestimoRepository emprestimoRepository) {
        this.leitorRepository = leitorRepository;
        this.carteirinhaRepository = carteirinhaRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public LeitorResponse cadastrar(LeitorRequest request) {
        if (leitorRepository.existsByEmail(request.email())) {
            throw new RegraDeNegocioException("Já existe um leitor cadastrado com o e-mail " + request.email());
        }
        if (carteirinhaRepository.findByNumero(request.carteirinha().numero()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma carteirinha com o número " + request.carteirinha().numero());
        }

        Leitor leitor = new Leitor(request.nome(), request.email(), request.telefone(), request.dataNascimento());
        // Os dois lados do OneToOne são preenchidos para manter a FK consistente.
        Carteirinha carteirinha = criarCarteirinha(request.carteirinha());
        carteirinha.setLeitor(leitor);
        leitor.setCarteirinha(carteirinha);
        return LeitorResponse.from(leitorRepository.save(leitor));
    }

    @Transactional(readOnly = true)
    public List<LeitorResponse> listar() {
        return leitorRepository.findAll().stream().map(LeitorResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public LeitorResponse buscarPorId(Long id) {
        return LeitorResponse.from(buscarEntidade(id));
    }

    @Transactional
    public LeitorResponse atualizar(Long id, LeitorRequest request) {
        Leitor leitor = buscarEntidade(id);
        leitor.setNome(request.nome());
        leitor.setTelefone(request.telefone());
        leitor.setDataNascimento(request.dataNascimento());

        if (!leitor.getEmail().equalsIgnoreCase(request.email())) {
            if (leitorRepository.existsByEmail(request.email())) {
                throw new RegraDeNegocioException("Já existe um leitor cadastrado com o e-mail " + request.email());
            }
            leitor.setEmail(request.email());
        }

        if (leitor.getCarteirinha() == null) {
            Carteirinha carteirinha = criarCarteirinha(request.carteirinha());
            carteirinha.setLeitor(leitor);
            leitor.setCarteirinha(carteirinha);
        } else {
            Carteirinha carteirinha = leitor.getCarteirinha();
            carteirinha.setNumero(request.carteirinha().numero());
            carteirinha.setDataEmissao(request.carteirinha().dataEmissao());
            carteirinha.setDataValidade(request.carteirinha().dataValidade());
            carteirinha.setTipo(request.carteirinha().tipo());
        }

        return LeitorResponse.from(leitorRepository.save(leitor));
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarEmprestimos(Long leitorId) {
        buscarEntidade(leitorId);
        return emprestimoRepository.findByLeitorIdOrderByDataEmprestimoDesc(leitorId)
                .stream().map(EmprestimoResponse::from).toList();
    }

    private Carteirinha criarCarteirinha(CarteirinhaRequest request) {
        return new Carteirinha(request.numero(), request.dataEmissao(), request.dataValidade(), request.tipo());
    }

    private Leitor buscarEntidade(Long id) {
        return leitorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Leitor não encontrado com o id " + id));
    }
}
