package br.edu.ufcg.p2lp2.hotelcalifornia.pagamento;

public class Dinheiro extends FormaPagamento{
    public Dinheiro(int id, double percentualDesconto) {
        super(id, percentualDesconto);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(super.getId()).append("] ").append("Forma de pagamento: DINHEIRO ");
        sb.append("(").append(super.formartarPercentual()).append("% de desconto em pagamentos).");
        return sb.toString();
    }
}
