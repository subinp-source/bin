/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent;

/**
 * Facade interface providing an API for performing operations on consents of customers.
 */
public interface CustomerConsentDataStrategy
{
    /**
     * Injects the customer consents into the session as a Hashmap.
     */
    void populateCustomerConsentDataInSession();
}
