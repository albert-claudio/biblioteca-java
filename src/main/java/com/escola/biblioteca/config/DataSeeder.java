package com.escola.biblioteca.config;

import com.escola.biblioteca.entity.Categoria;
import com.escola.biblioteca.entity.Carteirinha;
import com.escola.biblioteca.entity.Emprestimo;
import com.escola.biblioteca.entity.Leitor;
import com.escola.biblioteca.entity.Livro;
import com.escola.biblioteca.entity.StatusEmprestimo;
import com.escola.biblioteca.repository.CategoriaRepository;
import com.escola.biblioteca.repository.EmprestimoRepository;
import com.escola.biblioteca.repository.LeitorRepository;
import com.escola.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Popula o banco com dados de exemplo para demonstração dos três desafios.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final LeitorRepository leitorRepository;
    private final LivroRepository livroRepository;
    private final CategoriaRepository categoriaRepository;
    private final EmprestimoRepository emprestimoRepository;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    public DataSeeder(LeitorRepository leitorRepository,
                      LivroRepository livroRepository,
                      CategoriaRepository categoriaRepository,
                      EmprestimoRepository emprestimoRepository) {
        this.leitorRepository = leitorRepository;
        this.livroRepository = livroRepository;
        this.categoriaRepository = categoriaRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled || leitorRepository.count() > 0) {
            return;
        }

        // DESAFIO 3 - categorias
        Categoria tecnologia = categoriaRepository.save(new Categoria("Tecnologia", "Livros de tecnologia em geral"));
        Categoria programacao = categoriaRepository.save(new Categoria("Programação", "Linguagens e práticas de programação"));
        Categoria matematica = categoriaRepository.save(new Categoria("Matemática", "Matemática básica e avançada"));
        Categoria literatura = categoriaRepository.save(new Categoria("Literatura", "Obras literárias"));

        // DESAFIO 3 - livros com várias categorias
        Livro cleanCode = new Livro("Clean Code", "978-0132350884", 2008, 3);
        cleanCode.setCategorias(new LinkedHashSet<>(Set.of(tecnologia, programacao)));
        livroRepository.save(cleanCode);

        Livro effectiveJava = new Livro("Effective Java", "978-0134685991", 2018, 2);
        effectiveJava.setCategorias(new LinkedHashSet<>(Set.of(tecnologia, programacao)));
        livroRepository.save(effectiveJava);

        Livro matematicaDiscreta = new Livro("Matemática Discreta", "978-8576652744", 2017, 4);
        matematicaDiscreta.setCategorias(new LinkedHashSet<>(Set.of(matematica, tecnologia)));
        livroRepository.save(matematicaDiscreta);

        Livro domCasmurro = new Livro("Dom Casmurro", "978-8520416693", 1899, 6);
        domCasmurro.setCategorias(new LinkedHashSet<>(Set.of(literatura)));
        livroRepository.save(domCasmurro);

        // DESAFIO 1 - leitor com carteirinha (OneToOne)
        Leitor ana = new Leitor("Ana Souza", "ana.souza@escola.edu.br", "11988887777", LocalDate.of(2008, 5, 12));
        Carteirinha carteirinhaAna = new Carteirinha("CAR-2026-0001", LocalDate.now(), LocalDate.now().plusYears(1), "ESTUDANTE");
        carteirinhaAna.setLeitor(ana);
        ana.setCarteirinha(carteirinhaAna);
        Leitor anaSalva = leitorRepository.save(ana);

        Leitor bruno = new Leitor("Bruno Lima", "bruno.lima@escola.edu.br", "11999990000", LocalDate.of(2007, 11, 3));
        Carteirinha carteirinhaBruno = new Carteirinha("CAR-2026-0002", LocalDate.now(), LocalDate.now().plusYears(1), "ESTUDANTE");
        carteirinhaBruno.setLeitor(bruno);
        bruno.setCarteirinha(carteirinhaBruno);
        Leitor brunoSalvo = leitorRepository.save(bruno);

        // DESAFIO 2 - vários empréstimos para o mesmo leitor (OneToMany)
        emprestimoRepository.save(new Emprestimo(LocalDate.now().minusDays(10), LocalDate.now().plusDays(4),
                anaSalva, cleanCode));
        emprestimoRepository.save(new Emprestimo(LocalDate.now().minusDays(3), LocalDate.now().plusDays(11),
                anaSalva, domCasmurro));
        Emprestimo devolvido = new Emprestimo(LocalDate.now().minusDays(20), LocalDate.now().minusDays(6),
                brunoSalvo, matematicaDiscreta);
        devolvido.setStatus(StatusEmprestimo.DEVOLVIDO);
        devolvido.setDataDevolucao(LocalDate.now().minusDays(5));
        emprestimoRepository.save(devolvido);
    }
}
