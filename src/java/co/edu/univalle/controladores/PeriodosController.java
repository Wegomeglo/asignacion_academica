package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Perfiles;
import co.edu.univalle.entidades.Periodos;
import co.edu.univalle.entidades.Planes;
import co.edu.univalle.entidades.Usuarios;
import co.edu.univalle.modelos.PeriodosJpaController1;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.UsuariosJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;
import co.edu.univalle.modelos.exceptions.PreexistingEntityException;
import co.edu.univalle.utilidades.Cifrador;
import co.edu.univalle.utilidades.Constantes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "periodosController", eager = true)
@SessionScoped
public class PeriodosController extends Controller implements Serializable {

    private PeriodosJpaController1 jpaController = null;
    private Periodos primaryKey;
    private List<Periodos> consultaTabla;
    private List<Periodos> filtro;
    private Periodos selected;
    private int id = 0;

    public PeriodosController() {
        setColumnTemplate("año periodo nombre_Periodo");
        createDynamicColumns();
    }

    public List<Periodos> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT p FROM Periodos p WHERE p.idEstado=:ESTADO ORDER BY p.año");
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
                selected.setNombrePeriodo(selected.getNombrePeriodo().toUpperCase());
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PeriodosCreated"));
            } catch (PreexistingEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PeriodosError"));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PeriodosError"));
            }
        } else {
            try {
                selected.setNombrePeriodo(selected.getNombrePeriodo().toUpperCase());
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PeriodosUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
        }

        selected = new Periodos();

    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findPeriodo(getPrimaryKey().getIdPeriodo());
            id = selected.getIdPeriodo();
        }
    }

    public void desactivar() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findPeriodo(getPrimaryKey().getIdPeriodo());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                id = selected.getIdPeriodo();
                update();
            }
        }
    }

    private PeriodosJpaController1 getJpaController() {
        if (jpaController == null) {
            jpaController = new PeriodosJpaController1(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }

    public Periodos getSelected() {
        if (selected == null) {
            selected = new Periodos();
        }
        return selected;
    }

    public void setSelected(Periodos selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Periodos> getFiltro() {
        return filtro;
    }

    public void setFiltro(List<Periodos> filtro) {
        this.filtro = filtro;
    }

    public Periodos getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Periodos primaryKey) {
        this.primaryKey = primaryKey;
    }

}
