/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.modelos;

import co.edu.univalle.entidades.Historicos;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;
import co.edu.univalle.modelos.exceptions.PreexistingEntityException;

/**
 *
 * @author Aaron
 */
public class HistoricosJpaController implements Serializable {

    public HistoricosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historicos historicos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(historicos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoricos(historicos.getIdHistorico()) != null) {
                throw new PreexistingEntityException("Historicos " + historicos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historicos historicos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            historicos = em.merge(historicos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historicos.getIdHistorico();
                if (findHistoricos(id) == null) {
                    throw new NonexistentEntityException("The historicos with id " + id + " no longer exists.");
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
            Historicos historicos;
            try {
                historicos = em.getReference(Historicos.class, id);
                historicos.getIdHistorico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historicos with id " + id + " no longer exists.", enfe);
            }
            em.remove(historicos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Historicos> findHistoricosEntities() {
        return findHistoricosEntities(true, -1, -1);
    }

    public List<Historicos> findHistoricosEntities(int maxResults, int firstResult) {
        return findHistoricosEntities(false, maxResults, firstResult);
    }

    private List<Historicos> findHistoricosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historicos.class));
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

    public Historicos findHistoricos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historicos.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoricosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historicos> rt = cq.from(Historicos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
