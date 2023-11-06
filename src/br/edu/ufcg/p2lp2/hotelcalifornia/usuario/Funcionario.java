package br.edu.ufcg.p2lp2.hotelcalifornia.usuario;

public class Funcionario extends Usuario {
    public Funcionario(String id, String nome, long documento) {
        super(id, nome, documento);
    }

    @Override
    public boolean validarCadastro(String tipoUsuario) {
        if (!tipoUsuario.equals("CLI")) {
            return false;
        }
        return true;
    }
}
