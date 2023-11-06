package br.edu.ufcg.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoDouble;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoFamily;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoSingle;
import br.edu.ufcg.p2lp2.hotelcalifornia.quarto.QuartoSuper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuartosController {

    private static QuartosController uniqueInstance;
    private UsuarioController usuarioController;
    private Map<Integer, QuartoSuper> quartos;
    private QuartosController(){
        this.quartos = new HashMap<Integer, QuartoSuper>();
        this.usuarioController = UsuarioController.getInstance();
    }

    public static QuartosController getInstance(){
        if(uniqueInstance == null){
            uniqueInstance = new QuartosController();
        }
        return uniqueInstance;
    }

    public void init() {
        this.quartos.clear();
    }

    public String disponibilizarQuartoSingle(String idAutenticacao, int idQuartoNum, double precoBase, double precoPorPessoa){
        usuarioController.validarCadastroQuarto(idAutenticacao);
        jaTemQuarto(idQuartoNum);
        verificaPreco(precoPorPessoa);
        verificaPreco(precoBase);
        QuartoSingle q = new QuartoSingle(idQuartoNum,precoBase,precoPorPessoa);
        quartos.put(idQuartoNum,q);
        return q.exibirQuarto();
    }

    public String disponibilizarQuartoDouble(String idAutenticacao, int idQuartoNum, double precoBase, double precoPorPessoa,String[] pedidos){
        usuarioController.validarCadastroQuarto(idAutenticacao);
        jaTemQuarto(idQuartoNum);
        verificaPreco(precoPorPessoa);
        verificaPreco(precoBase);
        QuartoDouble q = new QuartoDouble(idQuartoNum,precoBase,precoPorPessoa,pedidos);
        quartos.put(idQuartoNum,q);
        return  q.exibirQuarto();
    }

    public String disponibilizarQuartoFamily(String idAutenticacao, int idQuartoNum, double precoBase,double precoPorPessoa,String[] pedidos,int qtdMaxPessoas){
        usuarioController.validarCadastroQuarto(idAutenticacao);
        jaTemQuarto(idQuartoNum);
        verificaPreco(precoPorPessoa);
        verificaPreco(precoBase);
        QuartoFamily q = new QuartoFamily(idQuartoNum,precoBase,precoPorPessoa,pedidos,qtdMaxPessoas);
        quartos.put(idQuartoNum,q);
        return  q.exibirQuarto();
    }

    public String exibirQuarto(int idQuarto){
        if(quartos.containsKey(idQuarto)){
            return quartos.get(idQuarto).exibirQuarto();
        }else{
            throw new HotelCaliforniaException("Id não encontrado");
        }
    }

    public String[] listarQuartos(){
        ArrayList<String> quartosArray = new ArrayList<>();
        for(QuartoSuper q: quartos.values()){
            quartosArray.add(q.exibirQuarto());
        }
        return quartosArray.toArray(new String[0]);
    }

    public void jaTemQuarto(int idQuarto){
        if(quartos.containsKey(idQuarto)){
            throw new HotelCaliforniaException("QUARTO JA EXISTE");
        }
    }

    public void verificaPreco(double preco){
        if(preco <= 0){
            throw new HotelCaliforniaException("Preço tem que ser maior que 0");
        }
    }

    public void verificarDisponibilidadeDoQuartoSingle(int idQuarto, LocalDateTime inicioNovaReserva, LocalDateTime fimNovaReserva) {
        boolean quartoDisponivel = this.quartos.get(getQuartoSingle(idQuarto)).reservarQuarto(inicioNovaReserva, fimNovaReserva);
        if (!quartoDisponivel) {
            throw new HotelCaliforniaException("Quarto não disponível");
        }
    }

    public void verificarDisponibilidadeDoQuartoDouble(int idQuarto, LocalDateTime inicioNovaReserva, LocalDateTime fimNovaReserva) {
        boolean quartoDisponivel = this.quartos.get(getQuartoDouble(idQuarto)).reservarQuarto(inicioNovaReserva, fimNovaReserva);
        if (!quartoDisponivel) {
            throw new HotelCaliforniaException("Quarto não disponível");
        }
    }
    public void verificarDisponibilidadeDoQuartoFamily(int idQuarto, LocalDateTime inicioNovaReserva, LocalDateTime fimNovaReserva) {
        boolean quartoDisponivel = this.quartos.get(getQuartoFamily(idQuarto)).reservarQuarto(inicioNovaReserva, fimNovaReserva);
        if (!quartoDisponivel) {
            throw new HotelCaliforniaException("JA EXISTE RESERVA PARA ESTA DATA");
        }
    }

    public int getQuartoSingle(int idQuarto){
        if(getQuarto(idQuarto) instanceof QuartoSingle ){
            return (idQuarto);
        }
        throw new HotelCaliforniaException("ID inválido");
    }

    public int getQuartoDouble(int idQuarto){
        if(getQuarto(idQuarto) instanceof QuartoDouble ){
            return (idQuarto);
        }
        throw new HotelCaliforniaException("ID inválido");
    }

    public int getQuartoFamily(int idQuarto){
        if(getQuarto(idQuarto) instanceof QuartoFamily ){
            return (idQuarto);
        }
        throw new HotelCaliforniaException("ID inválido");
    }

    public QuartoSuper getQuarto(int idQuarto){
        if(quartos.containsKey(idQuarto)){
            return quartos.get(idQuarto);
        }else{
            throw new HotelCaliforniaException("Quarto não existe");
        }
    }

}
