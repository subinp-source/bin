/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.synchronization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurableSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
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
public class AsPromotedItemSynchronizationTest extends AbstractAsSynchronizationTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";

	private static final String SEARCH_CONFIGURATION_UID = "searchConfiguration";

	private static final String UID1 = "cde588ec-d453-48bd-a3b1-b9aa00402256";
	private static final String UID2 = "0446cd45-3dbf-420b-a105-90c0ea3c0278";

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
	public void promotedItemNotFoundBeforeSynchronization()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsPromotedItemModel promotedItem = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setUid(UID1);
		promotedItem.setSearchConfiguration(searchConfiguration);
		promotedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(promotedItem);

		final Optional<AsPromotedItemModel> synchronizedPromotedItemOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedPromotedItemOptional.isPresent());
	}

	@Test
	public void synchronizeNewPromotedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsPromotedItemModel promotedItem = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setUid(UID1);
		promotedItem.setSearchConfiguration(searchConfiguration);
		promotedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(promotedItem);
		asConfigurationService.refreshConfiguration(promotedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedItemModel> synchronizedPromotedItemOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedPromotedItemOptional.isPresent());

		final AsPromotedItemModel synchronizedPromotedItem = synchronizedPromotedItemOptional.orElseThrow();
		assertFalse(synchronizedPromotedItem.isCorrupted());
		assertSynchronized(promotedItem, synchronizedPromotedItem, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeRemovedPromotedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsPromotedItemModel promotedItem = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setUid(UID1);
		promotedItem.setSearchConfiguration(searchConfiguration);
		promotedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(promotedItem);
		asConfigurationService.refreshConfiguration(promotedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		asConfigurationService.removeConfiguration(promotedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedItemModel> synchronizedPromotedItemOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedPromotedItemOptional.isPresent());
	}

	@Test
	public void synchronizeAfterRemovingItemForNewPromotedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsPromotedItemModel promotedItem = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setUid(UID1);
		promotedItem.setSearchConfiguration(searchConfiguration);
		promotedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(promotedItem);
		modelService.remove(product);
		modelService.refresh(promotedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedItemModel> synchronizedPromotedItemOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedPromotedItemOptional.isPresent());

		final AsPromotedItemModel synchronizedPromotedItem = synchronizedPromotedItemOptional.orElseThrow();
		assertTrue(synchronizedPromotedItem.isCorrupted());
		assertSynchronized(promotedItem, synchronizedPromotedItem, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeAfterRemovingItemForExistingPromotedItem()
	{
		// given
		final ProductModel product = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);

		final AsPromotedItemModel promotedItem = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setUid(UID1);
		promotedItem.setSearchConfiguration(searchConfiguration);
		promotedItem.setItem(product);

		// when
		asConfigurationService.saveConfiguration(promotedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		modelService.remove(product);
		modelService.refresh(promotedItem);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedItemModel> synchronizedPromotedItemOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedPromotedItemOptional.isPresent());

		final AsPromotedItemModel synchronizedPromotedItem = synchronizedPromotedItemOptional.orElseThrow();
		assertTrue(synchronizedPromotedItem.isCorrupted());
		assertSynchronized(promotedItem, synchronizedPromotedItem, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeAfterRemovingItemForMultipleExistingPromotedItems()
	{
		// given
		final ProductModel product1 = productService.getProductForCode(stagedCatalogVersion, PRODUCT1_CODE);
		final ProductModel product2 = productService.getProductForCode(stagedCatalogVersion, PRODUCT2_CODE);

		final AsPromotedItemModel promotedItem1 = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem1.setCatalogVersion(stagedCatalogVersion);
		promotedItem1.setUid(UID1);
		promotedItem1.setSearchConfiguration(searchConfiguration);
		promotedItem1.setItem(product1);

		final AsPromotedItemModel promotedItem2 = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem2.setCatalogVersion(stagedCatalogVersion);
		promotedItem2.setUid(UID2);
		promotedItem2.setSearchConfiguration(searchConfiguration);
		promotedItem2.setItem(product2);

		// when
		asConfigurationService.saveConfiguration(promotedItem1);
		asConfigurationService.saveConfiguration(promotedItem2);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		modelService.remove(product1);
		modelService.remove(product2);
		modelService.refresh(promotedItem1);
		modelService.refresh(promotedItem2);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedItemModel> synchronizedPromotedItem1Optional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID1);

		final Optional<AsPromotedItemModel> synchronizedPromotedItem2Optional = asConfigurationService
				.getConfigurationForUid(AsPromotedItemModel.class, onlineCatalogVersion, UID2);

		// then
		assertTrue(synchronizedPromotedItem1Optional.isPresent());

		final AsPromotedItemModel synchronizedPromotedItem1 = synchronizedPromotedItem1Optional.orElseThrow();
		assertTrue(synchronizedPromotedItem1.isCorrupted());
		assertSynchronized(promotedItem1, synchronizedPromotedItem1, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);

		assertTrue(synchronizedPromotedItem2Optional.isPresent());

		final AsPromotedItemModel synchronizedPromotedItem2 = synchronizedPromotedItem2Optional.orElseThrow();
		assertTrue(synchronizedPromotedItem2.isCorrupted());
		assertSynchronized(promotedItem2, synchronizedPromotedItem2, AbstractAsBoostItemConfigurationModel.UNIQUEIDX);
	}
}
