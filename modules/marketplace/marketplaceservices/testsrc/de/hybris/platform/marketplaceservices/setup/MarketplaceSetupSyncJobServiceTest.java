/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.marketplaceservices.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.commerceservices.setup.SetupSyncJobService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Test suite for {@link MarketplaceSetupSyncJobService}
 */
@IntegrationTest
public class MarketplaceSetupSyncJobServiceTest extends ServicelayerTest
{
	private static final String TEST_GLOBAL_MARKETPLACE_PRODUCT_CATALOG = "globalMarketplaceProductCatalog";
	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";

	@Resource
	private SetupSyncJobService setupSyncJobService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private ProductService productService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/marketplaceservices/test/testSystemSetup.impex", "utf8");
	}

	@Test
	public void testCreateProductCatalogSyncJob()
	{
		setupSyncJobService.createProductCatalogSyncJob(TEST_GLOBAL_MARKETPLACE_PRODUCT_CATALOG);
		assertNotNull("Catalog was null", getCatalogSyncJob(TEST_GLOBAL_MARKETPLACE_PRODUCT_CATALOG));
	}

	@Test
	public void testExecuteCatalogSyncJob()
	{
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(TEST_GLOBAL_MARKETPLACE_PRODUCT_CATALOG,
				CatalogManager.ONLINE_VERSION);
		setupSyncJobService.createProductCatalogSyncJob(TEST_GLOBAL_MARKETPLACE_PRODUCT_CATALOG);
		final PerformResult result = setupSyncJobService.executeCatalogSyncJob(TEST_GLOBAL_MARKETPLACE_PRODUCT_CATALOG);

		final ProductModel product1 = productService.getProductForCode(catalogVersion, PRODUCT1_CODE);
		final ProductModel product2 = productService.getProductForCode(catalogVersion, PRODUCT2_CODE);
		assertNotNull(product1);
		assertNotNull(product2);
		final long product1OnlineCategories = product1.getSupercategories().stream()
				.filter(category -> CatalogManager.ONLINE_VERSION.equals(category.getCatalogVersion().getVersion())).count();
		final long product2OnlineCategories = product2.getSupercategories().stream()
				.filter(category -> CatalogManager.ONLINE_VERSION.equals(category.getCatalogVersion().getVersion())).count();
		assertEquals(2, product1OnlineCategories);
		assertEquals(1, product2OnlineCategories);
		assertEquals(CronJobResult.SUCCESS, result.getResult());
		assertEquals(CronJobStatus.FINISHED, result.getStatus());
	}

	protected SyncItemJobModel getCatalogSyncJob(final String catalogId)
	{
		final List<SyncItemJobModel> synchronizations = catalogVersionService.getCatalogVersion(catalogId,
				CatalogManager.OFFLINE_VERSION).getSynchronizations();
		if (CollectionUtils.isNotEmpty(synchronizations))
		{
			return synchronizations.get(0);
		}
		return null;
	}
}
