package co.edu.univalle.utilidades;

public class ValidadorCruces {

    public static boolean validarCruces(String entrada1, String salida1, String entrada2, String salida2) {
        double e1 = stringToNumber(entrada1);
        double e2 = stringToNumber(entrada2);
        double s1 = stringToNumber(salida1);
        double s2 = stringToNumber(salida2);
        boolean entradaMayorEntrada = e1 > e2;
        boolean entradaMenorEntrada = e1 < e2;
        boolean entradaIgualSalida = e1 == s2;
        boolean entradaMayorSalida = e1 > s2;
        boolean entradaMenorSalida = e1 < s2;
        boolean salidaIgualEntrada = s1 == e2;
        boolean salidaMayorEntrada = s1 > e2;
        boolean salidaMenorEntrada = s1 < e2;
        boolean salidaMayorSalida = s1 > s2;
        boolean salidaMenorSalida = s1 < s2;
        boolean buena1 = entradaMayorEntrada
                && entradaMayorSalida
                && salidaMayorEntrada
                && salidaMayorSalida;
        boolean buena2 = entradaMenorEntrada
                && entradaMenorSalida
                && salidaMenorEntrada
                && salidaMenorSalida;
        boolean buena3 = entradaMayorEntrada
                && entradaIgualSalida
                && salidaMayorEntrada
                && salidaMayorSalida;
        boolean buena4 = entradaMenorEntrada
                && entradaMenorSalida
                && salidaIgualEntrada
                && salidaMenorSalida;
        return !(buena1 || buena2 || buena3 || buena4);
    }

    public static double stringToNumber(String convert) {
        String[] split = convert.split(":");
        double resultado = Double.parseDouble(split[0]);
        if (split[1].equals("30")) {
            resultado += 0.5;
        }
        return resultado;
    }

}
