package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CobrancaConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Cobranca;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.EnderecoEmail;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CobrancaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Component
public class ExternoClient {
    @Value("${vadbicicleta.externo.url}")
    private String url;

    private final CobrancaConverter converter;
    private final RestTemplate template;

    @Autowired
    public ExternoClient(RestTemplate template, CobrancaConverter converter) {
        this.template = template;
        this.converter = converter;
    }

    public void enviarEmail(EnderecoEmail email) {
        template.postForEntity(url+"/enviarEmail", email, Void.class);
    }

    public Cobranca realizarCobranca(BigDecimal valor, Integer ciclista){
        Map<String, Object> requestBody = Map.of("valor", valor, "ciclista", ciclista);
        ResponseEntity<CobrancaDTO> response = template.postForEntity(url+"/cobranca", requestBody, CobrancaDTO.class);
        return converter.cobrancaDTOtoEntity(Objects.requireNonNull(response.getBody()));
    }
}
