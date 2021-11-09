/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.storefrontsampledata.jalo;

import com.hybris.merchandising.storefrontsampledata.constants.MerchandisingstorefrontsampledataaddonConstants;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class MerchandisingstorefrontsampledataaddonManager extends GeneratedMerchandisingstorefrontsampledataaddonManager
{
	public static final MerchandisingstorefrontsampledataaddonManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MerchandisingstorefrontsampledataaddonManager) em.getExtension(MerchandisingstorefrontsampledataaddonConstants.EXTENSIONNAME);
	}
	
}
