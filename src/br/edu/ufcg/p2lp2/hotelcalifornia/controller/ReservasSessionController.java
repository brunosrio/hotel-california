package br.edu.ufcg.p2lp2.hotelcalifornia.controller;

import br.edu.ufcg.p2lp2.hotelcalifornia.areacomum.Auditorio;
import br.edu.ufcg.p2lp2.hotelcalifornia.exception.HotelCaliforniaException;
import br.edu.ufcg.p2lp2.hotelcalifornia.refeicao.Refeicao;
import br.edu.ufcg.p2lp2.hotelcalifornia.reserva.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class ReservasSessionController {

    private static ReservasSessionController uniqueInstance;
    private UsuarioController usuarioController;
    private QuartosController quartosController;
    private AreaComumController areaComumController;
    private RefeicaoController refeicaoController;
    private Map<Long, Reserva> reservas;
    private long nextId;

    private ReservasSessionController() {
        usuarioController = UsuarioController.getInstance();
        quartosController = QuartosController.getInstance();
        refeicaoController = RefeicaoController.getInstance();
        areaComumController = AreaComumController.getInstance();
        this.reservas = new HashMap<>();
        this.nextId = 1;
    }

    public static ReservasSessionController getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ReservasSessionController();
        }
        return uniqueInstance;
    }

    public void init() {
        this.reservas.clear();
        nextId = 1;
    }

    public String reservarQuartoSingle(String idAutenticacao, String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes) {
        usuarioController.validarReservaQuarto(idAutenticacao);
        quartosController.verificarDisponibilidadeDoQuartoSingle(numQuarto, dataInicio, dataFim);
        verificarDiaria(dataInicio, dataFim);
        long idReserva = nextId++;
        long[] idRefeicoesLong = Arrays.stream(idRefeicoes).mapToLong(Long::parseLong).toArray();
        ReservaQuarto r = new ReservaQuartoSingle(idReserva, idCliente, numQuarto, dataInicio, dataFim, idRefeicoesLong);
        reservas.put(idReserva, r);
        return r.toString();
    }

    public String reservarQuartoDouble(String idAutenticacao, String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos) {
        usuarioController.validarReservaQuarto(idAutenticacao);
        quartosController.verificarDisponibilidadeDoQuartoDouble(numQuarto, dataInicio, dataFim);
        verificarDiaria(dataInicio, dataFim);
        long idReserva = nextId++;
        long[] idRefeicoesLong = Arrays.stream(idRefeicoes).mapToLong(Long::parseLong).toArray();
        ReservaQuarto r = new ReservaQuartoDouble(idReserva, idCliente, numQuarto, dataInicio, dataFim, idRefeicoesLong, pedidos);
        this.reservas.put(idReserva, r);
        return r.toString();
    }

    public String reservarQuartoFamily(String idAutenticacao, String idCliente, int numQuarto, LocalDateTime dataInicio, LocalDateTime dataFim, String[] idRefeicoes, String[] pedidos, int numPessoas) {
        usuarioController.validarReservaQuarto(idAutenticacao);
        quartosController.verificarDisponibilidadeDoQuartoFamily(numQuarto, dataInicio, dataFim);
        verificarDiaria(dataInicio, dataFim);
        if (numPessoas > quartosController.getQuarto(numQuarto).getVagas()) {
            throw new HotelCaliforniaException("CAPACIDADE EXCEDIDA");
        }
        long idReserva = nextId++;
        long[] idRefeicoesLong = Arrays.stream(idRefeicoes).mapToLong(Long::parseLong).toArray();
        ReservaQuarto r = new ReservaQuartoFamily(idReserva, idCliente, numQuarto, dataInicio, dataFim, idRefeicoesLong, pedidos, numPessoas);
        this.reservas.put(idReserva, r);
        return r.toString();
    }

    private void verificarDiaria(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio.isAfter(dataFim)) {
            throw new HotelCaliforniaException("Data de início após data do fim");
        }
        LocalTime horaCheckin = LocalTime.of(13, 59, 59);
        LocalTime horaCheckout = LocalTime.of(12, 0, 1);
        verificarAntecedencia(dataInicio);
    }


    public String buscarIdClienteReserva(long idReserva) {
        return this.reservas.get(idReserva).getCliente();
    }

    //Quartos--------------------------------------------------------------------------------------


    // reserva restaurante -------------------------------------------------------------------- //
    public String reservarRestaurante(String idAutenticacao, String idCliente, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdePessoas, String idRefeicao) {
        usuarioController.validarReservaRestaurante(idAutenticacao);

        LocalDateTime novaDataIncio = ajustaDataInicioComRefeicao(dataInicio, Long.parseLong(idRefeicao));
        LocalDateTime novaDataFim = ajustaDataFimComRefeicao(dataFim, Long.parseLong(idRefeicao));
        validadarDatas(novaDataIncio, novaDataFim);
        verificarAntecedencia(dataInicio);
        verificarCapacidadeRestaurante(qtdePessoas);

        Refeicao refeicao = refeicaoController.buscarRefeicao(Long.parseLong(idRefeicao));

        //adicionarReservaRestauranteAoCliente(idReserva,idCliente);
        ReservaRestaurante reserva = new ReservaRestaurante(nextId, idCliente, novaDataIncio, novaDataFim, qtdePessoas, refeicao);
        this.reservas.put(nextId++, reserva);
        return reserva.toString();
    }

    private LocalDateTime ajustaDataFimComRefeicao(LocalDateTime dataFim, long idRefeicao) {
        LocalTime inicioRefeico = refeicaoController.buscarFimRefeicao(idRefeicao);
        return LocalDateTime.of(dataFim.toLocalDate(), inicioRefeico);
    }

    private LocalDateTime ajustaDataInicioComRefeicao(LocalDateTime dataInicio, long idRefeicao) {
        LocalTime inicioRefeico = refeicaoController.buscarInicioRefeicao(idRefeicao);
        return LocalDateTime.of(dataInicio.toLocalDate(), inicioRefeico);
    }

    private void validadarDatas(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio.isAfter(dataFim)) {
            throw new HotelCaliforniaException("Data inicio deve ser anterior a de fim");
        }
        verificarConflitoDatasReservadasRestaurante(dataInicio, dataFim);
        verificarAntecedencia(dataInicio);
    }

    private void verificarAntecedencia(LocalDateTime dataInicio) {
        if (LocalDateTime.now().until(dataInicio, DAYS) < 1) {
            throw new HotelCaliforniaException("NECESSARIO ANTECEDENCIA MINIMA DE 01 (UM) DIA");
        }
    }


    private void verificarConflitoDatasReservadasRestaurante(LocalDateTime dataInicio, LocalDateTime dataFim) {
        for (Reserva r : this.reservas.values()) {
            if (r instanceof ReservaRestaurante && !r.verificarConflitoDatas(dataInicio, dataFim)) {
                throw new HotelCaliforniaException("JA EXISTE RESERVA PARA ESTA DATA");
            }
        }
    }

    private void verificarCapacidadeRestaurante(int qtdePessoas) {
        if (qtdePessoas > 50) {
            throw new HotelCaliforniaException("CAPACIDADE EXCEDIDA");
        }
    }


    public double buscaValorReserva(long idReserva) {
        if (!this.reservas.containsKey(idReserva)) {
            throw new HotelCaliforniaException("ID invalido");
        }
        return this.reservas.get(idReserva).getValor();
    }

    public String pagarReserva(long idReserva, String status) {
        return this.reservas.get(idReserva).pagarReserva(status);
    }

// reserva restaurante -------------------------------------------------------------------- //

    //US9 cancelar reserva ================================================================
    public String cancelarReserva(String idCliente, String idReserva) {
        validarCliente(idCliente, idReserva);
        verificarAntecedencia(this.reservas.get(Long.parseLong(idReserva)).getDataInicio());
        return this.reservas.get(Long.parseLong(idReserva)).cancelarReserva();
    }

    private void validarCliente(String idCliente, String idReserva) {
        String idClienteReserva = this.reservas.get(Long.parseLong(idReserva)).getCliente();
        if (!idCliente.equals(idClienteReserva)) {
            throw new HotelCaliforniaException("SOMENTE O PROPRIO CLIENTE PODERA CANCELAR A SUA RESERVA");
        }
    }
//    ============================================================================== //

    // US11 reservar auditório
    public String reservarAuditorio(String idAutenticacao, String idCliente, long idAuditorio, LocalDateTime dataInicio, LocalDateTime dataFim, int qtdMaxPessoas) {
        usuarioController.validarReservaAreaComum(idAutenticacao);
        verificarConflitoDatasReservadasAuditorio(dataInicio, dataFim,idAuditorio);
        verificarAntecedencia(dataInicio);
        if (qtdMaxPessoas < areaComumController.getAreaComum(idAuditorio).getQtdMaxPessoas()) {
            if (areaComumController.getAreaComum(idAuditorio).isDisponivel()) {
                long idReserva = nextId++;
                ReservaAreaComum r = new ReservaAreaComum(idAuditorio, idReserva, idCliente, dataInicio, dataFim, qtdMaxPessoas, "PENDENTE");
                if (reservas.containsValue(r)) {
                    throw new HotelCaliforniaException("RESERVA JA EXISTE");
                } else {
                    reservas.put(idReserva, r);
                    return r.toString();
                }
            } else {
                throw new HotelCaliforniaException("AUDITORIO NAO ESTÁ DISPONIVEL");
            }
        } else {
            throw new HotelCaliforniaException("CAPACIDADE EXCEDIDA");
        }
    }

    private void verificarConflitoDatasReservadasAuditorio(LocalDateTime novaDataInicio, LocalDateTime novaDataFim,long idAuditorio) {
        novaDataInicio = formatarDataInicio(novaDataInicio,idAuditorio);
        novaDataFim = formatarDataFinal(novaDataFim,idAuditorio);
        for (Reserva r : this.reservas.values()) {
            if (r instanceof ReservaAreaComum) {
                if(!(novaDataFim.isBefore(r.getDataInicio()) || novaDataInicio.isAfter(r.getDataFim()))) {
                    throw new HotelCaliforniaException("JA EXISTE RESERVA PARA ESTA DATA");
                }
            }
        }
    }
    private LocalDateTime formatarDataInicio(LocalDateTime dataInicio,long idAuditorio){
        LocalTime horaInicioAuditorio = areaComumController.getAreaComum(idAuditorio).getHorarioInicio();
        return LocalDateTime.of(dataInicio.toLocalDate(), horaInicioAuditorio);
    }

    private LocalDateTime formatarDataFinal(LocalDateTime dataFim,long idAuditorio){
        LocalTime horaFimAuditorio = areaComumController.getAreaComum(idAuditorio).getHorarioFinal();
        return LocalDateTime.of(dataFim.toLocalDate(), horaFimAuditorio);
    }

    // US6 listar Reservas

    public String exibirReserva(String idAutenticacao, long idReserva) {
        usuarioController.validarVizualizarReserva(idAutenticacao);
        if (!reservas.containsKey(idReserva)) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservas.get(idReserva).toString();
    }

    public String[] listarReservasAtivasDoCliente(String idAutenticacao, String idCliente) {
        usuarioController.validarVizualizarReserva(idAutenticacao);
        ArrayList<String> reservasStr = new ArrayList<>();
        for (Reserva r : reservas.values()) {
            if (r.getCliente().equals(idCliente) && r.isStatus()) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    public String[] listarReservasAtivasDoClientePorTipo(String idAutenticacao, String idCliente, String tipo) {
        usuarioController.validarVizualizarReserva(idAutenticacao);
        switch (tipo) {
            case "QUARTO":
                return listarReservasClienteQuarto(idCliente);
            case "RESTAURANTE":
                return listarReservasClienteRestaurante(idCliente);
            case "AUDITORIO":
                return listarReservasClienteAuditorio(idCliente);
            default:
                throw new HotelCaliforniaException("TIPO INVÁLIDO");
        }
    }

    private String[] listarReservasClienteQuarto(String idCliente) {
        ArrayList<String> reservasStr = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r.getCliente().equals(idCliente) && r instanceof ReservaQuarto) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    private String[] listarReservasClienteRestaurante(String idCliente) {
        ArrayList<String> reservasStr = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r.getCliente().equals(idCliente) && r instanceof ReservaRestaurante) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    private String[] listarReservasClienteAuditorio(String idCliente) {
        ArrayList<String> reservasStr = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r.getCliente().equals(idCliente) && r instanceof ReservaAreaComum) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    public String[] listarReservasAtivasPorTipo(String idAutenticacao, String tipo) {
        usuarioController.validarVizualizarReserva(idAutenticacao);
        switch (tipo) {
            case "QUARTO":
                return listarReservasQuarto();
            case "RESTAURANTE":
                return listarReservasRestaurante();
            case "AUDITORIO":
                return listarReservasAuditorio();
            default:
                throw new HotelCaliforniaException("TIPO INVÁLIDO");
        }
    }

    private String[] listarReservasQuarto() {
        ArrayList<String> reservasStr = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r instanceof ReservaQuarto) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    private String[] listarReservasRestaurante() {
        ArrayList<String> reservasStr = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r instanceof ReservaRestaurante) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    private String[] listarReservasAuditorio() {
        ArrayList<String> reservasStr = new ArrayList<>();

        for (Reserva r : reservas.values()) {
            if (r instanceof ReservaAreaComum) {
                reservasStr.add(r.toString());
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    public String[] listarReservasAtivas(String idAutenticacao) {
        usuarioController.validarVizualizarReserva(idAutenticacao);
        ArrayList<String> reservasStr = new ArrayList<>();
        for (Reserva r : reservas.values()) {
            if (r.isStatus()) {
                if (LocalDateTime.now().isBefore(r.getDataFim())) {
                    reservasStr.add(r.toString());
                }
            }
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }

    public String[] listarReservasTodas(String idAutenticacao) {
        usuarioController.validarVizualizarReserva(idAutenticacao);
        ArrayList<String> reservasStr = new ArrayList<>();
        for (Reserva r : reservas.values()) {
                reservasStr.add(r.toString());
        }
        if (reservasStr.size() == 0) {
            throw new HotelCaliforniaException("RESERVA NAO ENCONTRADA");
        }
        return reservasStr.toArray(new String[0]);
    }
}

