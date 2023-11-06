package br.edu.ufcg.p2lp2.hotelcalifornia.quarto;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class QuartoSuper {

    private int vagas;
    private int idQuarto;
    private double precoPorPessoa;
    private double precoBase;
    protected String[] pedidos;
    private LocalDateTime inicioDaReserva;
    private LocalDateTime fimDaReserva;

    public QuartoSuper(int idQuarto,double precoPorPessoa,double precoBase){
        this.idQuarto = idQuarto;
        this.precoPorPessoa = precoPorPessoa;
        this.precoBase = precoBase;
    }

    public QuartoSuper(int idQuarto,double precoPorPessoa,double precoBase,String[] pedidos){
        this.idQuarto = idQuarto;
        this.precoPorPessoa = precoPorPessoa;
        this.precoBase = precoBase;
        this.pedidos = pedidos;
    }

    public double valorDaDiaria(){
        return this.precoBase + (precoPorPessoa * vagas);
    }

    public abstract String exibirQuarto();

    public int getVagas() {
        return vagas;
    }
    public void setVagas(int nVagas){
        this.vagas = nVagas;
    }

    public int getIdQuarto() {
        return idQuarto;
    }

    public double getPrecoPorPessoa() {
        return precoPorPessoa;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public String[] getPedidos(){
        return this.pedidos;
    }
    public void setPedidos(String[] pedidos){
        this.pedidos = pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuartoSuper that = (QuartoSuper) o;
        return idQuarto == that.idQuarto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuarto);
    }

    public boolean reservarQuarto(LocalDateTime inicioNovaReserva, LocalDateTime fimNovaReserva) {
        if (this.inicioDaReserva == null || this.fimDaReserva == null) {
            this.inicioDaReserva = inicioNovaReserva;
            this.fimDaReserva = fimNovaReserva;
            return true;
        } else if (inicioNovaReserva.isAfter(this.fimDaReserva) || fimNovaReserva.isBefore(this.inicioDaReserva)) {
            this.inicioDaReserva = inicioNovaReserva;
            this.fimDaReserva = fimNovaReserva;
            return true;
        } else {
            return false;
        }
    }
}
