/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.index.service.SnIndexService;
import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import de.hybris.platform.searchservices.search.service.SnSearchContextFactory;
import de.hybris.platform.searchservices.search.service.SnSearchListener;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;
import de.hybris.platform.searchservices.search.service.SnSearchStrategy;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * Default implementation for {@link SnSearchStrategy}.
 */
public class DefaultSnSearchStrategy implements SnSearchStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSnSearchStrategy.class);

	private SnSessionService snSessionService;
	private SnSearchContextFactory snSearchContextFactory;
	private SnIndexService snIndexService;
	private SnListenerFactory snListenerFactory;
	private SnSearchProviderFactory snSearchProviderFactory;


	@Override
	public SnSearchResponse execute(final SnSearchRequest searchRequest) throws SnSearchException
	{
		validateSearchRequest(searchRequest);

		LOG.debug("Search operation started");

		final SnSearchContext searchContext = snSearchContextFactory.createSearchContext(searchRequest);
		final List<SnSearchListener> listeners = snListenerFactory.getListeners(searchContext, SnSearchListener.class);

		try
		{
			snSessionService.initializeSession();

			final String indexId = snIndexService.getDefaultIndexId(searchRequest.getIndexTypeId());
			searchContext.setIndexId(indexId);

			executeBeforeSearchListeners(searchContext, listeners);

			final SnSearchResponse searchResponse = doExecute(searchContext);
			searchContext.setSearchResponse(searchResponse);

			executeAfterSearchListeners(searchContext, listeners);

			LOG.debug("Search operation finished");

			return searchResponse;
		}
		catch (final SnSearchException e)
		{
			searchContext.addException(e);
			executeAfterSearchErrorListeners(searchContext, listeners);

			LOG.error("Search operation failed", e);

			throw e;
		}
		catch (final SnException | RuntimeException e)
		{
			searchContext.addException(e);
			executeAfterSearchErrorListeners(searchContext, listeners);

			LOG.error("Search operation failed", e);

			throw new SnSearchException(e);
		}
		finally
		{
			snSessionService.destroySession();
		}
	}

	protected void validateSearchRequest(final SnSearchRequest searchRequest) throws SnSearchException
	{
		validateSearchQuery(searchRequest.getSearchQuery());
	}

	protected void validateSearchQuery(final SnSearchQuery searchQuery) throws SnSearchException
	{
		if (searchQuery == null)
		{
			throw new SnSearchException("Search query cannot be null");
		}
	}

	protected void executeBeforeSearchListeners(final SnSearchContext searchContext, final List<SnSearchListener> listeners)
			throws SnException
	{
		for (final SnSearchListener listener : listeners)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.beforeSearch ...", listener.getClass().getCanonicalName());
			}

			listener.beforeSearch(searchContext);
		}
	}

	protected void executeAfterSearchListeners(final SnSearchContext searchContext, final List<SnSearchListener> listeners)
			throws SnException
	{
		for (final SnSearchListener listener : Lists.reverse(listeners))
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Running {}.afterSearch ...", listener.getClass().getCanonicalName());
			}

			listener.afterSearch(searchContext);
		}
	}

	protected void executeAfterSearchErrorListeners(final SnSearchContext searchContext, final List<SnSearchListener> listeners)
	{
		for (final SnSearchListener listener : Lists.reverse(listeners))
		{
			try
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Running {}.afterSearchError ...", listener.getClass().getCanonicalName());
				}

				listener.afterSearchError(searchContext);
			}
			catch (final SnSearchException | RuntimeException exception)
			{
				searchContext.addException(exception);
			}
		}
	}

	protected SnSearchResponse doExecute(final SnSearchContext searchContext) throws SnException
	{
		final SnSearchRequest searchRequest = searchContext.getSearchRequest();
		final SnSearchProvider searchProvider = snSearchProviderFactory.getSearchProviderForContext(searchContext);
		final SnSearchResult searchResult = searchProvider.search(searchContext, searchContext.getIndexId(),
				searchRequest.getSearchQuery());
		return createSearchResponse(searchContext, searchResult);
	}

	protected SnSearchResponse createSearchResponse(final SnSearchContext searchContext, final SnSearchResult searchResult)
	{
		final DefaultSnSearchResponse searchResponse = new DefaultSnSearchResponse(searchContext.getIndexConfiguration(),
				searchContext.getIndexType());
		searchResponse.setSearchResult(searchResult);

		return searchResponse;
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

	public SnSearchContextFactory getSnSearchContextFactory()
	{
		return snSearchContextFactory;
	}

	@Required
	public void setSnSearchContextFactory(final SnSearchContextFactory snSearchContextFactory)
	{
		this.snSearchContextFactory = snSearchContextFactory;
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
