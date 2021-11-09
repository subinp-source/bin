/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.valueprovider.impl;

import de.hybris.platform.addonsupport.valueprovider.AddOnValueProvider;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AddOnValueProvider} interface.
 */
public class DefaultAddOnValueProvider implements AddOnValueProvider
{
	private static final Logger LOG = Logger.getLogger(DefaultAddOnValueProvider.class);

	private Map<String, Supplier> suppliers;

	@Override
	public <T> Optional<T> getValue(final String key, final Class<T> type)
	{
		final Supplier supplier = getSuppliers().get(key);

		if (supplier == null)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("No value supplied for key [%s]. Returning empty Optional.", key));
			}
			return Optional.empty();
		}

		final Object suppliedValue = supplier.get();

		if (suppliedValue != null && !suppliedValue.getClass().equals(type))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format(
						"Value supplied for key [%s] is of type [%s] but [%s] was expected. Returning empty Optional.", key,
						suppliedValue.getClass().getSimpleName(), ( type == null ? "null" : type.getSimpleName() ) ));
			}
			return Optional.empty();
		}

		return Optional.ofNullable((T) suppliedValue);
	}

	protected Map<String, Supplier> getSuppliers()
	{
		return suppliers;
	}

	@Required
	public void setSuppliers(final Map<String, Supplier> suppliers)
	{
		this.suppliers = suppliers;
	}

}
