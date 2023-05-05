package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Perfiles;
import co.edu.univalle.entidades.Planes;
import co.edu.univalle.entidades.Usuarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.UsuariosJpaController;
import co.edu.univalle.modelos.exceptions.NonexistentEntityException;
import co.edu.univalle.modelos.exceptions.PreexistingEntityException;
import co.edu.univalle.utilidades.Cifrador;
import co.edu.univalle.utilidades.Constantes;
import com.sun.tools.sjavac.CopyFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "usuariosController", eager = true)
@SessionScoped
public class UsuariosController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{perfilesController}")
    private PerfilesController perfilesController;
    @ManagedProperty("#{planesController}")
    private PlanesController planesController;
    @ManagedProperty("#{ciudadesController}")
    private CiudadesController ciudadesController;
    @ManagedProperty("#{infoAcadeController}")

    private InfoAcademicaController infoAcadeController;
    private Usuarios primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private UsuariosJpaController jpaController = null;
    private List<Usuarios> consultaTabla;
    private List<Usuarios> filtro;
    private List<Perfiles> perfiles;
    private Usuarios usuario;
    private Usuarios selected;
    private String id = "";
    private boolean reactivar = false;
    private CroppedImage croppeFoto;
    private String imageTemp;
    private UploadedFile file;
    private String destino;

    public UsuariosController() {
        usuario = new Usuarios();
        setColumnTemplate("login nombres apellidos ciudad");
        createDynamicColumns();
    }

    public void actionFoto() {
        this.croppeFoto = null;
        this.imageTemp = null;
    }

    private String destination = "/Users/andresangel/GD - Andrés Ángel/Docencia/2023-1/2711/750057M - Práctica Profesional/asignacion_academica/web/resources/img/fotoUsuarios/";

    public void upload(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());

            FacesMessage message = new FacesMessage("El Archivo " + event.getFile().getFileName() + "Se Cargo Correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void copyFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destination + this.getUsuario().getLoginUsuario()) + ".jpg");
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            FacesMessage message = new FacesMessage("El archivo se ha subido con éxito");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void actionGuardarFoto() {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/docente/");
        String archivo = path + File.separator + this.getUsuario().getLoginUsuario() + ".jpg";

        try {

            OutputStream outStream = new FileOutputStream(new File(archivo));
            InputStream inputStream = new FileInputStream(path + "/temp/" + this.getImageTemp());
            byte[] buffer = new byte[61240000];
            int bulk;
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                outStream.write(buffer, 0, bulk);
                outStream.flush();
            }
            outStream.close();
            inputStream.close();

            actionFoto();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadAttachment(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String txtField = ec.getRequestParameterMap().get("myform:txtField");
        String filePath = ec.getRealPath(String.format("/docente/", file.getFileName()));
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(file.getContents());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("Archivo cargado: %s ", file.getFileName()),
                String.format("Mensaje: %s", txtField)));

    }

    public List<Usuarios> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT u FROM Usuarios u WHERE u.idEstado=:ESTADO ORDER BY u.loginUsuario");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    public List<Usuarios> getConsultaDocentes() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT u FROM Usuarios u WHERE u.idEstado=:ESTADO AND (u.codigoPerfil.codigoPerfil=:DOCENTE OR u.codigoPerfil.codigoPerfil=:DOCENTEM) ORDER BY u.loginUsuario");
            query.setParameter("ESTADO", ACTIVO);
            query.setParameter("DOCENTE", "4");
            query.setParameter("DOCENTEM", "5");
            return query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return new ArrayList<Usuarios>();
    }

    public List<Usuarios> getConsultaDocentesConHorarios() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT u FROM Usuarios u WHERE u.idEstado=:ESTADO AND (u.codigoPerfil.codigoPerfil=:DOCENTE OR u.codigoPerfil.codigoPerfil=:DOCENTEM) AND u.loginUsuario IN (SELECT DISTINCT h.loginUsuario.loginUsuario FROM Horarios h) ORDER BY u.nombreUsuario, u.apellidoUsuario");
            query.setParameter("ESTADO", ACTIVO);
            query.setParameter("DOCENTE", "4");
            query.setParameter("DOCENTEM", "5");
            return query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return new ArrayList<Usuarios>();
    }

    public List<Perfiles> getConsultaPerfiles() {
        try {
            Query query;
            if ("1".equals(usuario.getCodigoPerfil().getCodigoPerfil())) {
                query = getJpaController().getEntityManager().createQuery("SELECT p FROM Perfiles p ORDER BY p.descripcionPerfil");
                perfiles = query.getResultList();
            } else {
                query = getJpaController().getEntityManager().createQuery("SELECT p FROM Perfiles p WHERE p.codigoPerfil>:PERFIL ORDER BY p.descripcionPerfil");
                query.setParameter("PERFIL", "3");
                perfiles = query.getResultList();
            }
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, "Error al generar las consultas");
        }
        return perfiles;
    }

    private UsuariosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new UsuariosJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }

    public void update() {
        createOrUpdate(UPDATE);
    }

    public void create() {
        createOrUpdate(CREATE);
    }

    public void createOrUpdate(String opcion) {
        if (opcion.equals(CREATE)) {
            try {
                selected.setPasswordUsuario(Cifrador.cifrar(selected.getLoginUsuario()));
                selected.setNombreUsuario(selected.getNombreUsuario().toUpperCase());
                selected.setApellidoUsuario(selected.getApellidoUsuario().toUpperCase());
                selected.setEmailUsuario(selected.getEmailUsuario());
                selected.setCodigoPerfil(getPerfilesController().getSelected());
                if ("3".equals(selected.getCodigoPerfil().getCodigoPerfil())) {
                    selected.setIdPlan(getPlanesController().getSelected());
                }
                selected.setIdCiudad(getCiudadesController().getSelected());
                selected.setIdEstado(ACTIVO);
                getJpaController().create(selected);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UsuariosCreated"));
            } catch (PreexistingEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("UsuariosError"));
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("UsuariosError"));
            }
        } else {
            try {
                if (!usuario.getCodigoPerfil().getCodigoPerfil().equals("1")) {
                    id = usuario.getLogin();
                    selected.setCodigoPerfil(usuario.getCodigoPerfil());
                } else {
                    selected.setCodigoPerfil(getPerfilesController().getSelected());
                }
                String newID = selected.getLoginUsuario();
                selected.setLoginUsuario(id);
                selected.setNombreUsuario(selected.getNombreUsuario().toUpperCase());
                selected.setApellidoUsuario(selected.getApellidoUsuario().toUpperCase());
                selected.setEmailUsuario(selected.getEmailUsuario().toUpperCase());
                selected.setIdCiudad(getCiudadesController().getSelected());
                getJpaController().edit(selected);
                if (!newID.equals(id)) {
                    EntityManager em = getJpaController().getEntityManager();
                    em.getTransaction().begin();
                    Query query = em.createQuery("UPDATE Usuarios SET loginUsuario=" + newID + " WHERE loginUsuario=:OLD");
                    query.setParameter("OLD", id);
                    query.executeUpdate();
                    em.getTransaction().commit();
                }
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("UsuariosUpdated"));
            } catch (NonexistentEntityException e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            }
        }
        if (usuario.getCodigoPerfil().getCodigoPerfil().equals("1")) {
            selected = new Usuarios();
            getPlanesController().setSelected(new Planes());
        }
    }

    public void prepararEdicion() {
        if (getPrimaryKey() != null) {
            selected = getJpaController().findUsuarios(getPrimaryKey().getLoginUsuario());
            getPerfilesController().setSelected(selected.getCodigoPerfil());
            if ("3".equals(selected.getCodigoPerfil().getCodigoPerfil())) {
                getPlanesController().setSelected(selected.getIdPlan());
            }
            id = selected.getLoginUsuario();
        } else {
            JsfUtil.addErrorMessage("Error al generar las consultas");
        }
    }

    public void desactivar() {
        if (primaryKey != null) {
            selected = getJpaController().findUsuarios(primaryKey.getLoginUsuario());
            if (selected != null) {
                selected.setIdEstado(DESACTIVADO);
                id = selected.getLoginUsuario();
                update();
            }
        }
    }

    /**
     * @return the usuario
     */
    public Usuarios getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    /**
     * Captura el usuario con la sesion activa para cargarlo en el formulario
     *
     * @return the selected
     */
    public Usuarios getSelected() {
        if (selected == null) {
            selected = new Usuarios();
        }
        if (usuario.getCodigoPerfil().getCodigoPerfil().equals("4") || usuario.getCodigoPerfil().getCodigoPerfil().equals("5")) {
            selected.setIdCiudad(getCiudadesController().getSelected());

            selected = usuario;
            getInfoAcadeController().getSelected().setLoginUsuario(usuario.getLoginUsuario());
        }
        getInfoAcadeController().getSelected().setLoginUsuario(selected.getLoginUsuario());

        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(Usuarios selected) {
        this.selected = selected;
    }

    /**
     * @return the primaryKey
     */
    public Usuarios getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(Usuarios primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return the filtro
     */
    public List<Usuarios> getFiltro() {
        return filtro;
    }

    /**
     * @param filtro the filtro to set
     */
    public void setFiltro(List<Usuarios> filtro) {
        this.filtro = filtro;
    }

    /**
     * @return the reactivar
     */
    public boolean isReactivar() {
        return reactivar;
    }

    /**
     * @param reactivar the reactivar to set
     */
    public void setReactivar(boolean reactivar) {
        this.reactivar = reactivar;
    }

    /**
     * @return the perfilesController
     */
    public PerfilesController getPerfilesController() {
        return perfilesController;
    }

    /**
     * @param perfilesController the perfilesController to set
     */
    public void setPerfilesController(PerfilesController perfilesController) {
        this.perfilesController = perfilesController;
    }

    /**
     * @return the planesController
     */
    public PlanesController getPlanesController() {
        return planesController;
    }

    /**
     * @param planesController the planController to set
     */
    public void setPlanesController(PlanesController planesController) {
        this.planesController = planesController;

    }

    @FacesConverter(forClass = Usuarios.class)
    public static class UsuariosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuariosController controller = (UsuariosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuariosController");
            return controller.getJpaController().findUsuarios(value);
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuarios) {
                Usuarios o = (Usuarios) object;
                return o.getLoginUsuario();
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuarios.class.getName());
            }
        }

    }

    /**
     * @return the ciudadesController
     */
    public CiudadesController getCiudadesController() {
        return ciudadesController;
    }

    /**
     * @param ciudadesController the ciudadesController to set
     */
    public void setCiudadesController(CiudadesController ciudadesController) {
        this.ciudadesController = ciudadesController;
    }

    public InfoAcademicaController getInfoAcadeController() {
        return infoAcadeController;
    }

    public void setInfoAcadeController(InfoAcademicaController infoAcadeController) {
        this.infoAcadeController = infoAcadeController;
    }

    public CroppedImage getCroppeFoto() {
        return croppeFoto;
    }

    public void setCroppeFoto(CroppedImage croppeFoto) {
        this.croppeFoto = croppeFoto;
    }

    public String getImageTemp() {
        return imageTemp;
    }

    public void setImageTemp(String imageTemp) {
        this.imageTemp = imageTemp;

    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

}
