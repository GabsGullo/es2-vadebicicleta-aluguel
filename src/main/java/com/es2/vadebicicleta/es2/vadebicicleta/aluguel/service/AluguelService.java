package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.AluguelAtivoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.AluguelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class AluguelService {

    private final AluguelRepository repository;
    private final CiclistaService ciclistaService;

    private final Random random = new Random();

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
        int cobranca = realizarCobranca();

        Aluguel.builder()
                .trancaInicio(tranca)
                .horaInicio(getLocalDateToIso())
                .cobranca(cobranca)
                .ciclista(ciclista)
                .bicicleta(bicicleta);

        //alterar status bicicleta
        alterarStatusBicicleta();

        //alterar status ciclista
        ciclistaService.alterarStatusAluguel(ciclista);

        return repository.register(Aluguel.builder().build());
    }

    private void verificarAluguelCiclista(Integer ciclista) {
        if(ciclistaService.getById(ciclista).getAluguelAtivo()){
            throw new AluguelAtivoException("Ciclista já possui um aluguel ativo");
        }
    }

    private void validateTranca(){
        //metodo vazio pois a validacao so sera feita apos a integracao
    }

    private void validateUsoBicicleta(){
        //metodo vazio pois a validacao so sera feita apos a integracao
    }

    private void alterarStatusBicicleta(){
        //metodo vazio pois a alteracao do status so sera feita apos a integracao
    }

    private int realizarCobranca(){
        return random.nextInt(100);
    }

    private int getBicicleta(){
        return random.nextInt(100);
    }

    private String getLocalDateToIso(){
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return localDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(formatter);
    }
}
