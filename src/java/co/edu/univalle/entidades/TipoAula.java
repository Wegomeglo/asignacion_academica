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
@Table(name = "tipo_aula")
public class TipoAula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tipo_aula")
    private Integer idTipoAula;
    @Basic(optional = false)
    @Column(name = "descripcion_tipo_aula")
    private String descripcionTipoAula;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @Transient
    private String nombre;

    public TipoAula() {
    }

    public TipoAula(Integer idTipoAula) {
        this.idTipoAula = idTipoAula;
    }

    public TipoAula(Integer idTipoAula, String descripcionTipoAula) {
        this.idTipoAula = idTipoAula;
        this.descripcionTipoAula = descripcionTipoAula;
    }

    public Integer getIdTipoAula() {
        return idTipoAula;
    }

    public void setIdTipoAula(Integer idTipoAula) {
        this.idTipoAula = idTipoAula;
    }

    public String getDescripcionTipoAula() {
        return descripcionTipoAula;
    }

    public void setDescripcionTipoAula(String descripcionTipoAula) {
        this.descripcionTipoAula = descripcionTipoAula;
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
        hash += (idTipoAula != null ? idTipoAula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAula)) {
            return false;
        }
        TipoAula other = (TipoAula) object;
        if ((this.idTipoAula == null && other.idTipoAula != null) || (this.idTipoAula != null && !this.idTipoAula.equals(other.idTipoAula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoAula[ idTipoAula=" + idTipoAula + " ]";
    }

    public String getNombre() {
        nombre = descripcionTipoAula;
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
