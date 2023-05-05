/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.univalle.controladores;

import co.edu.univalle.entidades.Estados;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AndresAngel
 */
public class Controller implements Serializable {

    public static final String CONSULTA = "Error al generar la consulta";
    public static final String BUNDLE = "/Bundle";
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final Estados ACTIVO = new Estados(1);
    public static final Estados DESACTIVADO = new Estados(2);

    //Implementando Tabla Dinamica
    public static class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    private List<ColumnModel> columns = new ArrayList<>();
    private String columnTemplate;

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public String getColumnTemplate() {
        return columnTemplate;
    }

    public void setColumnTemplate(String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }

    public void createDynamicColumns() {
        String[] columnKeys = columnTemplate.split(" ");
        columns.clear();

        for (String columnKey : columnKeys) {
            String key = columnKey.trim();
            String acum = "";
            for (String s : key.split("_")) {
                key = s.substring(0, 1).toUpperCase() + s.substring(1);
                if (key.equals("Codigo")) {
                    key = "Código";
                }
                acum += " " + key;
                acum = acum.trim();
            }
            columns.add(new ColumnModel(acum, columnKey));
        }
    }
}
