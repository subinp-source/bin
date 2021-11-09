/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.vendor.impl;

import de.hybris.platform.marketplaceservices.vendor.VendorPromotionRuleEngineContextService;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;


/**
 * A default implementation of {@link VendorPromotionRuleEngineContextService}
 */
public class DefaultVendorPromotionRuleEngineContextService implements VendorPromotionRuleEngineContextService
{

	private RuleEngineContextDao ruleEngineContextDao;

	@Override
	public AbstractRuleEngineContextModel findVendorRuleEngineContextByName(final String contextName)
	{
		return getRuleEngineContextDao().findRuleEngineContextByName(contextName);
	}

	protected RuleEngineContextDao getRuleEngineContextDao()
	{
		return ruleEngineContextDao;
	}

	public void setRuleEngineContextDao(final RuleEngineContextDao ruleEngineContextDao)
	{
		this.ruleEngineContextDao = ruleEngineContextDao;
	}

}
