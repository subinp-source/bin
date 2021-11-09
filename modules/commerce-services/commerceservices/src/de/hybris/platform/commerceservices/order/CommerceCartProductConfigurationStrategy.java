/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * Commerce cart strategy to update order entry's configurations.
 */
public interface CommerceCartProductConfigurationStrategy
{
    /**
     * Update configuration on a configurable product in given order entry.
     *
     * @param parameters configuration data
     * @throws CommerceCartModificationException in case of invalid parameters
     */
    void configureCartEntry(CommerceCartParameter parameters) throws CommerceCartModificationException;
}
