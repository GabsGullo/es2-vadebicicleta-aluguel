package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.AluguelAtivoException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AluguelRepository {

    private static final HashMap<Integer, Aluguel> registroAlugueis = new HashMap<>();
    private final IdGenerator id;

    public AluguelRepository(IdGenerator id) {
        this.id = id;
    }

    public Aluguel register(Aluguel aluguel){
        if(findById(aluguel.getIdAluguel()).isPresent()){
            registroAlugueis.replace(aluguel.getIdAluguel(), aluguel);
            return aluguel;
        }

        Integer idAluguel = id.generateIdAluguel();
        registroAlugueis.put(idAluguel, aluguel);
        return aluguel;
    }

    public Optional<Aluguel> findById(Integer id){
        return Optional.ofNullable(registroAlugueis.get(id));
    }

    public Optional<Aluguel> findByBicicletaIdHoraFimAluguel(Integer idBicicleta, LocalDateTime horaFim){
        List<Aluguel> list = registroAlugueis.values().stream().filter(aluguel -> Objects.equals(aluguel.getBicicleta(), idBicicleta) && Objects.equals(aluguel.getHoraFim(), horaFim)).toList();

        if(list.size() > 1){
            throw new AluguelAtivoException("A Busca retornou mais de 1 aluguel ativo");
        }

        if(list.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(list.get(0));
    }

    public Optional<Aluguel> findByCiclistaIdHoraFimAluguel(Integer idCiclista, LocalDateTime horaFim){
        List<Aluguel> list = registroAlugueis.values().stream().filter(aluguel -> Objects.equals(aluguel.getCiclista(), idCiclista) && Objects.equals(aluguel.getHoraFim(), horaFim)).toList();
        if(list.size() > 1){
            throw new AluguelAtivoException("A Busca retornou mais de 1 aluguel ativo");
        }

        if(list.isEmpty())
            return Optional.empty();

        return Optional.ofNullable(list.get(0));
    }
}
