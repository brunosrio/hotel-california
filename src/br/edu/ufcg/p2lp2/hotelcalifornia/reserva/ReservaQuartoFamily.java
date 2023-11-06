package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import java.time.LocalDateTime;

public class ReservaQuartoFamily extends ReservaQuarto {

    public ReservaQuartoFamily(long idReserva,String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, long[] refeicoes, String[] pedidos, int numPessoas) {
        super(idReserva,idCliente, numQuarto, dataInicio, dataFim, refeicoes,pedidos,numPessoas);
    }
}
