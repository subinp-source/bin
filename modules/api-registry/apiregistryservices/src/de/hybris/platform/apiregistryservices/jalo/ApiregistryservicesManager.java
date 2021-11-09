/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jalo;

import de.hybris.platform.apiregistryservices.constants.ApiregistryservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

public class ApiregistryservicesManager extends GeneratedApiregistryservicesManager
{

	public static final ApiregistryservicesManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ApiregistryservicesManager) em.getExtension(ApiregistryservicesConstants.EXTENSIONNAME);
	}
	
}
