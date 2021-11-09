/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.vendor;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;


/**
 * Service to find a specific RuleEngineContext for Vendor ProductCatalogVersion
 */
public interface VendorPromotionRuleEngineContextService
{
	/**
	 * find a specific Promotion RuleEngineContext by name
	 *
	 * @param contextName
	 *           the specific name for Promotion RuleEngineContext
	 * @return the Promotion RuleEngineContext found
	 */
	AbstractRuleEngineContextModel findVendorRuleEngineContextByName(String contextName);


}
