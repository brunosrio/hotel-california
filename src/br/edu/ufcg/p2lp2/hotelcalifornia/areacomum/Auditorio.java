package br.edu.ufcg.p2lp2.hotelcalifornia.areacomum;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Auditorio extends AreaComum{

    private String disponibilidade;
    public Auditorio(long idAreaComum, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas){
        super(idAreaComum, titulo, horarioInicio, horarioFinal, valorPessoa, disponivel, qtdMaxPessoas);
    }

    @Override
    public String toString(){
        if(super.isDisponivel()){
            this.disponibilidade = "VIGENTE.";
        }else{
            this.disponibilidade = "INDISPONIVEL.";
        }
        if(getValorPessoa() > 0){
        return String.format("[%d] AUDITORIO: %s (%s as %s). ",super.getIdAreaComum(),super.getTitulo(),formataHora(super.getHorarioInicio()),formataHora(super.getHorarioFinal())) +
                String.format("Valor por pessoa: R$%.2f",getValorPessoa()).replace(".",",") +
                String.format(". Capacidade: %d pessoa(s). %s",super.getQtdMaxPessoas(),this.disponibilidade);
        }else{
        return String.format("[%d] AUDITORIO: %s (%s as %s).",super.getIdAreaComum(),super.getTitulo(),formataHora(super.getHorarioInicio()),formataHora(super.getHorarioFinal())) +
                    String.format(" Valor por pessoa: Grátis. Capacidade: %d pessoa(s). %s",super.getQtdMaxPessoas(),this.disponibilidade);
        }
    }

    public String formataHora(LocalTime hora){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH'h'mm");
        return dtf.format(hora);
    }

}
