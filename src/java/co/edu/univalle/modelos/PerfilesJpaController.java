/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.modelos;

import co.edu.univalle.entidades.Perfiles;
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
public class PerfilesJpaController implements Serializable {

    public PerfilesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Perfiles perfiles) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(perfiles);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerfiles(perfiles.getCodigoPerfil()) != null) {
                throw new PreexistingEntityException("Perfiles " + perfiles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Perfiles perfiles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            perfiles = em.merge(perfiles);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = perfiles.getCodigoPerfil();
                if (findPerfiles(id) == null) {
                    throw new NonexistentEntityException("The perfiles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfiles perfiles;
            try {
                perfiles = em.getReference(Perfiles.class, id);
                perfiles.getCodigoPerfil();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfiles with id " + id + " no longer exists.", enfe);
            }
            em.remove(perfiles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Perfiles> findPerfilesEntities() {
        return findPerfilesEntities(true, -1, -1);
    }

    public List<Perfiles> findPerfilesEntities(int maxResults, int firstResult) {
        return findPerfilesEntities(false, maxResults, firstResult);
    }

    private List<Perfiles> findPerfilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Perfiles.class));
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

    public Perfiles findPerfiles(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Perfiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Perfiles> rt = cq.from(Perfiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
