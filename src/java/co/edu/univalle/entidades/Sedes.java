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
@Table(name = "sedes")
public class Sedes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sede")
    private Integer idSede;
    @Basic(optional = false)
    @Column(name = "siglas_sede")
    private String siglasSede;
    @Basic(optional = false)
    @Column(name = "descripcion_sede")
    private String descripcionSede;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @Transient
    private String nombre;

    public Sedes() {
    }

    public Sedes(Integer idSede) {
        this.idSede = idSede;
    }

    public Sedes(Integer idSede, String siglasSede, String descripcionSede) {
        this.idSede = idSede;
        this.siglasSede = siglasSede;
        this.descripcionSede = descripcionSede;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getSiglasSede() {
        return siglasSede;
    }

    public void setSiglasSede(String siglasSede) {
        this.siglasSede = siglasSede;
    }

    public String getDescripcionSede() {
        return descripcionSede;
    }

    public void setDescripcionSede(String descripcionSede) {
        this.descripcionSede = descripcionSede;
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
        hash += (idSede != null ? idSede.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sedes)) {
            return false;
        }
        Sedes other = (Sedes) object;
        if ((this.idSede == null && other.idSede != null) || (this.idSede != null && !this.idSede.equals(other.idSede))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Sedes[ idSede=" + idSede + " ]";
    }

    public String getNombre() {
        nombre = descripcionSede;
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
