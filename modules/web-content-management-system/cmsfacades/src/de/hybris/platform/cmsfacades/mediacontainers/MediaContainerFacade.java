/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.mediacontainers;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.MediaContainerData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * Facade for managing media containers.
 */
public interface MediaContainerFacade
{

	/**
	 * Creates a new Media Container with a sequential qualifier name.
	 *
	 * @return a new {@link MediaContainerModel}
	 */
	MediaContainerModel createMediaContainer();

	/**
	 * Creates a new Media Container with the given qualifier name.
	 *
	 * @return a new {@link MediaContainerModel}
	 */
	MediaContainerModel createMediaContainer(String qualifier);

	/**
	 * Gets a single media container.
	 *
	 * @param qualifier
	 *           - the identifier of the media container to retrieve
	 * @return media container
	 * @throws CMSItemNotFoundException
	 *            when the media container could not be found
	 */
	MediaContainerData getMediaContainerForQualifier(String qualifier) throws CMSItemNotFoundException;

	/**
	 * Finds media containers using a free-text form. It also supports pagination.
	 *
	 * @param text
	 *           The free-text string to be used on the media container search
	 * @param pageableData
	 *           the pagination object
	 * @return the search result object.
	 */
	SearchResult<MediaContainerData> findMediaContainers(String text, PageableData pageableData);
}
