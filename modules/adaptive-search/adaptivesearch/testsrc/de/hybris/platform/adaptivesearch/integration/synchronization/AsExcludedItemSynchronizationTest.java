/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.synchronization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class AsExcludedItemSynchronizationTest extends AbstractAsSynchronizationTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";

	private static final String SEARCH_CONFIGURATION_UID = "searchConfiguration";

	private static final String UID1 = "cde588ec-d453-48bd-a3b1-b9aa00402256";
	private static final String UID2 = "28d6cef9-e73f-4b7f-8c69-db5816fe67b6";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private CatalogSynchronizationService catalogSynchronizationService;

	@Resource
	private ProductService productService;

	@Resource
	private AsSearchProfileService asSearchProfileService;

	@Resource
	private AsSearchConfigurationService asSearchConfigurationService;

	@Resource
	private AsConfigurationService asConfigurationService;

	private CatalogVersionModel onlineCatalogVersion;
	private CatalogVersionModel stagedCatalogVersion;
	private AbstractAsConfigurableSearchConfigurationModel searchConfiguration;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/asBase.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchProfile.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchConfiguration.impex", StandardCharsets.UTF_8.name());

		stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final Optional<AbstractAsConfigurableSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(stagedCatalogVersion, SEARCH_CONFIGURATION_UID);
		searchConfiguration = searchConfigurationOptional.orElseThrow();
	}

	@Test
	public void excludedItemNotFoundBeforeSynchronization()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsExcludedItemModel excludedItem = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setUid(UID1);
		excludedItem.setSearchConfiguration(searchConfiguration);
		excludedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(excludedItem);

		final Optional<AsExcludedItemModel> synchronizedExcludedItemOptional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedExcludedItemOptional.isPresent());
	}

	@Test
	public void synchronizeNewExcludedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsExcludedItemModel excludedItem = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setUid(UID1);
		excludedItem.setSearchConfiguration(searchConfiguration);
		excludedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(excludedItem);
		modelService.refresh(excludedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsExcludedItemModel> synchronizedExcludedItemOptional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedExcludedItemOptional.isPresent());

		final AsExcludedItemModel synchronizedExcludedItem = synchronizedExcludedItemOptional.orElseThrow();
		assertFalse(synchronizedExcludedItem.isCorrupted());
		assertSynchronized(excludedItem, synchronizedExcludedItem, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeRemovedExcludedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsExcludedItemModel excludedItem = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setUid(UID1);
		excludedItem.setSearchConfiguration(searchConfiguration);
		excludedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(excludedItem);
		modelService.refresh(excludedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		asConfigurationService.removeConfiguration(excludedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsExcludedItemModel> synchronizedExcludedItemOptional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedExcludedItemOptional.isPresent());
	}

	@Test
	public void synchronizeAfterRemovingItemForNewExcludedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsExcludedItemModel excludedItem = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setUid(UID1);
		excludedItem.setSearchConfiguration(searchConfiguration);
		excludedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(excludedItem);
		modelService.remove(product);
		modelService.refresh(excludedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsExcludedItemModel> synchronizedExcludedItemOptional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedExcludedItemOptional.isPresent());

		final AsExcludedItemModel synchronizedExcludedItem = synchronizedExcludedItemOptional.orElseThrow();
		assertTrue(synchronizedExcludedItem.isCorrupted());
		assertSynchronized(excludedItem, synchronizedExcludedItem, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeAfterRemovingItemForExistingExcludedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsExcludedItemModel excludedItem = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setUid(UID1);
		excludedItem.setSearchConfiguration(searchConfiguration);
		excludedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(excludedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		modelService.remove(product);
		modelService.refresh(excludedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsExcludedItemModel> synchronizedExcludedItemOptional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedExcludedItemOptional.isPresent());

		final AsExcludedItemModel synchronizedExcludedItem = synchronizedExcludedItemOptional.orElseThrow();
		assertTrue(synchronizedExcludedItem.isCorrupted());
		assertSynchronized(excludedItem, synchronizedExcludedItem, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeAfterRemovingItemForMultipleExistingExcludedItems()
	{
		// given
		final ProductModel product1 = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);
		final ProductModel product2 = productService.getProductForCode(stagedCatalogVersion, PRODUCT2_CODE);

		final AsExcludedItemModel excludedItem1 = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem1.setCatalogVersion(stagedCatalogVersion);
		excludedItem1.setUid(UID1);
		excludedItem1.setSearchConfiguration(searchConfiguration);
		excludedItem1.setItem(product1);

		final AsExcludedItemModel excludedItem2 = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem2.setCatalogVersion(stagedCatalogVersion);
		excludedItem2.setUid(UID2);
		excludedItem2.setSearchConfiguration(searchConfiguration);
		excludedItem2.setItem(product2);

		// when
		asConfigurationService.saveConfiguration(excludedItem1);
		asConfigurationService.saveConfiguration(excludedItem2);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		modelService.remove(product1);
		modelService.remove(product2);
		modelService.refresh(excludedItem1);
		modelService.refresh(excludedItem2);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsExcludedItemModel> synchronizedExcludedItem1Optional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID1);

		final Optional<AsExcludedItemModel> synchronizedExcludedItem2Optional = asConfigurationService
				.getConfigurationForUid(AsExcludedItemModel.class, onlineCatalogVersion, UID2);

		// then
		assertTrue(synchronizedExcludedItem1Optional.isPresent());

		final AsExcludedItemModel synchronizedExcludedItem1 = synchronizedExcludedItem1Optional.orElseThrow();
		assertTrue(synchronizedExcludedItem1.isCorrupted());
		assertSynchronized(excludedItem1, synchronizedExcludedItem1, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);

		assertTrue(synchronizedExcludedItem2Optional.isPresent());

		final AsExcludedItemModel synchronizedExcludedItem2 = synchronizedExcludedItem2Optional.orElseThrow();
		assertTrue(synchronizedExcludedItem2.isCorrupted());
		assertSynchronized(excludedItem2, synchronizedExcludedItem2, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}
}
