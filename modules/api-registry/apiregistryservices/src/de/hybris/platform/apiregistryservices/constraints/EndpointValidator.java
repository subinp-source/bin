/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.constraints;

import de.hybris.platform.apiregistryservices.model.EndpointModel;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Validates if one of fields: the SpecUrl and the SpecData, is not empty of the given instance of
 * {@link de.hybris.platform.apiregistryservices.model.EndpointModel}.
 */
public class EndpointValidator implements ConstraintValidator<EndpointValid, EndpointModel>
{
    @Override
    public void initialize(final EndpointValid endpoint)
    {
        //empty
    }

    @Override
    public boolean isValid(final EndpointModel endpoint, final ConstraintValidatorContext validatorContext)
    {
        return StringUtils.isNotBlank(endpoint.getSpecUrl()) != StringUtils.isNotBlank(endpoint.getSpecData());
    }
}
