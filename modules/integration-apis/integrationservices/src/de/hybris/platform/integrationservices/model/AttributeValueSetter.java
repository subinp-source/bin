/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

/**
 * An AttributeValueSetter persists an attribute's value
 */
public interface AttributeValueSetter
{
	/**
	 * Sets the value on the given {@link Object}
	 *
	 * @param model Reference to the model that the attribute value will be set on
	 * @param value Value to be set in the model
	 */
	void setValue(Object model, Object value);
}
