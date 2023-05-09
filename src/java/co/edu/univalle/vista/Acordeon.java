package co.edu.univalle.vista;

import co.edu.univalle.controladores.AsignaturasController;
import co.edu.univalle.controladores.AulasController;
import co.edu.univalle.controladores.ModalidadesController;
import co.edu.univalle.controladores.PeriodosController;
import co.edu.univalle.controladores.PlanesController;
import co.edu.univalle.controladores.SedesController;
import co.edu.univalle.controladores.TipoAulaController;
import co.edu.univalle.controladores.UsuariosController;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "acordeon", eager = true)
@SessionScoped
public class Acordeon implements Serializable {

    @ManagedProperty("#{asignaturasController}")
    private AsignaturasController asignaturasController;
    @ManagedProperty("#{aulasController}")
    private AulasController aulasController;
    @ManagedProperty("#{modalidadesController}")
    private ModalidadesController modalidadesController;
    @ManagedProperty("#{planesController}")
    private PlanesController planesController;
    @ManagedProperty("#{sedesController}")
    private SedesController sedesController;
    @ManagedProperty("#{tipoAulaController}")
    private TipoAulaController tipoAulaController;
    @ManagedProperty("#{usuariosController}")
    private UsuariosController usuariosController;
    @ManagedProperty("#{periodosController}")
    private PeriodosController periodosController;
    private String tituloTabla = "";
    private String tituloFormulario = "";

    public void llenar() {
        String value = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        if ("/administrador/registrousuarios.xhtml".equals(value)) {
            tituloTabla = "Consultar Usuarios";
            if (getUsuariosController().getSelected().getLoginUsuario() == null) {
                tituloFormulario = "Guardar Usuario";
            } else {
                tituloFormulario = "Actualizar Usuario";
            }
        } else if ("/docente/hojavida.xhtml".equals(value)) {
            tituloTabla = "Datos Académicos";
            if (getUsuariosController().getSelected().getLoginUsuario() == null) {
                tituloFormulario = "Datos Basicos";
            } else {
                tituloFormulario = "Actualizar Datos Basicos";
            }
        } else if ("/administrador/registroplanes.xhtml".equals(value)) {
            tituloTabla = "Consultar Planes";
            if (getPlanesController().getSelected().getIdPlan() == null) {
                tituloFormulario = "Guardar Plan";
            } else {
                tituloFormulario = "Actualizar Plan";
            }
        } else if ("/administrador/registroasignaturas.xhtml".equals(value)) {
            tituloTabla = "Consultar Asignaturas";
            if (getAsignaturasController().getSelected().getCodigoAsignatura() == null) {
                tituloFormulario = "Guardar Asignatura";
            } else {
                tituloFormulario = "Actualizar Asignatura";
            }
        } else if ("/administrador/registrosedes.xhtml".equals(value)) {
            tituloTabla = "Consultar Sede";
            if (getSedesController().getSelected().getIdSede() == null) {
                tituloFormulario = "Guardar Sede";
            } else {
                tituloFormulario = "Actualizar Sede";
            }
        } else if ("/administrador/registroaulas.xhtml".equals(value)) {
            tituloTabla = "Consultar Aulas";
            if (getAulasController().getSelected().getIdAula() == null) {
                tituloFormulario = "Guardar Aula";
            } else {
                tituloFormulario = "Actualizar Aula";
            }
        } else if ("/administrador/registroperfiles.xhtml".equals(value)) {
            tituloTabla = "Consultar Perfil";
            if (getAulasController().getSelected().getIdAula() == null) {
                tituloFormulario = "Guardar Perfil";
            } else {
                tituloFormulario = "Actualizar Perfil";
            }
        } else if ("/administrador/registrotipoaula.xhtml".equals(value)) {
            tituloTabla = "Consultar Tipos de Aula";
            if (getTipoAulaController().getSelected().getIdTipoAula() == null) {
                tituloFormulario = "Guardar Tipo de Aula";
            } else {
                tituloFormulario = "Actualizar Tipo de Aula";
            }
        } else if ("/administrador/registromodalidades.xhtml".equals(value)) {
            tituloTabla = "Consultar Modalidades";
            if (getModalidadesController().getSelected().getIdModalidad() == null) {
                tituloFormulario = "Guardar Modalidad";
            } else {
                tituloFormulario = "Actualizar Modalidad";
            }
        } else if ("/administrador/registroperiodos.xhtml".equals(value)) {
            tituloTabla = "Consultar Periodos";
            if ("" + getPeriodosController().getSelected().getIdPeriodo() == null) {
                tituloFormulario = "Actualizar Periodo";
            } else {
                tituloFormulario = "Guardar Periodo";
            }
        }
    }

    /**
     * @return the tituloTabla
     */
    public String getTituloTabla() {
        llenar();
        return tituloTabla;
    }

    /**
     * @param tituloTabla the tituloTabla to set
     */
    public void setTituloTabla(String tituloTabla) {
        llenar();
        this.tituloTabla = tituloTabla;
    }

    /**
     * @return the tituloFormulario
     */
    public String getTituloFormulario() {
        llenar();
        return tituloFormulario;
    }

    /**
     * @param tituloFormulario the tituloFormulario to set
     */
    public void setTituloFormulario(String tituloFormulario) {
        llenar();
        this.tituloFormulario = tituloFormulario;
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

    public PeriodosController getPeriodosController() {
        return periodosController;
    }

    public void setPeriodosController(PeriodosController periodosController) {
        this.periodosController = periodosController;
    }

}
