package com.github.register.domain.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, METHOD, PARAMETER, TYPE})
@Constraint(validatedBy = AppUserValidation.AuthenticatedAppUserValidator.class)
public @interface AuthenticatedAppUser {

    String message() default "Not the currently logged-in user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
