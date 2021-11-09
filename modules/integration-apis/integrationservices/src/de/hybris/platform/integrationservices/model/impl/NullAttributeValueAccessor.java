/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueAccessor;

/**
 * An {@link AttributeValueAccessor} that returns a null value
 *
 * @deprecated Use {@link DelegatingAttributeValueAccessor} delegating to {@link NullAttributeValueGetter} and to
 * {@link NullAttributeValueSetter}
 */
@Deprecated(since = "1905.11-CEP", forRemoval = true)
public class NullAttributeValueAccessor extends DelegatingAttributeValueAccessor
{
	NullAttributeValueAccessor()
	{
		super(new NullAttributeValueGetter(), new NullAttributeValueSetter());
	}
}
