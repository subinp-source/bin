/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon.services.evaluator.impl;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.cms2.servicelayer.services.evaluator.CMSRestrictionEvaluator;
import de.hybris.platform.secureportaladdon.model.restrictions.CMSSecurePortalRestrictionModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Evaluates a user restriction accordingly to context information.
 * <p/>
 */
public class CMSSecurePortalRestrictionEvaluator implements CMSRestrictionEvaluator<CMSSecurePortalRestrictionModel>
{
	private final static Logger LOG = Logger.getLogger(CMSSecurePortalRestrictionEvaluator.class);
	private CMSSiteService cmsSiteService;

	@Override
	public boolean evaluate(final CMSSecurePortalRestrictionModel cmsUserRestriction, final RestrictionData context)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("isEnableRegistration: " + cmsSiteService.getCurrentSite().isEnableRegistration());
		}
		return cmsSiteService.getCurrentSite().isEnableRegistration();
	}

	protected CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	@Required
	public void setCmsSiteService(CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

}
