package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.controller.AreaComumController;
import br.edu.ufcg.p2lp2.hotelcalifornia.controller.UsuarioController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class ReservaAreaComum extends Reserva{
    private AreaComumController areaComumController;
    private UsuarioController usuarioController;
    private int qtdPessoas;
    private long idAuditorio;
    private double valorTotalReservaAreaComum;
    public ReservaAreaComum(long idAuditorio,long idReserva,String idCliente, LocalDateTime dataInicio,LocalDateTime dataFim,int qtdPessoas,String statusPagamento){
        super(idReserva,idCliente,dataInicio,dataFim,statusPagamento);
        areaComumController = AreaComumController.getInstance();
        usuarioController = UsuarioController.getInstance();
        this.qtdPessoas = qtdPessoas;
        this.idAuditorio = idAuditorio;
        this.valorTotalReservaAreaComum = valorTotal(qtdPessoas,areaComumController.getAreaComum(idAuditorio).getValorPessoa());
    }

    private double valorTotal(int qtdPessoas,double valorPessoa){
        return qtdPessoas * calcularDiarias() * valorPessoa;
    }

    private double valorPorDia(){
        return this.qtdPessoas * areaComumController.getAreaComum(idAuditorio).getValorPessoa();
    }

    @Override
    public String toString(){
        if(areaComumController.getAreaComum(idAuditorio).getValorPessoa() == 0){
            return String.format("[%d] Reserva de AUDITORIO em favor de:\n" +
                    "- %s\n" +
                    "Detalhes da reserva: \n" +
                    "- Periodo: %s ate %s\n" +
                    "- Qtde. de Convidados: %d pessoa(s)\n",this.idAuditorio,usuarioController.getCliente(super.getCliente()).toString(), formatarDataInicio(),formatarDataFinal(),this.qtdPessoas) +
                    String.format("- Valor por pessoa: Grátis\n VALOR TOTAL DA RESERVA: Grátis\n",valorPorDia(), calcularDiarias(),this.valorTotalReservaAreaComum).replace(".",",") +
                    String.format("SITUACAO DO PAGAMENTO: REALIZADO.\n");
        }
        return String.format("[%d] Reserva de AUDITORIO em favor de:\n" +
                "- %s\n" +
                "Detalhes da reserva: \n" +
                "- Periodo: %s ate %s\n" +
                "- Qtde. de Convidados: %d pessoa(s)\n",this.idAuditorio,usuarioController.getCliente(super.getCliente()).toString(), formatarDataInicio(),formatarDataFinal(),this.qtdPessoas) +
                String.format("- Valor por pessoa: R$%.2f\n VALOR TOTAL DA RESERVA: R$%.2f x%d (diarias) => R$%.2f\n",areaComumController.getAreaComum(idAuditorio).getValorPessoa(),valorPorDia(), calcularDiarias(),this.valorTotalReservaAreaComum).replace(".",",") +
                String.format("SITUACAO DO PAGAMENTO: %s.\n",this.statusPagamento);
    }

    private String formatarDataInicio(){
        LocalDateTime dataInicio = super.dataInicio;
        LocalTime horaInicioAuditorio = areaComumController.getAreaComum(idAuditorio).getHorarioInicio();
        LocalDateTime dataHoraInicio = dataInicio.toLocalDate().atTime(horaInicioAuditorio);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHoraInicio.format(formatter);
    }

    private String formatarDataFinal(){
        LocalDateTime dataInicio = super.dataFim;
        LocalTime horaInicioAuditorio = areaComumController.getAreaComum(idAuditorio).getHorarioFinal();
        LocalDateTime dataHoraInicio = dataInicio.toLocalDate().atTime(horaInicioAuditorio);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dataHoraInicio.format(formatter);
    }
}
