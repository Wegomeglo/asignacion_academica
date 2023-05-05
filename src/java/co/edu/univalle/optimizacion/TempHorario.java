package co.edu.univalle.optimizacion;

public class TempHorario {

    private Integer idHorario;
    private Integer idAula;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String restriccion = "";
    private int activo;

    public TempHorario() {
    }

    public TempHorario(Integer idHorario, Integer idAula, String dia, String horaInicio, String horaFin) {
        this.idHorario = idHorario;
        this.idAula = idAula;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public TempHorario(Integer idHorario, Integer idAula, String dia, String horaInicio, String horaFin, int activo) {
        this.idHorario = idHorario;
        this.idAula = idAula;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.activo = activo;
    }

    public TempHorario clonarConEstado(int activo) {
        return new TempHorario(idHorario, idAula, dia, horaInicio, horaFin, activo);
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFIn) {
        this.horaFin = horaFIn;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "TempHorario{" + "idHorario=" + idHorario + ", idAula=" + idAula + ", dia=" + dia + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + ", activo=" + activo + '}';
    }

    /**
     * @return the restriccion
     */
    public String getRestriccion() {
        return restriccion;
    }

    /**
     * @param restriccion the restriccion to set
     */
    public void setRestriccion(String restriccion) {
        this.restriccion = restriccion;
    }

    public void concatRestriccion(String append) {
        restriccion += append;
    }

}
