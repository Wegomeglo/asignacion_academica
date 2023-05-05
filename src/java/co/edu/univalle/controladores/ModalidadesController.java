package co.edu.univalle.controladores;

import static co.edu.univalle.controladores.Controller.ACTIVO;
import static co.edu.univalle.controladores.Controller.CONSULTA;
import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Modalidades;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.ModalidadesJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;

/**
 *
 * @author Aaron
 */
@ManagedBean(name = "modalidadesController")
@SessionScoped
public class ModalidadesController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private Modalidades primaryKey;
    ModalidadesJpaController jpaController;
    List<Modalidades> consultaTabla;
    private List<Modalidades> filtro;
    private Modalidades selected;

    public ModalidadesController() {
        setColumnTemplate("nombre");
        createDynamicColumns();
    }

    public List<Modalidades> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT m FROM Modalidades m WHERE m.idEstado=:ESTADO ORDER BY m.idModalidad");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    private ModalidadesJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new ModalidadesJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
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
                selected.setDescripcionModalidad(selected.getDescripcionModalidad().toUpperCase());
                selected.setIdEstado(ACTIVO);
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("ModalidadesCreated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("ModalidadesError"));
            }
        } else {
            try {
                selected.setDescripcionModalidad(selected.getDescripcionModalidad().toUpperCase());
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("ModalidadesUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
        }
        selected = new Modalidades();
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findModalidades(getPrimaryKey().getIdModalidad());
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findModalidades(getPrimaryKey().getIdModalidad());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                update();
            }
        }
    }

    /**
     * @return the selected
     */
    public Modalidades getSelected() {
        if (selected == null) {
            selected = new Modalidades();
        }
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Modalidades selected) {
        this.selected = selected;
    }

    @FacesConverter(forClass = Modalidades.class)
    public static class ModalidadesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModalidadesController controller = (ModalidadesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modalidadesController");
            return controller.getJpaController().findModalidades(Integer.parseInt(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Modalidades) {
                Modalidades o = (Modalidades) object;
                return "" + o.getIdModalidad();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Modalidades.class.getName());
            }
        }

    }

    /**
     * @return the primaryKey
     */
    public Modalidades getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Modalidades primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the filtro
     */
    public List<Modalidades> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<Modalidades> filtro) {
        this.filtro = filtro;
    }
}
