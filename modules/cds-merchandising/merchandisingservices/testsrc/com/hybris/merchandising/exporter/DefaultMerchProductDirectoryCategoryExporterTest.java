/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.exporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.hybris.merchandising.client.CategoryHierarchyWrapper;
import com.hybris.merchandising.client.MerchCatalogServiceProductDirectoryClient;
import com.hybris.merchandising.dao.MerchProductDirectoryConfigDao;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.service.MerchCatalogService;
import com.hybris.merchandising.service.impl.DefaultMerchProductDirectoryConfigService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.site.BaseSiteService;

/**
 * Test suite for {@link DefaultMerchProductDirectoryCategoryExporter}.
 *
 */
@UnitTest
public class DefaultMerchProductDirectoryCategoryExporterTest {
	DefaultMerchProductDirectoryCategoryExporter exporter;

	public static final String APPAREL_UK = "apparel-uk";
	public static final String ELECTRONICS = "electronics";
	public static final String CATALOG_ID = "123";
	public static final String CATALOG_VERSION_ID = "live";
	public static final String BASE_CAT_URL = "https://hybris.com";
	public static final String CDS_IDENTIFIER = "cds001";

	public static final Long ITEM_CHANGE_PK = Long.valueOf(1234);
	public static final String ITEM_CHANGE_INFO = "INFO";

	private BaseSiteModel baseSite;
	private MerchCatalogServiceProductDirectoryClient client;
	private MerchCatalogService catService;
	private BaseSiteService baseSiteService;
	private DefaultMerchProductDirectoryConfigService configService;

	@Before
	public void setUp() {
		exporter = new DefaultMerchProductDirectoryCategoryExporter();

		baseSiteService = Mockito.mock(BaseSiteService.class);
		baseSite = Mockito.mock(BaseSiteModel.class);
		Mockito.when(baseSite.getUid()).thenReturn(APPAREL_UK);
		Mockito.when(baseSite.getName()).thenReturn(APPAREL_UK);
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSite);
		exporter.setBaseSiteService(baseSiteService);

		final MerchProductDirectoryConfigDao configDao = Mockito.mock(MerchProductDirectoryConfigDao.class);
		final Collection<MerchProductDirectoryConfigModel> config = new ArrayList<>();
		config.add(getMockConfiguration(true));
		Mockito.when(configDao.findAllMerchProductDirectoryConfigs()).thenReturn(config);

		catService = Mockito.mock(MerchCatalogService.class);
		exporter.setMerchCatalogService(catService);
		client = Mockito.mock(MerchCatalogServiceProductDirectoryClient.class);
		exporter.setClient(client);

		configService = new DefaultMerchProductDirectoryConfigService();
		configService.setMerchProductDirectoryConfigDao(configDao);
		exporter.setMerchProductDirectoryConfigService(configService);
	}

	@Test
	public void testGetMerchCatalogService() 
	{
		final MerchCatalogService retrieved = exporter.getMerchCatalogService();
		Assert.assertEquals("Expected catalog service to be the same", catService, retrieved);
	}

	@Test
	public void testGetBaseSiteService() 
	{
		final BaseSiteService baseSiteServiceToSet = Mockito.mock(BaseSiteService.class);
		exporter.setBaseSiteService(baseSiteServiceToSet);
		final BaseSiteService retrieved = exporter.getBaseSiteService();
		Assert.assertEquals("Expected base site service to be the same", baseSiteServiceToSet, retrieved);
	}

	@Test
	public void testGetClient() 
	{
		final MerchCatalogServiceProductDirectoryClient client = Mockito.mock(MerchCatalogServiceProductDirectoryClient.class);
		exporter.setClient(client);
		final MerchCatalogServiceProductDirectoryClient retrieved = exporter.getClient();
		Assert.assertEquals("Expected client to be the same",  client, retrieved);
	}

	@Test
	public void testPerform() 
	{
		final InMemoryChangesCollector collector = Mockito.mock(InMemoryChangesCollector.class);
		final ItemChangeDTO changedItem = Mockito.mock(ItemChangeDTO.class);
		Mockito.when(changedItem.getItemPK()).thenReturn(ITEM_CHANGE_PK);
		Mockito.when(changedItem.getInfo()).thenReturn(ITEM_CHANGE_INFO);
		final List<ItemChangeDTO> changes = new ArrayList<>();
		changes.add(changedItem);
		Mockito.when(collector.getChanges()).thenReturn(changes);

		final BaseSiteService mockBaseSiteService = Mockito.mock(BaseSiteService.class);
		exporter.setBaseSiteService(mockBaseSiteService);
		final TypeService mockTypeService = Mockito.mock(TypeService.class);
		exporter.setTypeService(mockTypeService);

		final ChangeDetectionService mockChangeDetectionService = Mockito.mock(ChangeDetectionService.class);
		exporter.setChangeDetectionService(mockChangeDetectionService);

		exporter.perform(collector);
	}

	@Test
	public void testPerformNoChanges()
	{
		final InMemoryChangesCollector collector = Mockito.mock(InMemoryChangesCollector.class);
		final List<ItemChangeDTO> changes = new ArrayList<>();
	
		Mockito.when(collector.getChanges()).thenReturn(changes);

		final TypeService mockTypeService = Mockito.mock(TypeService.class);
		exporter.setTypeService(mockTypeService);

		final ChangeDetectionService mockChangeDetectionService = Mockito.mock(ChangeDetectionService.class);
		exporter.setChangeDetectionService(mockChangeDetectionService);
		exporter.perform(collector);
	}

	@Test
	public void testGetTypeService() 
	{
		final TypeService mockTypeService = Mockito.mock(TypeService.class);
		exporter.setTypeService(mockTypeService);
		final TypeService retrievedTypeService = exporter.getTypeService();
		Assert.assertEquals("Expected type service to be the same", mockTypeService, retrievedTypeService);
	}

	@Test
	public void testGetChangeDetectionService() 
	{
		final ChangeDetectionService cdService = Mockito.mock(ChangeDetectionService.class);
		exporter.setChangeDetectionService(cdService);
		final ChangeDetectionService retrievedCDService = exporter.getChangeDetectionService();
		Assert.assertEquals("Expected change deletion service to be the same", cdService, retrievedCDService);
	}

	@Test
	public void testExportCategoriesForCurrentBaseSite()
	{
		exporter.exportCategoriesForCurrentBaseSite();
		Mockito.verify(client).handleCategories(Mockito.eq(CDS_IDENTIFIER), Mockito.any(CategoryHierarchyWrapper.class));
	}

	private MerchProductDirectoryConfigModel getMockConfiguration(final boolean enabled)
	{
		final MerchProductDirectoryConfigModel config = Mockito.mock(MerchProductDirectoryConfigModel.class);
		Mockito.when(config.isEnabled()).thenReturn(enabled);
		Mockito.when(config.getCdsIdentifier()).thenReturn(CDS_IDENTIFIER);
		Mockito.when(config.getBaseSites()).thenReturn(Arrays.asList(baseSite));
		return config;
	}
}
