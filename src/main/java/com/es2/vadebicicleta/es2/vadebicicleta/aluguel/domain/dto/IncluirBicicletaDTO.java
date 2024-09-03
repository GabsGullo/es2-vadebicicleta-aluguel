package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncluirBicicletaDTO {

    Integer idTranca;
    Integer idBicicleta;
    Integer idFunciorio;
}
