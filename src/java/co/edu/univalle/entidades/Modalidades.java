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
@Table(name = "modalidades")
public class Modalidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_modalidad")
    private Integer idModalidad;
    @Basic(optional = false)
    @Column(name = "descripcion_modalidad")
    private String descripcionModalidad;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @Transient
    private String nombre;

    public Modalidades() {
    }

    public Modalidades(Integer idModalidad) {
        this.idModalidad = idModalidad;
    }

    public Modalidades(Integer idModalidad, String descripcionModalidad) {
        this.idModalidad = idModalidad;
        this.descripcionModalidad = descripcionModalidad;
    }

    public Integer getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(Integer idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getDescripcionModalidad() {
        return descripcionModalidad;
    }

    public void setDescripcionModalidad(String descripcionModalidad) {
        this.descripcionModalidad = descripcionModalidad;
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
        hash += (idModalidad != null ? idModalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modalidades)) {
            return false;
        }
        Modalidades other = (Modalidades) object;
        if ((this.idModalidad == null && other.idModalidad != null) || (this.idModalidad != null && !this.idModalidad.equals(other.idModalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Modalidades[ idModalidad=" + idModalidad + " ]";
    }

    public String getNombre() {
        nombre = descripcionModalidad;
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
