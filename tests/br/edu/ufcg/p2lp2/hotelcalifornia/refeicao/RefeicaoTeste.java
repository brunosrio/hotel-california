package br.edu.ufcg.p2lp2.hotelcalifornia.refeicao;

import br.edu.ufcg.p2lp2.hotelcalifornia.HotelCaliforniaSistema;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.RefeicaoController;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class RefeicaoTeste {

    private RefeicaoController driver;
    private UsuarioController users;

    @BeforeEach
    void preparaDriver(){
        this.driver = RefeicaoController.getInstance();
        this.users = UsuarioController.getInstance();
    }

    @AfterEach
    void limpaDriver(){
        driver.init();
    }

    @Test
    void testeDisponibilizaCafe(){
        users.cadastrarUsuario("ADM1","GERENTE 1","GER",123456L);
        assertEquals("[1] Cafe-da-manha: Café completo reforcado (06h00 as 10h00). Valor por pessoa: R$30,00. VIGENTE",driver.disponibilizarRefeicao("GER2","CAFE_DA_MANHA","Café completo reforcado",LocalTime.of(6, 0), LocalTime.of(10, 0),30.00,true));
    }

    @Test
    void testeDisponibilizaJanta(){
        users.cadastrarUsuario("ADM1","GERENTE 1","GER",123456L);
        assertEquals("[1] Jantar: Jantar a dois (18h00 as 20h00). Valor por pessoa: R$30,00. INDISPONIVEL",driver.disponibilizarRefeicao("GER2","JANTAR","Jantar a dois",LocalTime.of(18, 0), LocalTime.of(20, 0),30.00,false));
    }

    @Test
    void testeDisponibilizaRefeicaoSemPermissao(){
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class,() ->  driver.disponibilizarRefeicao("ADM1","CAFE_DA_MANHA","Café completo reforcado",LocalTime.of(6, 0), LocalTime.of(10, 0),30.00,true));
        assertTrue(hce.getMessage().toUpperCase().contains("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA REFEICAO"));
    }

    @Test
    void testeDisponibilizaRefeicaoUserNaoExiste(){
        HotelCaliforniaException hce = assertThrows(HotelCaliforniaException.class,() ->  driver.disponibilizarRefeicao("GER1","CAFE_DA_MANHA","Café completo reforcado",LocalTime.of(6, 0), LocalTime.of(10, 0),30.00,true));
        assertTrue(hce.getMessage().toUpperCase().contains("USUARIO NAO EXISTE"));
    }

    @Test
    void testeAlterarRefeicao(){
        users.cadastrarUsuario("ADM1","GERENTE 1","GER",123456L);

        assertEquals("[1] Jantar: Jantar a dois (18h00 as 20h00). Valor por pessoa: R$30,00. INDISPONIVEL"
                ,driver.disponibilizarRefeicao("GER2","JANTAR","Jantar a dois",LocalTime.of(18, 0), LocalTime.of(20, 0),30.00,false));

        assertEquals("[1] Jantar: Jantar a dois (19h00 as 21h00). Valor por pessoa: R$40,00. VIGENTE",driver.alterarRefeicao(1,LocalTime.of(19, 0), LocalTime.of(21, 0),40.00,true));
    }

    @Test
    void testeListarRefeicoes(){
        users.cadastrarUsuario("ADM1","GERENTE 1","GER",123456L);
        driver.disponibilizarRefeicao("GER2","JANTAR","Jantar a dois",LocalTime.of(18, 0), LocalTime.of(20, 0),30.00,false);
        driver.disponibilizarRefeicao("GER2","ALMOCO","Feijoada do Diego",LocalTime.of(19, 0), LocalTime.of(21, 0),40.00,true);
        String[] resultado = driver.listarRefeicoes();
        assertEquals(2,resultado.length);
    }

    @Test
    void testeCalculoSri(){
        users.cadastrarUsuario("ADM1","GERENTE 1","GER",123456L);
        driver.disponibilizarRefeicao("GER2","JANTAR","Jantar a dois",LocalTime.of(18, 0), LocalTime.of(20, 0),30.00,false);
        driver.disponibilizarRefeicao("GER2","ALMOCO","Feijoada do Diego",LocalTime.of(19, 0), LocalTime.of(21, 0),40.00,true);
        long[] idRefeicoes = {1,2};
        double resultado = driver.retornarSri(idRefeicoes);
        assertEquals(70.00, resultado);
    }



}

