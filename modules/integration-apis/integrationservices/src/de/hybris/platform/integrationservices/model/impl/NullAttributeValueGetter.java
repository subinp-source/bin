/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueGetter;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * An {@link AttributeValueGetter} that returns a null value
 */
public final class NullAttributeValueGetter implements AttributeValueGetter
{
	@Override
	public Object getValue(final Object model)
	{
		return null;
	}

	@Override
	public Object getValue(final Object model, final Locale locale)
	{
		return null;
	}

	@Override
	public Map<Locale, Object> getValues(final Object model, final Locale... locales)
	{
		return Collections.emptyMap();
	}
}
