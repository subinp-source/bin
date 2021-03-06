/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingCycleTypeData;
import de.hybris.platform.subscriptionfacades.data.BillingPlanData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.enums.BillingCycleType;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.BillingPlanModel;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

/**
 * Populator implementation for {@link BillingPlanModel} as source and {@link BillingPlanData} as target type.
 *
 * @param <SOURCE> source class
 * @param <TARGET> target class
 */
public class BillingPlanPopulator<SOURCE extends BillingPlanModel, TARGET extends BillingPlanData> implements
		Populator<SOURCE, TARGET>
{

	private Converter<BillingCycleType, BillingCycleTypeData> billingCycleTypeConverter;
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setBillingCycleDay(source.getBillingCycleDay());
		target.setBillingCycleType(getBillingCycleTypeConverter().convert(source.getBillingCycleType()));
		target.setBillingTime(getBillingTimeConverter().convert(source.getBillingFrequency()));
		target.setName(source.getName());
	}

	protected Converter<BillingCycleType, BillingCycleTypeData> getBillingCycleTypeConverter()
	{
		return billingCycleTypeConverter;
	}

	@Required
	public void setBillingCycleTypeConverter(final Converter<BillingCycleType, BillingCycleTypeData> billingCycleTypeConverter)
	{
		this.billingCycleTypeConverter = billingCycleTypeConverter;
	}

	protected Converter<BillingTimeModel, BillingTimeData> getBillingTimeConverter()
	{
		return billingTimeConverter;
	}

	@Required
	public void setBillingTimeConverter(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter)
	{
		this.billingTimeConverter = billingTimeConverter;
	}

}
