/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.valueprovider.impl;

import de.hybris.platform.addonsupport.valueprovider.AddOnValueProvider;
import de.hybris.platform.addonsupport.valueprovider.AddOnValueProviderRegistry;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AddOnValueProviderRegistry} interface.
 */
public class DefaultAddOnValueProviderRegistry implements AddOnValueProviderRegistry
{

	private Map<String, AddOnValueProvider> valueProviders;

	@Override
	public Optional<AddOnValueProvider> get(final String addOnName)
	{
		return Optional.ofNullable(getValueProviders().get(addOnName));
	}

	protected Map<String, AddOnValueProvider> getValueProviders()
	{
		return valueProviders;
	}

	@Required
	public void setValueProviders(final Map<String, AddOnValueProvider> valueProviders)
	{
		this.valueProviders = valueProviders;
	}

}
