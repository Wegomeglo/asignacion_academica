/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.modelos;

import co.edu.univalle.entidades.Sedes;
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
public class SedesJpaController implements Serializable {

    public SedesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sedes sedes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(sedes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSedes(sedes.getIdSede()) != null) {
                throw new PreexistingEntityException("Sedes " + sedes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sedes sedes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            sedes = em.merge(sedes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sedes.getIdSede();
                if (findSedes(id) == null) {
                    throw new NonexistentEntityException("The sedes with id " + id + " no longer exists.");
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
            Sedes sedes;
            try {
                sedes = em.getReference(Sedes.class, id);
                sedes.getIdSede();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sedes with id " + id + " no longer exists.", enfe);
            }
            em.remove(sedes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sedes> findSedesEntities() {
        return findSedesEntities(true, -1, -1);
    }

    public List<Sedes> findSedesEntities(int maxResults, int firstResult) {
        return findSedesEntities(false, maxResults, firstResult);
    }

    private List<Sedes> findSedesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sedes.class));
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

    public Sedes findSedes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sedes.class, id);
        } finally {
            em.close();
        }
    }

    public int getSedesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sedes> rt = cq.from(Sedes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
