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
package de.hybris.platform.integrationservices.populator;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

/**
 * Populate the map type of item model's attribute to Map.
 */
public class DefaultMapType2MapPopulator extends AbstractItemToMapPopulator
{
	private I18NService i18NService;

	@Override
	protected void populateToMap(
			final TypeAttributeDescriptor attr,
			final ItemToMapConversionContext context,
			final Map<String, Object> target)
	{
		if (attr.isLocalized())
		{
			final Object value = attr.accessor().getValue(context.getItemModel(), getI18NService().getCurrentLocale());

			if (value != null)
			{
				target.put(attr.getAttributeName(), value);
			}
		}
	}

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attributeDescriptor)
	{
		return attributeDescriptor.isMap();
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18NService)
	{
		this.i18NService = i18NService;
	}
}
