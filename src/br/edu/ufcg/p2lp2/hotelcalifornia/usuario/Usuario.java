package br.edu.ufcg.p2lp2.hotelcalifornia.usuario;

public abstract class Usuario {
    private String id;
    private String nome;
    private long documento;
    public Usuario(String id, String nome, long documento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public long getDocumento() {
        return documento;
    }


    public abstract boolean validarCadastro(String tipoUsuario);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.id).append("] ").append(this.nome);
        sb.append(" (No. Doc. ").append(this.documento).append(")");
        return sb.toString();
    }
}
