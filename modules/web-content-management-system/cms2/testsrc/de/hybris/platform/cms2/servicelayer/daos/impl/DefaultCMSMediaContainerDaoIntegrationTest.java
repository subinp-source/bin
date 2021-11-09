/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.daos.impl;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSMediaContainerDao;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSMediaContainerDaoIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private CMSMediaContainerDao cmsMediaContainerDao;

	private CatalogVersionModel catalogVersion;
	private MediaContainerModel defaultContainer;

	@Before
	public void setUp() throws Exception
	{
		super.createCoreData();
		super.createDefaultCatalog();
		catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");

		defaultContainer = createMockMediaContainer("valid-id");
		final MediaContainerModel summerContainer = createMockMediaContainer("summer-id");
		final MediaContainerModel winterContainer = createMockMediaContainer("winter-id");
		modelService.saveAll();
	}

	public MediaContainerModel createMockMediaContainer(final String qualifier)
	{
		final MediaContainerModel model = modelService.create(MediaContainerModel.class);
		model.setCatalogVersion(catalogVersion);
		model.setQualifier(qualifier);
		return model;
	}

	@Test
	public void shouldGetMediaContainerForQualifier()
	{
		final MediaContainerModel result = cmsMediaContainerDao.getMediaContainerForQualifier("valid-id", catalogVersion);

		assertThat(result, equalTo(defaultContainer));
	}

	@Test
	public void shouldFindMediaContainersWithPaging()
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0);
		pageableData.setPageSize(2);

		final SearchResult<MediaContainerModel> result = cmsMediaContainerDao.findMediaContainersForCatalogVersion("id",
				catalogVersion, pageableData);

		assertThat(result, not(nullValue()));
		assertThat(result.getResult(), hasItems( //
				allOf(hasProperty(MediaContainerModel.QUALIFIER, equalTo("summer-id"))),
				allOf(hasProperty(MediaContainerModel.QUALIFIER, equalTo("valid-id"))) //
		));
		assertThat(result.getTotalCount(), greaterThanOrEqualTo(3));
	}

}
