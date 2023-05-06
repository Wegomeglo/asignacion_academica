package co.edu.univalle.vista;

import co.edu.univalle.controladores.UsuariosController;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "menu")
@SessionScoped
public class Menu implements Serializable {

    @ManagedProperty("#{usuariosController}")
    private UsuariosController usuariosController;
    private MenuModel model;

    private void llenarMenu() {
        model = new DefaultMenuModel();

        // Formularios de ingreso
        DefaultSubMenu submenu;
        String perfil = getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil();
        if ("1".equals(perfil) || "3".equals(perfil)) {
            submenu = new DefaultSubMenu("Registro de Información");

            submenu.addElement(crearItem("Registrar Horarios", "/coordinador/registrohorario.xhtml"));

            if ("1".equals(perfil)) {
                submenu.addElement(crearItem("Horarios de las Aulas", "/administrador/horarioaulas.xhtml"));
            }

            submenu.addElement(crearItem("Registrar Usuarios", "/administrador/registrousuarios.xhtml"));

            submenu.addElement(crearItem("Registrar Asignaturas", "/administrador/registroasignaturas.xhtml"));

            if ("1".equals(perfil)) {
                submenu.addElement(crearItem("Registrar Planes", "/administrador/registroplanes.xhtml"));

                submenu.addElement(crearItem("Registrar Sedes", "/administrador/registrosedes.xhtml"));

                submenu.addElement(crearItem("Registrar Periodos", "/administrador/registroperiodos.xhtml"));

                submenu.addElement(crearItem("Registrar Aulas", "/administrador/registroaulas.xhtml"));

                submenu.addElement(crearItem("Registrar Tipos de Aulas", "/administrador/registrotipoaula.xhtml"));

                submenu.addElement(crearItem("Registrar Modalidades", "/administrador/registromodalidades.xhtml"));
            }
            model.addElement(submenu);
        }

        if ("2".equals(perfil)) {
            submenu = new DefaultSubMenu("Ver Horario");
            submenu.addElement(crearItem("Ver Horarios", "/coordinador/registrohorario.xhtml"));
            model.addElement(submenu);
        }

        submenu = new DefaultSubMenu("Programación   Actual");

        if ("1".equals(perfil)
                || "2".equals(perfil)
                || "3".equals(perfil)) {
            submenu.addElement(crearItem("Generar Reportes", "/secretario/reportesActuales.xhtml"));
            if ("1".equals(perfil)) {
                submenu.addElement(crearItem("Optimizar Salones", "/administrador/optimizacion.xhtml"));
            }
            model.addElement(submenu);
        }

        //*******************************************************************
        if ("1".equals(perfil)) {
            submenu = new DefaultSubMenu("Programación Históricos");
            submenu.addElement(crearItem("Reportes Historicos", "/secretario/reportesHistoricos.xhtml"));
            submenu.addElement(crearItem("Historicos Programación", "/administrador/historicoProgramacion.xhtml"));
            model.addElement(submenu);
        }
        //*******************************************************************

        submenu = new DefaultSubMenu("Asignación Académica");

        if ("4".equals(perfil)
                || "5".equals(perfil)) {
            submenu.addElement(crearItem("Horario", "/docente/horariodocente.xhtml"));
            model.addElement(submenu);
        }

        submenu = new DefaultSubMenu("Hoja de Vida");

        if ("4".equals(perfil)
                || "5".equals(perfil)) {
            submenu.addElement(crearItem("Hoja de Vida", "/docente/hojavida.xhtml"));
            model.addElement(submenu);
        }

    }

    public boolean esVisible() {
        String value = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        String perfil = "";
        if (getUsuariosController().getUsuario().getCodigoPerfil() != null) {
            perfil = getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil();
        }
        if ("/coordinador/registrohorario.xhtml".equals(value)
                || "/secretario/reportes.xhtml".equals(value)
                || "/index.xhtml".equals(value)
                || "/".equals(value)) {
            return true;
        }
        if (("/administrador/registrousuarios.xhtml".equals(value)
                || "/administrador/registroplanes.xhtml".equals(value)
                || "/administrador/registroasignaturas.xhtml".equals(value)
                || "/administrador/registrosedes.xhtml".equals(value)
                || "/administrador/registroaulas.xhtml".equals(value)
                || "/administrador/registrotipoaula.xhtml".equals(value)
                || "/administrador/registromodalidades.xhtml".equals(value))
                && ("1".equals(perfil) || "3".equals(perfil))) {
            return true;
        }

        return false;
    }

    public DefaultMenuItem crearItem(String valor, String url) {
        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue(valor);
        item.setUrl(url);
        return item;
    }

    public MenuModel getModel() {
        llenarMenu();
        return model;
    }

    /**
     * @return the usuariosController
     */
    public UsuariosController getUsuariosController() {
        return usuariosController;
    }

    /**
     * @param usuariosController the usuariosController to set
     */
    public void setUsuariosController(UsuariosController usuariosController) {
        this.usuariosController = usuariosController;
    }

}
