package de.hybris.training.core.customValidation;

import de.hybris.training.core.model.VehicleBaseModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegistrationValidator implements ConstraintValidator<ValidatorInterface, String> {


    @Override
    public void initialize(ValidatorInterface constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        return  value != null && !value.isEmpty() && value.startsWith("KL");
    }
}
