package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.p2lp2.hotelcalifornia.refeicao.Refeicao;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReservaRestaurante extends Reserva {
    private int qtdePessoas;
    private Refeicao refeicao;
    private long diarias;

    public ReservaRestaurante(long idReserva, String cliente, LocalDateTime dataInicial, LocalDateTime dataFinal, int qtdePessoas, Refeicao refeicao) {
        super(idReserva, cliente, dataInicial, dataFinal, "PENDENTE");
        this.qtdePessoas = qtdePessoas;
        this.refeicao = refeicao;
        this.diarias = calcularDiarias();
        super.valor = calcularValorReserva();
    }

    private String formatarValor(double valor) {
        String valorStr = new DecimalFormat("#,##0.00").format(valor);
        return valorStr;
    }

    private double calcularValorReserva() {
         return qtdePessoas * diarias * refeicao.getValor();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!super.status) { sb.append("[CACELADA]"); }
        sb.append("[").append(this.idReserva).append("] ").append("Reserva de RESTAURANTE em favor de:\n- ");
        sb.append(this.getInfoCliente()).append("\n").append("Detalhes da reserva: \n");
        sb.append("- Periodo: ").append(formatarData(super.dataInicio));
        sb.append(" ate ").append(formatarData(super.dataFim));
        sb.append("\n- Qtde. de Convidados: ").append(this.qtdePessoas).append(" pessoa(s)\n");
        sb.append("- Refeicao incluida: ").append(refeicao.toString());
        sb.append("\nVALOR TOTAL DA RESERVA: R$");
        sb.append(formatarValor(this.qtdePessoas * this.refeicao.getValor()));
        sb.append(" x").append(this.diarias).append(" (diarias)");
        sb.append(" => R$").append(formatarValor(this.valor));
        sb.append("\nSITUACAO DO PAGAMENTO: ").append(super.statusPagamento).append(".");
        return sb.toString();
    }

    private String getInfoCliente() {
        UsuarioController usuarioController = UsuarioController.getInstance();
        return usuarioController.buscarCliente(super.cliente);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaRestaurante that = (ReservaRestaurante) o;
        return idReserva == that.idReserva;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva);
    }

}
