package co.edu.univalle.controladores;

import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Departamentos;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Persistence;
import javax.persistence.Query;
import co.edu.univalle.modelos.DepartamentosJpaController;

@ManagedBean(name = "departamentosController")
@SessionScoped
public class DepartamentosController extends Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    private Departamentos primaryKey;  //usando para el modelo de tabla (con el que se va a buscar)
    private DepartamentosJpaController jpaController = null;
    private List<Departamentos> consultaTabla;
    private List<Departamentos> filtro;
    private Departamentos selected = new Departamentos();

    public DepartamentosController() {
        setColumnTemplate("nombreDepartamento");
        createDynamicColumns();
    }

    public List<Departamentos> getConsultaTabla() {
        try {
            Query query;
            query = getJpaController().getEntityManager().createQuery("SELECT a FROM Aulas a WHERE a.idEstado=:ESTADO ORDER BY a.descripcionAula");
            query.setParameter("ESTADO", ACTIVO);
            consultaTabla = query.getResultList();
        } catch (NullPointerException npe) {
            JsfUtil.addErrorMessage(npe, CONSULTA);
        }
        return consultaTabla;
    }

    private DepartamentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new DepartamentosJpaController(Persistence.createEntityManagerFactory("asignacion_academicaPU"));
        }
        return jpaController;
    }
}
