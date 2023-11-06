package br.edu.ufcg.p2lp2.hotelcalifornia.areacomum;

import br.edu.ufcg.p2lp2.hotelcalifornia.HotelCaliforniaSistema;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.AreaComumController;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class testesAreaComum {

    private AreaComumController driver;

    @BeforeEach
    void prepararDriver() {
        driver = AreaComumController.getInstance();
        driver.init();
    }

    @BeforeEach
    void adicionarUsuarios() {
        UsuarioController usr = UsuarioController.getInstance();
        usr.init();
        usr.cadastrarUsuario("ADM1", "Gerente A", "GER", 1234);
        usr.cadastrarUsuario("ADM1", "Funcionario A", "FUN", 1234);
        usr.cadastrarUsuario("ADM1", "Cliente A", "CLI", 1234);
    }


    @Test
    void testarCadastrarAreaComum() {
        String resultado = driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Caindo aos Pedacos",
                LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);
        assertTrue(resultado.contains("AUDITORIO: Auditorio Caindo aos Pedacos"));
        assertTrue(resultado.contains("(14h00 as 22h00)"));
        assertTrue(resultado.contains("Valor por pessoa: Grátis"));
        assertTrue(resultado.contains("VIGENTE"));
    }

    @Test
    void testarGerenteNaoPodeCadastrarAreaComum() {
        try {
            driver.disponibilizarAreaComum("GER2","AUDITORIO", "Auditorio Caindo aos Pedacos",
                    LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);
            fail();
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));
        }
    }

    @Test
    void testarFuncionarioNaoPodeCadastrarAreaComum() {
        try {
            driver.disponibilizarAreaComum("FUN3","AUDITORIO", "Auditorio Caindo aos Pedacos",
                    LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);
            fail();
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("NAO E POSSIVEL PARA USUARIO"));
        }
    }

    @Test
    void testarCadastrarAreaComumJaCadastrada() {
        driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Caindo aos Pedacos",
                LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);
        try {
            driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Caindo aos Pedacos",
                    LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);
        } catch (HotelCaliforniaException htc) {
            assertTrue(htc.getMessage().contains("JA EXISTE"));
        }
    }

    @Test
    void alterarAreaComum() {
        driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Caindo aos Pedacos",
                LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);

        String resultado = driver.alterarAreaComum("ADM1", 1L,
                LocalTime.of(9,0), LocalTime.of(23,59), 500.00, 100, true);

        assertTrue(resultado.contains("AUDITORIO: Auditorio Caindo aos Pedacos"));
        assertTrue(resultado.contains("(09h00 as 23h59)"));
        assertTrue(resultado.contains("Valor por pessoa: R$500,00"));
        assertTrue(resultado.contains("Capacidade: 100 pessoa(s)"));
        assertTrue(resultado.contains("VIGENTE"));
    }

    @Test
    void exibirAreaComum() {
        driver.disponibilizarAreaComum("ADM1","AUDITORIO", "Auditorio Caindo aos Pedacos",
                LocalTime.of(14,0), LocalTime.of(22,0), 0.0, true, 150);

        String resultado = driver.exibirAreaComum(1L);

        assertTrue(resultado.contains("AUDITORIO: Auditorio Caindo aos Pedacos"));
        assertTrue(resultado.contains("(14h00 as 22h00)"));
        assertTrue(resultado.contains("Valor por pessoa: Grátis"));
        assertTrue(resultado.contains("Capacidade: 150 pessoa(s)"));
        assertTrue(resultado.contains("VIGENTE"));
    }
}
