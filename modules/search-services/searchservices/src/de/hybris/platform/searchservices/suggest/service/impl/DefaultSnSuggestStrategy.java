/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.searchservices.suggest.SnSuggestException;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContext;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContextFactory;
import de.hybris.platform.searchservices.suggest.service.SnSuggestListener;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;
import de.hybris.platform.searchservices.suggest.service.SnSuggestStrategy;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * Default implementation for {@link SnSuggestStrategy}.
 */
public class DefaultSnSuggestStrategy implements SnSuggestStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSnSuggestStrategy.class);

	private SnSessionService snSessionService;
	private SnSuggestContextFactory snSuggestContextFactory;
	private SnIndexService snIndexService;
	private SnListenerFactory snListenerFactory;
	private SnSearchProviderFactory snSearchProviderFactory;

	@Override
	public SnSuggestResponse execute(final SnSuggestRequest suggestRequest) throws SnSuggestException
	{
		validateSuggestRequest(suggestRequest);

		LOG.debug("Suggest operation started");

		final SnSuggestContext suggestContext = snSuggestContextFactory.createSuggestContext(suggestRequest);
		final List<SnSuggestListener> listeners = snListenerFactory.getListeners(suggestContext, SnSuggestListener.class);

		try
		{
			snSessionService.initializeSession();

			final String indexId = snIndexService.getDefaultIndexId(suggestRequest.getIndexTypeId());
			suggestContext.setIndexId(indexId);

			executeBeforeSuggestListeners(suggestContext, listeners);

			final SnSuggestResponse suggestResponse = doExecute(suggestContext);
			suggestContext.setSuggestResponse(suggestResponse);

			executeAfterSuggestListeners(suggestContext, listeners);

			LOG.debug("Suggest operation finished");

			return suggestResponse;
		}
		catch (final SnSuggestException e)
		{
			suggestContext.addException(e);
			executeAfterSuggestErrorListeners(suggestContext, listeners);

			LOG.error("Suggest operation failed", e);

			throw e;
		}
		catch (final SnException | RuntimeException e)
		{
			suggestContext.addException(e);
			executeAfterSuggestErrorListeners(suggestContext, listeners);

			LOG.error("Suggest operation failed", e);

			throw new SnSuggestException(e);
		}
		finally
		{
			snSessionService.destroySession();
		}
	}

	protected void validateSuggestRequest(final SnSuggestRequest suggestRequest) throws SnSuggestException
	{
		validateSuggestQuery(suggestRequest.getSuggestQuery());
	}

	protected void validateSuggestQuery(final SnSuggestQuery suggestQuery) throws SnSuggestException
	{
		if (suggestQuery == null)
		{
			throw new SnSuggestException("Suggest query cannot be null");
		}

		if (StringUtils.isBlank(suggestQuery.getQuery()))
		{
			throw new SnSuggestException("Suggest query cannot be null or empty");
		}
	}

	protected void executeBeforeSuggestListeners(final SnSuggestContext suggestContext, final List<SnSuggestListener> listeners)
			throws SnException
	{
		for (final SnSuggestListener listener : listeners)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.beforeSuggest ...", listener.getClass().getCanonicalName());
			}

			listener.beforeSuggest(suggestContext);
		}
	}

	protected void executeAfterSuggestListeners(final SnSuggestContext suggestContext, final List<SnSuggestListener> listeners)
			throws SnException
	{
		for (final SnSuggestListener listener : Lists.reverse(listeners))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.afterSuggest ...", listener.getClass().getCanonicalName());
			}

			listener.afterSuggest(suggestContext);
		}
	}

	protected void executeAfterSuggestErrorListeners(final SnSuggestContext suggestContext,
			final List<SnSuggestListener> listeners)
	{
		for (final SnSuggestListener listener : Lists.reverse(listeners))
		{
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Running {}.afterSuggestError ...", listener.getClass().getCanonicalName());
				}

				listener.afterSuggestError(suggestContext);
			}
			catch (final SnSuggestException | RuntimeException exception)
			{
				suggestContext.addException(exception);
			}
		}
	}

	protected SnSuggestResponse doExecute(final SnSuggestContext suggestContext) throws SnException
	{
		final SnSuggestRequest suggestRequest = suggestContext.getSuggestRequest();

		final SnSearchProvider searchProvider = snSearchProviderFactory.getSearchProviderForContext(suggestContext);
		final SnSuggestResult suggestResult = searchProvider.suggest(suggestContext, suggestContext.getIndexId(),
				suggestRequest.getSuggestQuery());

		return createSuggestResponse(suggestContext, suggestResult);
	}

	protected SnSuggestResponse createSuggestResponse(final SnSuggestContext suggestContext, final SnSuggestResult suggestResult)
	{
		final DefaultSnSuggestResponse suggestResponse = new DefaultSnSuggestResponse(suggestContext.getIndexConfiguration(),
				suggestContext.getIndexType());
		suggestResponse.setSuggestResult(suggestResult);

		return suggestResponse;
	}

	public SnSessionService getSnSessionService()
	{
		return snSessionService;
	}

	@Required
	public void setSnSessionService(final SnSessionService snSessionService)
	{
		this.snSessionService = snSessionService;
	}

	public SnSuggestContextFactory getSnSuggestContextFactory()
	{
		return snSuggestContextFactory;
	}

	@Required
	public void setSnSuggestContextFactory(final SnSuggestContextFactory snSuggestContextFactory)
	{
		this.snSuggestContextFactory = snSuggestContextFactory;
	}

	public SnIndexService getSnIndexService()
	{
		return snIndexService;
	}

	@Required
	public void setSnIndexService(final SnIndexService snIndexService)
	{
		this.snIndexService = snIndexService;
	}

	public SnListenerFactory getSnListenerFactory()
	{
		return snListenerFactory;
	}

	@Required
	public void setSnListenerFactory(final SnListenerFactory snListenerFactory)
	{
		this.snListenerFactory = snListenerFactory;
	}

	public SnSearchProviderFactory getSnSearchProviderFactory()
	{
		return snSearchProviderFactory;
	}

	@Required
	public void setSnSearchProviderFactory(final SnSearchProviderFactory snSearchProviderFactory)
	{
		this.snSearchProviderFactory = snSearchProviderFactory;
	}
}
