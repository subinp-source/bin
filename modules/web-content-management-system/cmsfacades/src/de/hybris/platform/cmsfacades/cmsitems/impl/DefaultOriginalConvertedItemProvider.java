/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.impl;

import de.hybris.platform.cms2.common.service.SessionCachedContextProvider;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemContextProvider;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Provider responsible for storing (in a stack-like data structure) context information per transaction.
 * It contains map representation of the item model.
 */
public class DefaultOriginalConvertedItemProvider implements CMSItemContextProvider<Map<String, Object>>
{
	private SessionCachedContextProvider sessionCachedContextProvider;
	private static final String SESSION_ORIGINAL_CONVERTED_ITEM = "sessionOriginalConvertedItem";

	@Override
	public void initializeItem(final Map<String, Object> item)
	{
		getSessionCachedContextProvider().addItemToListCache(SESSION_ORIGINAL_CONVERTED_ITEM, item);
	}

	@Override
	public Map<String, Object> getCurrentItem()
	{
		final List<Object> originalItemSet = getSessionCachedContextProvider().getAllItemsFromListCache(SESSION_ORIGINAL_CONVERTED_ITEM);
		if (originalItemSet.size() == 0)
		{
			throw new IllegalStateException("There is no current item model. Please Initialize with #initializeItem before using this method.");
		}
		return (Map<String, Object>) originalItemSet.stream().skip(originalItemSet.size() - 1).findFirst().get();
	}

	@Override
	public void finalizeItem()
	{
		final List<Object> originalItemSet = getSessionCachedContextProvider().getAllItemsFromListCache(SESSION_ORIGINAL_CONVERTED_ITEM);
		if (originalItemSet.size() > 0)
		{
			originalItemSet.remove((originalItemSet.size() - 1));
		}
	}

	protected SessionCachedContextProvider getSessionCachedContextProvider()
	{
		return sessionCachedContextProvider;
	}

	@Required
	public void setSessionCachedContextProvider(
			final SessionCachedContextProvider sessionCachedContextProvider)
	{
		this.sessionCachedContextProvider = sessionCachedContextProvider;
	}
}
