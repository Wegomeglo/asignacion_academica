/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Planes;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.PlanesJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;

/**
 *
 * @author AndresAngel
 */
@ManagedBean(name = "planesController", eager = true)
@SessionScoped
public class PlanesController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private Planes primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private PlanesJpaController jpaController;
    private List<Planes> consultaTabla;
    private List<Planes> filtro;
    private Planes selected;
    private String id = "";

    public PlanesController() {
        setColumnTemplate("codigo nombre");
        createDynamicColumns();
    }

    public List<Planes> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT p FROM Planes p WHERE p.idEstado=:ESTADO ORDER BY p.descripcionPlan");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    /**
     * @return the jpaController
     */
    public PlanesJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new PlanesJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }

    public void update() {
        createOrUpdate(UPDATE);
    }

    public void create() {
        createOrUpdate(CREATE);
    }

    public void createOrUpdate(String opcion) {
        if (opcion.equals(CREATE)) {
            try {
                selected.setIdEstado(ACTIVO);
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PlanesCreated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PlanesError"));
            }
        } else {
            try {
                String newID = selected.getIdPlan();
                selected.setIdPlan(id);
                getJpaController().edit(selected);
                if (!newID.equals(id)) {
                    EntityManager em = getJpaController().getEntityManager();
                    em.getTransaction().begin();
                    Query query = em.createQuery("UPDATE Planes p SET p.idPlan=" + newID + " WHERE p.idPlan=:OLD");
                    query.setParameter("OLD", id);
                    query.executeUpdate();
                    em.getTransaction().commit();
                }
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PlanesUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
        }
        selected = new Planes();
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findPlanes(getPrimaryKey().getIdPlan());
            id = selected.getIdPlan();
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findPlanes(getPrimaryKey().getIdPlan());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                id = selected.getIdPlan();
                update();
            }
        }
    }

    /**
     * @return the primaryKey
     */
    public Planes getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Planes primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the filtro
     */
    public List<Planes> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<Planes> filtro) {
        this.filtro = filtro;
    }

    /**
     * @return the selected
     */
    public Planes getSelected() {
        if (selected == null) {
            selected = new Planes();
        }
        return selected;
    }

    public void setSelected(Planes selected) {
        this.selected = selected;
    }

    @FacesConverter(forClass = Planes.class)
    public static class PlanesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlanesController controller = (PlanesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "planesController");
            return controller.getJpaController().findPlanes(value);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Planes) {
                Planes o = (Planes) object;
                return o.getIdPlan();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Planes.class.getName());
            }
        }

    }

}
