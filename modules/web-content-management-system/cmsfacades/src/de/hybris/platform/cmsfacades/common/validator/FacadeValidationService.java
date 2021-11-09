/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.validator;

import de.hybris.platform.cmsfacades.exception.ValidationException;

import org.springframework.validation.Validator;


/**
 * Service used to perform validation in the facade.
 */
public interface FacadeValidationService
{
	/**
	 * Validate the given DTO.
	 *
	 * @param validator
	 *           - the validator to use for the validation
	 * @param validatee
	 *           - the object being validated
	 * @throws ValidationException
	 *            when validation errors were found
	 */
	void validate(Validator validator, Object validatee) throws ValidationException;

	/**
	 * Validate the given DTO.
	 *
	 * @param validator
	 *           - the validator to use for the validation
	 * @param validatee
	 *           - the object being validated
	 * @param bindingObject
	 *           - the object to use for binding the field errors
	 * @throws ValidationException
	 *            when validation errors were found
	 */
	void validate(Validator validator, Object validatee, Object bindingObject) throws ValidationException;

}
