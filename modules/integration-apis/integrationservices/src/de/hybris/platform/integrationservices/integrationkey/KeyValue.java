/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.integrationservices.integrationkey;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.KeyAttribute;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A value object encapsulating item key value regardless of whether the item has a simple or a composite key.
 */
public class KeyValue
{
	private final Set<KeyAttributeValue> attributeValues;

	/**
	 * Instantiates an empty key value.
	 */
	public KeyValue()
	{
		this(Collections.emptyList());
	}

	/**
	 * Instantiates a key value consisting of the provided attribute values
	 * @param values attribute values for this key
	 */
	public KeyValue(final Collection<KeyAttributeValue> values)
	{
		final Set<KeyAttributeValue> validValues = values.stream()
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		attributeValues = Collections.unmodifiableSet(validValues);
	}

	/**
	 * Retrieves values this key consists of.
	 * @return a collection of values of all key attributes comprising this key value or an empty collection, if the item does not
	 * have a key.
	 */
	public Collection<KeyAttributeValue> getKeyAttributeValues()
	{
		return attributeValues;
	}

	@Override
	public boolean equals(final Object o)
	{
		return this == o
				|| (o != null && getClass() == o.getClass() && attributeValues.equals(((KeyValue) o).attributeValues));
	}

	@Override
	public int hashCode()
	{
		return attributeValues.hashCode();
	}

	@Override
	public String toString()
	{
		return "KeyValue" + attributeValues;
	}

	/**
	 * A builder for the {@link KeyValue}
	 */
	public static class Builder
	{
		private final Collection<KeyAttributeValue> attributeValues = new HashSet<>();

		/**
		 * Specifies a single attribute value for the key value to build.
		 * @param attribute an attribute model, for which value is provided
		 * @param value value for the attribute
		 * @return a builder with the value specified
		 */
		public Builder withValue(final IntegrationObjectItemAttributeModel attribute, final Object value)
		{
			return withValue(new KeyAttribute(attribute), value);
		}

		/**
		 * Specifies a single attribute value for the key value to build.
		 * @param attribute an attribute, for which value is provided
		 * @param value value for the attribute
		 * @return a builder with the value specified
		 */
		public Builder withValue(final KeyAttribute attribute, final Object value)
		{
			attributeValues.add(new KeyAttributeValue(attribute, value));
			return this;
		}

		/**
		 * Specifies multiple attribute values for the key value to build.
		 * @param values attribute values for the key
		 * @return a builder with the attribute values specified.
		 */
		public Builder withValues(final Collection<KeyAttributeValue> values)
		{
			attributeValues.addAll(values);
			return this;
		}

		/**
		 * Instantiates a new {@code KeyValue} with the attribute values specified so far. This method does not reset the
		 * specifications, so that subsequent calls will produce equal instances.
		 * @return new {@code KeyValue} instance.
		 */
		public KeyValue build()
		{
			return new KeyValue(attributeValues);
		}
	}
}
