/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.validators.page.impl;

import de.hybris.platform.cmsfacades.common.predicate.ProductCodeExistsPredicate;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.dto.RenderingPageValidationDto;
import de.hybris.platform.cmsfacades.rendering.validators.page.RenderingPageChecker;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;

import java.util.function.Predicate;


/**
 * Implementation of {@link RenderingPageChecker} to validate Product page attributes of {@link RenderingPageValidationDto}.
 */
public class RenderingProductPageChecker implements RenderingPageChecker
{
	private Predicate<String> pagePredicate;
	private ProductCodeExistsPredicate productCodeExistsPredicate;

	@Override
	public Predicate<String> getConstrainedBy()
	{
		return pagePredicate;
	}

	@Override
	public void verify(RenderingPageValidationDto validationDto, Errors errors)
	{
		if (getProductCodeExistsPredicate().negate().test(validationDto.getCode()))
		{
			errors.rejectValue("code", CmsfacadesConstants.FIELD_NOT_ALLOWED);
		}
	}

	protected Predicate<String> getPagePredicate()
	{
		return pagePredicate;
	}

	@Required
	public void setPagePredicate(Predicate<String> pagePredicate)
	{
		this.pagePredicate = pagePredicate;
	}

	protected ProductCodeExistsPredicate getProductCodeExistsPredicate()
	{
		return productCodeExistsPredicate;
	}

	@Required
	public void setProductCodeExistsPredicate(
			ProductCodeExistsPredicate productCodeExistsPredicate)
	{
		this.productCodeExistsPredicate = productCodeExistsPredicate;
	}
}
