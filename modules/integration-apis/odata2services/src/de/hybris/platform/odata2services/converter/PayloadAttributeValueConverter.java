/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;

import javax.validation.constraints.NotNull;

/**
 * A converter of attribute values in an ODataEntry to values that are OData independent,
 * i.e. {@link org.apache.olingo.odata2.api.ep.feed.ODataFeed} values are converted to {@link java.util.Collection},
 * {@link org.apache.olingo.odata2.api.ep.entry.ODataEntry}s are converted to {@link de.hybris.platform.integrationservices.item.IntegrationItem}
 * or for the special cases they are converted to primitives, localized values, etc.
 */
public interface PayloadAttributeValueConverter
{
	/**
	 * Converts an ODataEntry attribute value to values acceptable by {@link de.hybris.platform.integrationservices.item.IntegrationItem}.
	 *
	 * @param parameters context for the value conversion, which carries attribute name, value, ODataContext, etc. For
	 *                   that reason the conversion parameters cannot be {@code null} or otherwise conversion is not
	 *                   possible.
	 * @return converted value that is OData independent and can be used as attribute value in
	 * {@link de.hybris.platform.integrationservices.item.IntegrationItem}. This value may be {@code null} because
	 * attributes may have {@code null} values.
	 */
	Object convertAttributeValue(@NotNull ConversionParameters parameters);
}
