package de.hybris.training.core.customValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(
        { FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = RegistrationValidator.class)
@Documented
public @interface ValidatorInterface {


    String message() default "{de.hybris.training.core.customValidation.RegistrationValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};



}
