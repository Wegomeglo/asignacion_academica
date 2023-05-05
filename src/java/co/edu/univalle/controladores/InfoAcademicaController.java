/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.univalle.controladores;

import static co.edu.univalle.controladores.Controller.ACTIVO;
import static co.edu.univalle.controladores.Controller.BUNDLE;
import static co.edu.univalle.controladores.Controller.CONSULTA;
import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.InfoAcademica;
import co.edu.univalle.entidades.Planes;
import co.edu.univalle.entidades.Usuarios;
import co.edu.univalle.modelos.InfoAcademicaJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;
import co.edu.univalle.modelos.exceptions.PreexistingEntityException;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

@ManagedBean(name = "infoAcadeController", eager = true)
@SessionScoped
public class InfoAcademicaController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private UsuariosController usuariosController;
    private InfoAcademicaJpaController jpaController = null;
    private InfoAcademica infoacademica;
    private InfoAcademica primaryKey;
    private List<InfoAcademica> consultaTabla;
    private List<InfoAcademica> filtro;
    private Usuarios usuario;
    private String id = "";
    private InfoAcademica selected = new InfoAcademica();

    public InfoAcademicaController() {
        infoacademica = new InfoAcademica();
        setColumnTemplate("nivel_academico titulo_academico institucion");
        createDynamicColumns();
    }

    private InfoAcademicaJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new InfoAcademicaJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }

    public List<InfoAcademica> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT i FROM InfoAcademica i WHERE i.loginUsuario=:DOCENTE");
            query.setParameter("DOCENTE", getSelected().getLoginUsuario());
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
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UsuariosCreated"));
            } catch (PreexistingEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("UsuariosError"));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("UsuariosError"));
            }
        } else {
            try {
                String newID = selected.getLoginUsuario();
                getJpaController().edit(selected);
                if (!newID.equals(id)) {
                    EntityManager em = getJpaController().getEntityManager();
                    em.getTransaction().begin();
                    Query query = em.createQuery("UPDATE Usuarios SET loginUsuario=" + newID + " WHERE loginUsuario=:OLD");
                    query.setParameter("OLD", id);
                    query.executeUpdate();
                    em.getTransaction().commit();
                }
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UsuariosUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
        }

        selected = new InfoAcademica();

    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findUsuarios(getPrimaryKey().getIdInfoacademica());
            id = selected.getIdInfoacademica();
        } else {
            JsfUtil.addErrorMessage("Error al generar las consultas");
        }
    }

    public void desactivar() {
        if (primaryKey != null) {
            selected = getJpaController().findUsuarios(primaryKey.getIdInfoacademica());
            if (selected != null) {
                id = selected.getIdInfoacademica();
                try {
                    getJpaController().destroy(id);
                    selected = new InfoAcademica();
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(InfoAcademicaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public UsuariosController getUsuariosController() {
        return usuariosController;
    }

    public void setUsuariosController(UsuariosController usuariosController) {
        this.usuariosController = usuariosController;
    }

    public InfoAcademica getSelected() {
        return selected;
    }

    public void setSelected(InfoAcademica selected) {
        this.selected = selected;
    }

    public InfoAcademica getInfoacademica() {
        return infoacademica;
    }

    public void setInfoacademica(InfoAcademica infoacademica) {
        this.infoacademica = infoacademica;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public void setFiltro(List<InfoAcademica> filtro) {
        this.filtro = filtro;
    }

    public List<InfoAcademica> getFiltro() {
        return filtro;
    }

    public InfoAcademica getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(InfoAcademica primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
