/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.PRIMITIVE_ENTITY_PROPERTY_NAME;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingPropertyException;

import javax.validation.constraints.NotNull;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

class ODataEntryAttributeValueConverter extends AbstractValueConverter
{
	public ODataEntryAttributeValueConverter(@NotNull final ODataEntryToIntegrationItemConverter entryConverter,
	                                         @NotNull final PayloadAttributeValueConverter valueConverter)
	{
		super(entryConverter, valueConverter);
	}

	@Override
	public boolean isApplicable(final ConversionParameters parameters)
	{
		return parameters.getAttributeValue() instanceof ODataEntry;
	}

	@Override
	public Object convert(final ConversionParameters parameters)
	{
		return isPrimitiveAttribute(parameters.getTypeDescriptor(), parameters.getAttributeName())
				? ((ODataEntry) parameters.getAttributeValue()).getProperties().get(PRIMITIVE_ENTITY_PROPERTY_NAME)
				: convertedODataEntry(parameters);
	}

	private boolean isPrimitiveAttribute(final TypeDescriptor type, final String attrName)
	{
		return type.getAttribute(attrName)
		           .map(TypeAttributeDescriptor::isPrimitive)
		           .orElse(false);
	}

	private Object convertedODataEntry(final ConversionParameters parameters)
	{
		final TypeDescriptor nestedType = parameters.getTypeDescriptor().getAttribute(parameters.getAttributeName())
		                                            .map(TypeAttributeDescriptor::getAttributeType)
		                                            .orElseThrow(() -> new MissingPropertyException(
				                                            parameters.getTypeDescriptor().getItemCode(),
				                                            parameters.getAttributeName()));
		return toIntegrationItem(parameters.getContext(), nestedType,
				(ODataEntry) parameters.getAttributeValue(), parameters.getIntegrationItem());
	}
}
