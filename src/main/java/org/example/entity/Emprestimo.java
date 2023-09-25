package org.example.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe utilizada para representar o emprestimo de um livro para um cliente.
 */
@NamedQueries({
        @NamedQuery(name = "Emprestimo.consultarTodos",
                query= "SELECT e FROM Emprestimo e"),

        @NamedQuery(name = "Emprestimo.qtdEmprestimosPorLivro",
                query = " SELECT count(e) " +
                        " FROM Emprestimo e " +
                        " WHERE e.livro.id = :id "),

        @NamedQuery(name = "Emprestimo.consultarTodosPorTituloLivro",
                query = " SELECT e " +
                        " FROM Emprestimo e, Livro l " +
                        " WHERE e.livro.id = l.id " +
                        " AND l.titulo LIKE :titulo ")
})
@Entity
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 7516813189218268079L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    private Livro livro;
    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    private Cliente cliente;
    @Temporal(TemporalType.DATE)
    private Date dataEmprestimo;
    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Date getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
}
