/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.context;

/**
 * Interface for service to expose ContextRepository.
 *
 */
public interface ContextService
{
	/**
	 * Returns the configured {@code ContextRepository}.
	 * 
	 * @return the configured {@code ContextRepository}.
	 */
	ContextRepository getContextRepository();
}
