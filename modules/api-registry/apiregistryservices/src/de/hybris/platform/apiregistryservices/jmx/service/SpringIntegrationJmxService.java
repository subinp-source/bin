/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.jmx.service;

/**
 * Service which loads QueueChannel classes from context, enables monitoring and register jmx beans for them.
 */
public interface SpringIntegrationJmxService
{
    /**
     * Register mbeans
     * @param jmxPath suffix for jxm path
     * @param beanInterface for mbean
     */
    void registerAllSpringQueues(final String jmxPath, final Class beanInterface);
}
