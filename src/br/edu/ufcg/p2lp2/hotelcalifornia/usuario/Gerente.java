package br.edu.ufcg.p2lp2.hotelcalifornia.usuario;

public class Gerente extends Usuario {
    public Gerente(String id, String nome, long documento) {
        super(id, nome, documento);
    }

    @Override
    public boolean validarCadastro(String tipoUsuario) {
        if (tipoUsuario.equals("ADM") || tipoUsuario.equals("GER")) {
            return false;
        }
        return true;
    }
}
