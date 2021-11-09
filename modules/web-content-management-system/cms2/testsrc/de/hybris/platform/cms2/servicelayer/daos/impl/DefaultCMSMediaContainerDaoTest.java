/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSMediaContainerDaoTest
{
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private ModelService modelService;
	@InjectMocks
	private DefaultCMSMediaContainerDao mediaContainerDao;

	@Mock
	private CatalogVersionModel catalogVersion;
	@Mock
	private PageableData pageableData;
	@Mock
	private SearchResult<MediaContainerModel> searchResult;
	@Mock
	private MediaContainerModel container1;
	@Mock
	private MediaContainerModel container2;

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailGetMediaContainerWithNullQualifier()
	{
		mediaContainerDao.getMediaContainerForQualifier(null, catalogVersion);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldFailGetMediaContainerWithUnknownQualifier()
	{
		doReturn(searchResult).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));
		doReturn(Collections.emptyList()).when(searchResult).getResult();

		mediaContainerDao.getMediaContainerForQualifier("invalid-id", catalogVersion);
	}

	@Test(expected = AmbiguousIdentifierException.class)
	public void shouldFailGetMediaContainerWithAmbiguousQualifier()
	{
		doReturn(searchResult).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));
		doReturn(Arrays.asList(container1, container2)).when(searchResult).getResult();

		mediaContainerDao.getMediaContainerForQualifier("ambiguous-id", catalogVersion);
	}

	@Test
	public void shouldGetMediaContainerForQualifier()
	{
		doReturn(searchResult).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));
		doReturn(Arrays.asList(container1)).when(searchResult).getResult();

		final MediaContainerModel result = mediaContainerDao.getMediaContainerForQualifier("valid-id", catalogVersion);

		assertThat(result, not(nullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailFindMediaContainersWithNullText()
	{
		mediaContainerDao.findMediaContainersForCatalogVersion(null, catalogVersion, pageableData);
	}

	@Test
	public void shouldFindMediaContainers()
	{
		doReturn(searchResult).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));
		doReturn(Arrays.asList(container1)).when(searchResult).getResult();

		final SearchResult<MediaContainerModel> result = mediaContainerDao.findMediaContainersForCatalogVersion("valid",
				catalogVersion, pageableData);

		assertThat(result, not(nullValue()));
	}

}
