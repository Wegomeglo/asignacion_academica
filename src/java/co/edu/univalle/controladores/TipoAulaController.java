package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.TipoAula;
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
import co.edu.univalle.modelos.TipoAulaJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;

@ManagedBean(name = "tipoAulaController")
@SessionScoped
public class TipoAulaController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private TipoAula primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private TipoAulaJpaController jpaController = null;
    private List<TipoAula> consultaTabla;
    private List<TipoAula> filtro;
    private TipoAula selected;

    public TipoAulaController() {
        setColumnTemplate("nombre");
        createDynamicColumns();
    }

    public List<TipoAula> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT t FROM TipoAula t WHERE t.idEstado=:ESTADO ORDER BY t.descripcionTipoAula");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    private TipoAulaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new TipoAulaJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
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
                selected.setDescripcionTipoAula(selected.getDescripcionTipoAula().toUpperCase());
                selected.setIdEstado(ACTIVO);
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("TipoAulaCreated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("TipoAulaError"));
            }
        } else {
            try {
                selected.setDescripcionTipoAula(selected.getDescripcionTipoAula().toUpperCase());
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("TipoAulaUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception ex) {
                Logger.getLogger(TipoAulaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        selected = new TipoAula();
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findTipoAula(getPrimaryKey().getIdTipoAula());
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findTipoAula(getPrimaryKey().getIdTipoAula());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                update();
            }
        }
    }

    /**
     * @return the primaryKey
     */
    public TipoAula getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(TipoAula primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the filtro
     */
    public List<TipoAula> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<TipoAula> filtro) {
        this.filtro = filtro;
    }

    /**
     * @return the selected
     */
    public TipoAula getSelected() {
        if (selected == null) {
            selected = new TipoAula();
        }
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(TipoAula selected) {
        this.selected = selected;
    }

    @FacesConverter(forClass = TipoAula.class)
    public static class TipoAulaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TipoAulaController controller = (TipoAulaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tipoAulaController");
            return controller.getJpaController().findTipoAula(Integer.parseInt(value));
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
            if (object instanceof TipoAula) {
                TipoAula o = (TipoAula) object;
                return "" + o.getIdTipoAula();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TipoAula.class.getName());
            }
        }

    }

}
