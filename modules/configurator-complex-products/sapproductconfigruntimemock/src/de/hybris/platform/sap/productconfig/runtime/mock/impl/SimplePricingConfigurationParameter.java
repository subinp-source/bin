/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.sap.productconfig.runtime.interf.PricingConfigurationParameter;

import java.util.Map;
import java.util.Optional;



/**
 * Simple implementation of {@link PricingConfigurationParameter} that is active if no SAP integration is deployed. Will
 * not access any persistence but just hard codes the pricing customizing for the usage with the mock configuration
 * engine
 */
public class SimplePricingConfigurationParameter implements PricingConfigurationParameter
{
	private Map<String, Boolean> productDeltaPriceDecision;

	protected Map<String, Boolean> getProductDeltaPriceDecision()
	{
		return productDeltaPriceDecision;
	}

	public void setProductDeltaPriceDecision(final Map<String, Boolean> productDeltaPriceDecision)
	{
		this.productDeltaPriceDecision = productDeltaPriceDecision;
	}

	@Override
	public boolean isPricingSupported()
	{
		return true;
	}

	@Override
	public String getSalesOrganization()
	{
		return null;
	}

	@Override
	public String getDistributionChannelForConditions()
	{
		return null;
	}

	@Override
	public String getDivisionForConditions()
	{
		return null;
	}

	/**
	 * Won't return the SAP code as it is not available without SAP integration. Instead returns ISO code.
	 */
	@Override
	public String retrieveCurrencySapCode(final CurrencyModel currencyModel)
	{
		return currencyModel.getIsocode();
	}

	/**
	 * Won't return the SAP code as it is not available without SAP integration. Instead returns ISO code.
	 */
	@Override
	public String retrieveUnitSapCode(final UnitModel unitModel)
	{
		return unitModel.getCode();
	}

	@Override
	public boolean showBasePriceAndSelectedOptions()
	{
		return true;
	}

	@Override
	public boolean showDeltaPrices()
	{
		return false;
	}

	@Override
	public boolean showDeltaPrices(final String productCode)
	{
		if (productCode == null)
		{
			return showDeltaPrices();
		}

		return getProductDeltaPriceDecision().containsKey(productCode) && getProductDeltaPriceDecision().get(productCode);
	}

	@Override
	public String retrieveCurrencyIsoCode(final CurrencyModel currencyModel)
	{
		return Optional.ofNullable(currencyModel).map(CurrencyModel::getIsocode).orElse(null);
	}

	@Override
	public String retrieveUnitIsoCode(final UnitModel unitModel)
	{
		return Optional.ofNullable(unitModel).map(UnitModel::getCode).orElse(null);
	}



	@Override
	public String convertSapToIsoCode(final String sapCode)
	{
		return null;
	}

}
