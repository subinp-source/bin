/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.promotionengineservices.test.configuration;

import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.impl.DefaultSwitchService;


public class PromotionEngineTestSupportSwitchService extends DefaultSwitchService
{
	public void enable(final Switch option)
	{
		getStatuses().put(option, Boolean.TRUE);
	}

	public void disable(final Switch option)
	{
		getStatuses().put(option, Boolean.FALSE);
	}

	public Boolean set(final Switch option, final Boolean value)
	{
		return getStatuses().put(option, value);
	}
}
