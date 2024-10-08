package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;

@Component
public class Validator {
    public static final String CICLISTA = "ciclista";
    public static final String CARTAO = "cartao";
    public static final String FUNCIONARIO = "funcionario";
    public static final String PASSAPORTE = "passaporte";

    public void validateFuncionario(Funcionario funcionario) {
        BindingResult result = new BeanPropertyBindingResult(funcionario, FUNCIONARIO);

        validateNome(funcionario.getNome(), FUNCIONARIO, result);
        validateIdade(funcionario.getIdade(), result);
        validateSenha(funcionario.getSenha(), FUNCIONARIO ,result);
        validateEmail(funcionario.getEmail(), FUNCIONARIO ,result);
        validateConfirmacaoSenha(funcionario.getSenha(), funcionario.getConfirmacaoSenha() ,result);
        validateCpf(funcionario.getCpf(), FUNCIONARIO,result);
        validateFuncao(funcionario.getFuncao(),result);

        if (result.hasErrors()) {
            throw new ValidacaoException(result);
        }
    }

    public void validateCiclista(Ciclista ciclista) {
        BindingResult result = new BeanPropertyBindingResult(ciclista, CICLISTA);

        validateNome(ciclista.getNome(), CICLISTA,result);
        validateNacionalidade(ciclista.getNacionalidade(), result);
        validateEmail(ciclista.getEmail(), CICLISTA, result);
        validateSenha(ciclista.getSenha(), CICLISTA, result);
        validateNascimento(ciclista.getNascimento(), result);
        validateDocumentos(ciclista.getCpf(), ciclista.getPassaporte(), ciclista.getNacionalidade(), result);

        if (result.hasErrors()) {
            throw new ValidacaoException(result);
        }
    }

    public void validateCartaoDeCredito(CartaoDeCredito cartaoDeCredito) {
        BindingResult result = new BeanPropertyBindingResult(cartaoDeCredito, CARTAO);

        validateNomeTitular(cartaoDeCredito.getNomeTitular(), result);
        validateNumero(cartaoDeCredito.getNumero(), result);
        validateCvv(cartaoDeCredito.getCvv(), result);

        if (result.hasErrors()){
            throw new ValidacaoException(result);
        }
    }

    private void validateNascimento(LocalDate nascimento, BindingResult result) {
        if (nascimento == null) {
            result.addError(new FieldError(Validator.CICLISTA, "nascimento", "Nascimento não pode ser nulo"));
        }
    }

    private void validateNome(String nome, String objectName, BindingResult result) {
        if (nome == null || nome.isEmpty()) {
            result.addError(new FieldError(objectName, "nome", "Nome não pode ser nulo ou vazio"));
        }
    }

    private void validateFuncao(FuncaoEnum funcaoEnum, BindingResult result) {
        if (funcaoEnum == null || (!funcaoEnum.equals(FuncaoEnum.ADMINISTRATIVO) && !funcaoEnum.equals(FuncaoEnum.REPARADOR))) {
            result.addError(new FieldError(FUNCIONARIO, "funcao", "Funcao não pode ser nulo e precisa ser adiministrativo ou reparador"));
        }
    }

    private void validateEmail(String email, String objectName, BindingResult result) {
        if (!isValidEmail(email)) {
            result.addError(new FieldError(objectName, "email", "Email inválido"));
        }
    }

    private void validateSenha(String senha, String objectName, BindingResult result) {
        if (senha == null || senha.isEmpty()) {
            result.addError(new FieldError(objectName, "senha", "Senha não pode ser nula ou vazia"));
        }
    }

    private void validateConfirmacaoSenha(String senha, String confirmacaoSenha, BindingResult result) {
        boolean valid = false;

        if(senha != null && confirmacaoSenha != null){
            valid = senha.equals(confirmacaoSenha);
        }

        if (!valid || senha.isEmpty()) {
            result.addError(new FieldError(FUNCIONARIO, "Confirmacao senha", "Senha e Confirmacao Senha não podem ser nulas ou vazias e precisam ser iguais"));
        }
    }

    private void validateCpf(String cpf, String objectName, BindingResult result) {
        if (cpf != null && !isValidCPF(cpf)) {
            result.addError(new FieldError(objectName, "cpf", "CPF inválido"));
        }
    }

    private void validateIdade(Integer idade,BindingResult result) {
        if (idade == null) {
            result.addError(new FieldError(FUNCIONARIO, "idade", "Idade inválida"));
        }
    }

    private boolean isValidCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        try {
            // Cálculo do primeiro dígito verificador
            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int check1 = 11 - (sum1 % 11);
            if (check1 >= 10) {
                check1 = 0;
            }

            // Cálculo do segundo dígito verificador
            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int check2 = 11 - (sum2 % 11);
            if (check2 >= 10) {
                check2 = 0;
            }

            // Verifica se os dígitos verificadores calculados são iguais aos informados
            return (check1 == Character.getNumericValue(cpf.charAt(9)) &&
                    check2 == Character.getNumericValue(cpf.charAt(10)));

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        // Implementação fictícia, substitua pela sua lógica de validação de Email
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void validateDocumentos(String cpf, Ciclista.Passaporte passaporte, NacionalidadeEnum nacionalidadeEnum,BindingResult result) {

        if (cpf == null && passaporte == null) {
            result.addError(new FieldError(CICLISTA, "cpf", "Nenhum documento informado"));
            result.addError(new FieldError(CICLISTA, PASSAPORTE, "Nenhum documento informado"));
        } else if (cpf != null && passaporte != null) {
            result.addError(new FieldError(CICLISTA, "cpf", "CPF e passaporte informados"));
            result.addError(new FieldError(CICLISTA, PASSAPORTE, "CPF e passaporte informados"));
        } else if (cpf == null && nacionalidadeEnum == NacionalidadeEnum.BRASILEIRO){
            result.addError(new FieldError(CICLISTA, "cpf", "CPF precisa ser informado"));
        } else if (cpf != null && nacionalidadeEnum != NacionalidadeEnum.BRASILEIRO){
            result.addError(new FieldError(CICLISTA, PASSAPORTE, "Passaporte precisa ser informado"));
            result.addError(new FieldError(CICLISTA, "cpf", "CPF não deve ser informado"));
        } else {
            validateCpf(cpf, CICLISTA, result);
            validatePassaporte(passaporte, result);
        }
    }

    private void validateNacionalidade(NacionalidadeEnum nacionalidadeEnum, BindingResult result) {
        if (nacionalidadeEnum == null) {
            result.addError(new FieldError(CICLISTA, "nacionalidade", "Nacionalidade não pode ser nula"));
        }
    }

    private void validatePassaporte(Ciclista.Passaporte passaporte, BindingResult result) {
        if (passaporte != null) {
            if (passaporte.getNumero() == null || passaporte.getNumero().isEmpty()) {
                result.addError(new FieldError(CICLISTA, "passaporte.numero", "Número do passaporte não pode ser nulo ou vazio"));
            }
            if (passaporte.getValidade() == null) {
                result.addError(new FieldError(CICLISTA, "passaporte.validade", "Validade do passaporte não pode ser nula ou vazia"));
            }
            if (passaporte.getPais() == null || passaporte.getPais().isEmpty()) {
                result.addError(new FieldError(CICLISTA, "passaporte.pais", "País do passaporte não pode ser nulo ou vazio"));
            }
        }
    }

    private void validateNomeTitular(String nomeTitular, BindingResult result) {
        if (nomeTitular == null || nomeTitular.trim().isEmpty()) {
            result.addError(new FieldError(CARTAO, "nomeTitular", "O nome do titular não pode ser nulo ou vazio"));
        }
    }

    private void validateNumero(String numero, BindingResult result) {
        if (numero == null || numero.trim().isEmpty()) {
            result.addError(new FieldError(CARTAO, "numero", "O número do cartão de crédito não pode ser nulo ou vazio"));
        }
        assert numero != null;
        if (!numero.matches("\\d{16}")) {
            result.addError(new FieldError(CARTAO, "numero", "O número do cartão de crédito deve conter exatamente 16 dígitos"));
        }
    }

    private void validateCvv(String cvv, BindingResult result) {
        if (cvv == null || cvv.trim().isEmpty()) {
            result.addError(new FieldError(CARTAO, "cvv", "O CVV não pode ser nulo ou vazio"));
        }
        assert cvv != null;
        if (!cvv.matches("\\d{3}")) {
            result.addError(new FieldError(CARTAO, "cvv", "O CVV deve conter exatamente 3 dígitos"));
        }
    }
}
