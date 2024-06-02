package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, ConfirmPasswordInterface> {

    @Override
    public boolean isValid(ConfirmPasswordInterface request, ConstraintValidatorContext context) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}