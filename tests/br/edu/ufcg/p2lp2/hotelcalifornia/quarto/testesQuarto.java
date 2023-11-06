package br.edu.ufcg.p2lp2.hotelcalifornia.quarto;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.QuartosController;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testesQuarto {

    private QuartosController driver;

    @BeforeEach
    void preparaDriver() {
        driver = QuartosController.getInstance();
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
    void testarCadastrarQuartoSingle() {
        String resultado = driver.disponibilizarQuartoSingle("ADM1", 1,
                5, 30);
        System.out.println(resultado);
        assertTrue(resultado.contains("Quarto Single"));
        assertTrue(resultado.contains("R$30,00"));
        assertTrue(resultado.contains("R$35,00"));
    }

    @Test
    void testarCadastroQuartoDouble() {
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        String resultado = driver.disponibilizarQuartoDouble("ADM1", 1,
                5, 30,  pedidos);
        assertTrue(resultado.contains("Quarto Double"));
        System.out.println(resultado);
        assertTrue(resultado.contains("R$30,00"));
        assertTrue(resultado.contains("R$40,00"));
        assertTrue(resultado.contains("Pedidos: [1 Frigobar, 1 cama adulta]"));
    }

    @Test
    void testarCadastroQuartoFamily() {
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        String resultado = driver.disponibilizarQuartoFamily("ADM1", 1,
                5, 30,  pedidos, 7);
        assertTrue(resultado.contains("Quarto Family"));
        assertTrue(resultado.contains("R$30,00"));
        assertTrue(resultado.contains("R$65,00"));
        assertTrue(resultado.contains("Pedidos: [1 Frigobar, 1 cama adulta]"));
        assertTrue(resultado.contains("Capacidade: 07 pessoa(s)"));
    }

    @Test
    void testarCadastrarQuartoFamilyCapacidadeInvalidaAcima() {
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        try {
            driver.disponibilizarQuartoFamily("ADM1", 1,
                    5, 30,  pedidos, 11);
            fail("Era esperado uma exceção aqui");
        } catch (HotelCaliforniaException e) {
            assertTrue(e.getMessage().contains("Quantidade"));
            assertTrue(e.getMessage().contains("inválida"));
        }

    }

    @Test
    void testarCadastrarQuartoFamilyCapacidadeInvalidaAbaixo() {
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        try {
            driver.disponibilizarQuartoFamily("ADM1", 1,
                    5, 30,  pedidos, 0);
            fail("Era esperado uma exceção aqui");
        } catch (HotelCaliforniaException e) {
            assertTrue(e.getMessage().contains("Quantidade"));
            assertTrue(e.getMessage().contains("inválida"));
        }

    }

    @Test
    void testarApenasAdmPodeCadastrarQuartos() {
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        try {
            driver.disponibilizarQuartoFamily("GER2", 1,
                    5, 30,  pedidos, 9);
            fail("Era esperado uma exceção aqui");
        } catch (HotelCaliforniaException e) {
            assertTrue(e.getMessage().contains("USUARIO"));
            assertTrue(e.getMessage().contains("NAO"));
            assertTrue(e.getMessage().contains("ADM"));
        }
    }

    @Test
    void testarExibirQuarto() {
        String[] pedidos = {"1 Frigobar", "1 cama adulta"};
        driver.disponibilizarQuartoDouble("ADM1", 1,
                5, 30,  pedidos);
        String resultado = driver.exibirQuarto(1);
        assertTrue(resultado.contains("Quarto Double"));
        assertTrue(resultado.contains("R$30,00"));
        assertTrue(resultado.contains("R$40,00"));
        assertTrue(resultado.contains("Pedidos: [1 Frigobar, 1 cama adulta]"));
    }

}
