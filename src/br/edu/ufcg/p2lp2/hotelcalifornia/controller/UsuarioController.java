package br.edu.ufcg.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.p2lp2.hotelcalifornia.usuario.*;

import java.util.HashMap;
import java.util.Map;

public class UsuarioController {
    private static UsuarioController instance;
    private Map<String, Usuario> usuarios;

    private UsuarioController() {
        this.usuarios = new HashMap<>();
        cadastrarAdmPadrao();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    public void init() {
        this.usuarios.clear();
        cadastrarAdmPadrao();
    }

    public String mostrarUsuariosParaTest() {
        return this.usuarios.values().toString();
    }


    private void cadastrarAdmPadrao() {
        this.usuarios.put("ADM1", new Administrador("ADM1","Joao Costa",123456L));
    }

    public String cadastrarUsuario(String idAutenticacao, String nome, String tipoUsuario, long documento) {
        String idUsuario = tipoUsuario + (this.usuarios.size() + 1);
        validarCadastro(idAutenticacao, tipoUsuario);
        switch (tipoUsuario) {
            case "ADM":
                this.usuarios.put(idUsuario, new Administrador(idUsuario, nome, documento));
                break;
            case "GER":
                verificarSeJaExisteGerente();
                this.usuarios.put(idUsuario, new Gerente(idUsuario, nome, documento));
                break;
            case "FUN":
                this.usuarios.put(idUsuario, new Funcionario(idUsuario, nome, documento));
                break;
            case "CLI":
                this.usuarios.put(idUsuario, new Cliente(idUsuario, nome, documento));
                break;
            default:
                throw new HotelCaliforniaException("Tipo de Usuario inexistente");
        }

        return this.usuarios.get(idUsuario).toString();
    }

    private void verificarSeJaExisteGerente() {
        for (String chave : this.usuarios.keySet()) {
            if (chave.contains("GER")) {
                throw new HotelCaliforniaException("SO DEVE HAVER UM GERENTE NO HOTEL");
            }
        }
    }

    private void validarCadastro(String idAutenticacao, String tipoUsuario) {
        Usuario u = this.usuarios.get(idAutenticacao);
        if (u == null) {
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
        if (!u.validarCadastro(tipoUsuario)) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UM NOVO USUARIO DO TIPO " + tipoUsuario);
        }
    }

    private void verificaSeUsuarioExiste(String idUsuario) {
        if (!this.usuarios.containsKey(idUsuario)) {
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
    }

    private void atualizaGerenteParaFuncionario() {
        for (String chave : this.usuarios.keySet()) {
            if (chave.contains("GER")) {
                atualizarUsuario("ADM1", chave, "FUN");
            }
        }
    }

    public String atualizarUsuario(String idAutenticacao, String idUsuario, String novoTipoUsuario) {
        validarAlteracaoUsuario(idAutenticacao, idUsuario);
        verificaSeUsuarioExiste(idUsuario);
        if (novoTipoUsuario.equals("GER")) {
            atualizaGerenteParaFuncionario();
        }
        String nomeUsuario = this.usuarios.get(idUsuario).getNome();
        long documentoUsuario = this.usuarios.get(idUsuario).getDocumento();
        this.usuarios.remove(idUsuario);
        return cadastrarUsuario(idAutenticacao, nomeUsuario, novoTipoUsuario, documentoUsuario);
    }

    private void validarAlteracaoUsuario(String idAutenticacao, String idUsuario) {
        if (!(idAutenticacao.contains("ADM"))) {
            throw new HotelCaliforniaException("APENAS O ADMINISTRADOR PODE ATUALIZAR OS USUARIOS");
        }
        if (idUsuario.contains("CLI")) {
            throw new HotelCaliforniaException("Cliente não pode mudar de cargo");
        }
    }

    public String exibirUsuario(String idUsuario) {
        return this.usuarios.get(idUsuario).toString();
    }

    public String[] listarUsuarios() {
        int cont = 0;
        String[] usuarios = new String[this.usuarios.size()];
        for (Usuario u : this.usuarios.values()) {
            usuarios[cont++] = u.toString();
        }
        return usuarios;
    }

    public void validarCadastroQuarto(String idAutenticacao) {
        if(!this.usuarios.containsKey(idAutenticacao)){
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
        if(!(idAutenticacao.contains("ADM"))) {
            throw new HotelCaliforniaException("USUARIO NAO E ADMINISTRADOR");
        }
    }


    public Usuario getUsuario(String idAutenticao){
        if(usuarios.containsKey(idAutenticao)){
            return usuarios.get(idAutenticao);
        }else{
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
    }

    public void validarReservaQuarto(String idAutenticao){
        if(!(idAutenticao.contains("GER") || (idAutenticao).contains("FUN"))){
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA RESERVA");
        }
    }

    public void validarCadastroRefeicao(String idAutenticacao) {
        if (!this.usuarios.containsKey(idAutenticacao)) {
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
        if (!(idAutenticacao.contains("GER") || idAutenticacao.contains("FUN"))) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA REFEICAO");
        }
    }


    public void validarReservaRestaurante(String idAutenticacao) {
        if (!(idAutenticacao.contains("GER") || idAutenticacao.contains("FUN"))) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA RESERVA");
        }
    }


    public Cliente getCliente(String idCliente) {
        if (idCliente.contains("CLI")) {
            return (Cliente) getUsuario(idCliente);
        }
        throw new HotelCaliforniaException("Cliente não encontrado");

}

    public String buscarCliente(String idCliente) {
        if (this.usuarios.get(idCliente) != null) {
            return this.usuarios.get(idCliente).toString();
        }
        throw new HotelCaliforniaException("ID invalido");
    }

    public void validarCadastroFormaPagamento(String idAutenticacao) {
        if (!idAutenticacao.contains("ADM")) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA FORMA DE PAGAMENTO");
        }
        if (!this.usuarios.containsKey(idAutenticacao)) {
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
    }

    public void validarPagamentoReserva(String idCliente) {
        Usuario cliente = this.usuarios.get(idCliente);
        if (cliente == null) { throw new HotelCaliforniaException("ID invalido"); }
    }


    //US10 e US11

    public void validarDisponibilizaAreaComum(String idAutenticacao) {
        if(!this.usuarios.containsKey(idAutenticacao)){
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
        if(!(idAutenticacao.contains("ADM"))) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA AREA COMUM");
        }
    }

    public void validarReservaAreaComum(String idAutenticacao) {
        if(!this.usuarios.containsKey(idAutenticacao)){
            throw new HotelCaliforniaException("USUARIO NAO EXISTE");
        }
        if(!(idAutenticacao.contains("FUN") || idAutenticacao.contains("GER"))) {
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO CADASTRAR UMA RESERVA");
        }
    }
// US10 e US11

    //US06
    public void validarVizualizarReserva(String idAutenticao){
        if (!(idAutenticao.contains("CLI") || idAutenticao.contains("GER") || idAutenticao.contains("FUN"))){
            throw new HotelCaliforniaException("NAO E POSSIVEL PARA USUARIO EXIBIR/LISTAR RESERVA(S) DO CLIENTE");
        }
    }
}

