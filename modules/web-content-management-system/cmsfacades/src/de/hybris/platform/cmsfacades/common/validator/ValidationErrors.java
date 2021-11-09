/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.validator;

import de.hybris.platform.cmsfacades.validator.data.ValidationError;

import java.util.List;

/**
 * Validation Errors to interface and manage {@link ValidationError} lists.
 * This ValidationErrors interface ca also support multilevel field validation with {@code pushField} and {@code popField}
 * methods.
 */
public interface ValidationErrors
{

	/**
	 * Operation to add the {@link ValidationError} to the list of errors.  
	 * @param validationError the validation error to be added to the list.
	 */
	void add(ValidationError validationError);

	/**
	 * Multilevel field Support method to push fields in a Stack.  
	 * @param field the field to be stacked when the logic enters a child entity. 
	 */
	void pushField(String field);

	/**
	 * Multilevel field Support method to pop fields from a Stack.  
	 */
	void popField();

	/**
	 * Operation to retrieve the string representation of the fields currently in the stack.
	 * @return the string representation of the values in the stack.
	 */
	String parseFieldStack();

	/**
	 * Returns the list of validation errors. 
	 * @return the list of {@link ValidationError}'s
	 */
	List<ValidationError> getValidationErrors();
}
