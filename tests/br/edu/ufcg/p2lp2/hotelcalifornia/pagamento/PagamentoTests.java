package br.edu.ufcg.p2lp2.hotelcalifornia.pagamento;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.*;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PagamentoTests {

    private PagamentoController driver;
    private UsuarioController users;
    private ReservasSessionController reservas;
    private QuartosController quartos;
    @BeforeEach
    void preparaDriver(){
        this.driver = PagamentoController.getInstance();
        this.users = UsuarioController.getInstance();
        this.reservas = ReservasSessionController.getInstance();
        this.quartos = QuartosController.getInstance();
        users.cadastrarUsuario("ADM1","Gerente 2","GER",2838494L);
        users.cadastrarUsuario("ADM1","CLiente 2","CLI",2838884L);

        quartos.disponibilizarQuartoSingle("ADM1",201,100.00,10.00);
        String[] idRefeicoes = {"1","2"};
        reservas.reservarQuartoSingle("GER2","CLI3",201, LocalDateTime.of(2023, Month.DECEMBER,2,12,0,0),LocalDateTime.of(2023, Month.DECEMBER,5,14,0,0),idRefeicoes);
    }

    @AfterEach
    void limpaDriver() {
        driver.init();
    }

    @Test
    void testDisponibilizarFormaDepagamento(){
        assertEquals("[3] Forma de pagamento: PIX (5% de desconto em pagamentos).",driver.disponibilizarFormaDePagamento("ADM1","PIX",0.05));
    }

    @Test
    void testDisponibilizarFormaDepagamentoCliente(){
        users.cadastrarUsuario("ADM1","Cliente A","CLI",123456L);
        assertThrows(HotelCaliforniaException.class, () -> driver.disponibilizarFormaDePagamento("CLI2","PIX",0.05),"NAO E POSSIVEL PARA USUARIO CADASTRAR UMA FORMA DE PAGAMENTO");
    }
    //US08 - PAGAR RESERVA

    @Test
    void testPagarReservaComCartao(){
        driver.disponibilizarFormaDePagamento("ADM1","CARTAO_DE_CREDITO",0.0);
        String resultado = driver.pagarReservaComCartao("CLI3",1,"Cliente A","1234987643216789","09/2024","902",3);
        assertAll(
                ()-> assertTrue(resultado.contains("SITUACAO DO PAGAMENTO: REALIZADO")),
                ()-> assertTrue(resultado.contains("Forma de pagamento: CARTAO DE CREDITO (0% de desconto em pagamentos")),
                ()-> assertTrue(resultado.contains("Total Efetivamente Pago: R$440.00 em 3x de R$146.67."))
        );
    }



}
