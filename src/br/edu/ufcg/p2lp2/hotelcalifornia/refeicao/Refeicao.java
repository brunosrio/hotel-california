package br.edu.ufcg.p2lp2.hotelcalifornia.refeicao;

import java.time.LocalTime;
import java.util.Objects;

public class Refeicao {
    private long id;
    private String tipo;
    private String titulo;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private double valor;
    private boolean disponivel;

    public Refeicao(long id, String tipo,String titulo, LocalTime horarioInicio, LocalTime horarioFim, double valor, boolean disponivel) {
        this.id = id;
        this.tipo = setTipo(tipo);
        this.titulo = titulo;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.valor = valor;
        this.disponivel = disponivel;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public String setTipo(String tipo) {
        String msg = "";
        if (tipo.contains("CAFE")) {
            msg = "Cafe-da-manha";
        } else if (tipo.contains("ALMOCO")) {
            msg = "Almoco";
        } else {
            msg = "Jantar";
        }
        return msg;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime novoHorarioInicio) {
        this.horarioInicio = novoHorarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime novoHorarioFim) {
        this.horarioFim = novoHorarioFim;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

   public boolean isDisponivel() {
        return this.disponivel;
   }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String formatarValor(double valor) {
        String valorStr = String.format("%.2f",valor).replace(".",",");
        return valorStr;
    }

    public String formatarData(LocalTime data) {
        String dataStr = String.valueOf(data).replace(":", "h");
        return dataStr;
    }
    private String verificarStatus() {
        return this.isDisponivel() ? "VIGENTE" : "INDISPONIVEL";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Refeicao refeicao = (Refeicao) o;
        return id == refeicao.id && Objects.equals(titulo, refeicao.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo);
    }

    @Override
    public String toString() {
        String status = verificarStatus();
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.getId()).append("] ");
        sb.append(this.tipo).append(": ").append(this.getTitulo()).append(" ");
        sb.append("(").append(formatarData(this.horarioInicio)).append(" as ");
        sb.append(formatarData(this.horarioFim)).append(").");
        sb.append(" Valor por pessoa: ");
        sb.append("R$").append(formatarValor(this.valor)).append(". ").append(status);
        return sb.toString();
    }


}
