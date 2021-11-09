/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.populator;

import java.util.Objects;

/**
 * A wrapper around a primitive value that is stored in a collection. For example, this would
 * generate JSON that looks like this
 * <code>
 *     {
 *         "value": 1234
 *     }
 * </code>
 */
public class PrimitiveCollectionElement
{
	private final Object value;

	private PrimitiveCollectionElement(final Object value)
	{
		this.value = value;
	}

	/**
	 * Create a new {@link PrimitiveCollectionElement} with the given value
	 * @param value Value to be wrapped
	 * @return a new instance
	 */
	public static PrimitiveCollectionElement create(final Object value)
	{
		return new PrimitiveCollectionElement(value);
	}

	/**
	 * Gets the value that is wrapped
	 * @return Value
	 */
	public Object getValue()
	{
		return value;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (this.getClass() != o.getClass())
		{
			return false;
		}
		final PrimitiveCollectionElement that = (PrimitiveCollectionElement) o;
		return Objects.equals(getValue(), that.getValue());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getValue());
	}

	@Override
	public String toString()
	{
		return "PrimitiveCollectionElement{" +
				"value=" + value +
				'}';
	}
}
