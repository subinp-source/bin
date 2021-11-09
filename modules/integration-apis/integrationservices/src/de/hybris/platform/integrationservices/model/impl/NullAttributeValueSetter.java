/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueSetter;

/**
 * An {@link AttributeValueSetter} that does not set any value in the model
 */
public final class NullAttributeValueSetter implements AttributeValueSetter
{
	@Override
	public void setValue(final Object model, final Object value)
	{
		// sets nothing being a null setter
	}
}
