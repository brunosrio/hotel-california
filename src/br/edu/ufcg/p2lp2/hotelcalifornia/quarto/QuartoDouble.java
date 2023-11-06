package br.edu.ufcg.p2lp2.hotelcalifornia.quarto;

import java.util.Arrays;

public class QuartoDouble extends QuartoSuper {


    public QuartoDouble(int idQuarto, double precoBase, double precoPorPessoa, String[] pedidos) {
        super(idQuarto, precoBase,precoPorPessoa,pedidos);
        super.setVagas(2);
    }

    @Override
    public String exibirQuarto(){
        if(super.pedidos.length == 0){
            return String.format("[%d] Quarto Double (custo basico: R$%.2f; por pessoa R$%.2f >>> R$%.2f diária)",super.getIdQuarto(),super.getPrecoBase(),super.getPrecoPorPessoa(),super.valorDaDiaria()).replace(".",",") + ". Pedidos: (nenhum)";
        }
        return String.format("[%d] Quarto Double (custo basico: R$%.2f; por pessoa: R$%.2f >>> " +
                    "R$%.2f diária)",super.getIdQuarto(),super.getPrecoBase(),super.getPrecoPorPessoa(),super.valorDaDiaria()).replace(".",",") +
                    ". Pedidos: " + Arrays.toString(super.getPedidos());
        }
    }

