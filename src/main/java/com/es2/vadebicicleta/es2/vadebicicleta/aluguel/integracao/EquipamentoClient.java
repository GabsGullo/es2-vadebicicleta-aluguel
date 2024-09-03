package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao;


import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Bicicleta;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Tranca;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.IncluirBicicletaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EquipamentoClient {
    @Value("${vadbicicleta.externo.url}")
    private String url;
    private final String trancaUrl = "/tranca/";
    private final RestTemplate template;

    @Autowired
    public EquipamentoClient(RestTemplate template){
        this.template = template;
    }

    public Tranca getTranca(Integer idTranca){
        ResponseEntity<Tranca> response = template.getForEntity(url+trancaUrl+idTranca, Tranca.class);
        if(response.getStatusCode().equals(HttpStatusCode.valueOf(404)))
            return null;

        return response.getBody();
    }

    public Bicicleta getBicicleta(Integer idBicicleta){
        ResponseEntity<Bicicleta> response = template.getForEntity(url+trancaUrl+idBicicleta, Bicicleta.class);
        if(response.getStatusCode().equals(HttpStatusCode.valueOf(404)))
            return null;

        return response.getBody();
    }

    public Tranca socilitarDestrancamento(Integer idTranca, Integer idBicicleta){
        ResponseEntity<Tranca> response = template.postForEntity(url+trancaUrl+idTranca+"/destrancar", idBicicleta, Tranca.class);
        if(response.getStatusCode().equals(HttpStatusCode.valueOf(404)) || response.getStatusCode().equals(HttpStatusCode.valueOf(422)))
            return null;

        return response.getBody();
    }

    public Integer incluirBicicletaRede(Integer idTranca, Integer idBicicleta, Integer idFuncionario){
        IncluirBicicletaDTO requestBody = new IncluirBicicletaDTO(idTranca,idBicicleta,idFuncionario);
        ResponseEntity<Void> response = template.postForEntity(url+"/bicicleta/integrarNaRede", requestBody, Void.class);
        if(response.getStatusCode().equals(HttpStatusCode.valueOf(422))){
            return null;
        }

        return idBicicleta;
    }

    public Tranca socilitarTrancamento(Integer idTranca, Integer idBicicleta){
        ResponseEntity<Tranca> response = template.postForEntity(url+"/tranca/"+idTranca+"/trancar", idBicicleta, Tranca.class);
        if(response.getStatusCode().equals(HttpStatusCode.valueOf(404)) || response.getStatusCode().equals(HttpStatusCode.valueOf(422)))
            return null;

        return response.getBody();
    }
}
