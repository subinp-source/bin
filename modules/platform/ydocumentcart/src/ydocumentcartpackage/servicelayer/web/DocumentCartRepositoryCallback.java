/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.servicelayer.web;

import de.hybris.platform.servicelayer.web.PolyglotPersistenceCallbackFilter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.CachedDocumentStorage;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.cache.StorageCache;

public class DocumentCartRepositoryCallback implements PolyglotPersistenceCallbackFilter.PolyglotCallback
{
	private final CachedDocumentStorage storage;

	public DocumentCartRepositoryCallback(final CachedDocumentStorage storage)
	{
		this.storage = storage;
	}

	@Override
	public void call(final ServletRequest servletRequest, final ServletResponse servletResponse,
	                 final PolyglotPersistenceCallbackFilter.CallbackCaller nextCaller) throws IOException, ServletException
	{
		try (final StorageCache.CacheContext cacheContext = storage.initCacheContext())
		{
			nextCaller.call(servletRequest, servletResponse);

			cacheContext.markAsSuccess();
		}
	}
}
