package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.QuartosController;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.RefeicaoController;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoDouble;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoFamily;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoSingle;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoSuper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class ReservaQuarto extends Reserva{
    private QuartosController quartosController;
    private UsuarioController usuarioController;
    private RefeicaoController refeicaoController;
    private int idQuarto;
    private int qtdHospedes;
    private long[] refeicoes;
    private int duracao;
    private String[] pedidos;

    public ReservaQuarto(long idReserva,String idCliente, int idQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, long[] refeicoes){
       super(idReserva,idCliente,dataInicio,dataFim,"PENDENTE");
        quartosController = QuartosController.getInstance();
        usuarioController = UsuarioController.getInstance();
        refeicaoController = RefeicaoController.getInstance();
         this.idQuarto = idQuarto;
         this.qtdHospedes = quartosController.getQuarto(idQuarto).getVagas();
         this.refeicoes = refeicoes;
         this.duracao = calculaDiasDaReserva(dataInicio,dataFim);
        quartosController.getQuarto(idQuarto).setPedidos(pedidos);
    }

    public ReservaQuarto(long idReserva,String idCliente, int idQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, long[] refeicoes, String[] pedidos){
        super(idReserva,idCliente,dataInicio,dataFim,"PENDENTE");
        quartosController = QuartosController.getInstance();
        usuarioController = UsuarioController.getInstance();
        refeicaoController = RefeicaoController.getInstance();
        this.idQuarto = idQuarto;
        this.qtdHospedes = quartosController.getQuarto(idQuarto).getVagas();
        this.refeicoes = refeicoes;
        this.duracao = calculaDiasDaReserva(dataInicio,dataFim);
        this.pedidos = pedidos;
        quartosController.getQuarto(idQuarto).setPedidos(pedidos);
    }

    public ReservaQuarto(long idReserva,String idCliente, int idQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, long[] refeicoes, String[] pedidos,int qtdPessoas){
        super(idReserva,idCliente,dataInicio,dataFim,"PENDENTE");
        quartosController = QuartosController.getInstance();
        usuarioController = UsuarioController.getInstance();
        refeicaoController = RefeicaoController.getInstance();
        this.idQuarto = idQuarto;
        this.qtdHospedes = qtdPessoas;
        this.refeicoes = refeicoes;
        this.duracao = calculaDiasDaReserva(dataInicio,dataFim);
        this.pedidos = pedidos;
        quartosController.getQuarto(idQuarto).setPedidos(pedidos);
    }

    public double valorTotalDaReserva(){
        double numeroDeDiarias = calculaDiasDaReserva(dataInicio,dataFim);
        super.valor = numeroDeDiarias * valorDiarioDaReserva();
        return numeroDeDiarias * valorDiarioDaReserva();
    }

    public double valorDiarioDaReserva(){
        double sri = refeicaoController.retornarSri(refeicoes);
        return valorDaDiaria() + this.qtdHospedes * sri;
    }


    public String refeicoesDaReserva(){
        return  Arrays.toString(refeicaoController.retornarRefeicoesParaReserva(this.refeicoes));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaQuarto that = (ReservaQuarto) o;
        return idReserva == that.idReserva;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva);
    }

    @Override
    public String toString(){
        String out = "";
        DecimalFormat df = new DecimalFormat("000000");
        DecimalFormat df2 = new DecimalFormat("#,###");
        if (!super.status) { out += "[CANCELADA] "; }
        out += String.format("[%s] Reserva de quarto em favor de:" +
                        "\n- %s\n Detalhes da instalacao: " +
                        "\n- %s\nDetalhes da reserva: " +
                        "\n- Periodo: %s ate %s\n - " +
                        "No. Hospedes: %d pessoa(s) " +
                        "\n- Refeicoes incluidas: %s ",df.format(super.idReserva),this.usuarioController.getUsuario(super.cliente).toString(),quartosController.getQuarto(idQuarto).exibirQuarto(),formatarDataInicio(super.dataInicio),
                formatarDataFim(super.dataFim),quartosController.getQuarto(idQuarto).getVagas(), refeicoesDaReserva());

        out += String.format("\nVALOR TOTAL DA RESERVA: R$%.2f x%d (diarias) => R$",valorDiarioDaReserva(),this.duracao).replace(".",",");
        out += df2.format(valorTotalDaReserva()).replace(",",".") + ",00" + "\n";
        out += "\nSITUACAO DO PAGAMENTO: " + statusPagamento + ".";

        return out;
    }


    public int calculaDiasDaReserva(LocalDateTime dataInicio, LocalDateTime dataFim){
        Duration duracao = Duration.between(dataInicio,dataFim);
        double dias = Math.ceil(((double) duracao.toHours() /24));
        return (int) dias;
    }

    public double valorDaDiaria(){
        return quartosController.getQuarto(idQuarto).valorDaDiaria();
    }

    private String formatarDataInicio(LocalDateTime dataInicio) {
        LocalTime hora14 = LocalTime.of(14, 0);
        LocalDateTime dataHoraInicio = dataInicio.toLocalDate().atTime(hora14);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataHoraInicio.format(formatter);
        return dataHoraFormatada;
    }

    private String formatarDataFim(LocalDateTime dataFinal) {
        LocalTime hora12 = LocalTime.of(12, 0);
        LocalDateTime dataHoraFinal = dataFinal.toLocalDate().atTime(hora12);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHoraFinal.format(formatter);
    }

}
