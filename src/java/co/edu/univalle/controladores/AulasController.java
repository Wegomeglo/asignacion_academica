package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Aulas;
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
import co.edu.univalle.modelos.AulasJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;

@ManagedBean(name = "aulasController")
@SessionScoped
public class AulasController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private Aulas primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private AulasJpaController jpaController = null;
    private List<Aulas> consultaTabla;
    private List<Aulas> filtro;
    private Aulas selected = new Aulas();

    public AulasController() {
        setColumnTemplate("nombre sede capacidad");
        createDynamicColumns();
    }

    public List<Aulas> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT a FROM Aulas a WHERE a.idEstado=:ESTADO ORDER BY a.descripcionAula");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    private AulasJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AulasJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
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
                selected.setDescripcionAula(selected.getDescripcionAula().toUpperCase());
                selected.setIdEstado(ACTIVO);
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AulasCreated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("AulasError"));
            }
        } else {
            try {
                selected.setDescripcionAula(selected.getDescripcionAula().toUpperCase());
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AulasUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception ex) {
                Logger.getLogger(AulasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        selected = new Aulas();
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findAulas(getPrimaryKey().getIdAula());
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findAulas(getPrimaryKey().getIdAula());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                update();
            }
        }
    }

    /**
     * @return the primaryKey
     */
    public Aulas getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Aulas primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the filtro
     */
    public List<Aulas> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<Aulas> filtro) {
        this.filtro = filtro;
    }

    /**
     * @return the selected
     */
    public Aulas getSelected() {
        if (selected == null) {
            selected = new Aulas();
        }
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Aulas selected) {
        this.selected = selected;
    }

    @FacesConverter(forClass = Aulas.class)
    public static class HorariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            if (value.equals("null")) {
                return null;
            }
            AulasController controller = (AulasController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "aulasController");
            return controller.getJpaController().findAulas(Integer.parseInt(value));
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
            if (object instanceof Aulas) {
                Aulas o = (Aulas) object;
                return "" + o.getIdAula();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Aulas.class.getName());
            }
        }

    }

}
