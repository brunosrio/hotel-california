package br.edu.ufcg.p2lp2.hotelcalifornia.reserva;

import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public abstract class Reserva {

    protected long idReserva;
    protected double valor;
    protected String cliente;
    protected LocalDateTime dataInicio;
    protected LocalDateTime dataFim;
    protected String statusPagamento;

    protected boolean status;

    public Reserva(long id, String cliente, LocalDateTime dataInicio, LocalDateTime dataFim, String statusPagamento) {
        this.idReserva = id;
        this.cliente = cliente;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.statusPagamento = statusPagamento;
        this.status = true;
    }

    public String getCliente() {
        return this.cliente;
    }

    protected long calcularDiarias() {
        return this.dataInicio.until(this.dataFim, DAYS) + 1L;
    }

    public boolean verificarConflitoDatas(LocalDateTime inicio, LocalDateTime fim) {
        return (inicio.isAfter(this.dataFim) || fim.isBefore(this.dataInicio));
    }

    public String pagarReserva(String infoPagamento) {
        if (this.statusPagamento.contains("REALIZADO")) {
            throw new HotelCaliforniaException("RESERVA JA FOI PAGA");
        }
        this.statusPagamento = infoPagamento;
        return toString();
     }

    protected String formatarData(LocalDateTime data) {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(data);
    }

    public double getValor() {
        return this.valor;
    }

    public abstract String toString();

    public String cancelarReserva() {
        this.status = false;
        return this.toString();
    }

    public LocalDateTime getDataInicio() {
        return this.dataInicio;
    }

    public LocalDateTime getDataFim() {
        return this.dataFim;
    }

    public boolean isStatus() {
        return status;
    }

}
