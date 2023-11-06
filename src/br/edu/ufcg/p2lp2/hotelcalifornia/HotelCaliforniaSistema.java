package br.edu.ufcg.p2lp2.hotelcalifornia;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class HotelCaliforniaSistema {

    private UsuarioController usuarioController;
    private QuartosController quartosController;
    private ReservasSessionController reservasSessionController;
    private RefeicaoController refeicaoController;
    private AreaComumController areaComumController;
    private PagamentoController pagamamentoController;

    public HotelCaliforniaSistema() {
        this.usuarioController = UsuarioController.getInstance();
        this.quartosController = QuartosController.getInstance();
        this.reservasSessionController = ReservasSessionController.getInstance();
        this.pagamamentoController = PagamentoController.getInstance();
        this.refeicaoController = RefeicaoController.getInstance();
        this.areaComumController = AreaComumController.getInstance();
        init();
    }

    public void init() {
        usuarioController.init();
        quartosController.init();
        refeicaoController.init();
        pagamamentoController.init();
        reservasSessionController.init();
        areaComumController.init();
    }

    public String cadastrarUsuario(String idAutenticacao, String nome, String tipoUsuario, long documento) {
        return this.usuarioController.cadastrarUsuario(idAutenticacao.toUpperCase(), nome, tipoUsuario, documento);
    }

    public String atualizarUsuario(String idAutenticacao, String idUsuario, String novoTipoUsuario) {
        return this.usuarioController.atualizarUsuario(idAutenticacao, idUsuario, novoTipoUsuario);
    }

    public String exibirUsuario(String idUsuario) {
        return this.usuarioController.exibirUsuario(idUsuario);
    }

    public String[] listarUsuarios() {
        return this.usuarioController.listarUsuarios();
    }

    public String disponibilizarQuartoDouble(String idAutenticacao, int idQuartoNum, double precoBase, double precoPorPessoa, String[] pedidos) {
        return this.quartosController.disponibilizarQuartoDouble(idAutenticacao,idQuartoNum, precoPorPessoa, precoBase, pedidos);
    }

    public String disponibilizarQuartoFamily(String idAutenticacao, int idQuartoNum, double precoBase, double precoPorPessoa, String[] pedidos, int qtdMaxPessoas) {
        return this.quartosController.disponibilizarQuartoFamily(idAutenticacao,idQuartoNum, precoPorPessoa, precoBase, pedidos, qtdMaxPessoas);
    }

    public String disponibilizarQuartoSingle(String idAutenticacao, int idQuartoNum, double precoBase, double precoPorPessoa) {
        return this.quartosController.disponibilizarQuartoSingle(idAutenticacao,idQuartoNum, precoPorPessoa, precoBase);
    }

    public String exibirQuarto(int idQuartoNum) {
        return quartosController.exibirQuarto(idQuartoNum);
    }

    public String[] listarQuartos() {
        return this.quartosController.listarQuartos();
    }

    public String reservarQuartoSingle(String idAutenticacao, String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes) {
        return reservasSessionController.reservarQuartoSingle(idAutenticacao,idCliente, numQuarto, dataInicio, dataFim, idRefeicoes);
    }

    public String reservarQuartoDouble(String idAutenticacao, String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos){
        return reservasSessionController.reservarQuartoDouble(idAutenticacao,idCliente, numQuarto, dataInicio, dataFim, idRefeicoes,pedidos);
    }

    public String reservarQuartoFamily(String idAutenticacao, String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos, int numPessoas){
        return reservasSessionController.reservarQuartoFamily(idAutenticacao,idCliente, numQuarto, dataInicio, dataFim, idRefeicoes,pedidos,numPessoas);
    }

    public String exibirReserva(String idAutenticacao, long idReserva){
        return reservasSessionController.exibirReserva(idAutenticacao,idReserva);
    };
    public String[] listarReservasAtivasDoCliente(String idAutenticacao, String idCliente){
        return reservasSessionController.listarReservasAtivasDoCliente(idAutenticacao,idCliente);
    };
    public String[] listarReservasAtivasDoClientePorTipo(String idAutenticacao, String idCliente, String tipo){
        return reservasSessionController.listarReservasAtivasDoClientePorTipo(idAutenticacao, idCliente, tipo);
    };
    public String[] listarReservasAtivasPorTipo(String idAutenticacao, String tipo){
        return reservasSessionController.listarReservasAtivasPorTipo(idAutenticacao,tipo);
    };
    public String[] listarReservasAtivas(String idAutenticacao){
        return reservasSessionController.listarReservasAtivas(idAutenticacao);
    };
    public String[] listarReservasTodas(String idAutenticacao){
        return reservasSessionController.listarReservasTodas(idAutenticacao);
    };

    public String disponibilizarRefeicao(String idAutenticacao, String tipoRefeicao, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valor, boolean disponivel) {
        return refeicaoController.disponibilizarRefeicao(idAutenticacao, tipoRefeicao, titulo, horarioInicio, horarioFinal, valor, disponivel);
    }
    public String alterarRefeicao(long idRefeicao, LocalTime horarioInicio, LocalTime horarioFinal, double valorPorPessoa, boolean disponivel){
        return refeicaoController.alterarRefeicao(idRefeicao, horarioInicio, horarioFinal, valorPorPessoa, disponivel);
    }
    public String exibirRefeicao(long idRefeicao){
        return refeicaoController.exibirRefeicao(idRefeicao);
    }
    public String[] listarRefeicoes() {
        return refeicaoController.listarRefeicoes();
    }
    public String reservarRestaurante(String idAutenticacao, String idCliente, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdePessoas, String refeicao){
        return reservasSessionController.reservarRestaurante(idAutenticacao, idCliente, dataInicio, dataFim, qtdePessoas, refeicao);
    }
    public String disponibilizarFormaDePagamento(String idAutenticacao, String formaPagamento, double percentualDesconto){
        return pagamamentoController.disponibilizarFormaDePagamento(idAutenticacao, formaPagamento, percentualDesconto);
    }
    public String alterarFormaDePagamento(String idAutenticacao, int idFormaPagamento, String formaPagamento, double percentualDesconto){
        return pagamamentoController.alterarFormaDePagamento(idAutenticacao, idFormaPagamento, formaPagamento, percentualDesconto);
    }
    public String exibirFormaPagamento(int idFormaPagamento) {
        return pagamamentoController.exibirFormaPagamento(idFormaPagamento);
    }

    public String[] listarFormasPagamentos() {
        return pagamamentoController.listarFormasPagamentos();
    }

    public String pagarReservaComDinheiro(String idCliente, long idReserva, String nomeTitular){
        return this.pagamamentoController.pagarReservaComDinheiro(idCliente, idReserva, nomeTitular);
    }

    public String pagarReservaComCartao(String idCliente, long idReserva, String nomeTitular, String numCartao, String validade, String codigoDeSeguranca, int qtdeParcelas){
        return pagamamentoController.pagarReservaComCartao(idCliente, idReserva, nomeTitular, numCartao, validade, codigoDeSeguranca, qtdeParcelas);
    }
    public String pagarReservaComPix(String idCliente, long idReserva, String nomeTitular, String cpf, String banco){
        return this.pagamamentoController.pagarReservaComPix(idCliente, idReserva, nomeTitular, cpf, banco);
    }

    public String cancelarReserva(String idCliente, String idReserva){
        return this.reservasSessionController.cancelarReserva(idCliente, idReserva);
    }
    public String disponibilizarAreaComum(String idAutenticacao, String tipoAreaComum, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas){
        return areaComumController.disponibilizarAreaComum(idAutenticacao, tipoAreaComum, titulo, horarioInicio, horarioFinal, valorPessoa, disponivel, qtdMaxPessoas);
    }
    public String alterarAreaComum(String idAutenticacao, long idAreaComum, LocalTime novoHorarioInicio, LocalTime novoHorarioFinal, double novoPreco, int capacidadeMax, boolean ativa){
        return areaComumController.alterarAreaComum(idAutenticacao, idAreaComum, novoHorarioInicio, novoHorarioFinal, novoPreco, capacidadeMax, ativa);
    }
    public String exibirAreaComum(long idAreaComum){
        return areaComumController.exibirAreaComum(idAreaComum);
    };
    public String[] listarAreasComuns(){
            return areaComumController.listarAreasComuns();
    }

    public String reservarAuditorio(String idAutenticacao, String idCliente, long idAuditorio, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdMaxPessoas){
      return reservasSessionController.reservarAuditorio(idAutenticacao,idCliente,idAuditorio,dataInicio,dataFim,qtdMaxPessoas);
    }

}
