/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class SnWeightValidator implements ConstraintValidator<SnWeight, Float>
{
	@Override
	public boolean isValid(final Float value, final ConstraintValidatorContext constraintValidatorContext)
	{
		// null values are valid
		if (value == null)
		{
			return true;
		}

		return value.floatValue() > 0;
	}
}
