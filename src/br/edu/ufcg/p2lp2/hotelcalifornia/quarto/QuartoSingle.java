package br.edu.ufcg.p2lp2.hotelcalifornia.quarto;

public class QuartoSingle extends QuartoSuper {

    public QuartoSingle(int idQuarto, double precoBase, double precoPorPessoa){
        super(idQuarto, precoBase,precoPorPessoa);
        super.setVagas(1);
    }

    @Override
    public String exibirQuarto(){
        return String.format("[%s] Quarto Single (custo basico: R$%.2f; por pessoa: R$%.2f >>> R$%.2f diária)",super.getIdQuarto(),super.getPrecoBase(),super.getPrecoPorPessoa(),super.valorDaDiaria()).replace(".",",");
    }



}
