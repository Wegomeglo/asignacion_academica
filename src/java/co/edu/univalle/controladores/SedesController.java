package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Sedes;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.SedesJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;

@ManagedBean(name = "sedesController")
@SessionScoped
public class SedesController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private Sedes primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private SedesJpaController jpaController = null;
    private Sedes selected = new Sedes();
    private List<Sedes> consultaTabla;
    private List<Sedes> filtro;

    public SedesController() {
        setColumnTemplate("nombre");
        createDynamicColumns();
    }

    public List<Sedes> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT a FROM Sedes a WHERE a.idEstado=:ESTADO ORDER BY a.descripcionSede");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    private SedesJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new SedesJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
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
                selected.setDescripcionSede(selected.getDescripcionSede().toUpperCase());
                selected.setIdEstado(ACTIVO);
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("SedesCreated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("SedesError"));
            }
        } else {
            try {
                selected.setDescripcionSede(selected.getDescripcionSede().toUpperCase());
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("SedesUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception ex) {
                Logger.getLogger(SedesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        selected = new Sedes();
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findSedes(getPrimaryKey().getIdSede());
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findSedes(getPrimaryKey().getIdSede());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                update();
            }
        }
    }

    /**
     * @return the primaryKey
     */
    public Sedes getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Sedes primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the filtro
     */
    public List<Sedes> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<Sedes> filtro) {
        this.filtro = filtro;
    }

    /**
     * @return the selected
     */
    public Sedes getSelected() {
        if (selected == null) {
            selected = new Sedes();
        }
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Sedes selected) {
        this.selected = selected;
    }

    @FacesConverter(forClass = Sedes.class)
    public static class HorariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SedesController controller = (SedesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sedesController");
            return controller.getJpaController().findSedes(Integer.parseInt(value));
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
            if (object instanceof Sedes) {
                Sedes o = (Sedes) object;
                return "" + o.getIdSede();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Sedes.class.getName());
            }
        }

    }

}
