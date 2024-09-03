package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tranca {

    private Integer id;
    private Integer bicicleta;
    private Integer numero;
    private String localizacao;
    private String status;
}
