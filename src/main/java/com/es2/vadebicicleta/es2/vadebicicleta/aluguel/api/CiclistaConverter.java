package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPostDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaOutDTO;
import org.springframework.stereotype.Component;

@Component
public class CiclistaConverter {

    public CiclistaOutDTO entityToOutDTO(Ciclista ciclista){
        CiclistaOutDTO dto = new CiclistaOutDTO();

        dto.setId(ciclista.getId());
        dto.setStatus(String.valueOf(ciclista.getStatus()));
        dto.setNome(ciclista.getNome());
        dto.setNascimento(ciclista.getNascimento());
        dto.setCpf(ciclista.getCpf());
        dto.setPassaporte(ciclista.getPassaporte());
        dto.setNacionalidade(String.valueOf(ciclista.getNacionalidade()));
        dto.setEmail(ciclista.getEmail());
        dto.setUrlFotoDocumento(ciclista.getUrlFotoDocumento());

        return dto;
    }

    public Ciclista inPutDtoToEntity(CiclistaInPutDTO dto){
        Ciclista ciclista = new Ciclista();

        ciclista.setNome(dto.getNome());
        ciclista.setNascimento(dto.getNascimento());
        ciclista.setCpf(dto.getCpf());
        ciclista.setPassaporte(dto.getPassaporte());
        ciclista.setNacionalidade(NacionalidadeEnum.valueOf(dto.getNacionalidade()));
        ciclista.setEmail(dto.getEmail());
        ciclista.setUrlFotoDocumento(dto.getUrlFotoDocumento());

        return ciclista;
    }

    public Ciclista inPostDtoToEntity(CiclistaInPostDTO dto){
        Ciclista ciclista = new Ciclista();

        ciclista.setNome(dto.getNome());
        ciclista.setNascimento(dto.getNascimento());
        ciclista.setCpf(dto.getCpf());
        ciclista.setPassaporte(dto.getPassaporte());
        ciclista.setNacionalidade(NacionalidadeEnum.valueOf(dto.getNacionalidade()));
        ciclista.setEmail(dto.getEmail());
        ciclista.setUrlFotoDocumento(dto.getUrlFotoDocumento());
        ciclista.setMeioDePagamento(dto.getMeioDePagamento());

        return ciclista;
    }
}
