package co.edu.univalle.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "historicos")
public class Historicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_historico")
    private Integer idHistorico;
    @Basic(optional = false)
    @Column(name = "cohorte_historico")
    private int cohorteHistorico;
    @Basic(optional = false)
    @Column(name = "grupo_historico")
    private int grupoHistorico;
    @Column(name = "cupos")
    private Integer cupos;
    @Basic(optional = false)
    @Column(name = "intensidad_historico")
    private String intensidadHistorico;
    @Basic(optional = false)
    @Column(name = "dia_historico")
    private String diaHistorico;
    @Basic(optional = false)
    @Column(name = "h_entrada_historico")
    private String hEntradaHistorico;
    @Basic(optional = false)
    @Column(name = "h_salida_historico")
    private String hSalidaHistorico;
    @Basic(optional = false)
    @Column(name = "anio_historico")
    private int anioHistorico;
    @Basic(optional = false)
    @Column(name = "periodo_historico")
    private String periodoHistorico;
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
    @ManyToOne(optional = false)
    private Planes idPlan;
    @JoinColumn(name = "login_usuario", referencedColumnName = "login_usuario")
    @ManyToOne(optional = false)
    private Usuarios loginUsuario;
    @JoinColumn(name = "codigo_asignatura", referencedColumnName = "codigo_asignatura")
    @ManyToOne(optional = false)
    private Asignaturas codigoAsignatura;
    @JoinColumn(name = "id_tipo_aula", referencedColumnName = "id_tipo_aula")
    @ManyToOne(optional = false)
    private TipoAula idTipoAula;
    @JoinColumn(name = "id_aula", referencedColumnName = "id_aula")
    @ManyToOne(optional = false)
    private Aulas idAula;
    @JoinColumn(name = "id_modalidad", referencedColumnName = "id_modalidad")
    @ManyToOne(optional = false)
    private Modalidades idModalidad;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    
    
    

    public Historicos() {
    }

    public Historicos(Integer idHistorico) {
        this.idHistorico = idHistorico;
    }

    public Historicos(Integer idHistorico, int cohorteHistorico, int grupoHistorico, String intensidadHistorico, String diaHistorico, String hEntradaHistorico, String hSalidaHistorico, int anioHistorico, String periodoHistorico) {
        this.idHistorico = idHistorico;
        this.cohorteHistorico = cohorteHistorico;
        this.grupoHistorico = grupoHistorico;
        this.intensidadHistorico = intensidadHistorico;
        this.diaHistorico = diaHistorico;
        this.hEntradaHistorico = hEntradaHistorico;
        this.hSalidaHistorico = hSalidaHistorico;
        this.anioHistorico = anioHistorico;
        this.periodoHistorico = periodoHistorico;
    }

    public Integer getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(Integer idHistorico) {
        this.idHistorico = idHistorico;
    }

    public int getCohorteHistorico() {
        return cohorteHistorico;
    }

    public void setCohorteHistorico(int cohorteHistorico) {
        this.cohorteHistorico = cohorteHistorico;
    }

    public int getGrupoHistorico() {
        return grupoHistorico;
    }

    public void setGrupoHistorico(int grupoHistorico) {
        this.grupoHistorico = grupoHistorico;
    }

    public String getIntensidadHistorico() {
        return intensidadHistorico;
    }

    public void setIntensidadHistorico(String intensidadHistorico) {
        this.intensidadHistorico = intensidadHistorico;
    }

    public String getDiaHistorico() {
        return diaHistorico;
    }

    public void setDiaHistorico(String diaHistorico) {
        this.diaHistorico = diaHistorico;
    }

    public String getHEntradaHistorico() {
        return hEntradaHistorico;
    }

    public void setHEntradaHistorico(String hEntradaHistorico) {
        this.hEntradaHistorico = hEntradaHistorico;
    }

    public String getHSalidaHistorico() {
        return hSalidaHistorico;
    }

    public void setHSalidaHistorico(String hSalidaHistorico) {
        this.hSalidaHistorico = hSalidaHistorico;
    }

    public int getAnioHistorico() {
        return anioHistorico;
    }

    public void setAnioHistorico(int anioHistorico) {
        this.anioHistorico = anioHistorico;
    }

    public String getPeriodoHistorico() {
        return periodoHistorico;
    }

    public void setPeriodoHistorico(String periodoHistorico) {
        this.periodoHistorico = periodoHistorico;
    }

    public Asignaturas getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(Asignaturas codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public Aulas getIdAula() {
        return idAula;
    }

    public void setIdAula(Aulas idAula) {
        this.idAula = idAula;
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

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public String gethEntradaHistorico() {
        return hEntradaHistorico;
    }

    public void sethEntradaHistorico(String hEntradaHistorico) {
        this.hEntradaHistorico = hEntradaHistorico;
    }

    public String gethSalidaHistorico() {
        return hSalidaHistorico;
    }

    public void sethSalidaHistorico(String hSalidaHistorico) {
        this.hSalidaHistorico = hSalidaHistorico;
    }

    public TipoAula getIdTipoAula() {
        return idTipoAula;
    }

    public void setIdTipoAula(TipoAula idTipoAula) {
        this.idTipoAula = idTipoAula;
    }
    
    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorico != null ? idHistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historicos)) {
            return false;
        }
        Historicos other = (Historicos) object;
        if ((this.idHistorico == null && other.idHistorico != null) || (this.idHistorico != null && !this.idHistorico.equals(other.idHistorico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Historicos[ idHistorico=" + idHistorico + " ]";
    }

}
