package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.AluguelAtivoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.AluguelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class AluguelService {

    private final AluguelRepository repository;
    private final CiclistaService ciclistaService;

    @Autowired
    public AluguelService(AluguelRepository repository, CiclistaService ciclistaService){
        this.repository = repository;
        this.ciclistaService = ciclistaService;
    }

    public Aluguel realizarAluguel(Integer ciclista, Integer tranca){
        //verifica a tranca
        validateTranca();

        //verificar se bicicleta esta em uso
        int bicicleta = getBicicleta();
        validateUsoBicicleta();

        //verifica o ciclista
        verificarAluguelCiclista(ciclista);

        //realiza cobranca
        BigDecimal cobranca = realizarCobranca(BigDecimal.TEN);

        // Hora de início
        LocalDateTime horaInicio = LocalDateTime.now();

        // Hora de fim (2 horas após o início)
        LocalDateTime horaFim = horaInicio.plusHours(2);

        Aluguel aluguel = Aluguel.builder()
                .trancaInicio(tranca)
                .horaInicio(getLocalDateToIso(horaInicio))
                .horaFim(getLocalDateToIso(horaFim))
                .cobranca(cobranca)
                .ciclista(ciclista)
                .bicicleta(bicicleta)
                .build();

        //alterar status bicicleta
        alterarStatusBicicleta();

        //alterar status ciclista
        ciclistaService.alterarStatusAluguel(ciclista);

        return repository.register(aluguel);
    }

    public Aluguel realizarDevolucao(Integer idTranca, Integer idBicicleta){
        //valida bicicleta
        validateUsoBicicleta();

        Aluguel aluguel = getByIdBicicleta(idBicicleta);

        //calcular cobranca extra
        LocalDateTime horaDevolucao = LocalDateTime.now();
        String horaFim = aluguel.getHoraFim();

        //realiza cobranca
        BigDecimal valor = calculaValorExtra(horaFim, horaDevolucao);
        if(valor.floatValue() != 0){
            realizarCobranca(valor);
        }

        //atualizar aluguel
        aluguel.setHoraFim(getLocalDateToIso(horaDevolucao));
        aluguel.setCobranca(aluguel.getCobranca().add(valor));
        aluguel.setTrancaFim(idTranca);
        aluguel.setAluguelAtivo(false);
        repository.register(aluguel);

        ciclistaService.alterarStatusAluguel(aluguel.getCiclista());
        alterarStatusBicicleta();
        solicitarFechamentoTranca();

        return aluguel;
    }

    private void verificarAluguelCiclista(Integer ciclista) {
        boolean aptoAluguel = ciclistaService.getById(ciclista).getAluguelAtivo();
        if(aptoAluguel){
            throw new AluguelAtivoException("Ciclista já possui um aluguel ativo");
        }

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

    private int getBicicleta(){
        return 1;
    }

    public String getLocalDateToIso(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return dateTime.atOffset(ZoneOffset.UTC).format(formatter);
    }

    private BigDecimal calculaValorExtra(String horaFimIso, LocalDateTime horaDevolucao){
        LocalDateTime horaFim = parseIsoToLocalDateTime(horaFimIso);
        long minutosTotais = ChronoUnit.MINUTES.between(horaFim.plusHours(2), horaDevolucao);

        if (minutosTotais <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal periodosMeiaHoraExcedentes = BigDecimal.valueOf(minutosTotais / 30);

        return periodosMeiaHoraExcedentes.multiply(BigDecimal.valueOf(5));
    }

    private LocalDateTime parseIsoToLocalDateTime(String isoString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return OffsetDateTime.parse(isoString, formatter).toLocalDateTime();
    }

    public Aluguel getByIdBicicleta(Integer idBicicleta){
        return repository.findByBicicletaId(idBicicleta).orElseThrow(
                () -> new NotFoundException("Aluguel não encontrado", HttpStatus.NOT_FOUND.toString()));
    }
}
