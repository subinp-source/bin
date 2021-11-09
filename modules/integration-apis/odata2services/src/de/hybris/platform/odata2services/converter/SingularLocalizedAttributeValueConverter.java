/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import javax.validation.constraints.NotNull;

public class SingularLocalizedAttributeValueConverter implements ValueConverter
{
	private final LocalizedValueProvider localizationProvider;

	public SingularLocalizedAttributeValueConverter(@NotNull final LocalizedValueProvider provider)
	{
		localizationProvider = provider;
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return !parameters.isReplaceAttributesRequest() && isLocalizedSingularValue(
				parameters.getTypeAttributeDescriptor());
	}

	boolean isLocalizedSingularValue(final TypeAttributeDescriptor descriptor)
	{
		return descriptor != null && !descriptor.isCollection() && descriptor.isLocalized();
	}

	@Override
	public Object convert(final ConversionParameters parameters)
	{
		return getLocalizationProvider().toLocalizedValue(parameters);
	}

	LocalizedValueProvider getLocalizationProvider()
	{
		return localizationProvider;
	}
}
