/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout;

import java.util.concurrent.Callable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * The TimeoutService provides a convenient way to execute some logic within a given period of time.
 */
public interface TimeoutService
{
	/**
	 * Executes the {@link Callable} and wait until the timeout period expires.
	 * When the timeout occurred the implementation may decide to return a value
	 * indicating so, or an exception is thrown.
	 *
	 * @param callable The logic to execute
	 * @param timeout  The period to wait for the execution to complete.
	 *                 Negative value should result in an {@link IntegrationTimeoutException}.
	 *                 The implementation can decide what to do with 0. It can throw an
	 *                 IntegrationTimeoutException, or equate 0 to wait indefinitely.
	 * @param <V>      The type of the return value from the Callable
	 * @return The value returned from the Callable
	 */
	<V> V execute(@NotNull Callable<V> callable, @PositiveOrZero long timeout);
}
