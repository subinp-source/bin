/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config;

/**
 * This interface defines how to generate a {@link String} identifier for each model that is associated with the creation of an OutboundSyncChannelConfiguration.
 *
 * @param <P> - type of the object that helps derive the identifier.
 */
public interface IdentifierGenerator<P>
{
	/**
	 * Derives an identifier to be set on a model
	 *
	 * @param object generic item to help derive a name that will be returned
	 * @return an identifier for a model to associate it with a channel
	 */
	String generate(P object);
}
