package co.edu.univalle.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Aaron
 */
@Entity
@Table(name = "asignaturas")
public class Asignaturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo_asignatura")
    private String codigoAsignatura;
    @Basic(optional = false)
    @Column(name = "nombre_asignatura")
    private String nombreAsignatura;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @Transient
    private String codigoNombre;
    @Transient
    private String codigo;
    @Transient
    private String nombre;

    public Asignaturas() {
    }

    public Asignaturas(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public Asignaturas(String codigoAsignatura, String nombreAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
        this.nombreAsignatura = nombreAsignatura;
    }

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public Estados getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estados idEstado) {
        this.idEstado = idEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoAsignatura != null ? codigoAsignatura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asignaturas)) {
            return false;
        }
        Asignaturas other = (Asignaturas) object;
        if ((this.codigoAsignatura == null && other.codigoAsignatura != null) || (this.codigoAsignatura != null && !this.codigoAsignatura.equals(other.codigoAsignatura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Asignaturas[ codigoAsignatura=" + codigoAsignatura + " ]";
    }

    public String getCodigoNombre() {
        codigoNombre = codigoAsignatura + " - " + nombreAsignatura;
        return codigoNombre;
    }

    public void setCodigoNombre(String codigoNombre) {
        this.codigoNombre = codigoNombre;
    }

    public String getCodigo() {
        codigo = codigoAsignatura;
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        nombre = nombreAsignatura;
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
