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

@Entity
@Table(name = "planes")
public class Planes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_plan")
    private String idPlan;
    @Basic(optional = false)
    @Column(name = "descripcion_plan")
    private String descripcionPlan;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @Transient
    private String codigo;
    @Transient
    private String nombre;

    public Planes() {
    }

    public Planes(String idPlan) {
        this.idPlan = idPlan;
    }

    public Planes(String idPlan, String descripcionPlan) {
        this.idPlan = idPlan;
        this.descripcionPlan = descripcionPlan;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    public String getDescripcionPlan() {
        return descripcionPlan;
    }

    public void setDescripcionPlan(String descripcionPlan) {
        this.descripcionPlan = descripcionPlan;
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
        hash += (idPlan != null ? idPlan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planes)) {
            return false;
        }
        Planes other = (Planes) object;
        if ((this.idPlan == null && other.idPlan != null) || (this.idPlan != null && !this.idPlan.equals(other.idPlan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Planes[ idPlan=" + idPlan + " ]";
    }

    public String getCodigo() {
        codigo = idPlan;
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        nombre = descripcionPlan;
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
