/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.sap.productconfig.facades.ConfigurationExpertModeFacade;
import de.hybris.platform.sap.productconfig.services.constants.SapproductconfigservicesConstants;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ConfigurationExpertModeFacadeImpl implements ConfigurationExpertModeFacade
{
	private static final Logger LOG = Logger.getLogger(ConfigurationExpertModeFacadeImpl.class);

	private UserService userService;
	private SessionService sessionService;

	@Override
	public boolean isExpertModeAllowedForCurrentUser()
	{
		final UserModel currentUser = getUserService().getCurrentUser();
		final Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(currentUser);

		return userGroups.stream().anyMatch(grp -> SapproductconfigservicesConstants.EXPERT_MODE_USER_GROUP.equalsIgnoreCase(grp.getUid()));
	}

	@Override
	public boolean isExpertModeActive()
	{
		final Boolean expertModeActive = getSessionService().getAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION);

		if (expertModeActive == null)
		{
			return false;
		}

		return expertModeActive;
	}

	@Override
	public void enableExpertMode()
	{
		if (!isExpertModeAllowedForCurrentUser())
		{
			LOG.info("User tried to enable expert mode, but belongs not to the expert mode group");
			return;
		}

		getSessionService().setAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION, Boolean.TRUE);
	}

	@Override
	public void disableExpertMode()
	{
		if (!isExpertModeAllowedForCurrentUser())
		{
			LOG.info("User tried to enable expert mode, but belongs not to the expert mode group");
			return;
		}

		getSessionService().setAttribute(SapproductconfigservicesConstants.EXPERT_MODE_STATE_IN_SESSION, Boolean.FALSE);
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
