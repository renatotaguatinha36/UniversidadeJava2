package org.example.repository;


Exemplo de classe DAO que executa as operações: salvar, alterar, remover, consultar por id, consultar todos os emprestimos, consultar a quantidade de emprestimos de um livro e consultar todos os emprestimos de um livro.

        package br.universidadejava.jpa.exemplo.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import br.universidadejava.jpa.exemplo.modelo.Emprestimo;
import org.example.entity.Emprestimo;

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