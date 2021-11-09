/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.assistedserviceyprofileaddon.jalo;

import de.hybris.platform.assistedserviceyprofileaddon.constants.AssistedserviceyprofileaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;


@SuppressWarnings("PMD")
public class AssistedserviceyprofileaddonManager extends GeneratedAssistedserviceyprofileaddonManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(AssistedserviceyprofileaddonManager.class.getName());

	public static final AssistedserviceyprofileaddonManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (AssistedserviceyprofileaddonManager) em.getExtension(AssistedserviceyprofileaddonConstants.EXTENSIONNAME);
	}

}
