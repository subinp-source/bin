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
package de.hybris.platform.integration.cis.payment.jalo;

import de.hybris.platform.integration.cis.payment.constants.CispaymentConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("PMD")
public class CispaymentManager extends GeneratedCispaymentManager
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(CispaymentManager.class.getName());

	public static final CispaymentManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CispaymentManager) em.getExtension(CispaymentConstants.EXTENSIONNAME);
	}

}
