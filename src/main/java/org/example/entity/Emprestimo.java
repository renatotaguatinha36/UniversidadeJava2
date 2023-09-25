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
    Exemplo de classe DAO que executa as operações: salvar, alterar, remover, consultar por id, consultar todos os emprestimos, consultar a quantidade de emprestimos de um livro e consultar todos os emprestimos de um livro.

        package br.universidadejava.jpa.exemplo.dao;

        import java.util.List;
        import javax.persistence.EntityManager;
        import javax.persistence.EntityManagerFactory;
        import javax.persistence.Persistence;
        import javax.persistence.Query;
        import br.universidadejava.jpa.exemplo.modelo.Emprestimo;

/**
 * Classe utilizada para representar as operações sobre a entidade emprestimo.
 */
public class EmprestimoDAO {
    public EntityManager getEntityManager() {
        EntityManagerFactory factory = null;
        EntityManager entityManager = null;
        try {
            factory = Persistence.createEntityManagerFactory("UnidadeDePersistencia");
            entityManager = factory.createEntityManager();
        } finally {
            factory.close();
        }
        return entityManager;
    }

    public Emprestimo consultarPorId(Long id) {
        EntityManager entityManager = getEntityManager();
        Emprestimo emprestimo = null;
        try {
            emprestimo = entityManager.find(Emprestimo.class, id);
        } finally {
            entityManager.close();
        }
        return emprestimo;
    }

    public Emprestimo salvar(Emprestimo emprestimo) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            if(emprestimo.getId() == null) {
                entityManager.persist(emprestimo);
            } else {
                entityManager.merge(emprestimo);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return emprestimo;
    }

    public void apagar(Long id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Emprestimo emprestimo = entityManager.find(Emprestimo.class, id);
            entityManager.remove(emprestimo);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public List<Emprestimo> consultarTodos() {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery("Emprestimo.consultarTodos");
        return query.getResultList();
    }

    public Long getQtdEmprestimosPorLivro(Long id) {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery("Emprestimo.qtdEmprestimosPorLivro");
        query.setParameter("id", id);
        return (Long) query.getSingleResult();
    }

    public List<Emprestimo> consultarTodosPorTituloLivro(String tituloLivro) {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNamedQuery("Emprestimo.consultarTodosPorTituloLivro");
        query.setParameter("titulo", tituloLivro);
        return query.getResultList();
    }
}