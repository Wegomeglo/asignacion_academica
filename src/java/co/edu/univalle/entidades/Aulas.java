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
@Table(name = "aulas")
public class Aulas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_aula")
    private Integer idAula;
    @Basic(optional = false)
    @Column(name = "descripcion_aula")
    private String descripcionAula;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne
    private Estados idEstado;
    @JoinColumn(name = "id_sede", referencedColumnName = "id_sede")
    @ManyToOne(optional = false)
    private Sedes idSede;
    @JoinColumn(name = "id_tipo_aula", referencedColumnName = "id_tipo_aula")
    @ManyToOne(optional = false)
    private TipoAula idTipoAula;
    @Column(name = "capacidad")
    private Integer capacidad;
    @Transient
    private String nombre;
    @Transient
    private String sede;
    @Transient
    private String siglasNombre;

    public Aulas() {
    }

    public Aulas(Integer idAula) {
        this.idAula = idAula;
    }

    public Aulas(Integer idAula, String descripcionAula) {
        this.idAula = idAula;
        this.descripcionAula = descripcionAula;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public String getDescripcionAula() {
        return descripcionAula;
    }

    public void setDescripcionAula(String descripcionAula) {
        this.descripcionAula = descripcionAula;
    }

    public Estados getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estados idEstado) {
        this.idEstado = idEstado;
    }

    public Sedes getIdSede() {
        return idSede;
    }

    public void setIdSede(Sedes idSede) {
        this.idSede = idSede;
    }

    public TipoAula getIdTipoAula() {
        return idTipoAula;
    }

    public void setIdTipoAula(TipoAula idTipoAula) {
        this.idTipoAula = idTipoAula;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAula != null ? idAula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aulas)) {
            return false;
        }
        Aulas other = (Aulas) object;
        if ((this.idAula == null && other.idAula != null) || (this.idAula != null && !this.idAula.equals(other.idAula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Aulas[ idAula=" + idAula + " ]";
    }

    public String getNombre() {
        nombre = descripcionAula;
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSede() {
        sede = idSede.getDescripcionSede();
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    /**
     * @return the siglasNombre
     */
    public String getSiglasNombre() {
        siglasNombre = idSede.getSiglasSede() + " - " + descripcionAula;
        return siglasNombre;
    }

    /**
     * @param siglasNombre the siglasNombre to set
     */
    public void setSiglasNombre(String siglasNombre) {
        this.siglasNombre = siglasNombre;
    }

}
