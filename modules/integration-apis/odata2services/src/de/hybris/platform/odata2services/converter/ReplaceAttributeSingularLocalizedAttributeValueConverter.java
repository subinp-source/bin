/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import de.hybris.platform.integrationservices.item.LocalizedValue;

import javax.validation.constraints.NotNull;

class ReplaceAttributeSingularLocalizedAttributeValueConverter extends SingularLocalizedAttributeValueConverter
{
	public ReplaceAttributeSingularLocalizedAttributeValueConverter(@NotNull final LocalizedValueProvider provider)
	{
		super(provider);
	}

	@Override
	public boolean isApplicable(final ConversionParameters params)
	{
		return params.isReplaceAttributesRequest() && isLocalizedSingularValue(params.getTypeAttributeDescriptor());
	}

	@Override
	public Object convert(final ConversionParameters parameters)
	{
		return parameters.getAttributeValue() != null
				? handleNonNullAttributeValue(parameters)
				: getLocalizationProvider().toNullLocalizedValue(parameters);
	}

	private LocalizedValue handleNonNullAttributeValue(final ConversionParameters parameters)
	{
		final LocalizedValue nullLocalizedValue = getLocalizationProvider().toNullLocalizedValue(parameters);
		final LocalizedValue localizedValue = getLocalizationProvider().toLocalizedValue(parameters);
		return nullLocalizedValue.combine(localizedValue);
	}
}
