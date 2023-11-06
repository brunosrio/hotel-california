package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.HotelCaliforniaSistema;
import br.edu.ufcg.p2lp2.hotelcalifornia.areacomum.AreaComum;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class testesReservaAuditorio {

    private HotelCaliforniaSistema driver;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    @BeforeEach
    void prepararDriver() {
        driver = new HotelCaliforniaSistema();

        dataInicio = LocalDateTime.of(2024, Month.OCTOBER, 6, 14, 0);
        dataFim = LocalDateTime.of(2024, Month.OCTOBER, 8, 12, 0);

        driver.cadastrarUsuario("ADM1", "Novo Gerente", "GER", 123);
        driver.cadastrarUsuario("ADM1", "Novo Cliente", "CLI", 124);
        driver.cadastrarUsuario("ADM1", "Novo Funcionario", "FUN", 125);
        driver.disponibilizarAreaComum("ADM1", "AUDITORIO", "Auditorio Caindo aos Pedacos",
                LocalTime.of(14, 0), LocalTime.of(22, 0), 0.0, true, 200);
    }

    @Test
    void testarReservarAuditorio() {
        String resultado = driver.reservarAuditorio("GER2", "CLI3",
                1, dataInicio, dataFim, 5);

        assertTrue(resultado.contains("[CLI3] Novo Cliente"));
        assertTrue(resultado.contains("06/10/2024 14:00:00 ate 08/10/2024 22:00:00"));
        assertTrue(resultado.contains("Qtde. de Convidados: 5 pessoa(s)"));
        assertTrue(resultado.contains("Valor por pessoa: Grátis"));
        assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO"));
    }

    @Test
    void testarConflitoDatas() {
        driver.reservarAuditorio("GER2", "CLI3",
                1, dataInicio, dataFim, 5);
        try {
            driver.reservarAuditorio("GER2", "CLI3",
                    1, dataInicio, dataFim, 5);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("JA EXISTE RESERVA"));
        }
    }

    @Test
    void testarAdmReservarAuditorio() {
        driver.reservarAuditorio("GER2", "CLI3",
                1, dataInicio, dataFim, 5);
        try {
            driver.reservarAuditorio("ADM1", "CLI3",
                    1, dataInicio, dataFim, 5);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));
            assertTrue(htc.getMessage().contains("CADASTRAR UMA RESERVA"));
        }
    }

    @Test
    void testarClienteReservarAuditorio() {
        driver.reservarAuditorio("GER2", "CLI3",
                1, dataInicio, dataFim, 5);
        try {
            driver.reservarAuditorio("CLI3", "CLI3",
                    1, dataInicio, dataFim, 5);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));
            assertTrue(htc.getMessage().contains("CADASTRAR UMA RESERVA"));
        }
    }

    @Test
    void testarReservaAuditorioCapacidadeInvalida() {
        driver.reservarAuditorio("GER2", "CLI3",
                1, dataInicio, dataFim, 5);
        try {
            driver.reservarAuditorio("GER2", "CLI3",
                    1, dataInicio.plusDays(3), dataFim, 151);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("CAPACIDADE EXCEDIDA"));
        }

        try {
            driver.reservarAuditorio("GER2", "CLI3",
                    1, dataInicio.plusDays(3), dataFim, 6);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("CAPACIDADE EXCEDIDA"));
        }
    }
}