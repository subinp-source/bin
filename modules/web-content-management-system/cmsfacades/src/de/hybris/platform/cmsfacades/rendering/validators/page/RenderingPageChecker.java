/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.validators.page;

import de.hybris.platform.cmsfacades.dto.RenderingPageValidationDto;
import org.springframework.validation.Errors;

import java.util.function.Predicate;


/**
 * Interface responsible for providing predicate to test whether the page qualifier exists or not.
 * Qualifier can be page id or label for Content page.
 * Or it can be a product code for Product page.
 */
public interface RenderingPageChecker
{
	/**
	 * Predicate to test if a given page type code matches the page supplier.
	 * <p>
	 * Returns <tt>TRUE</tt> if the supplier exists; <tt>FALSE</tt> otherwise.
	 * </p>
	 */
	Predicate<String> getConstrainedBy();

	/**
	 * Validates attributes inside {@link RenderingPageValidationDto}
	 */
	void verify(RenderingPageValidationDto renderPageValidationDto, Errors errors);
}
