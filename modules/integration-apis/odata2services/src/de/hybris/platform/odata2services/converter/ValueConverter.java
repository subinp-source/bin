/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.converter;


import javax.validation.constraints.NotNull;

/**
 * Converter of a particular kind of ODataEntry attribue value. For example, one implementation may be responsible for
 * converting {@link java.util.Collection} values, another one for converting {@link org.apache.olingo.odata2.api.ep.entry.ODataEntry}
 * values, etc.
 */
public interface ValueConverter
{
	/**
	 * Deduce based on the conversion context whether this converter can handle the attribute value or not.
	 * @param parameters conversion parameters carrying the context for the value conversion.
	 * @return {@code true}, if this converter is applicable to the provided conversion parameters and can handle the
	 * attribute value conversion; {@code false} otherwise.
	 */
	boolean isApplicable(@NotNull ConversionParameters parameters);

	/**
	 * Converts ODataEntry attribute value to the value that can exist inside an {@link de.hybris.platform.integrationservices.item.IntegrationItem}.
	 * In a typical usecase, when {@link #isApplicable(ConversionParameters)} returns {@code true}, only then call this method.
	 * @param parameters conversion context containing the attribute value and other context information. This should be
	 *                   same {@code ConversionParameters}, which were passed {@link #isApplicable(ConversionParameters)}
	 * @return converted value.
	 */
	Object convert(@NotNull ConversionParameters parameters);
}
