package br.edu.ufcg.p2lp2.hotelcalifornia.usuario;

public class Administrador extends Usuario {

    public Administrador(String id, String nome, long documento) {
        super(id, nome, documento);
    }

    @Override
    public boolean validarCadastro(String tipoUsuario) {
        return true;
    }


}
