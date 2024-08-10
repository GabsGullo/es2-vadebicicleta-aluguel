package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CiclistaInPutDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String nascimento;
    @CPF
    private String cpf;
    private Ciclista.Passaporte passaporte;
    @NotBlank
    private String nacionalidade;
    @Email
    private String email;
    private String urlFotoDocumento;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setPassaporte(Ciclista.Passaporte passaporte) {
        this.passaporte = passaporte;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUrlFotoDocumento(String urlFotoDocumento) {
        this.urlFotoDocumento = urlFotoDocumento;
    }
}
