/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;


import de.hybris.platform.catalog.enums.ConfiguratorType;

/**
 * Keeping configuration handlers.
 */
public interface ProductConfigurationHandlerFactory
{
    /**
     * Find {@code ProductConfigurationHandler} responsible for given configuration type.
     * @param configuratorType configuration type
     * @return handler of {@code null} if there is no handler registered for this configuration type.
     */
    ProductConfigurationHandler handlerOf(ConfiguratorType configuratorType);
}
