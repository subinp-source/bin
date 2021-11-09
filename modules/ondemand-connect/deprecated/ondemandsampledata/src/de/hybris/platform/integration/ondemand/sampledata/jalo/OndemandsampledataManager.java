/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integration.ondemand.sampledata.jalo;

import de.hybris.platform.integration.ondemand.sampledata.constants.OndemandsampledataConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

@SuppressWarnings("PMD")
public class OndemandsampledataManager extends GeneratedOndemandsampledataManager
{
	public static final OndemandsampledataManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (OndemandsampledataManager) em.getExtension(OndemandsampledataConstants.EXTENSIONNAME);
	}
	
}
