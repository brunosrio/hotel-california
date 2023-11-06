package br.edu.ufcg.p2lp2.hotelcalifornia.areacomum;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Random;

public abstract class AreaComum {

    private String titulo;
    private long idAreaComum;
    private LocalTime horarioInicio;
    private LocalTime horarioFinal;
    private double valorPessoa;
    private boolean disponivel;
    private int qtdMaxPessoas;

    public AreaComum(long idAreaComum,String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas){
        this.titulo = titulo;
        this.horarioInicio = horarioInicio;
        this.horarioFinal = horarioFinal;
        this.valorPessoa = valorPessoa;
        this.disponivel = disponivel;
        this.qtdMaxPessoas = qtdMaxPessoas;
        this.idAreaComum = idAreaComum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaComum areaComum = (AreaComum) o;
        return Objects.equals(titulo, areaComum.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo, idAreaComum);
    }

    @Override
    public abstract String toString();

    public String getTitulo() {
        return titulo;
    }

    public long getIdAreaComum() {
        return idAreaComum;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFinal() {
        return horarioFinal;
    }

    public double getValorPessoa() {
        return valorPessoa;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public int getQtdMaxPessoas() {
        return qtdMaxPessoas;
    }

    public void alterarAreaComum(LocalTime novoHorarioInicio, LocalTime novoHorarioFinal, double novoPreco, int capacidadeMax, boolean ativa){
        this.horarioInicio = novoHorarioInicio;
        this.horarioFinal = novoHorarioFinal;
        this.valorPessoa = novoPreco;
        this.qtdMaxPessoas = capacidadeMax;
        this.disponivel = ativa;
    }
}
