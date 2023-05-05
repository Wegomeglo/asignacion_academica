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
@Table(name = "infoacademica")

//ingresa informacion en las columnas de la DB

public class InfoAcademica implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_infoacademica")
    private String idInfoacademica;    
    @Basic(optional = false)
    @Column(name = "login_usuario")
    private String loginUsuario;
    @Basic(optional = false)
    @Column(name = "nivel_academico")
    private String nivelAcademico;
    @Basic(optional = false)
    @Column(name = "titulo_academico")
    private String tituloAcademico;
    @Column(name = "fecha_inicio")
    private String fechaInicio;
    @Column(name = "fecha_final")
    private String fechaFinal;
    @Basic(optional = false)
    @Column(name = "institucion")
    private String institucion;   
    
    @Transient
    private String nivel_academico;
    @Transient
    private String titulo_academico;
    
    
    public InfoAcademica() {
    }

    public InfoAcademica(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public InfoAcademica(String loginUsuario, String nivelAcademico, String tituloAcademico, String fechaInicio, String fechaFinal, String institucion) {
        this.loginUsuario = loginUsuario;
        this.nivelAcademico = nivelAcademico;
        this.tituloAcademico = tituloAcademico;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.institucion = institucion;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(String nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public String getTituloAcademico() {
        return tituloAcademico;
    }

    public void setTituloAcademico(String tituloAcademico) {
        this.tituloAcademico = tituloAcademico;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public String getIdInfoacademica() {
        return idInfoacademica;
    }

    public void setIdInfoacademica(String idInfoacademica) {
        this.idInfoacademica = idInfoacademica;
    }

    public String getNivel_academico() {
        return nivelAcademico;
    }

    public void setNivel_academico(String nivel_academico) {
        this.nivel_academico = nivel_academico;
    }

    public String getTitulo_academico() {
        return tituloAcademico;
    }

    public void setTitulo_academico(String titulo_academico) {
        this.titulo_academico = titulo_academico;
    }
    
 


}
