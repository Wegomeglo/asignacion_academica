package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Asignaturas;
import co.edu.univalle.entidades.Historicos;
import co.edu.univalle.entidades.Horarios;
import co.edu.univalle.entidades.Usuarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.HorariosJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;
import co.edu.univalle.utilidades.ValidadorCruces;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

@ManagedBean(name = "horariosController")
@SessionScoped
public class HorariosController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String CARGA = "La carga del docente supera el horario";
    private static final String LUNES = "Lunes";
    private static final String MARTES = "Martes";
    private static final String MIERCOLES = "Miércoles";
    private static final String JUEVES = "Jueves";
    private static final String VIERNES = "Viernes";
    private static final String SABADO = "Sábado";
    private static final String DOMINGO = "Domingo";
    @ManagedProperty("#{usuariosController}")
    private UsuariosController usuariosController;
    @ManagedProperty("#{planesController}")
    private PlanesController planesController;
    @ManagedProperty("#{asignaturasController}")
    private AsignaturasController asignaturasController;
    @ManagedProperty("#{modalidadesController}")
    private ModalidadesController modalidadesController;
    @ManagedProperty("#{aulasController}")
    private AulasController aulasController;
    @ManagedProperty("#{tipoAulaController}")
    private TipoAulaController tipoAulaController;
    private Horarios primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private HorariosJpaController jpaController = null;
    private Horarios selected = new Horarios();
    private ScheduleModel eventModel = new DefaultScheduleModel();
    private DefaultScheduleEvent event = new DefaultScheduleEvent();
    private List<Horarios> consultaTabla;
    private List<Historicos> consultaTablaH;
    private List<Horarios> filtro;

    public HorariosController() {
        setColumnTemplate("año periodo semestre plan codigo_asignatura grupo dia hora_entrada hora_salida sede salon nombre_asignatura nombre_docente");
        createDynamicColumns();
    }

    public List<Horarios> getConsultaHorariosPrograma() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE h.idPlan=:PLAN AND h.idEstado.idEstado=:ESTADO ORDER BY h.cohorteHorario");
            query.setParameter("ESTADO", 1);
            if ("3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
                query.setParameter("PLAN", getUsuariosController().getUsuario().getIdPlan());
            } else {
                query.setParameter("PLAN", getPlanesController().getSelected());
            }
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
            consultaTabla = new ArrayList<Horarios>();
        }
        return consultaTabla;
    }
    
    

    public List<Horarios> getConsultaHorariosCohorte() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE h.idPlan=:PLAN AND h.cohorteHorario=:COHORTE AND h.idEstado.idEstado=:ESTADO");
            query.setParameter("ESTADO", 1);
            if ("3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
                query.setParameter("PLAN", getUsuariosController().getUsuario().getIdPlan());
            } else {
                query.setParameter("PLAN", getPlanesController().getSelected());
            }
            query.setParameter("COHORTE", selected.getCohorteHorario());
            return query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return new ArrayList<>();
    }

    public List<Integer> getConsultaCohortesConHorarios() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT DISTINCT h.cohorteHorario FROM Horarios h WHERE h.idPlan=:PLAN ORDER BY h.cohorteHorario");
            query.setParameter("PLAN", getPlanesController().getSelected());
            return query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return new ArrayList<>();
    }

    public List<Horarios> getConsultaHorariosGrupo() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE (h.idPlan!=:PLAN OR h.cohorteHorario!=:COHORTE) AND h.codigoAsignatura=:ASIGNATURA AND h.grupoHorario=:GRUPO AND h.idEstado.idEstado=:ESTADO");
            query.setParameter("ESTADO", 1);
            query.setParameter("PLAN", getPlanesController().getSelected());
            query.setParameter("COHORTE", selected.getCohorteHorario());
            query.setParameter("ASIGNATURA", getAsignaturasController().getSelected());
            query.setParameter("GRUPO", selected.getGrupoHorario());
            return query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return new ArrayList<>();
    }

    public List<Horarios> getConsultaAsignaturaDocente() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE h.loginUsuario=:DOCENTE AND h.idEstado.idEstado=:ESTADO ORDER BY h.idPlan.idPlan, h.cohorteHorario");
            query.setParameter("ESTADO", 1);
            query.setParameter("DOCENTE", getUsuariosController().getSelected());
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    public List<Horarios> getConsultaAula() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE h.idAula.idAula=:AULA");
            query.setParameter("AULA", getAulasController().getSelected().getIdAula());
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
            consultaTabla = new ArrayList<Horarios>();
        }
        return consultaTabla;
    }

    public List<Horarios> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE h.idEstado=:ESTADO ORDER BY h.idPlan.idPlan, h.cohorteHorario, h.loginUsuario.nombreUsuario, h.loginUsuario.apellidoUsuario");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }
    
    public List<Horarios> getConsultaTablaHistorico() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT h FROM Horarios h WHERE h.idEstado=:ESTADO ORDER BY h.idPlan.idPlan, h.cohorteHorario, h.loginUsuario.nombreUsuario, h.loginUsuario.apellidoUsuario");
            query.setParameter("ESTADO", ACTIVO);
            query.setParameter("DOCENTE", getUsuariosController().getSelected());
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    public void insertar() {
        selected.setIdEstado(ACTIVO);
        create();
    }

    public void desactivar() {
        try {
            getJpaController().destroy(selected.getIdHorario());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("HorariosDeleted"));
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(HorariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addEvent() {
        int cohorte = selected.getCohorteHorario();
        int grupo = selected.getGrupoHorario();

        Calendar fechaEntrada = Calendar.getInstance();
        Calendar fechaSalida = Calendar.getInstance();
        int n = obtenerDia(selected.getDiaHorario());
        if (n == 6) {
            JsfUtil.addErrorMessage("No se pueden establecer clases los domingos");
            return;
        }

        fechaEntrada.setTime(event.getStartDate());
        fechaEntrada.set(Calendar.YEAR, 2016);
        fechaEntrada.set(Calendar.MONTH, Calendar.JANUARY);
        fechaEntrada.set(Calendar.DAY_OF_MONTH, 4 + n);

        fechaSalida.setTime(event.getEndDate());
        fechaSalida.set(Calendar.YEAR, 2016);
        fechaSalida.set(Calendar.MONTH, Calendar.JANUARY);
        fechaSalida.set(Calendar.DAY_OF_MONTH, 4 + n);

        event.setStartDate(fechaEntrada.getTime());
        event.setEndDate(fechaSalida.getTime());
        if (event.getStartDate().getTime() == event.getEndDate().getTime()) {
            JsfUtil.addErrorMessage("Las horas de entrada y salida deben ser diferentes");
            return;
        }
        if (event.getStartDate().getTime() > event.getEndDate().getTime()) {
            JsfUtil.addErrorMessage("Las horas de salida debe ser mayor a la de entrada");
            return;
        }
        event.setTitle(getAsignaturasController().getSelected().getNombreAsignatura() + " - "
                + getUsuariosController().getSelected().getNombreLogin());
        String entrada = extraerHora(fechaEntrada);
        String salida = extraerHora(fechaSalida);
        if ("10:30".equals(salida)) {
            JsfUtil.addErrorMessage("No se puede seleccionar salidas a las 10:30 PM");
            return;
        }
        if (validarHorariosNuevo(getUsuariosController().getSelected().getCodigoPerfil().getCodigoPerfil(),
                selected.getDiaHorario(), entrada, salida, selected)) {
            return;
        }

        Calendar fechaIntencidad = Calendar.getInstance();
        fechaIntencidad.setTime(fechaSalida.getTime());
        fechaIntencidad.add(Calendar.HOUR_OF_DAY, -fechaEntrada.get(Calendar.HOUR_OF_DAY));
        fechaIntencidad.add(Calendar.MINUTE, -fechaEntrada.get(Calendar.MINUTE));
        String intensidad = extraerHora(fechaIntencidad);

        selected.setIntensidadHorario(intensidad);
        selected.setHEntradaHorario(entrada);
        selected.setHSalidaHorario(salida);
        selected.setIdEstado(ACTIVO);
        selected.setCodigoAsignatura(getAsignaturasController().getSelected());
        selected.setIdPlan(getPlanesController().getSelected());
        selected.setLoginUsuario(getUsuariosController().getSelected());
        selected.setIdModalidad(getModalidadesController().getSelected());
        selected.setIdAula(getAulasController().getSelected());
        selected.setIdTipoAula(getTipoAulaController().getSelected());

        if (event.getData() == null) {
            create();
        } else {
            update();
        }
        event.setId("" + selected.getIdHorario());
        event.setData(selected);
        getEventModel().addEvent(event);
        int cohorteHorario = selected.getCohorteHorario();
        event = new DefaultScheduleEvent();
        selected.setCohorteHorario(cohorteHorario);
        getUsuariosController().setSelected(new Usuarios());
        getAsignaturasController().setSelected(new Asignaturas());
        cargarEventos();
        selected.setCohorteHorario(cohorte);
        selected.setGrupoHorario(grupo);
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        selected = (Horarios) event.getData();
        getUsuariosController().setSelected(selected.getLoginUsuario());
        getAsignaturasController().setSelected(selected.getCodigoAsignatura());
        getModalidadesController().setSelected(selected.getIdModalidad());
        getTipoAulaController().setSelected(selected.getIdTipoAula());
        getAulasController().setSelected(selected.getIdAula());
    }

    public void onDateSelect(SelectEvent selectEvent) {
        int cohorte = selected.getCohorteHorario();
        selected = new Horarios();
        selected.setCohorteHorario(cohorte);
        getUsuariosController().setSelected(new Usuarios());
        getAsignaturasController().setSelected(new Asignaturas());
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent eve) {
        this.event = (DefaultScheduleEvent) eve.getScheduleEvent();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(event.getStartDate());
        c1.add(Calendar.DAY_OF_MONTH, eve.getDayDelta());
        c1.add(Calendar.MINUTE, eve.getMinuteDelta());
        event.setStartDate(c1.getTime());
        Calendar c2 = Calendar.getInstance();
        c2.setTime(event.getEndDate());
        c2.add(Calendar.DAY_OF_MONTH, eve.getDayDelta());
        c2.add(Calendar.MINUTE, eve.getMinuteDelta());
        event.setStartDate(c2.getTime());
        addEvent();
    }

    public void onEventResize(ScheduleEntryResizeEvent eve) {
        this.event = (DefaultScheduleEvent) eve.getScheduleEvent();
        Calendar c = Calendar.getInstance();
        c.setTime(event.getEndDate());
        c.add(Calendar.MINUTE, eve.getMinuteDelta());
        event.setStartDate(c.getTime());
        addEvent();
    }

    private boolean validarHorariosNuevo(String perfil, String dia, String entrada, String salida, Horarios id) {
        if (validarEntradaSalida(dia, entrada, salida, id)
                && validarCarga(perfil, entrada, salida, id)
                && validarEntradaSalidaCohorte(dia, entrada, salida, id)
                && validarGrupo()) {
            return false;
        }
        return true;
    }

    private boolean validarEntradaSalida(
            String dia, String entrada, String salida, Horarios id) {
        for (Horarios h : getConsultaAsignaturaDocente()) {
            if (id.getIdHorario() == null || !id.getIdHorario().equals(h.getIdHorario())) {
                if (dia.equals(h.getDiaHorario())
                        && ValidadorCruces.validarCruces(entrada, salida, h.getHEntradaHorario(), h.getHSalidaHorario())) {
                    JsfUtil.addErrorMessage("El docente ya tiene una asignatura en este horario");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validarCarga(String perfil, String entrada, String salida, Horarios id) {
        double acumulador;
        String ent = entrada.replace(":", ".");
        ent = ent.replace("30", "5");
        String sal = salida.replace(":", ".");
        sal = sal.replace("30", "5");
        acumulador = Double.parseDouble(sal) - Double.parseDouble(ent);
        if ("4".equals(perfil) && acumulador > 20) {
            JsfUtil.addErrorMessage(CARGA);
            return false;
        }
        if ("5".equals(perfil) && acumulador > 8) {
            JsfUtil.addErrorMessage(CARGA);
            return false;
        }
        for (Horarios h : getConsultaAsignaturaDocente()) {
            if (id.getIdHorario() == null || !id.getIdHorario().equals(h.getIdHorario())) {
                String intensidad = h.getIntensidadHorario().replace(":", ".");
                intensidad = intensidad.replace("30", "5");
                acumulador += Double.parseDouble(intensidad);
            }
        }
        if ("4".equals(perfil) && acumulador > 20) {
            JsfUtil.addErrorMessage(CARGA);
            return false;
        }
        if ("5".equals(perfil) && acumulador > 8) {
            JsfUtil.addErrorMessage(CARGA);
            return false;
        }
        return true;
    }

    private boolean validarEntradaSalidaCohorte(
            String dia, String entrada, String salida, Horarios id) {
        for (Horarios h : getConsultaHorariosCohorte()) {
            if (id.getIdHorario() == null || !id.getIdHorario().equals(h.getIdHorario())) {
                if (dia.equals(h.getDiaHorario())
                        && ValidadorCruces.validarCruces(entrada, salida, h.getHEntradaHorario(), h.getHSalidaHorario())) {
                    JsfUtil.addErrorMessage("Ya existe una materia en ese horario para este cohorte");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validarGrupo() {
        if (!getConsultaHorariosGrupo().isEmpty()) {
            JsfUtil.addErrorMessage("Otro plan o cohorte ya uso este grupo en la misma asignatura");
            return false;
        }
        return true;
    }

    public void cargarEventos() {
        eventModel = new DefaultScheduleModel();
        List<Horarios> consulta = getConsultaHorariosCohorte();
        for (Horarios h : consulta) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            int n = obtenerDia(h.getDiaHorario());
            start.setTime(new Date());
            start.set(Calendar.YEAR, 2016);
            start.set(Calendar.MONTH, Calendar.JANUARY);
            start.set(Calendar.DAY_OF_MONTH, 4 + n);
            String hEntrada = h.getHEntradaHorario();
            String[] entrada = hEntrada.split(":");
            start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(entrada[0]));
            start.set(Calendar.MINUTE, Integer.parseInt(entrada[1]));

            end.setTime(new Date());
            end.set(Calendar.YEAR, 2016);
            end.set(Calendar.MONTH, Calendar.JANUARY);
            end.set(Calendar.DAY_OF_MONTH, 4 + n);
            String hSalida = h.getHSalidaHorario();
            String[] salida = hSalida.split(":");
            end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(salida[0]));
            end.set(Calendar.MINUTE, Integer.parseInt(salida[1]));

            DefaultScheduleEvent evt = new DefaultScheduleEvent(h.getCodigoAsignatura().getCodigoAsignatura()
                    + " - Grp: " + h.getGrupoHorario() + "\n"
                    + h.getCodigoAsignatura().getNombreAsignatura() + " Cupos: " + h.getCupos() + "\n"
                    + h.getLoginUsuario().getNombreLogin() + "\n" + (h.getIdAula() != null ? ("Sede: " + h.getIdAula().getIdSede().getSiglasSede() + "Aula: " + h.getIdAula().getNombre()) : ""),
                    start.getTime(), end.getTime(), h);
            eventModel.addEvent(evt);
        }
    }

    public void cargarEventosAula() {
        eventModel = new DefaultScheduleModel();
        List<Horarios> consulta = getConsultaAula();
        for (Horarios h : consulta) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            int n = obtenerDia(h.getDiaHorario());
            start.setTime(new Date());
            start.set(Calendar.YEAR, 2016);
            start.set(Calendar.MONTH, Calendar.JANUARY);
            start.set(Calendar.DAY_OF_MONTH, 4 + n);
            String hEntrada = h.getHEntradaHorario();
            String[] entrada = hEntrada.split(":");
            start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(entrada[0]));
            start.set(Calendar.MINUTE, Integer.parseInt(entrada[1]));

            end.setTime(new Date());
            end.set(Calendar.YEAR, 2016);
            end.set(Calendar.MONTH, Calendar.JANUARY);
            end.set(Calendar.DAY_OF_MONTH, 4 + n);
            String hSalida = h.getHSalidaHorario();
            String[] salida = hSalida.split(":");
            end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(salida[0]));
            end.set(Calendar.MINUTE, Integer.parseInt(salida[1]));

            DefaultScheduleEvent evt = new DefaultScheduleEvent(h.getCodigoAsignatura().getCodigoAsignatura()
                    + " - Grp: " + h.getGrupoHorario() + "\n"
                    + h.getCodigoAsignatura().getNombreAsignatura() + " Cupos: " + h.getCupos() + "\n"
                    + h.getLoginUsuario().getNombreLogin() + "\n" + (h.getIdAula() != null ? ("Sede: " + h.getIdAula().getIdSede().getSiglasSede() + "Aula: " + h.getIdAula().getNombre()) : ""),
                    start.getTime(), end.getTime(), h);
            eventModel.addEvent(evt);
        }
    }

    public void cargarEventosDocentes() {
        eventModel = new DefaultScheduleModel();
        getUsuariosController().setSelected(getUsuariosController().getUsuario());
        List<Horarios> consulta = getConsultaAsignaturaDocente();
        for (Horarios h : consulta) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            int n = obtenerDia(h.getDiaHorario());
            start.setTime(new Date());
            start.set(Calendar.YEAR, 2016);
            start.set(Calendar.MONTH, Calendar.JANUARY);
            start.set(Calendar.DAY_OF_MONTH, 4 + n);
            String hEntrada = h.getHEntradaHorario();
            String[] entrada = hEntrada.split(":");
            start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(entrada[0]));
            start.set(Calendar.MINUTE, Integer.parseInt(entrada[1]));

            end.setTime(new Date());
            end.set(Calendar.YEAR, 2016);
            end.set(Calendar.MONTH, Calendar.JANUARY);
            end.set(Calendar.DAY_OF_MONTH, 4 + n);
            String hSalida = h.getHSalidaHorario();
            String[] salida = hSalida.split(":");
            end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(salida[0]));
            end.set(Calendar.MINUTE, Integer.parseInt(salida[1]));

            DefaultScheduleEvent evt = new DefaultScheduleEvent(h.getCodigoAsignatura().getCodigoAsignatura()
                    + " - Grp: " + h.getGrupoHorario() + "\n"
                    + h.getCodigoAsignatura().getNombreAsignatura() + " Cupos: " + h.getCupos() + "\n"
                    + h.getLoginUsuario().getNombreLogin() + "\n" + (h.getIdAula() != null ? ("Sede: " + h.getIdAula().getIdSede().getSiglasSede() + "Aula: " + h.getIdAula().getNombre()) : ""),
                    start.getTime(), end.getTime(), h);
            eventModel.addEvent(evt);
        }
        activarDia();
    }

    public void activarDia() {
        if (event.getStartDate() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(event.getStartDate());
            switch (c.get(Calendar.DAY_OF_MONTH)) {
                case 4:
                    selected.setDiaHorario(LUNES);
                    break;
                case 5:
                    selected.setDiaHorario(MARTES);
                    break;
                case 6:
                    selected.setDiaHorario(MIERCOLES);
                    break;
                case 7:
                    selected.setDiaHorario(JUEVES);
                    break;
                case 8:
                    selected.setDiaHorario(VIERNES);
                    break;
                case 9:
                    selected.setDiaHorario(SABADO);
                    break;
                default:
                    selected.setDiaHorario(DOMINGO);
                    break;
            }
        }
    }

    public HorariosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new HorariosJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
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
        try {
            if (getAulasController().getSelected().getIdAula() == null) {
                selected.setIdAula(null);
            }
            if ("3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
                selected.setIdPlan(getUsuariosController().getUsuario().getIdPlan());
            }
            if (opcion.equals(CREATE)) {
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("HorariosCreated"));
            } else {
                getJpaController().edit(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("HorariosUpdated"));
            }
            int cohorte = selected.getCohorteHorario();

            selected = new Horarios();

            selected.setCohorteHorario(cohorte);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
        }
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findHorarios(getPrimaryKey().getIdHorario());

        } else {
            Logger.getLogger(HorariosController.class
                    .getName()).log(Level.INFO, "Parametros nulos");
        }
    }

    public String extraerHora(Calendar objetivo) {
        String minutos;
        if (objetivo.get(Calendar.MINUTE) == 0) {
            minutos = "00";
        } else {
            minutos = "30";
        }
        return objetivo.get(Calendar.HOUR_OF_DAY) + ":" + minutos;
    }

    public int obtenerDia(String dia) {
        switch (dia) {
            case LUNES:
                return 0;
            case MARTES:
                return 1;
            case MIERCOLES:
                return 2;
            case JUEVES:
                return 3;
            case VIERNES:
                return 4;
            case SABADO:
                return 5;
            default:
                return 6;
        }
    }

    /**
     * @return the primaryKey
     */
    public Horarios getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Horarios primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @param jpaController the jpaController to set
     */
    public void setJpaController(HorariosJpaController jpaController) {
        this.jpaController = jpaController;
    }

    /**
     * @return the selected
     */
    public Horarios getSelected() {
        if (selected == null) {
            selected = new Horarios();
        }
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Horarios selected) {
        this.selected = selected;
    }

    public UsuariosController getUsuariosController() {
        return usuariosController;
    }

    public void setUsuariosController(UsuariosController usuariosController) {
        this.usuariosController = usuariosController;
    }

    public PlanesController getPlanesController() {
        return planesController;
    }

    public void setPlanesController(PlanesController planesController) {
        this.planesController = planesController;
    }

    /**
     * @return the eventModel
     */
    public ScheduleModel getEventModel() {
        if (getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil().equals("4")
                || getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil().equals("5")) {
            cargarEventosDocentes();
        }
        return eventModel;

    }

    /**
     * @param eventModel the eventModel to set
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    /**
     * @return the asignaturaController
     */
    public AsignaturasController getAsignaturasController() {
        return asignaturasController;
    }

    /**
     * @param asignaturaController the asignaturaController to set
     */
    public void setAsignaturasController(AsignaturasController asignaturasController) {
        this.asignaturasController = asignaturasController;
    }

    /**
     * @return the event
     */
    public DefaultScheduleEvent getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(DefaultScheduleEvent event) {
        this.event = event;

    }

    public boolean activarCalendario() {
        if (getUsuariosController().getUsuario().getIdPlan() != null && selected.getCohorteHorario() != 0) {
            selected.setIdPlan(getUsuariosController().getUsuario().getIdPlan());
            getPlanesController().setSelected(getUsuariosController().getUsuario().getIdPlan());
        }
        if (selected != null
                && ("1".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())
                || "3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil()))
                && getPlanesController().getSelected().getIdPlan() != null
                && selected.getCohorteHorario() != 0) {
            return true;
        }
        return false;
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

    public Object setSelected(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FacesConverter(forClass = Horarios.class)
    public static class HorariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HorariosController controller = (HorariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "horariosController");
            return controller.getJpaController().findHorarios(Integer.parseInt(value));
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
            if (object instanceof Horarios) {
                Horarios o = (Horarios) object;
                return "" + o.getIdHorario();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Horarios.class.getName());
            }
        }

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
     * @return the filtro
     */
    public List<Horarios> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<Horarios> filtro) {
        this.filtro = filtro;
    }
}
