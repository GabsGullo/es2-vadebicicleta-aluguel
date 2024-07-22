package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternoClient {
    @Value("vadbicicleta.externo.url")
    private String url;

    @Autowired
    private RestTemplate template;

    public Email enviarEmail(Email email) {
        ResponseEntity<Email> response = template.postForEntity(url, email, Email.class);
        return response.getBody();
    }
}
