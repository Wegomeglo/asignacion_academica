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
@Table(name = "usuarios")
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "login_usuario")
    private String loginUsuario;
    @Basic(optional = false)
    @Column(name = "password_usuario")
    private String passwordUsuario;
    @Basic(optional = false)
    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    @Column(name = "apellido_usuario")
    private String apellidoUsuario;
    @Column(name = "direccion")
    private String direccionUsuario;
    @Column(name = "foto")
    private byte fotoUsuario[];
    @Column(name = "telefono_usuario")
    private String telefonoUsuario;
    @Basic(optional = false)
    @Column(name = "email_usuario")
    private String emailUsuario;
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
    @ManyToOne
    private Ciudades idCiudad;
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private Estados idEstado;
    @JoinColumn(name = "codigo_perfil", referencedColumnName = "codigo_perfil")
    @ManyToOne
    private Perfiles codigoPerfil;
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan")
    @ManyToOne
    private Planes idPlan;
    @Transient
    private String nombreLogin;
    @Transient
    private String login;
    @Transient
    private String nombres;
    @Transient
    private String apellidos;
    @Transient
    private String direccion;
    @Transient
    private String loginNombre;
    @Transient
    private String ciudad;

    public Usuarios() {
    }

    public Usuarios(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public Usuarios(String loginUsuario, String passwordUsuario, String nombreUsuario, String emailUsuario) {
        this.loginUsuario = loginUsuario;
        this.passwordUsuario = passwordUsuario;
        this.nombreUsuario = nombreUsuario;
        this.emailUsuario = emailUsuario;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }
    
    //prueba
    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        this.direccionUsuario = direccionUsuario;
    }
    //fin prueba
    //prueba foto
    
    //fin prueba foto

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Ciudades getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Ciudades idCiudad) {
        this.idCiudad = idCiudad;
    }

    public Estados getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Estados idEstado) {
        this.idEstado = idEstado;
    }

    public Perfiles getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(Perfiles codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    public Planes getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(Planes idPlan) {
        this.idPlan = idPlan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginUsuario != null ? loginUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.loginUsuario == null && other.loginUsuario != null) || (this.loginUsuario != null && !this.loginUsuario.equals(other.loginUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usuarios[ loginUsuario=" + loginUsuario + " ]";
    }

    public String getNombreLogin() {
        nombreLogin = nombreUsuario + " " + apellidoUsuario;
        return nombreLogin;
    }

    public void setNombreLogin(String nombreLogin) {
        this.nombreLogin = nombreLogin;
    }

    public String getLogin() {
        login = loginUsuario;
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombres() {
        nombres = nombreUsuario;
        return nombres;
    }

    public void setNombres(String nombre) {
        this.nombres = nombre;
    }

    public String getApellidos() {
        apellidos = apellidoUsuario;
        return apellidos;
    }

    public void setApellidos(String apellido) {
        this.apellidos = apellido;
    }
    
    //prueba
    public String getDireccion() {
        direccion = direccionUsuario;
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    //fin prueba

    public String getLoginNombre() {
        loginNombre = loginUsuario + " - " + nombreUsuario + " " + apellidoUsuario;
        return loginNombre;
    }

    public void setLoginNombre(String loginNombre) {
        this.loginNombre = loginNombre;
    }

    public String getCiudad() {
        if (idCiudad == null) {
            ciudad = "No asignada";
        } else {
            ciudad = idCiudad.getNombreCiudad();
        }
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public byte[] getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(byte[] fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

}
