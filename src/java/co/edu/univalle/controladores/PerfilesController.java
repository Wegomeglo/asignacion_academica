package co.edu.univalle.controladores;

import static co.edu.univalle.controladores.Controller.ACTIVO;
import static co.edu.univalle.controladores.Controller.BUNDLE;
import static co.edu.univalle.controladores.Controller.CONSULTA;
import static co.edu.univalle.controladores.Controller.CREATE;
import static co.edu.univalle.controladores.Controller.UPDATE;
import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Perfiles;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Persistence;
import co.edu.univalle.modelos.PerfilesJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;
import co.edu.univalle.modelos.exceptions.PreexistingEntityException;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.Query;

@ManagedBean(name = "perfilesController")
@SessionScoped
public class PerfilesController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private PerfilesJpaController jpaController = null;
    private Perfiles primaryKey;
    private List<Perfiles> consultaTabla;
    private List<Perfiles> filtro;
    private Perfiles selected;
    private String id = "";
    
    public PerfilesController() {
        setColumnTemplate("codigoPerfil descripcionPerfil ");
        createDynamicColumns();
    }

    private PerfilesJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new PerfilesJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }

    /**
     * @return the selected
     */
    public Perfiles getSelected() {
        if (selected == null) {
            selected = new Perfiles();
        }
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Perfiles selected) {
        this.selected = selected;
    }

    public List<Perfiles> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT p FROM Perfiles p WHERE p.idEstado=:ESTADO ORDER BY p.codigoPerfil");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
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
                selected.setDescripcionPerfil(selected.getDescripcionPerfil().toUpperCase());
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PerfilCreated"));
            } catch (PreexistingEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PerfilError"));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PerfilError"));
            }
        } else {
            try {
                selected.setDescripcionPerfil(selected.getDescripcionPerfil().toUpperCase());
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PerfilUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
        }

        selected = new Perfiles();

    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findPerfiles(getPrimaryKey().getCodigoPerfil());
            id = selected.getCodigoPerfil();
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findPerfiles(getPrimaryKey().getCodigoPerfil());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                id = selected.getCodigoPerfil();
                update();
            }
        }
    }

    @FacesConverter(forClass = Perfiles.class)
    public static class PerfilesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PerfilesController controller = (PerfilesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "perfilesController");
            return controller.getJpaController().findPerfiles(value);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Perfiles) {
                Perfiles o = (Perfiles) object;
                return o.getCodigoPerfil();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Perfiles.class.getName());
            }
        }

    }

    public Perfiles getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Perfiles primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Perfiles> getFiltro() {
        return filtro;
    }

    public void setFiltro(List<Perfiles> filtro) {
        this.filtro = filtro;
    }

}
