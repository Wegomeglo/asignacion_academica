/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.vista;

import co.edu.univalle.controladores.AsignaturasController;
import co.edu.univalle.controladores.AulasController;
import co.edu.univalle.controladores.CiudadesController;
import co.edu.univalle.controladores.DepartamentosController;
import co.edu.univalle.controladores.HorariosController;
import co.edu.univalle.controladores.ModalidadesController;
import co.edu.univalle.controladores.PerfilesController;
import co.edu.univalle.controladores.PlanesController;
import co.edu.univalle.controladores.SedesController;
import co.edu.univalle.controladores.TipoAulaController;
import co.edu.univalle.controladores.UsuariosController;
import co.edu.univalle.entidades.Asignaturas;
import co.edu.univalle.entidades.Aulas;
import co.edu.univalle.entidades.Ciudades;
import co.edu.univalle.entidades.Horarios;
import co.edu.univalle.entidades.Modalidades;
import co.edu.univalle.entidades.Perfiles;
import co.edu.univalle.entidades.Planes;
import co.edu.univalle.entidades.Sedes;
import co.edu.univalle.entidades.TipoAula;
import co.edu.univalle.entidades.Usuarios;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;

/**
 *
 * @author Aaron
 */
@ManagedBean(name = "indexLoader")
@SessionScoped
public class IndexLoader implements Serializable {

    @ManagedProperty("#{asignaturasController}")
    private AsignaturasController asignaturasController;
    @ManagedProperty("#{aulasController}")
    private AulasController aulasController;
    @ManagedProperty("#{horariosController}")
    private HorariosController horariosController;
    @ManagedProperty("#{modalidadesController}")
    private ModalidadesController modalidadesController;
    @ManagedProperty("#{perfilesController}")
    private PerfilesController perfilesController;
    @ManagedProperty("#{planesController}")
    private PlanesController planesController;
    @ManagedProperty("#{sedesController}")
    private SedesController sedesController;
    @ManagedProperty("#{tipoAulaController}")
    private TipoAulaController tipoAulaController;
    @ManagedProperty("#{usuariosController}")
    private UsuariosController usuariosController;
    @ManagedProperty("#{ciudadesController}")
    private CiudadesController ciudadesController;
    @ManagedProperty("#{departamentosController}")
    private DepartamentosController departamentosController;
    @ManagedProperty("#{sesion}")
    private Sesion sesion;

    public void indexLoader() {
        getAsignaturasController().setSelected(new Asignaturas());
        getAulasController().setSelected(new Aulas());
        getCiudadesController().setSelected(new Ciudades());
        getHorariosController().setEvent(new DefaultScheduleEvent());
        getHorariosController().setEventModel(new DefaultScheduleModel());
        getHorariosController().setSelected(new Horarios());
        getModalidadesController().setSelected(new Modalidades());
        getPerfilesController().setSelected(new Perfiles());
        getPlanesController().setSelected(new Planes());
        getSedesController().setSelected(new Sedes());
        getTipoAulaController().setSelected(new TipoAula());
        getUsuariosController().setReactivar(false);
        getUsuariosController().setSelected(new Usuarios());
    }

    public void limpiarCruds() {
        getAsignaturasController().setSelected(new Asignaturas());
        getAulasController().setSelected(new Aulas());
        getCiudadesController().setSelected(new Ciudades());
        getHorariosController().setEvent(new DefaultScheduleEvent());
        getHorariosController().setEventModel(new DefaultScheduleModel());
        getHorariosController().setSelected(new Horarios());
        getModalidadesController().setSelected(new Modalidades());
        getPerfilesController().setSelected(new Perfiles());
        getPlanesController().setSelected(new Planes());
        getSedesController().setSelected(new Sedes());
        getTipoAulaController().setSelected(new TipoAula());
        getUsuariosController().setReactivar(false);
        getUsuariosController().setSelected(new Usuarios());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Actualizacion cancelada", "La actulizacion del registro a sido cancelada"));
    }

    /**
     * @return the asignaturasController
     */
    public AsignaturasController getAsignaturasController() {
        return asignaturasController;
    }

    /**
     * @param asignaturasController the asignaturasController to set
     */
    public void setAsignaturasController(AsignaturasController asignaturasController) {
        this.asignaturasController = asignaturasController;
    }

    /**
     * @return the aulasController
     */
    public AulasController getAulasController() {
        return aulasController;
    }

    /**
     * @param aulasController the aulasController to set
     */
    public void setAulasController(AulasController aulasController) {
        this.aulasController = aulasController;
    }

    /**
     * @return the horariosController
     */
    public HorariosController getHorariosController() {
        return horariosController;
    }

    /**
     * @param horariosController the horariosController to set
     */
    public void setHorariosController(HorariosController horariosController) {
        this.horariosController = horariosController;
    }

    /**
     * @return the modalidadesController
     */
    public ModalidadesController getModalidadesController() {
        return modalidadesController;
    }

    /**
     * @param modalidadesController the modalidadesController to set
     */
    public void setModalidadesController(ModalidadesController modalidadesController) {
        this.modalidadesController = modalidadesController;
    }

    /**
     * @return the perfilesController
     */
    public PerfilesController getPerfilesController() {
        return perfilesController;
    }

    /**
     * @param perfilesController the perfilesController to set
     */
    public void setPerfilesController(PerfilesController perfilesController) {
        this.perfilesController = perfilesController;
    }

    /**
     * @return the planesController
     */
    public PlanesController getPlanesController() {
        return planesController;
    }

    /**
     * @param planesController the planesController to set
     */
    public void setPlanesController(PlanesController planesController) {
        this.planesController = planesController;
    }

    /**
     * @return the sedesController
     */
    public SedesController getSedesController() {
        return sedesController;
    }

    /**
     * @param sedesController the sedesController to set
     */
    public void setSedesController(SedesController sedesController) {
        this.sedesController = sedesController;
    }

    /**
     * @return the tipoAulaController
     */
    public TipoAulaController getTipoAulaController() {
        return tipoAulaController;
    }

    /**
     * @param tipoAulaController the tipoAulaController to set
     */
    public void setTipoAulaController(TipoAulaController tipoAulaController) {
        this.tipoAulaController = tipoAulaController;
    }

    /**
     * @return the usuariosController
     */
    public UsuariosController getUsuariosController() {
        return usuariosController;
    }

    /**
     * @param usuariosController the usuariosController to set
     */
    public void setUsuariosController(UsuariosController usuariosController) {
        this.usuariosController = usuariosController;
    }

    /**
     * @return the sesion
     */
    public Sesion getSesion() {
        return sesion;
    }

    /**
     * @param sesion the sesion to set
     */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    /**
     * @return the ciudadesController
     */
    public CiudadesController getCiudadesController() {
        return ciudadesController;
    }

    /**
     * @param ciudadesController the ciudadesController to set
     */
    public void setCiudadesController(CiudadesController ciudadesController) {
        this.ciudadesController = ciudadesController;
    }

    /**
     * @return the departamentosController
     */
    public DepartamentosController getDepartamentosController() {
        return departamentosController;
    }

    /**
     * @param departamentosController the departamentosController to set
     */
    public void setDepartamentosController(DepartamentosController departamentosController) {
        this.departamentosController = departamentosController;
    }

}
