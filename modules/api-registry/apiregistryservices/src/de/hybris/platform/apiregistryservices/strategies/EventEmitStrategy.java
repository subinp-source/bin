/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies;

/**
 * Strategy interface for sending event payload
 */
public interface EventEmitStrategy
{
    /**
     * @param payload
     */
    void sendEvent(Object payload);
}
