package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.FuncaoEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
public class FuncionarioService {
    public static final String OBJECT_NAME = "funcionario";
    private final FuncionarioRepository repository;

    @Autowired
    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> getAllFuncionarios(){
        return repository.getAllFuncionarios();
    }

    public Funcionario getById(Integer idFuncionario){
        return repository.findById(idFuncionario).orElseThrow(
                () -> new NotFoundException("Funcionario não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Funcionario save(Funcionario funcionario){
        validateFuncionario(funcionario);

        return repository.save(funcionario);
    }

    public Funcionario update(Funcionario funcionarioNovo, Integer idFuncionario){
        Funcionario funcionarioCadastrado = getById(idFuncionario);

        alterarDados(funcionarioNovo, funcionarioCadastrado);

        validateFuncionario(funcionarioCadastrado);

        return repository.save(funcionarioCadastrado);
    }

    private static void alterarDados(Funcionario funcionarioNovo, Funcionario funcionarioCadastrado) {
        funcionarioCadastrado.setSenha(funcionarioNovo.getSenha());
        funcionarioCadastrado.setConfirmacaoSenha(funcionarioNovo.getConfirmacaoSenha());
        funcionarioCadastrado.setEmail(funcionarioNovo.getEmail());
        funcionarioCadastrado.setNome(funcionarioNovo.getNome());
        funcionarioCadastrado.setIdade(funcionarioNovo.getIdade());
        funcionarioCadastrado.setFuncao(funcionarioNovo.getFuncao());
        funcionarioCadastrado.setCpf(funcionarioNovo.getCpf());
    }

    public Object delete(Integer idFuncionario){
        if(idFuncionario < 0)
            throw new UnprocessableEntityException("Chave invalida", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        Object retorno = repository.delete(idFuncionario);
        if(retorno == null){
            throw new NotFoundException("Funcionário não encontrado", HttpStatus.NOT_FOUND.toString());
        }

        return retorno;
    }

    private void validateFuncionario(Funcionario funcionario) {
        BindingResult result = new BeanPropertyBindingResult(funcionario, OBJECT_NAME);

        validateNome(funcionario, result);
        validateIdade(funcionario, result);
        validateSenha(funcionario, result);
        validateEmail(funcionario, result);
        validateConfirmacaoSenha(funcionario, result);
        validateCpf(funcionario, result);
        validateFuncao(funcionario,result);

        if (result.hasErrors()) {
            throw new ValidacaoException(result);
        }
    }

    private void validateNome(Funcionario funcionario, BindingResult result) {
        if (funcionario.getNome() == null || funcionario.getNome().isEmpty()) {
            result.addError(new FieldError(OBJECT_NAME, "nome", "Nome não pode ser nulo ou vazio"));
        }
    }

    private void validateFuncao(Funcionario funcionario, BindingResult result) {
        if (funcionario.getFuncao() == null || (!funcionario.getFuncao().equals(FuncaoEnum.ADMINISTRATIVO) && !funcionario.getFuncao().equals(FuncaoEnum.REPARADOR))) {
            result.addError(new FieldError(OBJECT_NAME, "funcao", "Funcao não pode ser nulo e precisa ser adiministrativo ou reparador"));
        }
    }

    private void validateEmail(Funcionario funcionario, BindingResult result) {
        if (funcionario.getEmail() == null || !isValidEmail(funcionario.getEmail())) {
            result.addError(new FieldError(OBJECT_NAME, "email", "Email inválido"));
        }
    }

    private void validateSenha(Funcionario funcionario, BindingResult result) {
        if (funcionario.getSenha() == null || funcionario.getSenha().isEmpty()) {
            result.addError(new FieldError(OBJECT_NAME, "senha", "Senha não pode ser nula ou vazia"));
        }
    }

    private void validateConfirmacaoSenha(Funcionario funcionario, BindingResult result) {
        boolean valid = false;

        if(funcionario.getSenha() != null && funcionario.getConfirmacaoSenha() != null){
            valid = funcionario.getSenha().equals(funcionario.getConfirmacaoSenha());
        }

        if (!valid || funcionario.getConfirmacaoSenha().isEmpty()) {
            result.addError(new FieldError(OBJECT_NAME, "Confirmacao senha", "Senha e Confirmacao Senha não podem ser nulas ou vazias e precisam ser iguais"));
        }
    }

    private void validateCpf(Funcionario funcionario, BindingResult result) {
        if (funcionario.getCpf() != null && !isValidCPF(funcionario.getCpf())) {
            result.addError(new FieldError(OBJECT_NAME, "cpf", "CPF inválido"));
        }
    }

    private void validateIdade(Funcionario funcionario, BindingResult result) {
        if (funcionario.getIdade() == null) {
            result.addError(new FieldError(OBJECT_NAME, "idade", "Idade inválida"));
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
}