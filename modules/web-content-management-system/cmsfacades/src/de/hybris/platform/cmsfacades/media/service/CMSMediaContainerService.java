/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * Service layer to manage media containers.
 */
public interface CMSMediaContainerService
{
	/**
	 * Retrieve a single media container that has the specified qualifier value in a given catalog version.
	 *
	 * @param qualifier
	 *           the identifier of the media container to find
	 * @param catalogVersion
	 *           the catalog version that is active in the session
	 * @return media container matching the specified qualifier
	 * @throws IllegalArgumentException
	 *            when qualifier is <tt>null</tt>
	 * @throws UnknownIdentifierException
	 *            when no media container is found for the specified qualifier
	 * @throws AmbiguousIdentifierException
	 *            when multiple media containers are found for the specified qualifier
	 */
	MediaContainerModel getMediaContainerForQualifier(String qualifier, CatalogVersionModel catalogVersion);

	/**
	 * Finds media containers using a free-text form in a given catalog version. It also supports pagination.
	 *
	 * @param text
	 *           The free-text string to be used on the media container search
	 * @param catalogVersion
	 *           The catalog version that is active in the session
	 * @param pageableData
	 *           the pagination object
	 * @return the search result object.
	 */
	SearchResult<MediaContainerModel> findMediaContainersForCatalogVersion(String text, CatalogVersionModel catalogVersion,
			PageableData pageableData);
}
