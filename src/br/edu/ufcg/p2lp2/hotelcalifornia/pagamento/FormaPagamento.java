package br.edu.ufcg.p2lp2.hotelcalifornia.pagamento;

public abstract class FormaPagamento {
    private int id;
    private double percentualDesconto;

    public FormaPagamento(int id, double percentualDesconto) {
        this.id = id;
        this.percentualDesconto = percentualDesconto;
    }

    public int getId() {
        return id;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    protected int formartarPercentual() {
        return (int) (this.percentualDesconto * 100);
    }
    public double aplicarDesconto(double valorCompra) {
        valorCompra -= (valorCompra * getPercentualDesconto());
        return valorCompra;
    }
}
