package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ConfirmPassword;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ConfirmPasswordInterface;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfirmPassword
public class FuncionarioInDTO implements ConfirmPasswordInterface {
    @NotBlank
    private String senha;
    @NotBlank
    private String confirmacaoSenha;
    @Email
    private String email;
    @NotBlank
    private String nome;
    @NotNull
    private Integer idade;
    @NotBlank
    private String funcao;
    @CPF
    private String cpf;

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getConfirmPassword() {
        return confirmacaoSenha;
    }

}
