/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.modelos;

import co.edu.univalle.entidades.Aulas;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;

/**
 *
 * @author Aaron
 */
public class AulasJpaController implements Serializable {

    public AulasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aulas aulas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(aulas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aulas aulas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            aulas = em.merge(aulas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aulas.getIdAula();
                if (findAulas(id) == null) {
                    throw new NonexistentEntityException("The aulas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aulas aulas;
            try {
                aulas = em.getReference(Aulas.class, id);
                aulas.getIdAula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aulas with id " + id + " no longer exists.", enfe);
            }
            em.remove(aulas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aulas> findAulasEntities() {
        return findAulasEntities(true, -1, -1);
    }

    public List<Aulas> findAulasEntities(int maxResults, int firstResult) {
        return findAulasEntities(false, maxResults, firstResult);
    }

    private List<Aulas> findAulasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aulas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Aulas findAulas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aulas.class, id);
        } finally {
            em.close();
        }
    }

    public int getAulasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aulas> rt = cq.from(Aulas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
