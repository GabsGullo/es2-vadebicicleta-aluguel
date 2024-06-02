package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {
    String message() default "As senhas sao incompativeis";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
