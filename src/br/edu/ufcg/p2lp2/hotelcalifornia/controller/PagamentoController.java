package br.edu.ufcg.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.p2lp2.hotelcalifornia.pagamento.Cartao;
import br.edu.ufcg.p2lp2.hotelcalifornia.pagamento.Dinheiro;
import br.edu.ufcg.p2lp2.hotelcalifornia.pagamento.FormaPagamento;
import br.edu.ufcg.p2lp2.hotelcalifornia.pagamento.Pix;

import java.text.DecimalFormat;

public class PagamentoController {
    private static PagamentoController instance;
    private static final int POS_CARTAO = 0;
    private static final int POS_DINHEIRO = 1;
    private static final int POS_PIX = 2;
    private UsuarioController usuarioController;
    private ReservasSessionController reservasSessionController;
    private ReservasSessionController reservaController;
    private FormaPagamento[] formasPagamento;

    private PagamentoController() {
        this.formasPagamento = new FormaPagamento[3];
        this.reservaController = ReservasSessionController.getInstance();
        this.usuarioController = UsuarioController.getInstance();
        this.reservasSessionController = ReservasSessionController.getInstance();
        this.reservaController = ReservasSessionController.getInstance();
    }

    public static PagamentoController getInstance() {
        if (instance == null) {
            instance = new PagamentoController();
        }
        return instance;
    }

    public void init() {
        this.formasPagamento = new FormaPagamento[3];
    }


    public String disponibilizarFormaDePagamento(String idAutenticacao, String formaPagamento, double percentualDesconto) {
        validarCadastroPagamento(idAutenticacao);
        String msg = "";
        switch (formaPagamento.toUpperCase()) {
            case "CARTAO_DE_CREDITO":
                verififcarSeJaExiste(POS_CARTAO);
                this.formasPagamento[POS_CARTAO] = new Cartao(POS_CARTAO + 1, percentualDesconto);
                msg = this.formasPagamento[POS_CARTAO].toString();
                break;
            case "DINHEIRO":
                verififcarSeJaExiste(POS_DINHEIRO);
                this.formasPagamento[POS_DINHEIRO] = new Dinheiro(POS_DINHEIRO + 1, percentualDesconto);
                msg = this.formasPagamento[POS_DINHEIRO].toString();
                break;
            case "PIX":
                verififcarSeJaExiste(POS_PIX);
                this.formasPagamento[POS_PIX] = new Pix(POS_PIX + 1, percentualDesconto);
                msg = this.formasPagamento[POS_PIX].toString();
                break;
            default:
                throw new HotelCaliforniaException("Forma de pagamamento não existe");
        }
        return msg;
    }

    private void verififcarSeJaExiste(int posPagamento) {
        if (this.formasPagamento[posPagamento] != null) {
            throw new HotelCaliforniaException("FORMA DE PAGAMENTO JA EXISTE");
        }
    }


    private void validarCadastroPagamento(String idAutenticacao) {
        usuarioController.validarCadastroFormaPagamento(idAutenticacao);
    }

    public String alterarFormaDePagamento(String idAutenticacao, int idFormaPagamento, String formaPagamento, double percentualDesconto) {
        validarCadastroPagamento(idAutenticacao);
        if (!(idFormaPagamento >= 1 && idFormaPagamento < 4)) {
            throw new HotelCaliforniaException("ID inválido");
        }
        this.formasPagamento[idFormaPagamento - 1].setPercentualDesconto(percentualDesconto);
        return this.formasPagamento[idFormaPagamento - 1].toString();
    }

    public String exibirFormaPagamento(int idFormaPagamento) {
        if (idFormaPagamento < 1 || idFormaPagamento > 3) {
            throw new HotelCaliforniaException("Id invalido");
        }
        return this.formasPagamento[idFormaPagamento - 1].toString();
    }

    public String[] listarFormasPagamentos() {
        int cont = 0;
        String[] saida = new String[3];
        for (FormaPagamento p : this.formasPagamento) {
            saida[cont++] = p.toString();
        }
        return saida;
    }

    public String pagarReservaComDinheiro(String idCliente, long idReserva, String nomeTitular) {
        verificarIdCliente(idCliente, idReserva);
        String pagamento = criarResumoPagamento(idReserva, POS_DINHEIRO, 1);
        return pagarReserva(idReserva, pagamento);
    }

    private String criarResumoPagamento(long idReserva, int idFormaPagamento, int qtdeParcelas) {
        double valorReserva = this.reservasSessionController.buscaValorReserva(idReserva);
        double valorDescontado = this.formasPagamento[idFormaPagamento].aplicarDesconto(valorReserva);
        StringBuilder sb = new StringBuilder();
        sb.append("REALIZADO.\n").append(this.formasPagamento[idFormaPagamento].toString()).append("\n");
        sb.append("Total Efetivamente Pago: R$").append(new DecimalFormat("#,##0.00").format(valorDescontado)).append(" em ").append(qtdeParcelas);
        sb.append("x de R$").append(new DecimalFormat("#,##0.00").format(valorDescontado / qtdeParcelas));
        return sb.toString();
    }

    public String pagarReservaComCartao(String idCliente, long idReserva, String nomeTitular, String numCartao, String validade, String codigoDeSeguranca, int qtdeParcelas) {
        verificarIdCliente(idCliente, idReserva);
        validarCartao(numCartao, codigoDeSeguranca, qtdeParcelas);
        String pagamento = criarResumoPagamento(idReserva, POS_CARTAO, qtdeParcelas);
        return pagarReserva(idReserva, pagamento);
    }

    private void validarCartao(String numCartao, String codigoDeSeguranca, int qtdeParcelas) {
        if(numCartao.length() != 16) {
            throw new HotelCaliforniaException("O numero do cartao invalido");
        }
        if (codigoDeSeguranca.length() != 3) {
            throw new HotelCaliforniaException("codigo de seguranca invalido");
        }
        if (qtdeParcelas > 12) {
            throw new HotelCaliforniaException("só é possivel dividir em ate 12x");
        }
    }

    public String pagarReservaComPix(String idCliente, long idReserva, String nomeTitular, String cpf, String banco) {
        verificarIdCliente(idCliente, idReserva);
        verificarCPF(cpf);
        String pagamento = criarResumoPagamento(idReserva, POS_PIX, 1);
        return pagarReserva(idReserva, pagamento);
    }

    private void verificarCPF(String cpf) {
        if (cpf.length() != 11) {
            throw new HotelCaliforniaException("CPF invalido");
        }
    }

    private String pagarReserva(long idReserva, String status) {
        return this.reservaController.pagarReserva(idReserva, status);
    }

    private void verificarIdCliente(String idCliente, long idReserva) {
        String id = reservaController.buscarIdClienteReserva(idReserva);
        this.usuarioController.validarPagamentoReserva(idCliente);
        if (!id.equals(idCliente)) {
            throw new HotelCaliforniaException("SOMENTE O PROPRIO CLIENTE PODERA PAGAR A SUA RESERVA");
        }

    }


}
