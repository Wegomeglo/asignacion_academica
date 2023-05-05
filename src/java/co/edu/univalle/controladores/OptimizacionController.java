package co.edu.univalle.controladores;

import static co.edu.univalle.controladores.Controller.CONSULTA;
import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Horarios;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import co.edu.univalle.optimizacion.GestorModelos;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Aaron
 */
@ManagedBean(name = "optimizacionController", eager = true)
@SessionScoped
public class OptimizacionController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManagedProperty("#{horariosController}")
    private HorariosController horariosController;
    @ManagedProperty("#{aulasController}")
    private AulasController aulasController;

    public void optimizar() {
        List<Horarios> crearModelo = GestorModelos.crearModelo(getHorariosController().getConsultaTabla(), getAulasController().getConsultaTabla());
        if (crearModelo.size()>1) {
            for (Horarios h : crearModelo) {
                try {
                    getHorariosController().getJpaController().edit(h);
                } catch (Exception ex) {
                    JsfUtil.addErrorMessage(ex, CONSULTA);
                }
            }
        } else {
            JsfUtil.addErrorMessage("No se cuentan con suficientes aulas para generar la optimizacion");
            JsfUtil.addErrorMessage("No se cuentan con suficientes aulas para generar la optimizacion");
        }
    }

    /**
     * @return the horariosController
     */
    public HorariosController getHorariosController() {
        return horariosController;
    }

    /**
     * @param horariosController the horariosController to set
     */
    public void setHorariosController(HorariosController horariosController) {
        this.horariosController = horariosController;
    }

    /**
     * @return the aulasController
     */
    public AulasController getAulasController() {
        return aulasController;
    }

    /**
     * @param aulasController the aulasController to set
     */
    public void setAulasController(AulasController aulasController) {
        this.aulasController = aulasController;
    }

}
