package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import java.time.LocalDateTime;

public class ReservaQuartoDouble extends ReservaQuarto {
    public ReservaQuartoDouble(long idReserva,String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, long[] refeicoes, String[] pedidos) {
        super(idReserva,idCliente, numQuarto, dataInicio, dataFim, refeicoes,pedidos);
    }
}
