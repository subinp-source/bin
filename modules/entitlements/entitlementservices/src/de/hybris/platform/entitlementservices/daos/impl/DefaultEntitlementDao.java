/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.daos.impl;

import de.hybris.platform.entitlementservices.daos.EntitlementDao;
import de.hybris.platform.entitlementservices.model.EntitlementModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Default implementation of the {@link EntitlementDao}.
 */
public class DefaultEntitlementDao extends AbstractItemDao implements EntitlementDao
{

    @Override
    public EntitlementModel findEntitlementByCode( final String id) throws ModelNotFoundException
    {

        validateParameterNotNull(id, "Entitlement id must not be null");

        final EntitlementModel example = new EntitlementModel();
        example.setId(id);

        return getFlexibleSearchService().getModelByExample(example);
    }
}
