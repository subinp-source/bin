/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.entitlementservices.entitlement;

import de.hybris.platform.entitlementservices.model.EntitlementModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

/**
 * Entitlement service that exposes methods to deal with entitlement operations.
 *
 * @spring.bean entitlementService
 */
public interface EntitlementService
{

    /**
     * This method returns the entitlement by entitlement id <code>entitlementId</code>
     *
     * @param entitlementId
     *           Entitlement Id
     * @return EntitlementModel {@link EntitlementModel}
     * @throws de.hybris.platform.servicelayer.exceptions.ModelNotFoundException if nothing was found
     */
    EntitlementModel getEntitlementForCode(final String entitlementId) throws ModelNotFoundException;
}
