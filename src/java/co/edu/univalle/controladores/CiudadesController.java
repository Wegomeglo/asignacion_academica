package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Ciudades;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.CiudadesJpaController;

@ManagedBean(name = "ciudadesController")
@SessionScoped
public class CiudadesController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private Ciudades primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private CiudadesJpaController jpaController = null;
    private List<Ciudades> consultaTabla;
    private List<Ciudades> filtro;
    private Ciudades selected = new Ciudades();

    public CiudadesController() {
        setColumnTemplate("nombre");
        createDynamicColumns();
    }

    public List<Ciudades> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT c FROM Ciudades c ORDER BY c.nombreCiudad");
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    private CiudadesJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new CiudadesJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            setSelected(getJpaController().findCiudades(getPrimaryKey().getIdCiudad()));
        }
    }

    /**
     * @return the primaryKey
     */
    public Ciudades getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Ciudades primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the selected
     */
    public Ciudades getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Ciudades selected) {
        this.selected = selected;
    }

    @FacesConverter(forClass = Ciudades.class)
    public static class ModalidadesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CiudadesController controller = (CiudadesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ciudadesController");
            return controller.getJpaController().findCiudades(value);
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
            if (object instanceof Ciudades) {
                Ciudades o = (Ciudades) object;
                return "" + o.getIdCiudad();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Ciudades.class.getName());
            }
        }

    }
}
