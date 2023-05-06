package co.edu.univalle.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "horarios")
public class Horarios implements Serializable {

    
    
   
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_horario")
    private Integer idHorario;
    @Basic(optional = false)
    @Column(name = "cohorte_horario")
    private int cohorteHorario;
    @Basic(optional = false)
    @Column(name = "grupo_horario")
    private int grupoHorario;
    @Column(name = "cupos")
    private Integer cupos;
    @Basic(optional = false)
    @Column(name = "intensidad_horario")
    private String intensidadHorario;
    @Basic(optional = false)
    @Column(name = "dia_horario")
    private String diaHorario;
    @Basic(optional = false)
    @Column(name = "h_entrada_horario")
    private String hEntradaHorario;
    @Basic(optional = false)
    @Column(name = "h_salida_horario")
    private String hSalidaHorario;
     @Basic(optional = false)
    @Column(name = "anio_horario")
    private String año;
    
    @Basic(optional = false)
    @Column(name = "periodo_horario")
    private String periodo;
    
    
    
    @JoinColumn(name = "codigo_asignatura", referencedColumnName = "codigo_asignatura")
    @ManyToOne(optional = false)
    private Asignaturas codigoAsignatura;
    @JoinColumn(name = "id_tipo_aula", referencedColumnName = "id_tipo_aula")
    @ManyToOne(optional = false)
    private TipoAula idTipoAula;
    @JoinColumn(name = "id_aula", referencedColumnName = "id_aula")
    @ManyToOne(optional = false)
    private Aulas idAula;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @JoinColumn(name = "id_modalidad", referencedColumnName = "id_modalidad")
    @ManyToOne(optional = false)
    private Modalidades idModalidad;
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
    @ManyToOne(optional = false)
    private Planes idPlan;
    @JoinColumn(name = "login_usuario", referencedColumnName = "login_usuario")
    @ManyToOne(optional = false)
    private Usuarios loginUsuario;
    @Transient
    private String semestre;
    @Transient
    private String codigo_asignatura;
    @Transient
    private String grupo;
    @Transient
    private String dia;
    @Transient
    private String hora_entrada;
    @Transient
    private String hora_salida;
    @Transient
    private String sede;
    @Transient
    private String salon;
    @Transient
    private String nombre_asignatura;
    @Transient
    private String usuario;
    @Transient
    private String plan;

    public Horarios() {
    }

    public Horarios(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Horarios(Integer idHorario, int cohorteHorario, int grupoHorario, Integer cupos, String intensidadHorario, String diaHorario, String hEntradaHorario, String hSalidaHorario, String año, String periodo, Asignaturas codigoAsignatura, TipoAula idTipoAula, Aulas idAula, Estados idEstado, Modalidades idModalidad, Planes idPlan, Usuarios loginUsuario, String semestre, String codigo_asignatura, String grupo, String dia, String hora_entrada, String hora_salida, String sede, String salon, String nombre_asignatura, String usuario, String plan) {
        this.idHorario = idHorario;
        this.cohorteHorario = cohorteHorario;
        this.grupoHorario = grupoHorario;
        this.cupos = cupos;
        this.intensidadHorario = intensidadHorario;
        this.diaHorario = diaHorario;
        this.hEntradaHorario = hEntradaHorario;
        this.hSalidaHorario = hSalidaHorario;
        this.año = año;
        this.periodo = periodo;
        this.codigoAsignatura = codigoAsignatura;
        this.idTipoAula = idTipoAula;
        this.idAula = idAula;
        this.idEstado = idEstado;
        this.idModalidad = idModalidad;
        this.idPlan = idPlan;
        this.loginUsuario = loginUsuario;
        this.semestre = semestre;
        this.codigo_asignatura = codigo_asignatura;
        this.grupo = grupo;
        this.dia = dia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.sede = sede;
        this.salon = salon;
        this.nombre_asignatura = nombre_asignatura;
        this.usuario = usuario;
        this.plan = plan;
    }

 
    

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public int getCohorteHorario() {
        return cohorteHorario;
    }

    public void setCohorteHorario(int cohorteHorario) {
        this.cohorteHorario = cohorteHorario;
    }

    public int getGrupoHorario() {
        return grupoHorario;
    }

    public void setGrupoHorario(int grupoHorario) {
        this.grupoHorario = grupoHorario;
    }

    public String getIntensidadHorario() {
        return intensidadHorario;
    }

    public void setIntensidadHorario(String intensidadHorario) {
        this.intensidadHorario = intensidadHorario;
    }

    public String getDiaHorario() {
        return diaHorario;
    }

    public void setDiaHorario(String diaHorario) {
        this.diaHorario = diaHorario;
    }

    public String getHEntradaHorario() {
        return hEntradaHorario;
    }

    public void setHEntradaHorario(String hEntradaHorario) {
        this.hEntradaHorario = hEntradaHorario;
    }

    public String getHSalidaHorario() {
        return hSalidaHorario;
    }

    public void setHSalidaHorario(String hSalidaHorario) {
        this.hSalidaHorario = hSalidaHorario;
    }

    public Asignaturas getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(Asignaturas codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public Estados getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estados idEstado) {
        this.idEstado = idEstado;
    }

    public Modalidades getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(Modalidades idModalidad) {
        this.idModalidad = idModalidad;
    }

    public Planes getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Planes idPlan) {
        this.idPlan = idPlan;
    }

    public Usuarios getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(Usuarios loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorario != null ? idHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horarios)) {
            return false;
        }
        Horarios other = (Horarios) object;
        if ((this.idHorario == null && other.idHorario != null) || (this.idHorario != null && !this.idHorario.equals(other.idHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Horarios[ idHorario=" + idHorario + " ]";
    }

    public Aulas getIdAula() {
        return idAula;
    }

    public void setIdAula(Aulas idAula) {
        this.idAula = idAula;
    }

    public TipoAula getIdTipoAula() {
        return idTipoAula;
    }

    public void setIdTipoAula(TipoAula idTipoAula) {
        this.idTipoAula = idTipoAula;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public String getSemestre() {
        semestre = "" + cohorteHorario;
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getCodigo_asignatura() {
        codigo_asignatura = codigoAsignatura.getCodigoAsignatura();
        return codigo_asignatura;
    }

    public void setCodigo_asignatura(String codigo_asignatura) {
        this.codigo_asignatura = codigo_asignatura;
    }

    public String getGrupo() {
        grupo = "" + grupoHorario;
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getHora_entrada() {
        hora_entrada = hEntradaHorario;
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getHora_salida() {
        hora_salida = hSalidaHorario;
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public String getSede() {
        if (idAula != null) {
            sede = idAula.getIdSede().getSiglasSede();
        } else {
            sede = "No asignado";
        }
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getSalon() {
        if (idAula != null) {
            salon = idAula.getDescripcionAula();
        } else {
            salon = "No asignado";
        }
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public String getNombre_asignatura() {
        nombre_asignatura = codigoAsignatura.getNombreAsignatura();
        return nombre_asignatura;
    }

    public void setNombre_asignatura(String nombre_asignatura) {
        this.nombre_asignatura = nombre_asignatura;
    }

    public String getNombre_docente() {
        usuario = loginUsuario.getNombreLogin();
        return usuario;
    }

    public void setNombre_docente(String nombre_docente) {
        this.usuario = nombre_docente;
    }

    public String getDia() {
        dia = diaHorario;
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getPlan() {
        plan = idPlan.getCodigo();
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String gethEntradaHorario() {
        return hEntradaHorario;
    }

    public void sethEntradaHorario(String hEntradaHorario) {
        this.hEntradaHorario = hEntradaHorario;
    }

    public String gethSalidaHorario() {
        return hSalidaHorario;
    }

    public void sethSalidaHorario(String hSalidaHorario) {
        this.hSalidaHorario = hSalidaHorario;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    

}
