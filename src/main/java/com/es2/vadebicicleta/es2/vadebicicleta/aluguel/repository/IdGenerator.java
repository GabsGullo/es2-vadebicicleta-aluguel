package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class IdGenerator {
    private Integer id = 0;

    public Integer addElement(){
       this.id = id + 1;
       return id;
    }
}
