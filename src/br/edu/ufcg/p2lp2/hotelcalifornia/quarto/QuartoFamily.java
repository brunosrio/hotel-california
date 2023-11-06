package br.edu.ufcg.p2lp2.hotelcalifornia.quarto;

import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.text.DecimalFormat;
import java.util.Arrays;

public class QuartoFamily extends QuartoSuper{
    public QuartoFamily(int idQuarto, double precoBase, double precoPorPessoa, String[] pedidos,int qtdMaxPessoas) {
        super(idQuarto, precoBase,precoPorPessoa,pedidos);
        if(qtdMaxPessoas > 10 || qtdMaxPessoas <= 0){
            throw new HotelCaliforniaException("Quantidade de pessoas inválida");
        }else{
            super.setVagas(qtdMaxPessoas);
        }
    }

    @Override
    public String exibirQuarto(){
        DecimalFormat df = new DecimalFormat("00");
        if(super.pedidos.length == 0){
            return String.format("[%d] Quarto Family (custo basico: R$%.2f; por pessoa R$%.2f >>> R$%.2f diária)",super.getIdQuarto(),super.getPrecoBase(),super.getPrecoPorPessoa(),super.valorDaDiaria()).replace(".",",") + ". Capacidade: " + df.format(super.getVagas()) + " pessoa(s). Pedidos: (nenhum)";
        }else{
            return String.format("[%d] Quarto Family (custo basico: R$%.2f; por pessoa: R$%.2f >>> " +
                    "R$%.2f diária)",super.getIdQuarto(),super.getPrecoBase(),super.getPrecoPorPessoa(),super.valorDaDiaria()).replace(".",",") +
                    ". Capacidade: " + df.format(super.getVagas()) + " pessoa(s). Pedidos: " + Arrays.toString(super.getPedidos());
        }
    }

}
