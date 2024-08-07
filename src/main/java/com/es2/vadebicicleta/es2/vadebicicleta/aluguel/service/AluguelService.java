package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
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

    @Autowired
    public AluguelService(AluguelRepository repository, CiclistaService ciclistaService){
        this.repository = repository;
        this.ciclistaService = ciclistaService;
    }

    public Aluguel realizarAluguel(int ciclista, int tranca){
        //validar tranca
        validateTranca(tranca);
        int bicicleta = getBicicleta(tranca);



        //realiza cobranca
        int cobranca = realizarCobranca(ciclista);

        Aluguel.builder()
                .trancaInicio(tranca)
                .horaInicio(getLocalDateToIso())
                .cobranca(cobranca)
                .ciclista(ciclista)
                .bicicleta(bicicleta);

        return repository.register(Aluguel.builder().build());
    }

    private void validateTranca(int tranca){
    }

    private int realizarCobranca(int ciclista){
        Random random = new Random();
        return random.nextInt(100);
    }

    private int getBicicleta(int tranca){
        Random random = new Random();
        return random.nextInt(100);
    }

    private String getLocalDateToIso(){
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return localDate.atStartOfDay().atOffset(ZoneOffset.UTC).format(formatter);
    }
}
