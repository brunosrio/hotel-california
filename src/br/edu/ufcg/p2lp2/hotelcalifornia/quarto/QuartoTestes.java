package br.edu.ufcg.p2lp2.hotelcalifornia.quarto;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.QuartosController;

import java.util.Arrays;

public class QuartoTestes {

    private static QuartosController driver = QuartosController.getInstance();
    public static void main(String[] args) {
        driver.disponibilizarQuartoSingle("ADM1", 101, 80.0, 20.0);
        driver.disponibilizarQuartoDouble("ADM1", 501, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"});
        driver.disponibilizarQuartoFamily("ADM1", 201, 80.0, 20.0, new String[]{"cama extra infantil", "agua quente"}, 10);

        String[] listaQuartos = driver.listarQuartos();
        String out = "";

        System.out.println(Arrays.toString(listaQuartos));



    }
}
