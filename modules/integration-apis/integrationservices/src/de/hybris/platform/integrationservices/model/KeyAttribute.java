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

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.integrationservices.exception.LocalizedKeyAttributeException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * An attribute declared as a key attribute in integration object item definition.
 */
public class KeyAttribute
{
	private final IntegrationObjectItemAttributeModel attributeModel;

	public KeyAttribute(final IntegrationObjectItemAttributeModel model)
	{
		Preconditions.checkArgument(model != null, "Attribute model is required to instantiate a KeyAttribute");
		Preconditions.checkArgument(model.getAttributeDescriptor() != null, "AttributeDescriptor cannot be null");
		final Boolean localized = model.getAttributeDescriptor().getLocalized();
		if (BooleanUtils.isTrue(localized))
		{
			throw new LocalizedKeyAttributeException(model);
		}
		attributeModel = model;
	}

	private String getObjectCode()
	{
		return attributeModel.getIntegrationObjectItem().getIntegrationObject().getCode();
	}

	/**
	 * Retrieves integration object item code this attribute belongs to.
	 *
	 * @return value of the corresponding {@link IntegrationObjectItemModel#getCode()}
	 */
	public String getItemCode()
	{
		return attributeModel.getIntegrationObjectItem().getCode();
	}

	/**
	 * Retrieves name of the integration object item attribute.
	 *
	 * @return value of the corresponding {@link IntegrationObjectItemAttributeModel#getAttributeName()}
	 */
	public String getName()
	{
		return attributeModel.getAttributeName();
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
			final KeyAttribute that = (KeyAttribute) o;
			return getName().equals(that.getName())
					&& getItemCode().equals(that.getItemCode())
					&& getObjectCode().equals(that.getObjectCode());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
				.append(getName())
				.append(getItemCode())
				.append(getObjectCode())
				.build();
	}

	@Override
	public String toString()
	{
		final IntegrationObjectItemModel item = attributeModel.getIntegrationObjectItem();
		return "KeyAttribute{" +
				item.getIntegrationObject().getCode() + ":" +
				item.getCode() + ":" +
				attributeModel.getAttributeName() +
				'}';
	}
}
