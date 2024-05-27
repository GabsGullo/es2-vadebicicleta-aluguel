package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdGenerator {
    private Integer id;

    public Integer addElement(){
       this.id = this.id + 1;
       return this.id;
    }
}
