package co.edu.univalle.utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdministradorArchivos {

    public static void guardarArchivo(String texto, File archivo) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(archivo);
            bw = new BufferedWriter(fw);
            bw.write(texto);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String leerArchivo(File archivo) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String sCurrentLine;
            String acumulador1 = "";
            String acumulador2 = "";
            br = new BufferedReader(new FileReader(archivo));
            while ((sCurrentLine = br.readLine()) != null) {
                acumulador1 = acumulador2;
                acumulador1 += sCurrentLine;
                acumulador2 = acumulador1 + "\n";
            }
            return acumulador1;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}
