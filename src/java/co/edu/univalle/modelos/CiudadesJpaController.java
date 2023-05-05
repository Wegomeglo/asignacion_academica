/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.modelos;

import co.edu.univalle.entidades.Ciudades;
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
public class CiudadesJpaController implements Serializable {

    public CiudadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudades ciudades) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ciudades);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCiudades(ciudades.getIdCiudad()) != null) {
                throw new PreexistingEntityException("Ciudades " + ciudades + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudades ciudades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ciudades = em.merge(ciudades);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ciudades.getIdCiudad();
                if (findCiudades(id) == null) {
                    throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.");
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
            Ciudades ciudades;
            try {
                ciudades = em.getReference(Ciudades.class, id);
                ciudades.getIdCiudad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudades with id " + id + " no longer exists.", enfe);
            }
            em.remove(ciudades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudades> findCiudadesEntities() {
        return findCiudadesEntities(true, -1, -1);
    }

    public List<Ciudades> findCiudadesEntities(int maxResults, int firstResult) {
        return findCiudadesEntities(false, maxResults, firstResult);
    }

    private List<Ciudades> findCiudadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudades.class));
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

    public Ciudades findCiudades(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudades.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudades> rt = cq.from(Ciudades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
