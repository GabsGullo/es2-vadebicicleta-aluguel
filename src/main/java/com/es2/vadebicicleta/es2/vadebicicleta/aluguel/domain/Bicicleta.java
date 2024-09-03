package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bicicleta {

    private Integer id = null;
    private Integer numero;
    private String status;
    private LocalDateTime dataHoraInsRet;
    private Integer funcionario = 0;
}
