/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.items.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.permissions.PermissionCachedCRUDService;
import de.hybris.platform.cmsfacades.data.AbstractCMSComponentData;
import de.hybris.platform.cmsfacades.rendering.ComponentRenderingService;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultComponentItemFacadeTest
{
	private static final String COMPONENT_ID = "componentId1";
	private static final String COMPONENT_ID2 = "componentId2";
	private static final String CATEGORY_CODE = "categoryCode";
	private static final String PRODUCT_CODE = "productCode";
	private static final String CATALOG_CODE = "catalogCode";
	private static final String INVALID = "invalidId";

	@InjectMocks
	private DefaultComponentItemFacade componentFacade;
	@Mock
	private ComponentRenderingService componentRenderingService;

	@Mock
	private AbstractCMSComponentData componentData;
	@Mock
	private SearchPageData searchPageData;
	@Mock
	private PermissionCachedCRUDService permissionCachedCRUDService;

	@Test
	public void shouldGetComponentById() throws CMSItemNotFoundException
	{
		when(componentRenderingService.getComponentById(COMPONENT_ID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE))
				.thenReturn(componentData);

		componentFacade.getComponentById(COMPONENT_ID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE);

		verify(componentRenderingService).getComponentById(COMPONENT_ID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE);
		verify(permissionCachedCRUDService).initCache();
	}

	@Test(expected = CMSItemNotFoundException.class)
	public void shouldFailGetComponentById_InvalidId() throws CMSItemNotFoundException
	{
		when(componentRenderingService.getComponentById(INVALID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE))
				.thenThrow(new CMSItemNotFoundException("invalid component id"));

		componentFacade.getComponentById(INVALID, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE);
	}

	@Test
	public void shouldGetComponentsByIds()
	{
		final List<String> componentIds = Arrays.asList(COMPONENT_ID, COMPONENT_ID2);
		when(componentRenderingService.getComponentsByIds(componentIds, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData))
				.thenReturn(searchPageData);

		componentFacade.getComponentsByIds(componentIds, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData);

		verify(componentRenderingService).getComponentsByIds(componentIds, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE,
				searchPageData);
		verify(permissionCachedCRUDService).initCache();
	}

	@Test
	public void shouldGetAllComponentsForNullIdList()
	{
		when(componentRenderingService.getAllComponents(CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData))
				.thenReturn(searchPageData);

		componentFacade.getComponentsByIds(null, CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData);

		verify(componentRenderingService).getAllComponents(CATEGORY_CODE, PRODUCT_CODE, CATALOG_CODE, searchPageData);
		verify(permissionCachedCRUDService).initCache();
	}
}
