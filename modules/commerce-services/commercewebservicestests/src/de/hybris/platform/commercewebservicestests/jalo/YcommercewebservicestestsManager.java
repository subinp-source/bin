/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.commercewebservicestests.constants.YcommercewebservicestestsConstants;

import org.apache.log4j.Logger;


public class YcommercewebservicestestsManager extends GeneratedYcommercewebservicestestsManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(YcommercewebservicestestsManager.class.getName());

	public static final YcommercewebservicestestsManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YcommercewebservicestestsManager) em.getExtension(YcommercewebservicestestsConstants.EXTENSIONNAME);
	}

}
