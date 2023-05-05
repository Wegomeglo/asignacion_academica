package co.edu.univalle.optimizacion;

import co.edu.univalle.entidades.Aulas;
import co.edu.univalle.entidades.Horarios;
import java.io.File;
import java.util.List;
import co.edu.univalle.utilidades.AdministradorArchivos;
import co.edu.univalle.utilidades.ValidadorCruces;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestorModelos {

    static String acumuladorMatematico = "";
    static String acumuladorBounds = "";
    static String acumuladorValor = "";
    static List<TempHorario> tempHorarios = new ArrayList<>();
    static List<TempHorario> tempAulas = new ArrayList<>();
    static List<TempHorario> tempHoras = new ArrayList<>();
    static int count = 0;
    static boolean flag = true;

    public static List<Horarios> crearModelo(List<Horarios> horarios, List<Aulas> aulas) {
        acumuladorMatematico = "";
        acumuladorBounds = "";
        acumuladorValor = "";
        tempHorarios = new ArrayList<>();
        tempAulas = new ArrayList<>();
        tempHoras = new ArrayList<>();
        count = 0;
        flag = true;

        horarios.stream().forEach(h -> {
            TempHorario horario = new TempHorario();
            horario.setIdHorario(h.getIdHorario());
            tempHoras.add(horario);
            acumuladorValor += "1,";
        });
        horarios.stream().forEach((horario) -> {
            aulas.stream().forEach((aula) -> {
                if (Objects.equals(horario.getIdTipoAula().getIdTipoAula(), aula.getIdTipoAula().getIdTipoAula())
                        && horario.getCupos() <= aula.getCapacidad()) {
                    acumuladorMatematico += "-1,";
                    acumuladorBounds += "0,1;";
                    tempHorarios.add(
                            new TempHorario(
                                    horario.getIdHorario(),
                                    aula.getIdAula(),
                                    horario.getDiaHorario(),
                                    horario.getHEntradaHorario(),
                                    horario.getHSalidaHorario()));
                    tempHoras.stream().forEach(h -> {
                        if (Objects.equals(horario.getIdHorario(), h.getIdHorario())) {
                            h.concatRestriccion("1,");
                        } else {
                            h.concatRestriccion("0,");
                        }
                    });

                }
            });
        });
        tempHorarios.stream().forEach(h -> {
            flag = true;
            tempAulas.stream().forEach(a -> {
                if (Objects.equals(h.getIdAula(), a.getIdAula())
                        && h.getDia().equals(a.getDia())
                        && Objects.equals(h.getHoraInicio(), a.getHoraInicio())
                        && Objects.equals(h.getHoraFin(), a.getHoraFin())) {
                    flag = false;
                }
            });
            if (flag) {
                TempHorario horario = new TempHorario();
                horario.setIdHorario(h.getIdHorario());
                horario.setIdAula(h.getIdAula());
                horario.setDia(h.getDia());
                horario.setHoraInicio(h.getHoraInicio());
                horario.setHoraFin(h.getHoraFin());
                tempAulas.add(horario);
                acumuladorValor += "1,";
            }
        });
        tempHorarios.stream().forEach(h -> {
            tempAulas.stream().forEach(a -> {
                if (Objects.equals(h.getIdAula(), a.getIdAula())
                        && h.getDia().equals(a.getDia())
                        && ValidadorCruces.validarCruces(h.getHoraInicio(), h.getHoraFin(), a.getHoraInicio(), a.getHoraFin())) {
                    a.concatRestriccion("1,");
                } else {
                    a.concatRestriccion("0,");
                }
            });
        });
        acumuladorMatematico = acumuladorMatematico.substring(0, acumuladorMatematico.length() - 1) + "\n";

        tempHoras.stream().forEach(h -> {
            acumuladorMatematico += h.getRestriccion().substring(0, h.getRestriccion().length() - 1) + ";";
        });
        tempHoras = null;

        tempAulas.stream().forEach(a -> {
            acumuladorMatematico += a.getRestriccion().substring(0, a.getRestriccion().length() - 1) + ";";
        });
        tempAulas = null;
        acumuladorMatematico = acumuladorMatematico.substring(0, acumuladorMatematico.length() - 1) + "\n";
        acumuladorMatematico += acumuladorValor;
        acumuladorValor = "";
        acumuladorMatematico = acumuladorMatematico.substring(0, acumuladorMatematico.length() - 1) + "\n";
        acumuladorMatematico += acumuladorBounds;
        acumuladorBounds = "";
        acumuladorMatematico = acumuladorMatematico.substring(0, acumuladorMatematico.length() - 1);
        //File modelo = new File(System.getProperty("user.home") + "/Desktop/Proyectos/Optimizacion/modelo.txt");
        //File resultado = new File(System.getProperty("user.home") + "/Desktop/Proyectos/Optimizacion/resultado.txt");
        File modelo = new File("/opt/tomcat9/webapps/asignacion_academica/WEB-INF/classes/modelo.txt");
        File resultado = new File("/opt/tomcat9/webapps/asignacion_academica/WEB-INF/classes/resultado.txt");

        modelo.delete();

        resultado.delete();
        acumuladorMatematico += acumuladorBounds;

        AdministradorArchivos.guardarArchivo(acumuladorMatematico, modelo);

        try {
            //Process p = Runtime.getRuntime().exec("python " + System.getProperty("user.home") + "/Desktop/Proyectos/Optimizacion/Optimizacion.py");
            Process p = Runtime.getRuntime().exec("python /opt/tomcat9/webapps/asignacion_academica/WEB-INF/classes/Optimizacion.py");
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(GestorModelos.class.getName()).log(Level.SEVERE, null, ex);
        }
        String leer = AdministradorArchivos.leerArchivo(resultado);
        String[] split = leer.split(",");
        count = 0;
        for (String s : split) {
            if (s.equals("0.0")) {
                count++;
            } else if (s.equals("1.0")) {
                TempHorario get = tempHorarios.get(count);
                horarios.stream().filter((h) -> (Objects.equals(get.getIdHorario(), h.getIdHorario()))).forEachOrdered((h) -> {
                    aulas.stream().filter((a) -> (Objects.equals(get.getIdAula(), a.getIdAula()))).forEachOrdered((a) -> {
                        h.setIdAula(a);
                    });
                });
                count++;
            } else {
                List<Horarios> list = new ArrayList<>();
                Horarios h = new Horarios();
                list.add(h);
                return list;
            }
        }
        return horarios;
    }

}
