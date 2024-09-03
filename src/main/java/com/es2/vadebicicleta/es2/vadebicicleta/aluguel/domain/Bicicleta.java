package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bicicleta {

    private Integer id = null;
    private Integer numero;
    private String status;
    private LocalDateTime dataHoraInsRet;
    private Integer funcionario = 0;
}
