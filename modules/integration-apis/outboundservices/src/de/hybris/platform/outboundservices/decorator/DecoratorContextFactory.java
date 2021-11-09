/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator;

import de.hybris.platform.outboundservices.facade.SyncParameters;

import javax.validation.constraints.NotNull;

/**
 * A factory for creating {@link DecoratorContext} instances.
 */
public interface DecoratorContextFactory
{
	/**
	 * Creates item synchronization context based on the parameters submitted.
	 *
	 * @param params parameters to feed the context creation.
	 * @return a context to be used by the decorators that has its state initialized according to the parameters provided.
	 */
	@NotNull DecoratorContext createContext(@NotNull SyncParameters params);
}
