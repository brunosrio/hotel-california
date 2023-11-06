package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.HotelCaliforniaSistema;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class testesReservaQuarto {
    private HotelCaliforniaSistema driver;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;


    @BeforeEach
    void preparaDriver() {
        driver = new HotelCaliforniaSistema();

        dataInicio = LocalDateTime.of(2024, Month.OCTOBER, 6, 14, 0);
        dataFim = LocalDateTime.of(2024, Month.OCTOBER, 8, 12, 0);

        driver.cadastrarUsuario("ADM1", "Gerente A", "GER", 1234);
        driver.cadastrarUsuario("ADM1", "Funcionario A", "FUN", 1234);
        driver.cadastrarUsuario("ADM1", "Cliente A", "CLI", 1234);

        LocalTime inicio = LocalTime.of(18, 0);
        LocalTime fim = LocalTime.of(21, 0);
        driver.disponibilizarRefeicao("GER2", "JANTAR", "jantar ruim",
                inicio, fim, 200.00, true);
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};

        driver.disponibilizarQuartoSingle("ADM1", 1,
                30.00, 5);
        driver.disponibilizarQuartoDouble("ADM1", 2,
                30.00, 5,  pedidos);
        driver.disponibilizarQuartoFamily("ADM1", 3,
                30.00, 5,  pedidos, 10);

    }


    @Test
    void testarReservarQuartoSingle() {
        String[] refeicoes = {"1"};
        String resultado = driver.reservarQuartoSingle("GER2", "CLI4", 1,
                dataInicio, dataFim, refeicoes);
        assertTrue(resultado.contains("Reserva de quarto em favor de:"));
        assertTrue(resultado.contains("Cliente A"));
        assertTrue(resultado.contains("Quarto Single"));
        assertTrue(resultado.contains("1 pessoa(s)"));
        assertTrue(resultado.contains("06/10/2024 14:00:00 ate 08/10/2024 12:00:00"));
    }

    @Test
    void testarReservaQuartoDouble() {
        String[] refeicoes = {"1"};
        String[] pedidos = {"5 camas"};
        String resultado = driver.reservarQuartoDouble("GER2", "CLI4", 2,
                dataInicio, dataFim, refeicoes, pedidos);
        assertTrue(resultado.contains("Reserva de quarto em favor de:"));
        assertTrue(resultado.contains("Cliente A"));
        assertTrue(resultado.contains("Quarto Double"));
        assertTrue(resultado.contains("2 pessoa(s)"));
        assertTrue(resultado.contains("5 camas"));
        assertTrue(resultado.contains("06/10/2024 14:00:00 ate 08/10/2024 12:00:00"));
    }

    @Test
    void testarReservaQuartoFamily() {
        String[] refeicoes = {"1"};
        String[] pedidos = {"10 camas"};
        String resultado = driver.reservarQuartoFamily("GER2", "CLI4", 3,
                dataInicio, dataFim, refeicoes, pedidos, 8);
        assertTrue(resultado.contains("Reserva de quarto em favor de:"));
        assertTrue(resultado.contains("Cliente A"));
        assertTrue(resultado.contains("Quarto Family"));
        assertTrue(resultado.contains("10 pessoa(s)"));
        assertTrue(resultado.contains("10 camas"));
        assertTrue(resultado.contains("06/10/2024 14:00:00 ate 08/10/2024 12:00:00"));
    }

    @Test
    void testarAdmNaoPodeReservarQuarto() {
        String[] refeicoes = {"1"};
        String[] pedidos = {"10 camas"};
        try {
            driver.reservarQuartoDouble("ADM1", "CLI4", 3,
                    dataInicio, dataFim, refeicoes, pedidos);
            fail("Era esperado um exceção");
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));
        }
    }

    @Test
    void testarReservarSemAntecedencia() {
        String[] refeicoes = {"1"};
        String[] pedidos = {"10 camas"};
        dataInicio = LocalDateTime.of(2023, Month.OCTOBER, 2, 14, 0);
        try {
            driver.reservarQuartoDouble("GER2", "CLI4", 2,
                    dataInicio, dataFim, refeicoes, pedidos);
            fail("Era esperado um exceção");
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NECESSARIO"));
            assertTrue(htc.getMessage().contains("ANTECEDENCIA"));
        }
    }

}
