/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util.timeout.impl;

import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.util.timeout.IntegrationExecutionException;
import de.hybris.platform.integrationservices.util.timeout.IntegrationTimeoutException;
import de.hybris.platform.integrationservices.util.timeout.TimeoutService;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

/**
 * Default implementation of the {@link TimeoutService}
 */
public class DefaultTimeoutService implements TimeoutService
{
	private static final Logger LOGGER = Log.getLogger(DefaultTimeoutService.class);
	private static final String EXECUTION_ID_TEMPLATE = "IntegrationServices.DefaultTimeoutService-%s";

	/**
	 * {@inheritDoc}
	 * A timeout value less than 1 ms will timeout immediately, and an {@link IntegrationTimeoutException} will be thrown.
	 */
	@Override
	public <V> V execute(final Callable<V> callable, final long timeout)
	{
		validate(callable, timeout);

		try
		{
			LOGGER.debug("Starting to execute callable with a timeout of {} ms", timeout);
			final var timeLimiter = getTimeLimiter(timeout);
			return timeLimiter.executeFutureSupplier(execute(callable));
		}
		catch (final TimeoutException e)
		{
			LOGGER.debug("A timeout occurred executing the callable {}", callable, e);
			throw new IntegrationTimeoutException(timeout);
		}
		catch (final Exception e)
		{
			LOGGER.debug("An exception occurred while calling execute on the time limiter", e);
			throw new IntegrationExecutionException(e);
		}
	}

	private <V> void validate(final Callable<V> callable, final long timeout)
	{
		Preconditions.checkArgument(callable != null, "Callable must be provided.");
		if (timeout < 1)
		{
			throw new IntegrationTimeoutException(timeout);
		}
	}

	private TimeLimiter getTimeLimiter(final long timeout)
	{
		final var timeLimiterConfig = TimeLimiterConfig.custom()
		                                               .cancelRunningFuture(true)
		                                               .timeoutDuration(Duration.ofMillis(timeout))
		                                               .build();
		return TimeLimiter.of(generateExecutionId(), timeLimiterConfig);
	}

	private String generateExecutionId()
	{
		return String.format(EXECUTION_ID_TEMPLATE, UUID.randomUUID());
	}

	private <V> Supplier<CompletableFuture<V>> execute(final Callable<V> callable)
	{
		return () -> CompletableFuture.supplyAsync(() -> {
			try
			{
				return callable.call();
			}
			catch (final Exception e)
			{
				LOGGER.debug("An exception occurred while executing the callable {}", callable, e);
				throw new IntegrationExecutionException(e);
			}
		});
	}
}