package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.HotelCaliforniaSistema;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class testesExibirReserva {

    private HotelCaliforniaSistema driver;

    @BeforeEach
    void prepararDriver() {
        driver = new HotelCaliforniaSistema();
    }

    private void cadastrarUsuarios() {
        driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123);
        driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 125);
        driver.cadastrarUsuario("ADM1", "Novo Cliente A", "CLI", 124);
        driver.cadastrarUsuario("ADM1", "Novo Cliente B", "CLI", 124);

    }

    private void cadastrarQuartos() {
        driver.disponibilizarQuartoSingle("ADM1", 1,
                30.00, 5);
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        driver.disponibilizarQuartoDouble("ADM1", 2,
                30.00, 5, pedidos);
        driver.disponibilizarQuartoFamily("ADM1", 3,
                30.00, 5, pedidos, 10);
    }

    private void cadastrarAudiotorio() {
        driver.disponibilizarAreaComum("ADM1", "AUDITORIO", "Auditorio Caindo aos Pedacos",
                LocalTime.of(14, 0), LocalTime.of(22, 0), 0.0,
                true, 150);
    }

    private void cadastrarRefeicao() {
        driver.disponibilizarRefeicao("GER2", "JANTAR",
                "Jantar Italiano", LocalTime.of(18, 0), LocalTime.of(22, 30),
                100.0, true);
    }

    private void cadastrarReservas() {
        String[] refeicoes = {"1"};
        String[] pedidos = {"5 camas"};
        LocalDateTime dataInicio = LocalDateTime.of(2024, Month.OCTOBER, 6, 14, 0);
        LocalDateTime dataFim = LocalDateTime.of(2024, Month.OCTOBER, 8, 12, 0);

        driver.reservarQuartoSingle("GER2", "CLI4", 1,
                dataInicio, dataFim, refeicoes);
        driver.reservarQuartoDouble("GER2", "CLI4", 2,
                dataInicio, dataFim, refeicoes, pedidos);
        driver.reservarQuartoFamily("GER2", "CLI5", 3,
                dataInicio, dataFim, refeicoes, pedidos, 8);

        driver.reservarAuditorio("GER2", "CLI5",
                1, dataInicio, dataFim, 5);
        driver.reservarAuditorio("GER2", "CLI5",
                1, dataInicio.plusDays(4), dataFim.plusDays(4), 5);

    }

    private void cadastrarBase() {
        cadastrarUsuarios();
        cadastrarAudiotorio();
        cadastrarRefeicao();
        cadastrarQuartos();
        cadastrarReservas();
    }


    @Test
    void testarExibirReserva() {
        cadastrarBase();
        String resultado = driver.exibirReserva("GER2", 1);
        assertTrue(resultado.contains("[CLI4]"));
        assertTrue(resultado.contains("Reserva de quarto em favor de:"));
        assertTrue(resultado.contains("Novo Cliente A"));
        assertTrue(resultado.contains("Quarto Single"));
        assertTrue(resultado.contains("1 pessoa(s)"));
        assertTrue(resultado.contains("06/10/2024 14:00:00 ate 08/10/2024 12:00:00"));
    }

    @Test
    void testarAdmExibirReserva() {
        cadastrarBase();
        try {
            driver.exibirReserva("ADM1", 1);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));
        }
    }

    @Test
    void testarExibirReservasAtivasDoCliente() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivasDoCliente("GER2", "CLI4");
        assertEquals(2, resultado.length);
    }

    @Test
    void testarExibirReservasAtivasDoCliente2() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivasDoCliente("GER2", "CLI5");
        assertEquals(3, resultado.length);
    }

    @Test
    void testarExibirReservasAtivasDoCliente3() {
        cadastrarBase();
        driver.cancelarReserva("CLI4", "1");
        String[] resultado = driver.listarReservasAtivasDoCliente("GER2", "CLI4");
        assertEquals(1, resultado.length);
    }

    @Test
    void testarExibirReservasAtivasDoClientePorTipoQuarto() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivasDoClientePorTipo("GER2", "CLI5", "QUARTO");
        assertEquals(1, resultado.length);
    }

    @Test
    void testarExibirReservasAtivasDoClientePorTipoAuditorio() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivasDoClientePorTipo("GER2", "CLI5", "AUDITORIO");
        assertEquals(2, resultado.length);
    }

    @Test
    void testarListarReservasAtivasPorTipoQuarto() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivasPorTipo("GER2", "QUARTO");
        assertEquals(3, resultado.length);
    }

    @Test
    void testarListarReservasAtivasPorTipoaAuditorio() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivasPorTipo("GER2", "AUDITORIO");
        assertEquals(2, resultado.length);
    }

    @Test
    void testarListarReservasAtivas() {
        cadastrarBase();
        String[] resultado = driver.listarReservasAtivas("GER2");
        assertEquals(5, resultado.length);
    }

    @Test
    void testarListarReservas() {
        cadastrarBase();
        driver.cancelarReserva("CLI4", "1");
        String[] resultado = driver.listarReservasTodas("GER2");
        assertEquals(5, resultado.length);
    }




}
