/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.constraints;


import de.hybris.platform.core.model.product.ProductModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Validates that the price rows of the given {@link ProductModel} are configured correctly.
 */
public class PriceRowsValidValidator implements ConstraintValidator<PriceRowsValid, Object>
{

	private static final Logger LOG = Logger.getLogger(PriceRowsValidValidator.class.getName());

	@Override
	public void initialize(final PriceRowsValid constraintAnnotation)
	{
		if (StringUtils.isEmpty(constraintAnnotation.priceRowType()))
		{
			throw new IllegalArgumentException("parameter 'priceRowType' must not be empty");
		}
	}


	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context)
	{
		boolean valid = false;

		if (value instanceof Boolean)
		{
			valid = (Boolean) value;
		}
		else
		{
			LOG.error("Provided object is not an instance of Boolean: " + value.getClass());
		}

		return valid;
	}

}
