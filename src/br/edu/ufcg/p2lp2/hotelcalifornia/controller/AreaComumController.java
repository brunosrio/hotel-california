package br.edu.ufcg.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.p2lp2.hotelcalifornia.areacomum.AreaComum;
import br.edu.ufcg.p2lp2.hotelcalifornia.areacomum.Auditorio;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class AreaComumController {

    private static AreaComumController uniqueInstance;
    private UsuarioController usuarioController;
    private HashMap<Long, AreaComum> areasComuns;
    private long nextId;
    private AreaComumController(){
        this.areasComuns = new HashMap<>();
        this.usuarioController = UsuarioController.getInstance();
        this.nextId = 1;
    }

    public static AreaComumController getInstance(){
        if(uniqueInstance == null){
            uniqueInstance = new AreaComumController();
        }
        return uniqueInstance;
    }

    public void init() {
        this.areasComuns.clear();
        nextId = 1;
    }

    public String disponibilizarAreaComum(String idAutenticacao, String tipoAreaComum, String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas) {
        usuarioController.validarDisponibilizaAreaComum(idAutenticacao);
        verificarHoras(horarioInicio,horarioFinal);
        switch (tipoAreaComum){
            case "AUDITORIO":
                return disponibilizarAuditorio(nextId++, titulo, horarioInicio, horarioFinal, valorPessoa, disponivel, qtdMaxPessoas);

            default :
                return  "TIPO INVÁLIDO";
        }
    }


    private String disponibilizarAuditorio(long idAreaComum,String titulo, LocalTime horarioInicio, LocalTime horarioFinal, double valorPessoa, boolean disponivel, int qtdMaxPessoas){
        AreaComum a = new Auditorio(idAreaComum, titulo, horarioInicio, horarioFinal, valorPessoa, disponivel, qtdMaxPessoas);
        if(areasComuns.containsValue(a)){
            throw new HotelCaliforniaException("AREA COMUM JA EXISTE");
        }else{
        areasComuns.put(idAreaComum,a);
        return a.toString();
        }
    };

    public String alterarAreaComum(String idAutenticacao, long idAreaComum, LocalTime novoHorarioInicio, LocalTime novoHorarioFinal, double novoPreco, int capacidadeMax, boolean ativa){
        usuarioController.validarDisponibilizaAreaComum(idAutenticacao);
        verificarHoras(novoHorarioInicio,novoHorarioFinal);
        if(!areasComuns.containsKey(idAreaComum)){throw new HotelCaliforniaException("AREA COMUM NAO EXISTE");}
        AreaComum a = areasComuns.get(idAreaComum);
        a.alterarAreaComum(novoHorarioInicio,novoHorarioFinal,novoPreco,capacidadeMax,ativa);
        return a.toString();
    }

    public String exibirAreaComum(long idAreaComum){
        return areasComuns.get(idAreaComum).toString();
    }

    public String[] listarAreasComuns(){
        ArrayList<String> areasStr = new ArrayList<>();
        for(AreaComum a: areasComuns.values()){
            areasStr.add(a.toString());
        }
        return areasStr.toArray(new String[0]);
    }

    private void verificarHoras(LocalTime horaInicio,LocalTime horaFinal){
        if(horaInicio.isAfter(horaFinal)){
            throw new HotelCaliforniaException("HORARIO DE FIM DEVE SER POSTERIOR AO HORARIO DE INICIO");
        }
    }

    public AreaComum getAreaComum(long idAreaComum){
        if(!(areasComuns.containsKey(idAreaComum))){
            throw new HotelCaliforniaException("ID INVALIDO");
        }
        return areasComuns.get(idAreaComum);
    }


}
