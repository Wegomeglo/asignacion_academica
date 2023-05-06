package co.edu.univalle.vista;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import co.edu.univalle.controladores.AsignaturasController;
import co.edu.univalle.controladores.HorariosController;
import co.edu.univalle.controladores.PeriodosController;
import co.edu.univalle.controladores.UsuariosController;
import co.edu.univalle.controladores.util.JsfUtil;
import co.edu.univalle.entidades.Asignaturas;
import co.edu.univalle.entidades.Horarios;
import co.edu.univalle.entidades.Usuarios;
import co.edu.univalle.utilidades.ValidadorCruces;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "reportes")
@SessionScoped
public class Reportes {

    private static final String ARIAL = "arial";
    @ManagedProperty("#{sesion}")
    private Sesion sesion;
    @ManagedProperty("#{usuariosController}")
    private UsuariosController usuariosController;
    @ManagedProperty("#{asignaturasController}")
    private AsignaturasController asignaturasController;
    @ManagedProperty("#{horariosController}")
    private HorariosController horariosController;
    @ManagedProperty("#{periodosController}")
    private PeriodosController periodoController = new PeriodosController();

    String Periodo = periodoController.getConsultaTabla().get(0).getNombrePeriodo();
    String Año = periodoController.getConsultaTabla().get(0).getAño();

    private Date inicio = null;
    private Date fin = null;
    private Document document;
    private PdfPTable tabla;

    public void pdfCruds(int tipo) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            if (tipo == 6) {
                document = new Document(PageSize.LETTER);
            } else {
                document = new Document(PageSize.LETTER.rotate());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            if (!document.isOpen()) {
                document.open();
            }
            String fileName = null;
            switch (tipo) {
                case 0:
                    fileName = "Usuarios";
                    reporteUsuarios();
                    break;
                case 1:
                    fileName = "Asignaturas";
                    reporteAsignaturas();
                    break;
                case 2:
                    fileName = "Asignaturas por Docente";
                    reporteHorarioAsignaturaDocente();
                    break;
                case 3:
                    fileName = "Horarios Plan";
                    reporteHorarioPlan();
                    break;
                case 4:
                    fileName = "Horarios General";
                    reporteHorarioGeneral();
                    break;
                case 5:
                    fileName = "Horarios por Cohorte";
                    reporteHorarioCohorte();
                    break;
                case 6:
                    fileName = "Reporte Horario Academico";
                    reporteAcademico();
                    break;
                default:
            }

            document.close();
            context.getExternalContext().responseReset();
            context.getExternalContext().setResponseContentType("application/pdf");
            context.getExternalContext().setResponseHeader("Expires", "0");
            context.getExternalContext().setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            context.getExternalContext().setResponseHeader("Pragma", "public");
            context.getExternalContext().setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
            context.getExternalContext().setResponseContentLength(baos.size());
            OutputStream out = context.getExternalContext().getResponseOutputStream();
            baos.writeTo(out);
            context.getExternalContext().responseFlushBuffer();
            context.responseComplete();
        } catch (DocumentException | IOException e) {
            JsfUtil.addErrorMessage(e, e.toString());
        }
    }

    public Document reporteUsuarios() {
        try {
            String titulo = "Usuarios";
            document = reporteEncabezado(titulo);
            tabla = new PdfPTable(3);// Numero de campos de la tabla
            tabla.setWidthPercentage(100);
            ArrayList<String> nombres = new ArrayList<>();
            nombres.add("Nombres");
            nombres.add("Apellidos");
            nombres.add("Perfiles");
            reporteTablaGeneral(nombres);
            for (Usuarios m : getUsuariosController().getConsultaTabla()) {
                tabla.addCell(new PdfPCell(new Phrase(m.getNombres(), FontFactory.getFont(ARIAL, 10))));
                tabla.addCell(new PdfPCell(new Phrase(m.getApellidos(), FontFactory.getFont(ARIAL, 10))));
                tabla.addCell(new PdfPCell(new Phrase(m.getCodigoPerfil().getDescripcionPerfil(), FontFactory.getFont(ARIAL, 10))));
            }
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public Document reporteAsignaturas() {
        try {
            String titulo = "Asignaturas";
            document = reporteEncabezado(titulo);
            tabla = new PdfPTable(2);// Numero de campos de la tabla
            tabla.setWidthPercentage(100);
            ArrayList<String> nombres = new ArrayList<>();
            nombres.add("Codigo Asignatura");
            nombres.add("Nombre Asignatura");
            reporteTablaGeneral(nombres);
            for (Asignaturas m : getAsignaturasController().getConsultaTabla()) {
                tabla.addCell(new PdfPCell(new Phrase(m.getCodigoAsignatura(), FontFactory.getFont(ARIAL, 10))));
                tabla.addCell(new PdfPCell(new Phrase(m.getNombreAsignatura(), FontFactory.getFont(ARIAL, 10))));
            }
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public Document reporteHorarioAsignaturaDocente() {
        try {
            document = reporteEncabezado("UNIVERSIDAD DEL VALLE SEDE CAICEDONIA - NODO SEVILLA");
            document = reporteEncabezado("PERIODO ACADÉMICO - " + Periodo + " " + Año);
            document = reporteEncabezado("HORARIO DOCENTE");
            if ("4".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())
                    || "5".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
                getUsuariosController().setSelected(getUsuariosController().getUsuario());
            }
            String docente = getUsuariosController().getSelected().getNombreLogin();
            document = reporteEncabezado(docente);
            document = reporteEncabezado(" ");
            document = reporteEncabezado(" ");
            reporteTablaHorario();
            reporteLlenarTablaHorarios(getHorariosController().getConsultaAsignaturaDocente());
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public Document reporteHorarioPlan() {
        try {
            document = reporteEncabezado("UNIVERSIDAD DEL VALLE SEDE CAICEDONIA - NODO SEVILLA");
            document = reporteEncabezado("PERIODO ACADÉMICO - " + Periodo + " " + Año);
            String programa = "";
            if ("3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
                programa = getUsuariosController().getUsuario().getIdPlan().getIdPlan() + " - "
                        + getUsuariosController().getUsuario().getIdPlan().getDescripcionPlan();
            } else {
                programa = getHorariosController().getPlanesController().getSelected().getIdPlan() + " - "
                        + getHorariosController().getPlanesController().getSelected().getDescripcionPlan();
            }
            document = reporteEncabezado(programa);
            document = reporteEncabezado("HORARIO");
            document = reporteEncabezado(" ");
            document = reporteEncabezado(" ");
            reporteTablaHorario();
            reporteLlenarTablaHorarios(getHorariosController().getConsultaHorariosPrograma());
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public Document reporteHorarioGeneral() {
        try {
            document = reporteEncabezado("UNIVERSIDAD DEL VALLE SEDE CAICEDONIA - NODO SEVILLA");
            document = reporteEncabezado("PERIODO ACADÉMICO - " + Periodo + " " + Año);
            document = reporteEncabezado("HORARIO GENERAL");
            document = reporteEncabezado(" ");
            document = reporteEncabezado(" ");
            reporteTablaHorario();
            reporteLlenarTablaHorarios(getHorariosController().getConsultaTabla());
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public Document reporteHorarioCohorte() {
        try {
            document = reporteEncabezado("UNIVERSIDAD DEL VALLE SEDE CAICEDONIA - NODO SEVILLA");
            document = reporteEncabezado("PERIODO ACADÉMICO - " + Periodo + " " + Año);
            String programa = "";
            if ("3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
                programa = getUsuariosController().getUsuario().getIdPlan().getIdPlan() + " - "
                        + getUsuariosController().getUsuario().getIdPlan().getDescripcionPlan();
            } else {
                programa = getHorariosController().getPlanesController().getSelected().getIdPlan() + " - "
                        + getHorariosController().getPlanesController().getSelected().getDescripcionPlan();
            }
            document = reporteEncabezado(programa);
            document = reporteEncabezado("COHORTE " + getHorariosController().getSelected().getCohorteHorario());
            document = reporteEncabezado("HORARIO");
            document = reporteEncabezado(" ");
            document = reporteEncabezado(" ");
            reporteTablaHorario();
            reporteLlenarTablaHorarios(getHorariosController().getConsultaHorariosCohorte());
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public Document reporteEncabezado(String titulo) {
        try {
            Paragraph paragraph = new Paragraph(titulo, FontFactory.getFont(ARIAL, 16, Font.BOLD));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (DocumentException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }

    public PdfPTable reporteTablaHorario() {
        float tamaños[] = {4, 5, 8, 5, 7, 6, 6, 4, 6, 20, 20};
        tabla = new PdfPTable(tamaños);
        tabla.setWidthPercentage(100);
        int tamaño[] = {2};
        ArrayList<String> nombres = new ArrayList<>();
        //   nombres.add("Año");
        // nombres.add("Periodo");
        nombres.add("Sem");
        nombres.add("Plán");
        nombres.add("Código Asignatura");
        nombres.add("Grupo");
        nombres.add("Día");
        nombres.add("Hora Entrada");
        nombres.add("Hora Salida");
        nombres.add("Sede");
        nombres.add("Salón");
        nombres.add("Nombre Asignatura");
        nombres.add("Nombre Docente");
        reporteTablaGeneral(nombres);
        return tabla;
    }

    public PdfPTable reporteTablaAcademico() {
        float tamaños[] = {4, 9, 7, 6, 40, 6, 13, 15};
        tabla.setWidthPercentage(100);
        int tamaño[] = {2};
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add("No");
        nombres.add("Código");
        nombres.add("Grupo");
        nombres.add("Cupo");
        nombres.add("Nombre Asignatura");
        nombres.add("Horas");
        nombres.add("Horario");
        nombres.add("Credito");
        reporteTablaGeneral(nombres);
        return tabla;
    }

    public PdfPTable reporteTablaGeneral(List<String> nombres) {
        for (String n : nombres) {
            PdfPCell cell = new PdfPCell(new Phrase(n, FontFactory.getFont(ARIAL, 10, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }
        return tabla;
    }

    public PdfPTable reporteLlenarTablaAcademica(List<Horarios> consulta) throws DocumentException {
        float tamaños[] = {4, 9, 7, 6, 40, 6, 13, 15};
        tabla = new PdfPTable(tamaños);
        tabla.setWidthPercentage(100);

        PdfPCell celda = new PdfPCell(new Phrase("PROGRAMA", FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(4);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);

        String programa = "";
        if ("3".equals(getUsuariosController().getUsuario().getCodigoPerfil().getCodigoPerfil())) {
            programa = getUsuariosController().getUsuario().getIdPlan().getIdPlan() + " - "
                    + getUsuariosController().getUsuario().getIdPlan().getDescripcionPlan();
        } else {
            programa = getHorariosController().getPlanesController().getSelected().getIdPlan() + " - "
                    + getHorariosController().getPlanesController().getSelected().getDescripcionPlan();
        }

        celda = new PdfPCell(new Phrase(programa, FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("PERIODO ACADÉMICO", FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(4);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase(Periodo + " " + Año, FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("No. ESTUD. POR ADMISIÓN", FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(4);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("45", FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("No. RESOL. COBIJA AL EST.", FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(4);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        celda = new PdfPCell(new Phrase("Resolución No. 091 de JULIO 04 de 2002", FontFactory.getFont(ARIAL, 10)));
        celda.setColspan(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);

        String Cohorte = "";
        if (getHorariosController().getSelected().getCohorteHorario() == 1) {
            Cohorte = "PRIMER";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 2) {
            Cohorte = "SEGUNDO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 3) {
            Cohorte = "TERCER";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 4) {
            Cohorte = "CUARTO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 5) {
            Cohorte = "QUINTO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 6) {
            Cohorte = "SEXTO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 7) {
            Cohorte = "SÉPTIMO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 8) {
            Cohorte = "OCTAVO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 9) {
            Cohorte = "NOVENO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 10) {
            Cohorte = "DECIMO";
        } else if (getHorariosController().getSelected().getCohorteHorario() == 11) {
            Cohorte = "UNDÉCIMO";
        }

        celda = new PdfPCell(new Phrase("ASIGNATURAS OBLIGATORIAS A MATRICULAR EN " + Cohorte + " SEMESTRE", FontFactory.getFont(ARIAL, 10, Color.RED)));
        celda.setColspan(8);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);

        int i = 1;
        reporteTablaAcademico();

        for (Horarios m : consulta) {
            celda = new PdfPCell(new Phrase("\n"
                    + "\n"
                    + "\n"
                    + i++, FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n"
                    + "\n"
                    + "\n"
                    + m.getCodigoAsignatura().getCodigoAsignatura(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n"
                    + "\n"
                    + "\n"
                    + m.getGrupoHorario(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n"
                    + "\n"
                    + "\n"
                    + m.getCupos(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n"
                    + m.getCodigoAsignatura().getNombreAsignatura() + "\n"
                    + "Docente:  " + m.getLoginUsuario().getNombreLogin() + "\n"
                    + "CC:      " + m.getLoginUsuario().getLogin() + "\n"
                    + "Cel:     " + m.getLoginUsuario().getTelefonoUsuario() + "\n"
                    + "Email:   " + m.getLoginUsuario().getEmailUsuario() + "\n"
                    + " ", FontFactory.getFont(ARIAL, 9)));
            tabla.addCell(celda);

            double entrada = ValidadorCruces.stringToNumber(m.getHEntradaHorario());
            double salida = ValidadorCruces.stringToNumber(m.getHSalidaHorario());
            salida = salida - entrada;
            int entero = (int) salida;
            String resultado = "" + entero;
            salida = salida - entero;
            if (salida > 0) {
                resultado += ":" + (salida * 60);
            } else {
                resultado += ":00";
            }

            celda = new PdfPCell(new Phrase("\n"
                    + "\n"
                    + "\n" + resultado, FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("\n"
                    + m.getDia() + "\n"
                    + "\n"
                    + "De:  " + m.getHEntradaHorario() + "\n"
                    + "A:   " + m.getHSalidaHorario(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(m.getIdModalidad().getDescripcionModalidad(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

        }

        return tabla;
    }

    public PdfPTable reporteLlenarTablaHorarios(List<Horarios> consulta) {
        for (Horarios m : consulta) {

            PdfPCell celda = new PdfPCell(new Phrase("" + m.getSemestre(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("" + m.getIdPlan().getCodigo(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(m.getCodigoAsignatura().getCodigoAsignatura(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("" + m.getGrupoHorario(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(m.getDiaHorario(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(m.getHEntradaHorario(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(m.getHSalidaHorario(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            if (m.getIdAula() != null) {
                celda = new PdfPCell(new Phrase(m.getIdAula().getIdSede().getSiglasSede(), FontFactory.getFont(ARIAL, 10)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(m.getIdAula().getNombre(), FontFactory.getFont(ARIAL, 10)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
            } else {
                celda = new PdfPCell(new Phrase("  .  .", FontFactory.getFont(ARIAL, 10)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase("  .", FontFactory.getFont(ARIAL, 10)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
            }

            celda = new PdfPCell(new Phrase(m.getCodigoAsignatura().getNombreAsignatura(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase(m.getLoginUsuario().getNombreLogin(), FontFactory.getFont(ARIAL, 10)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

        }
        return tabla;
    }

    public void reporteAcademico1() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String fileName = "Reporte academico";
            document = new Document(PageSize.LETTER);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            if (!document.isOpen()) {
                document.open();
            }

            float tamaños[] = {5, 11, 6, 5, 40, 6, 12, 15};
            tabla = new PdfPTable(tamaños);
            tabla.setWidthPercentage(100);

            PdfPCell celda = new PdfPCell(new Phrase("PROGRAMA", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(4);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("CONTADURIA PUBLICA", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("CÓD. PROGRAMA", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(4);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("3841 - NOCTURNO", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("PERIODO ACADÉMICO", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(4);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("FEBRERO - JUNIO 2017", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("No. ESTUD. POR ADMISIÓN", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(4);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("45", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("No. RESOL. COBIJA AL EST.", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(4);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Resolución No. 091 de JULIO 04 de 2002", FontFactory.getFont(ARIAL, 10)));
            celda.setColspan(5);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("ASIGNATURAS OBLIGATORIAS A MATRICULAR EN QUINTO SEMESTRE", FontFactory.getFont(ARIAL, 10, Color.RED)));
            celda.setColspan(8);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("No.", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Código", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Grupo", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Cupo", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Nombre Asignatura", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Horas", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Horario", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);
            celda = new PdfPCell(new Phrase("Créditos", FontFactory.getFont(ARIAL, 9)));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            for (int i = 1; i <= 8; i++) {
                celda = new PdfPCell(new Phrase("\n"
                        + "\n"
                        + "\n"
                        + i, FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "\n"
                        + "\n"
                        + "802086M", FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "\n"
                        + "\n"
                        + "50", FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "\n"
                        + "\n"
                        + "40", FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "FUNDAMENTOS Y TÉCNICAS DE LABORATORIO PARA CERTIFICACIÓN DE PRODUCTOS\n"
                        + "Docente:  " + "JAVIER ESTEBAN ESCOBARRRRRRR\n"
                        + "CC:      " + "1.115.182.957\n"
                        + "Cel:     " + "3122974189\n"
                        + "Email:   " + "jeep519@hotmail.com\n"
                        + " ", FontFactory.getFont(ARIAL, 9)));
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "\n"
                        + "\n"
                        + "3", FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "MARTES\n"
                        + "\n"
                        + "11:59-12:00pm\n"
                        + "\n"
                        + "MG - 3\n", FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
                celda = new PdfPCell(new Phrase("\n"
                        + "2"
                        + "\n"
                        + "\n"
                        + "VALIDABLE - SI\n"
                        + "HABILITABLE-NO", FontFactory.getFont(ARIAL, 9)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);

            }
            document.add(tabla);
            document.close();
            context.getExternalContext().responseReset();
            context.getExternalContext().setResponseContentType("application/pdf");
            context.getExternalContext().setResponseHeader("Expires", "0");
            context.getExternalContext().setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            context.getExternalContext().setResponseHeader("Pragma", "public");
            context.getExternalContext().setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
            context.getExternalContext().setResponseContentLength(baos.size());
            OutputStream out = context.getExternalContext().getResponseOutputStream();
            baos.writeTo(out);
            context.getExternalContext().responseFlushBuffer();
            context.responseComplete();
        } catch (DocumentException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Document reporteAcademico() {
        try {
            reporteLlenarTablaAcademica(getHorariosController().getConsultaHorariosCohorte());
            document.add(tabla);
        } catch (DocumentException ex) {
            JsfUtil.addErrorMessage(ex, ex.toString());
        }
        return document;
    }

    public void reporteAdministrativo() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String fileName = "Reporte academico";
            document = new Document(PageSize.LETTER);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            if (!document.isOpen()) {
                document.open();
            }
            tabla = new PdfPTable(9);// Numero de campos de la tabla
            tabla.setWidthPercentage(100);

            document.add(tabla);
            document.close();
            context.getExternalContext().responseReset();
            context.getExternalContext().setResponseContentType("application/pdf");
            context.getExternalContext().setResponseHeader("Expires", "0");
            context.getExternalContext().setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            context.getExternalContext().setResponseHeader("Pragma", "public");
            context.getExternalContext().setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
            context.getExternalContext().setResponseContentLength(baos.size());
            OutputStream out = context.getExternalContext().getResponseOutputStream();
            baos.writeTo(out);
            context.getExternalContext().responseFlushBuffer();
            context.responseComplete();
        } catch (DocumentException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param sesion the sesion to set
     */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the fin
     */
    public Date getFin() {
        return fin;
    }

    /**
     * @param fin the fin to set
     */
    public void setFin(Date fin) {
        Calendar c = Calendar.getInstance();
        c.setTime(fin);
        c.add(Calendar.HOUR, 23);
        c.add(Calendar.MINUTE, 59);
        c.add(Calendar.SECOND, 59);
        this.fin = c.getTime();
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
     * @return the asignaturasController
     */
    public AsignaturasController getAsignaturasController() {
        return asignaturasController;
    }

    /**
     * @param asignaturasController the asignaturasController to set
     */
    public void setAsignaturasController(AsignaturasController asignaturasController) {
        this.asignaturasController = asignaturasController;
    }

    public PeriodosController getPeriodoController() {
        return periodoController;
    }

    public void setPeriodoController(PeriodosController periodoController) {
        this.periodoController = periodoController;
    }

}
