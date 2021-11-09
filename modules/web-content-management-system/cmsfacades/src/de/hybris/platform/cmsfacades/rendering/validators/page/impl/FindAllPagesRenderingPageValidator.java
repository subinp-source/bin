/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.validators.page.impl;

import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.dto.RenderingPageValidationDto;

import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator to validate attributes used to extract multiple pages for rendering.
 */
public class FindAllPagesRenderingPageValidator implements Validator
{
	private Predicate<String> typeCodeExistsPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return RenderingPageValidationDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final RenderingPageValidationDto validationDto = (RenderingPageValidationDto) obj;

		if (Objects.nonNull(validationDto.getPageTypeCode())
				&& getTypeCodeExistsPredicate().negate().test(validationDto.getPageTypeCode()))
		{
			errors.rejectValue("pageTypeCode", CmsfacadesConstants.FIELD_NOT_ALLOWED);
		}
	}

	protected Predicate<String> getTypeCodeExistsPredicate()
	{
		return typeCodeExistsPredicate;
	}

	@Required
	public void setTypeCodeExistsPredicate(final Predicate<String> typeCodeExistsPredicate)
	{
		this.typeCodeExistsPredicate = typeCodeExistsPredicate;
	}
}
