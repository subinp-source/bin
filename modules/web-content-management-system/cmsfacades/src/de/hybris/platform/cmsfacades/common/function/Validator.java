/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.function;

/**
 * Validator is a Functional Interface that consumes an object to be validated.
 * @param <T> the type of the input to be validated. 
 */
@FunctionalInterface
public interface Validator<T>
{
	/**
	 * Method to perform validation on a given object. 
	 * @param validatee the inpected object being validated. 
	 */
	void validate(T validatee);

}
