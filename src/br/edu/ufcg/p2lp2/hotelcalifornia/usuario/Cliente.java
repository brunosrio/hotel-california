package br.edu.ufcg.p2lp2.hotelcalifornia.usuario;

import java.util.ArrayList;

public class Cliente extends Usuario {
    private ArrayList<Long> idsReservasQuarto;

    public Cliente(String id, String nome, long documento) {
        super(id, nome, documento);
        idsReservasQuarto = new ArrayList<>();
    }

    public boolean validarCadastro(String tipoUsuario) {
        return false;
    }


}
