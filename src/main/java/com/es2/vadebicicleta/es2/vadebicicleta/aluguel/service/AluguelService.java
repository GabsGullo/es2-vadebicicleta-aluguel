package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.EquipamentoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.AluguelRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.DevolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

@Service
public class AluguelService {

    private final ExternoClient externoClient;
    private final AluguelRepository repository;
    private final CiclistaService ciclistaService;
    private final CartaoDeCreditoService cartaoDeCreditoService;
    private final DevolucaoRepository devolucaoRepository;
    private final EquipamentoClient equipamentoClient;

    @Autowired
    public AluguelService(AluguelRepository repository, CiclistaService ciclistaService, ExternoClient externoClient,
                          DevolucaoRepository devolucaoRepository, CartaoDeCreditoService cartaoDeCreditoService,
                          EquipamentoClient equipamentoClient){
        this.repository = repository;
        this.ciclistaService = ciclistaService;
        this.externoClient = externoClient;
        this.devolucaoRepository = devolucaoRepository;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
        this.equipamentoClient = equipamentoClient;
    }

    public Aluguel realizarAluguel(Integer ciclista, Integer idTranca){
        //verifica a tranca
        Tranca tranca = equipamentoClient.getTranca(idTranca);
        if(tranca == null){
            throw new NotFoundException("Tranca não existe", HttpStatus.NOT_FOUND.toString());
        }

        //verificar se bicicleta esta apta
        Integer idBicicleta = tranca.getBicicleta();
        Bicicleta bicicleta = equipamentoClient.getBicicleta(idBicicleta);
        if(bicicleta == null){
            throw new NotFoundException("Bicicleta não existe", HttpStatus.NOT_FOUND.toString());
        }

        if(!Objects.equals(bicicleta.getStatus(), "DISPONIVEL")){
            throw new UnprocessableEntityException("Bicicleta não pode ser alugada", "422");
        }

        //verifica o ciclista
        verificarAluguelCiclista(ciclista);

        //realiza cobranca
        Cobranca cobranca = externoClient.realizarCobranca(BigDecimal.TEN, ciclista);

        // Hora de início
        LocalDateTime horaInicio = LocalDateTime.now();

        Aluguel aluguel = Aluguel.builder()
                .trancaInicio(idTranca)
                .horaInicio(horaInicio)
                .horaFim(null)
                .cobranca(cobranca.getId())
                .ciclista(ciclista)
                .bicicleta(idBicicleta)
                .cartaoDeCredito(cartaoDeCreditoService.getCartaoByCiclistaId(ciclista))
                .build();

        //pedir abretura tranca
        tranca = equipamentoClient.socilitarDestrancamento(idTranca, idBicicleta);
        if(tranca == null){
            throw new UnprocessableEntityException("Dados para solicitação de trancamento", "422");
        }

        //enviar mensagem ao ciclista
        EnderecoEmail enderecoEmail = new EnderecoEmail();
        enderecoEmail.setAssunto("Aluguel Realizado com Sucesso");

        String mensagem = "Dados do Aluguel:\n" + "Cobranca: " + cobranca + "\n" +
                "Hora: " + horaInicio + "\n" +
                "Totem: " + tranca.getLocalizacao();

        enderecoEmail.setMensagem(mensagem);
        enderecoEmail.setEmail(ciclistaService.getById(ciclista).getEmail());
        externoClient.enviarEmail(enderecoEmail);

        return repository.register(aluguel);
    }

    public Aluguel realizarDevolucao(Integer idTranca, Integer idBicicleta){
        //valida bicicleta
        Bicicleta bicicleta = equipamentoClient.getBicicleta(idBicicleta);
        if(bicicleta == null){
            throw new NotFoundException("Bicicleta não existe", HttpStatus.NOT_FOUND.toString());
        }

        Tranca tranca = equipamentoClient.getTranca(idTranca);
        if(tranca == null){
            throw new NotFoundException("Tranca não existe", HttpStatus.NOT_FOUND.toString());
        }

        //se bicicleta for nova ou em reparo chamar incluir bicicleta no totem
        if(bicicleta.getStatus().equals("NOVA") || bicicleta.getStatus().equals("EM_REPARO")){
            Integer response = equipamentoClient.incluirBicicletaRede(idTranca, idBicicleta, bicicleta.getFuncionario());
            if(response == null){
                throw new UnprocessableEntityException("Erro nos dados para inclusão", "422");
            }
        }

        Aluguel aluguel = getByIdBicicleta(idBicicleta);

        //calcular cobranca extra
        LocalDateTime horaDevolucao = LocalDateTime.now();
        LocalDateTime horaInicio = aluguel.getHoraInicio();

        //realiza cobranca
        BigDecimal valor = calculaValorExtra(horaInicio, horaDevolucao);

        Cobranca cobrancaExtra;
        LocalDateTime horaCobrancaExtra = null;
        if(valor.floatValue() != 0){
            cobrancaExtra = externoClient.realizarCobranca(valor, aluguel.getCiclista());
            horaCobrancaExtra = cobrancaExtra.getHoraSolicitacao();
        }

        if(horaCobrancaExtra == null)
            horaCobrancaExtra = aluguel.getHoraCobranca();

        //atualizar aluguel
        aluguel.setHoraFim(horaDevolucao);
        aluguel.setTrancaFim(idTranca);
        repository.register(aluguel);

        //registra devolucao
        Devolucao devolucao = Devolucao.builder()
                .idAluguel(aluguel.getIdAluguel())
                .horaDevolucao(horaDevolucao)
                .horaCobranca(horaCobrancaExtra)
                .valorExtra(valor)
                .cartaoDeCredito(cartaoDeCreditoService.getCartaoByCiclistaId(aluguel.getCiclista()))
                .numeroTranca(idTranca)
                .numeroBicicleta(idBicicleta)
                .build();

        devolucaoRepository.register(devolucao);

        tranca = equipamentoClient.socilitarTrancamento(idTranca, idBicicleta);
        if(tranca == null){
            throw new UnprocessableEntityException("Dados para solicitação de trancamento", "422");
        }

        // Enviar mensagem ao ciclista
        EnderecoEmail enderecoEmail = new EnderecoEmail();
        enderecoEmail.setAssunto("Devolução Realizada com Sucesso");

        String mensagem = "Dados da Devolução:\n" +
                "Hora de Devolução: " + devolucao.getHoraDevolucao() + "\n" +
                "Hora da Cobrança: " + devolucao.getHoraCobranca() + "\n" +
                "Valor Extra: " + devolucao.getValorExtra() + "\n" +
                "Cartão de Crédito: " + devolucao.getCartaoDeCredito() + "\n" +
                "Número da Tranca: " + devolucao.getNumeroTranca() + "\n" +
                "Número da Bicicleta: " + devolucao.getNumeroBicicleta();

        enderecoEmail.setMensagem(mensagem);
        enderecoEmail.setEmail(ciclistaService.getById(aluguel.getCiclista()).getEmail());
        externoClient.enviarEmail(enderecoEmail);

        return aluguel;
    }

    private void verificarAluguelCiclista(Integer idCiclista) {

        Optional<Aluguel> aluguel = repository.findByCiclistaIdHoraFimAluguel(idCiclista, null);
        if(aluguel.isPresent()){
            Ciclista ciclista = ciclistaService.getById(idCiclista);
            Tranca tranca = equipamentoClient.getTranca(aluguel.get().getTrancaInicio());
            EnderecoEmail enderecoEmail = new EnderecoEmail();

            enderecoEmail.setAssunto("Dados do Aluguel Ativo");
            String mensagem = "Dados do Aluguel:\n" + "Cobranca: " + aluguel.get().getCobranca()+ "\n" +
                    "Hora: " + aluguel.get().getHoraInicio() + "\n" +
                    "Totem: " + tranca.getLocalizacao();

            enderecoEmail.setMensagem(mensagem);
            enderecoEmail.setEmail(ciclista.getEmail());
            externoClient.enviarEmail(enderecoEmail);
        }
    }

    private BigDecimal calculaValorExtra(LocalDateTime horaFim, LocalDateTime horaDevolucao){
        long minutosTotais = ChronoUnit.MINUTES.between(horaFim.plusHours(2), horaDevolucao);

        if (minutosTotais <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal periodosMeiaHoraExcedentes = BigDecimal.valueOf(minutosTotais / 30);

        return periodosMeiaHoraExcedentes.multiply(BigDecimal.valueOf(5));
    }

    public Aluguel getByIdBicicleta(Integer idBicicleta){
        return repository.findByBicicletaIdHoraFimAluguel(idBicicleta,null).orElseThrow(
                () -> new NotFoundException("Aluguel não encontrado", HttpStatus.NOT_FOUND.toString()));
    }
}
