/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.converter;

import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LANGUAGE_KEY_PROPERTY_NAME;

import de.hybris.platform.integrationservices.item.LocalizedValue;
import de.hybris.platform.integrationservices.item.StringToLocaleConverter;
import de.hybris.platform.odata2services.odata.persistence.MissingLanguageException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

/**
 * Localized attributes values for a given EDM type. For example, if a Product has localized attributes, then the localized attributes
 * will be captured in a Localized___Product EDM type. This class represents the Localized___... EDM types.
 */
public class LocalizedAttributes
{
	private static final StringToLocaleConverter TAG_CONVERTER = new StringToLocaleConverter();
	/**
	 * An empty {@code LocalizedAttributes} object that does not contain a single attribute value.
	 */
	public static final LocalizedAttributes EMPTY = new LocalizedAttributes();

	protected final Map<String, LocalizedValue> attributes;

	LocalizedAttributes()
	{
		attributes = new HashMap<>();
	}

	/**
	 * Creates instance of {@code LocalizedAttributes} and populates it from the provided OData entry
	 *
	 * @param entry an entry that corresponds to the 'Localized___...' type.
	 * @return an instance with all localized attributes present in the OData entry
	 */
	public static LocalizedAttributes createFrom(final ODataEntry entry)
	{
		if (entry == null)
		{
			return null;
		}
		final Map<String, Object> properties = entry.getProperties();
		final LocalizedAttributes localizedAttributes = new LocalizedAttributes();
		localizedAttributes.addAll(extractLanguage(properties), properties);
		return localizedAttributes;
	}

	/**
	 * Creates instance of {@code LocalizedAttributes} and populates it with the provided attribute values
	 *
	 * @param values a Map containing localized attribute values.
	 * @return an instance with provided localized attributes values
	 */
	public static LocalizedAttributes createWithValues(final Map<String, LocalizedValue> values)
	{
		if (values == null)
		{
			return null;
		}
		final LocalizedAttributes localizedAttributes = new LocalizedAttributes();
		localizedAttributes.attributes.putAll(values);
		return localizedAttributes;
	}

	private static Locale extractLanguage(final Map<String, Object> properties)
	{
		return Optional.ofNullable(properties.get(LANGUAGE_KEY_PROPERTY_NAME))
				.map(String.class::cast)
				.filter(StringUtils::isNotEmpty)
				.map(TAG_CONVERTER::convert)
				.orElseThrow(MissingLanguageException::new);
	}

	private void addAll(final Locale locale, final Map<String, Object> properties)
	{
		properties.forEach((attr, val) -> setAttribute(attr, locale, val));
	}

	/**
	 * Performs the given action for each entry in this map until all entries have been processed or the action throws an
	 * exception. Actions are performed in the order of entry set iteration. Exceptions thrown by the action are relayed back to
	 * the caller.
	 *
	 * @param action The action to be performed for each entry
	 * @throws NullPointerException if the specified action is {@code null}
	 * @throws java.util.ConcurrentModificationException if an entry is found to be removed during iteration
	 */
	public void forEachAttribute(final BiConsumer<? super String, ? super LocalizedValue> action)
	{
		attributes.forEach(action);
	}

	/**
	 * Combines this localized attributes with the attributes present in the other instance of {@code LocalizedAttributes}
	 *
	 * @param another an instance to combine with
	 * @return an instance of {@code LocalizedAttributes} that contains all attributes of this instance and all attributes of
	 * the provided {@code another} instance. If {@code another} instance contains values for the same attributes as in this
	 * instance those values will be preferred to the values in this instance.
	 */
	public LocalizedAttributes combine(final LocalizedAttributes another)
	{
		return another == null
				? this
				: new LocalizedAttributes()
				.setAttributes(attributes)
				.setAttributes(another.attributes);
	}

	/**
	 * Resets all localized attributes to the provided value without mutating this {@code LocalizedAttributes} instance.
	 * @param value localized value to set in all localized attributes
	 * @return new instance of the {@code LocalizedAttributes} with all attributes reset to the provided value
	 */
	public LocalizedAttributes setAll(final LocalizedValue value)
	{
		final LocalizedValue newValue = value != null ? value : LocalizedValue.EMPTY;
		final LocalizedAttributes copy = new LocalizedAttributes();
		attributes.keySet().forEach(n -> copy.setAttribute(n, newValue));
		return copy;
	}

	private LocalizedAttributes setAttributes(final Map<String, LocalizedValue> attributes)
	{
		attributes.forEach(this::setAttribute);
		return this;
	}

	private void setAttribute(final String attrName, final Locale lang, final Object value)
	{
		if (value != null)
		{
			setAttribute(attrName, LocalizedValue.of(lang, value));
		}
	}

	private void setAttribute(final String attrName, final LocalizedValue value)
	{
		if (!LANGUAGE_KEY_PROPERTY_NAME.equals(attrName) && StringUtils.isNotEmpty(attrName))
		{
			final LocalizedValue currentValue = attributes.getOrDefault(attrName, LocalizedValue.EMPTY);
			attributes.put(attrName, currentValue.combine(value));
		}
	}

	@Override
	public boolean equals(final Object o)
	{
		return (this == o)
				|| (o != null && getClass() == o.getClass() && attributes.equals(((LocalizedAttributes) o).attributes));
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(getClass())
				.append(attributes)
				.build();
	}

	@Override
	public String toString()
	{
		return attributes.toString();
	}
}
