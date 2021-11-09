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

import de.hybris.platform.integrationservices.model.KeyAttribute;

import java.util.Objects;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * Captures a value of a single key attribute in a data item.
 */
public class KeyAttributeValue
{
	private final KeyAttribute attribute;
	private final Object value;

	public KeyAttributeValue(final KeyAttribute attribute, final Object value)
	{
		Preconditions.checkArgument(attribute != null, "KeyAttributeValue cannot be instantiated with null KeyAttribute");
		this.attribute = attribute;
		this.value = value;
	}

	/**
	 * Reads key attribute.
	 * @return an item key attribute
	 */
	public KeyAttribute getAttribute()
	{
		return attribute;
	}

	/**
	 * Reads value of the attribute.
	 * @return value the attribute has in a data item.
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
		if (o != null && getClass() == o.getClass())
		{
			final KeyAttributeValue that = (KeyAttributeValue) o;
			return attribute.equals(that.attribute) && Objects.equals(value, that.value);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(attribute)
				.append(value)
				.build();
	}

	@Override
	public String toString()
	{
		return attribute + "=" + value;
	}
}
