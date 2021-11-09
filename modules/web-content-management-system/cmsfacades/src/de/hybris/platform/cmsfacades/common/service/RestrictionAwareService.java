/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.service;

import de.hybris.platform.cms2.servicelayer.data.RestrictionData;
import java.util.function.Supplier;


/**
 * Sets restriction information in place during the execution of a {@link Supplier}.
 */
public interface RestrictionAwareService
{
	/**
	 * Makes restriction data available during the execution of the provided supplier.
	 * @param data The restriction data to make available during the execution of the supplier.
	 * @param supplier The supplier to execute.
	 * @param <T> The type returned by the supplier.
	 * @return The value calculated by the supplier.
	 */
	<T> T execute(RestrictionData data, Supplier<T> supplier);
}
