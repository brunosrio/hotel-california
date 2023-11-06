package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import java.time.LocalDateTime;

public class ReservaQuartoSingle extends ReservaQuarto {

    public ReservaQuartoSingle(long idReserva,String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, long[] refeicoes){
        super(idReserva,idCliente, numQuarto, dataInicio, dataFim, refeicoes);
    }
}
