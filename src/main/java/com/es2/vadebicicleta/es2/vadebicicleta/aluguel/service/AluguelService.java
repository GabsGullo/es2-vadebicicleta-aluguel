package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.EnderecoEmail;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.AluguelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AluguelService {

    private final ExternoClient externoClient;
    private final AluguelRepository repository;
    private final CiclistaService ciclistaService;

    @Autowired
    public AluguelService(AluguelRepository repository, CiclistaService ciclistaService, ExternoClient externoClient){
        this.repository = repository;
        this.ciclistaService = ciclistaService;
        this.externoClient = externoClient;
    }

    public Aluguel realizarAluguel(Integer ciclista, Integer tranca){
        //verifica a tranca
        validateTranca();

        //verificar se bicicleta esta em uso
        int bicicleta = getBicicleta(1);
        validateUsoBicicleta();

        //verifica o ciclista
        verificarAluguelCiclista(ciclista);

        //realiza cobranca
        BigDecimal cobranca = realizarCobranca(BigDecimal.TEN);

        // Hora de início
        LocalDateTime horaInicio = LocalDateTime.now();

        Aluguel aluguel = Aluguel.builder()
                .trancaInicio(tranca)
                .horaInicio(horaInicio)
                .horaFim(null)
                .cobranca(cobranca)
                .ciclista(ciclista)
                .bicicleta(bicicleta)
                .build();

        //alterar status bicicleta
        alterarStatusBicicleta();

        //enviar mensagem ao ciclista
        EnderecoEmail enderecoEmail = new EnderecoEmail();
        enderecoEmail.setAssunto("Aluguel Realizado com Sucesso");

        //pegar totem
        int totem = getTotemTranca(tranca);
        String mensagem = "Dados do Aluguel:\n" + "Cobranca: " + cobranca + "\n" +
                "Hora: " + horaInicio + "\n" +
                "Totem: " + totem;

        enderecoEmail.setMensagem(mensagem);
        enderecoEmail.setEmail(ciclistaService.getById(ciclista).getEmail());
        externoClient.enviarEmail(enderecoEmail);

        return repository.register(aluguel);
    }

    public Aluguel realizarDevolucao(Integer idTranca, Integer idBicicleta){
        //valida bicicleta
        validateUsoBicicleta();

        Aluguel aluguel = getByIdBicicleta(idBicicleta);

        //calcular cobranca extra
        LocalDateTime horaDevolucao = LocalDateTime.now();
        LocalDateTime horaFim = aluguel.getHoraInicio().plusHours(2);

        //realiza cobranca
        BigDecimal valor = calculaValorExtra(horaFim, horaDevolucao);
        if(valor.floatValue() != 0){
            realizarCobranca(valor);
        }

        //atualizar aluguel
        aluguel.setHoraFim(horaDevolucao);
        aluguel.setCobranca(aluguel.getCobranca().add(valor));
        aluguel.setTrancaFim(idTranca);
        aluguel.setAluguelAtivo(false);
        repository.register(aluguel);

        alterarStatusBicicleta();
        solicitarFechamentoTranca();

        return aluguel;
    }

    private void verificarAluguelCiclista(Integer ciclista) {
       repository.findByCiclistaIdHoraFimAluguel(ciclista, null);
    }

    private int getTotemTranca(int tranca){
        //metodo vazio pois a alteracao do status so sera feita apos a integracao
        return tranca;
    }

    private void validateTranca(){
        //metodo vazio pois a validacao so sera feita apos a integracao
    }

    private void solicitarFechamentoTranca(){
        //metodo vazio pois a validacao so sera feita apos a integracao
    }

    private void validateUsoBicicleta(){
        //metodo vazio pois a validacao so sera feita apos a integracao
    }

    private void alterarStatusBicicleta(){
        //metodo vazio pois a alteracao do status so sera feita apos a integracao
    }

    private BigDecimal realizarCobranca(BigDecimal valor){
        //metodo vazio pois a cobranca so sera feita apos a integracao
        return valor;
    }

    private int getBicicleta(Integer idBicicleta){
        return idBicicleta;
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
