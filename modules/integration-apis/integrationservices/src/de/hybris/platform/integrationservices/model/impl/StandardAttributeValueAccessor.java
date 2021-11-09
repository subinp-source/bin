/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * Provides access to standard platform attributes, which are modeled as {@code attribute} element in items.xml.
 * @deprecated Use {@link StandardAttributeValueGetter} instead
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public class StandardAttributeValueAccessor extends DelegatingAttributeValueAccessor
{
	/**
	 * Instantiates this attribute value accessor
	 *
	 * @param attribute attribute whose values should be accessed
	 * @param service model service for reading/writing attribute values.
	 */
	public StandardAttributeValueAccessor(final TypeAttributeDescriptor attribute, final ModelService service)
	{
		this(new StandardAttributeValueGetter(attribute, service), new StandardAttributeValueSetter(attribute, service));
	}

	private StandardAttributeValueAccessor(final StandardAttributeValueGetter getter, final StandardAttributeValueSetter setter)
	{
		super(getter, setter);
	}
}
