/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.constraints;

import de.hybris.platform.apiregistryservices.utils.EventExportUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that the given string value is a valid url.
 * It also checks the protocol of the url matches one of the protocols which are specified in project.properties.
 */
public class ApiUrlValidator implements ConstraintValidator<ApiUrlValid, String>
{

	@Override
	public void initialize(final ApiUrlValid urlValid)
	{
		// empty
	}

	@Override
	public boolean isValid(final String url, final ConstraintValidatorContext constraintValidatorContext)
	{
		return EventExportUtils.isUrlValid(url);
	}
}
