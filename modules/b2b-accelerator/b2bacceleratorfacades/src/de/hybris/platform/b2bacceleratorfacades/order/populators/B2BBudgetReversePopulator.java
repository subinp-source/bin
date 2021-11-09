/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.order.populators;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.StandardDateRange;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link B2BBudgetData} with {@link B2BBudgetModel}.
 *
 * @deprecated Since 6.0. Use
 *             {@link de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BBudgetReversePopulator}
 *             instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class B2BBudgetReversePopulator implements Populator<B2BBudgetData, B2BBudgetModel>
{
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2BUnitService;
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final B2BBudgetData source, final B2BBudgetModel target) throws ConversionException
	{
		target.setCode(source.getCode());
		target.setName(source.getName());
		target.setBudget(source.getBudget());
		final B2BUnitModel b2BUnitModel = getB2BUnitService().getUnitForUid(source.getUnit().getUid());
		if (b2BUnitModel != null)
		{
			target.setUnit(b2BUnitModel);
		}
		final CurrencyModel currencyModel = getCommonI18NService().getCurrency(source.getCurrency().getIsocode());
		target.setCurrency(currencyModel);
		target.setDateRange(new StandardDateRange(source.getStartDate(), source.getEndDate()));
	}

	protected B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2BUnitService()
	{
		return b2BUnitService;
	}

	@Required
	public void setB2BUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		b2BUnitService = b2bUnitService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
