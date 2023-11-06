package br.edu.ufcg.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.p2lp2.hotelcalifornia.refeicao.Refeicao;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RefeicaoController {
    private static RefeicaoController instance;
    private HashMap<Long, Refeicao> refeicoes;
    private UsuarioController userController;
    private Locale locale;

    private RefeicaoController() {
        this.refeicoes = new HashMap<>();
        this.userController = UsuarioController.getInstance();
        this.locale = new Locale("pt", "BR");
    }

    public static RefeicaoController getInstance() {
        if (instance == null) {
            instance = new RefeicaoController();
        }
        return instance;
    }

    public void init() {
        this.refeicoes.clear();
    }

    public String disponibilizarRefeicao(String idAutenticacao, String tipoRefeicao, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valor, boolean disponivel){
        if (horarioFinal.isBefore(horarioInicio)) { throw new HotelCaliforniaException("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"); }
        verificarSeRefeicaoExiste(titulo);
        validarCadastro(idAutenticacao);

        long idRefeicao = this.refeicoes.size() + 1L;
        this.refeicoes.put(idRefeicao, new Refeicao(idRefeicao, tipoRefeicao, titulo, horarioInicio, horarioFinal, valor, disponivel));
        return this.refeicoes.get(idRefeicao).toString();
    }

    private void validarCadastro(String idAutenticacao) {
        userController.validarCadastroRefeicao(idAutenticacao);
    }

    private void verificarSeRefeicaoExiste(String titulo) {
        for (Refeicao r : this.refeicoes.values()) {
            if (r.getTitulo().equals(titulo)) {
                throw new HotelCaliforniaException("REFEICAO JA EXISTE");
            }
        }
    }


    public String alterarRefeicao(long idRefeicao, LocalTime horarioInicio, LocalTime horarioFinal, double valorPorPessoa, boolean disponivel) {
        if (this.refeicoes.get(idRefeicao) == null) { throw new HotelCaliforniaException("REFEICAO NAO EXISTE"); }
        if (horarioFinal.isBefore(horarioInicio)) { throw new HotelCaliforniaException("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO"); }
        this.refeicoes.get(idRefeicao).setHorarioInicio(horarioInicio);
        this.refeicoes.get(idRefeicao).setHorarioFim(horarioFinal);
        this.refeicoes.get(idRefeicao).setValor(valorPorPessoa);
        this.refeicoes.get(idRefeicao).setDisponivel(disponivel);
        return this.refeicoes.get(idRefeicao).toString();
    }


    public String exibirRefeicao(long idRefeicao) {
        return this.refeicoes.get(idRefeicao).toString();
    }

    public String[] listarRefeicoes() {
        int cont = 0;
        String[] refs = new String[this.refeicoes.size()];
        for (Refeicao r : this.refeicoes.values()) {
            refs[cont++] = r.toString();
        }
        return refs;
    }

    public String[] retornarRefeicoesParaReserva(long[] idRefeicoes){
        ArrayList<String> refeicoesStr= new ArrayList<String>();
        for(Refeicao r: this.refeicoes.values()){
            for(Long refStr: idRefeicoes){
                if(r.getId() == refStr){
                    refeicoesStr.add(r.toString());
                }
            }
        }
        return refeicoesStr.toArray(new String[0]);
        }


    public double retornarSri(long[] idRefeicoes){
        double valorTotal = 0;
        for(Refeicao r: this.refeicoes.values()){
            for(Long refStr: idRefeicoes){
                if(r.getId() == refStr){
                    valorTotal += r.getValor();
                }
            }
        }
        return valorTotal;
    }

    public LocalTime buscarInicioRefeicao(long idRefeicao) {
        if(this.refeicoes.containsKey(idRefeicao)) {
            return this.refeicoes.get(idRefeicao).getHorarioInicio();
        }
        throw new HotelCaliforniaException("Tipo de refeicao inexistente");
    }
    public LocalTime buscarFimRefeicao(long idRefeicao) {
        if(this.refeicoes.containsKey(idRefeicao)) {
            return this.refeicoes.get(idRefeicao).getHorarioFim();
        }
        throw new HotelCaliforniaException("Tipo de refeicao inexistente");
    }

    public Refeicao buscarRefeicao(long idRefeicao) {
        if(this.refeicoes.containsKey(idRefeicao)) {
            return this.refeicoes.get(idRefeicao);
        }
        throw new HotelCaliforniaException("Tipo de refeicao inexistente");
    }

}

