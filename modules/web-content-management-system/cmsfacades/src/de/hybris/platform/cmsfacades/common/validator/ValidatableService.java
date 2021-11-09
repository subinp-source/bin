/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.validator;

import de.hybris.platform.cmsfacades.exception.ValidationException;

import java.util.function.Supplier;

/**
 * Interface to process the validatable supplier and throws a {@link ValidationException} with the ValidationErrors when errors > 0. 
 */
public interface ValidatableService
{
	/**
	 * Executes the validatable supplier to collect any validation error after execution
	 * @param validatable the validatable supplier. 
	 * @return the expected result after successful validation
	 * @throws ValidationException when there are validation errors after completion of the given supplier. 
	 */
	<T> T execute(Supplier<T> validatable);
	
}
