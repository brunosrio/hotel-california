package br.edu.ufcg.p2lp2.hotelcalifornia.usuario;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class testesUsuario {

    private UsuarioController controladorBase ;

    @BeforeEach
    void preparaSistemaBase(){
        this.controladorBase = UsuarioController.getInstance();
    }

    @BeforeEach
    void cadastraUsuariosBase() {
        controladorBase.cadastrarUsuario("ADM1", "Gerente teste", "GER", 345L);
        controladorBase.cadastrarUsuario("ADM1", "Funcionário 1","FUN",32L);
        controladorBase.cadastrarUsuario("ADM1", "Cliente um","CLI",32L);
    }

    //Testes US1
    @Test
    void testarGerenteCadastrarAdm() {
        try {
            controladorBase.cadastrarUsuario("GER2", "Adm teste", "ADM", 123L);
            fail();
        } catch (HotelCaliforniaException e) {
            assertEquals("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO ADM", e.getMessage());
        }
    }
    @Test
    void testarGerenteCadastrarGerente() {
        try {
            controladorBase.cadastrarUsuario("GER2", "GER teste", "GER", 123L);
            fail();
        } catch (HotelCaliforniaException e) {
            assertEquals("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO GER", e.getMessage());
        }
    }

    @Test
    void testarFuncionarioCadastrarGerente() {
        try {
            controladorBase.cadastrarUsuario("FUN3", "GER teste", "GER", 123L);
            fail();
        } catch (HotelCaliforniaException e) {
            assertEquals("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO GER", e.getMessage());
        }
    }

    @AfterEach
    void limpaControladorBase(){
        controladorBase.init();
    }

    @Test
    void testarFuncionarioCadastrarFuncionario() {
        try {
            controladorBase.cadastrarUsuario("FUN3", "Fun teste", "FUN", 123L);
            fail();
        } catch (HotelCaliforniaException e) {
            assertEquals("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO FUN", e.getMessage());
        }
    }

    @Test
    void testarFuncionarioCadastrarAdm() {
        try {
            controladorBase.cadastrarUsuario("FUN3", "Adm teste", "ADM", 123L);
            fail();
        } catch (HotelCaliforniaException e) {
            assertEquals("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO ADM", e.getMessage());
        }
    }

    @Test
    void testarClienteCadastrarCliente() {
        try {
            controladorBase.cadastrarUsuario("CLI4", "CLI teste", "CLI", 2123L);
            fail();
        } catch (HotelCaliforniaException e) {
            assertEquals("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO CLI", e.getMessage());
        }
    }

}