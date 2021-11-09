/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

/**
 * Populator implementation for {@link BillingTimeModel} as source and {@link BillingTimeData} as target type.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class BillingTimePopulator<SOURCE extends BillingTimeModel, TARGET extends BillingTimeData> implements
		Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setCode(source.getCode());
		target.setName(source.getNameInCart());
		target.setNameInOrder(source.getNameInOrder());
		target.setDescription(source.getDescription());
		target.setOrderNumber(source.getOrder() == null ? 0 : source.getOrder());
	}
}
