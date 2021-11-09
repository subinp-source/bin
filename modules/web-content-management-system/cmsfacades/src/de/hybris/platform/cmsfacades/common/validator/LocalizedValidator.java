/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.validator;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.springframework.validation.Errors;


/**
 * Validator to validate localized attributes.
 */
public interface LocalizedValidator
{
	/**
	 * Validate localized attributes for all languages.
	 *
	 * @param consumer
	 *           - contains the logic to perform the actual validation
	 * @param function
	 *           - contains the method to use to extract the localized content to be validated
	 * @param errors
	 *           - contains the current error context
	 */
	<T> void validateAllLanguages(BiConsumer<String, T> consumer, Function<String, T> function, Errors errors);

	/**
	 * Validate localized attributes for required languages only.
	 *
	 * @param consumer
	 *           - contains the logic to perform the actual validation
	 * @param function
	 *           - contains the method to use to extract the localized content to be validated
	 * @param errors
	 *           - contains the current error context
	 */
	<T> void validateRequiredLanguages(BiConsumer<String, T> consumer, Function<String, T> function, Errors errors);
}
