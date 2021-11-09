/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.integrationservices.model.AttributeValueAccessor;
import de.hybris.platform.integrationservices.model.AttributeValueGetter;
import de.hybris.platform.integrationservices.model.AttributeValueSetter;

import java.util.Locale;
import java.util.Map;

import com.google.common.base.Preconditions;

/**
 * A default implementation the {@code AttributeValueAccessor}, which delegates its calls to {@link de.hybris.platform.integrationservices.model.AttributeValueSetter}
 * and to {@link de.hybris.platform.integrationservices.model.AttributeValueGetter}
 */
public class DelegatingAttributeValueAccessor implements AttributeValueAccessor
{
	private final AttributeValueGetter getter;
	private final AttributeValueSetter setter;

	public DelegatingAttributeValueAccessor(final AttributeValueGetter get, final AttributeValueSetter set)
	{
		Preconditions.checkArgument(get != null, "getter cannot be null");
		Preconditions.checkArgument(set != null, "setter cannot be null");
		getter = get;
		setter = set;
	}

	@Override
	public Object getValue(final Object model)
	{
		return getter.getValue(model);
	}

	@Override
	public Object getValue(final Object model, final Locale locale)
	{
		return getter.getValue(model, locale);
	}

	@Override
	public Map<Locale, Object> getValues(final Object model, final Locale... locales)
	{
		return getter.getValues(model, locales);
	}

	@Override
	public void setValue(final Object model, final Object value)
	{
		setter.setValue(model, value);
	}
}
