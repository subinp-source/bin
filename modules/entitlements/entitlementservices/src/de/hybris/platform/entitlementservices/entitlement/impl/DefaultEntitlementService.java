/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.entitlement.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.entitlementservices.daos.EntitlementDao;
import de.hybris.platform.entitlementservices.entitlement.EntitlementService;
import de.hybris.platform.entitlementservices.model.EntitlementModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link EntitlementService}.
 *
 */
public class DefaultEntitlementService implements EntitlementService
{
	private EntitlementDao entitlementDao;

	@Override
	public EntitlementModel getEntitlementForCode(final String entitlementId)
	{

		validateParameterNotNull(entitlementId, "Entitlement id cannot be null");
		return getEntitlementDao().findEntitlementByCode(entitlementId);
	}

	protected EntitlementDao getEntitlementDao()
	{
		return entitlementDao;
	}

	@Required
	public void setEntitlementDao(final EntitlementDao entitlementDao)
	{
		this.entitlementDao = entitlementDao;
	}
}
