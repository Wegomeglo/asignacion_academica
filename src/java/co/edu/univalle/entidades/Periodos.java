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
@Table(name = "periodos")

//ingresa informacion en las columnas de la DB
public class Periodos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Basic(optional = false)
    @Column(name = "id_periodo")
    private int idPeriodo;
    
    @Basic(optional = false)
    @Column(name = "anio")
    private String año;
    
    @Basic(optional = false)
    @Column(name = "periodo")
    private String periodo;
    
    @Basic(optional = false)
    @Column(name = "nombre_periodo")
    private String nombrePeriodo;
    
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    
    @Transient
    private String nombre_Periodo;
    
    
    public Periodos() {
    }

    public Periodos(int idPeriodo, String año, String periodo, String nombrePeriodo, Estados idEstado, String nombre_periodo) {
        this.idPeriodo = idPeriodo;
        this.año = año;
        this.periodo = periodo;
        this.nombrePeriodo = nombrePeriodo;
        this.idEstado = idEstado;
        this.nombre_Periodo = nombre_periodo;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
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

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    public Estados getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estados idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre_Periodo() {
         nombre_Periodo = "" + nombrePeriodo;
        return nombre_Periodo;
    }

    public void setNombre_Periodo(String nombre_Periodo) {
        this.nombre_Periodo = nombre_Periodo;
    }

     

}
