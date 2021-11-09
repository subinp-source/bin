/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.synchronization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.enums.AsBoostItemsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostRulesMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.adaptivesearch.enums.AsFacetsMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsGroupMergeMode;
import de.hybris.platform.adaptivesearch.enums.AsSortsMergeMode;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedSortModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedSortModel;
import de.hybris.platform.adaptivesearch.model.AsSortModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class AsCategoryAwareSearchConfigurationSynchronizationTest extends AbstractAsSynchronizationTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String CATEGORY1_CODE = "category1";
	private static final String CATEGORY2_CODE = "category2";

	private static final String SEARCH_PROFILE_CODE = "searchProfile";

	private static final String UID1 = "c5be51d4-5649-4a7f-b27d-c18758c5dfff";
	private static final String UID2 = "397bad42-150c-472b-be12-9bcb81ec029e";
	private static final String UID3 = "289032c0-4423-4ca7-8570-93d79a95c058";

	private static final String INDEX_PROPERTY1 = "property1";
	private static final String INDEX_PROPERTY2 = "property2";
	private static final String INDEX_PROPERTY3 = "property3";

	private static final String VALUE1 = "value1";

	private static final Float BOOST1 = Float.valueOf(1.1f);

	private static final String SORT_CODE1 = "code1";
	private static final String SORT_CODE2 = "code2";
	private static final String SORT_CODE3 = "code3";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private CatalogSynchronizationService catalogSynchronizationService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private AsSearchProfileService asSearchProfileService;

	@Resource
	private AsSearchConfigurationService asSearchConfigurationService;

	@Resource
	private AsConfigurationService asConfigurationService;

	private CatalogVersionModel onlineCatalogVersion;
	private CatalogVersionModel stagedCatalogVersion;
	private AsCategoryAwareSearchProfileModel searchProfile;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/asBase.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asCategoryAwareSearchProfile.impex", StandardCharsets.UTF_8.name());

		stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final Optional<AsCategoryAwareSearchProfileModel> searchProfileOptional = asSearchProfileService
				.getSearchProfileForCode(stagedCatalogVersion, SEARCH_PROFILE_CODE);
		searchProfile = searchProfileOptional.orElseThrow();
	}

	@Test
	public void searchConfigurationNotFoundBeforeSynchronization()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(category);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertFalse(searchConfigurationOptional.isPresent());
	}

	@Test
	public void synchronizeNewSearchConfiguration()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(category);
		searchConfiguration.setGroupMergeMode(AsGroupMergeMode.REPLACE);
		searchConfiguration.setGroupExpression(INDEX_PROPERTY2);

		final AsPromotedFacetModel promotedFacet = asConfigurationService.createConfiguration(AsPromotedFacetModel.class);
		promotedFacet.setCatalogVersion(stagedCatalogVersion);
		promotedFacet.setSearchConfiguration(searchConfiguration);
		promotedFacet.setIndexProperty(INDEX_PROPERTY1);
		promotedFacet.setFacetType(AsFacetType.REFINE);

		final AsFacetModel facet = asConfigurationService.createConfiguration(AsFacetModel.class);
		facet.setCatalogVersion(stagedCatalogVersion);
		facet.setSearchConfiguration(searchConfiguration);
		facet.setIndexProperty(INDEX_PROPERTY2);
		facet.setFacetType(AsFacetType.REFINE);

		final AsExcludedFacetModel excludedFacet = asConfigurationService.createConfiguration(AsExcludedFacetModel.class);
		excludedFacet.setCatalogVersion(stagedCatalogVersion);
		excludedFacet.setSearchConfiguration(searchConfiguration);
		excludedFacet.setIndexProperty(INDEX_PROPERTY3);
		excludedFacet.setFacetType(AsFacetType.REFINE);

		final AsPromotedItemModel promotedItem = asConfigurationService.createConfiguration(AsPromotedItemModel.class);
		promotedItem.setCatalogVersion(stagedCatalogVersion);
		promotedItem.setSearchConfiguration(searchConfiguration);
		promotedItem.setItem(stagedCatalogVersion);

		final AsExcludedItemModel excludedItem = asConfigurationService.createConfiguration(AsExcludedItemModel.class);
		excludedItem.setCatalogVersion(stagedCatalogVersion);
		excludedItem.setSearchConfiguration(searchConfiguration);
		excludedItem.setItem(onlineCatalogVersion);

		final AsBoostRuleModel boostRule = asConfigurationService.createConfiguration(AsBoostRuleModel.class);
		boostRule.setCatalogVersion(stagedCatalogVersion);
		boostRule.setSearchConfiguration(searchConfiguration);
		boostRule.setIndexProperty(INDEX_PROPERTY1);
		boostRule.setOperator(AsBoostOperator.EQUAL);
		boostRule.setValue(VALUE1);
		boostRule.setBoost(BOOST1);

		final AsPromotedSortModel promotedSort = asConfigurationService.createConfiguration(AsPromotedSortModel.class);
		promotedSort.setCatalogVersion(stagedCatalogVersion);
		promotedSort.setSearchConfiguration(searchConfiguration);
		promotedSort.setCode(SORT_CODE1);

		final AsSortModel sort = asConfigurationService.createConfiguration(AsSortModel.class);
		sort.setCatalogVersion(stagedCatalogVersion);
		sort.setSearchConfiguration(searchConfiguration);
		sort.setCode(SORT_CODE2);

		final AsExcludedSortModel excludedSort = asConfigurationService.createConfiguration(AsExcludedSortModel.class);
		excludedSort.setCatalogVersion(stagedCatalogVersion);
		excludedSort.setSearchConfiguration(searchConfiguration);
		excludedSort.setCode(SORT_CODE3);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		asConfigurationService.saveConfiguration(promotedFacet);
		asConfigurationService.saveConfiguration(facet);
		asConfigurationService.saveConfiguration(excludedFacet);
		asConfigurationService.saveConfiguration(promotedItem);
		asConfigurationService.saveConfiguration(excludedItem);
		asConfigurationService.saveConfiguration(boostRule);
		asConfigurationService.saveConfiguration(promotedSort);
		asConfigurationService.saveConfiguration(sort);
		asConfigurationService.saveConfiguration(excludedSort);
		asConfigurationService.refreshConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeNewSearchConfigurationForGlobalCategory()
	{
		// given
		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(null);
		searchConfiguration.setGroupMergeMode(AsGroupMergeMode.REPLACE);
		searchConfiguration.setGroupExpression(INDEX_PROPERTY2);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		asConfigurationService.refreshConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeSearchConfiguration()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(category);
		searchConfiguration.setGroupMergeMode(AsGroupMergeMode.REPLACE);
		searchConfiguration.setGroupExpression(INDEX_PROPERTY2);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		modelService.refresh(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		searchConfiguration.setFacetsMergeMode(AsFacetsMergeMode.REPLACE);
		searchConfiguration.setPromotedFacets(Collections.emptyList());
		searchConfiguration.setFacets(Collections.emptyList());
		searchConfiguration.setExcludedFacets(Collections.emptyList());
		searchConfiguration.setBoostItemsMergeMode(AsBoostItemsMergeMode.REPLACE);
		searchConfiguration.setPromotedItems(Collections.emptyList());
		searchConfiguration.setExcludedItems(Collections.emptyList());
		searchConfiguration.setBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
		searchConfiguration.setBoostRules(Collections.emptyList());
		searchConfiguration.setSortsMergeMode(AsSortsMergeMode.REPLACE);
		searchConfiguration.setPromotedSorts(Collections.emptyList());
		searchConfiguration.setSorts(Collections.emptyList());
		searchConfiguration.setExcludedSorts(Collections.emptyList());

		asConfigurationService.saveConfiguration(searchConfiguration);
		modelService.refresh(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeSearchConfigurationForNullCategory()
	{
		// given
		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(null);
		searchConfiguration.setGroupMergeMode(AsGroupMergeMode.REPLACE);
		searchConfiguration.setGroupExpression(INDEX_PROPERTY2);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		modelService.refresh(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		searchConfiguration.setFacetsMergeMode(AsFacetsMergeMode.REPLACE);
		searchConfiguration.setPromotedFacets(Collections.emptyList());
		searchConfiguration.setFacets(Collections.emptyList());
		searchConfiguration.setExcludedFacets(Collections.emptyList());
		searchConfiguration.setBoostItemsMergeMode(AsBoostItemsMergeMode.REPLACE);
		searchConfiguration.setPromotedItems(Collections.emptyList());
		searchConfiguration.setExcludedItems(Collections.emptyList());
		searchConfiguration.setBoostRulesMergeMode(AsBoostRulesMergeMode.REPLACE);
		searchConfiguration.setBoostRules(Collections.emptyList());
		searchConfiguration.setSortsMergeMode(AsSortsMergeMode.REPLACE);
		searchConfiguration.setPromotedSorts(Collections.emptyList());
		searchConfiguration.setSorts(Collections.emptyList());
		searchConfiguration.setExcludedSorts(Collections.emptyList());

		asConfigurationService.saveConfiguration(searchConfiguration);
		modelService.refresh(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeRemovedSearchConfiguration()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(category);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		modelService.refresh(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		asConfigurationService.removeConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertFalse(searchConfigurationOptional.isPresent());
	}

	@Test
	public void synchronizeAfterRemovingCategoryForNewSearchConfiguration()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(category);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		modelService.remove(category);
		asConfigurationService.refreshConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertTrue(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeAfterRemovingCategoryForExistingSearchConfiguration()
	{
		// given
		final CategoryModel category = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setCategory(category);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		modelService.remove(category);
		asConfigurationService.refreshConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertTrue(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeAfterRemovingCategoryForMultipleExistingSearchConfigurations()
	{
		// given
		final CategoryModel category1 = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY1_CODE);
		final CategoryModel category2 = categoryService.getCategoryForCode(stagedCatalogVersion, CATEGORY2_CODE);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration1 = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration1.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration1.setUid(UID1);
		searchConfiguration1.setSearchProfile(searchProfile);
		searchConfiguration1.setCategory(category1);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration2 = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration2.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration2.setUid(UID2);
		searchConfiguration2.setSearchProfile(searchProfile);
		searchConfiguration2.setCategory(category2);

		final AsCategoryAwareSearchConfigurationModel searchConfiguration3 = asConfigurationService
				.createConfiguration(AsCategoryAwareSearchConfigurationModel.class);
		searchConfiguration3.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration3.setUid(UID3);
		searchConfiguration3.setSearchProfile(searchProfile);
		searchConfiguration3.setCategory(null);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration1);
		asConfigurationService.saveConfiguration(searchConfiguration2);
		asConfigurationService.saveConfiguration(searchConfiguration3);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		modelService.remove(category1);
		modelService.remove(category2);
		asConfigurationService.refreshConfiguration(searchConfiguration1);
		asConfigurationService.refreshConfiguration(searchConfiguration2);
		asConfigurationService.refreshConfiguration(searchConfiguration3);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfiguration1Optional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);
		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfiguration2Optional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID2);
		final Optional<AsCategoryAwareSearchConfigurationModel> searchConfiguration3Optional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID3);

		// then
		assertTrue(searchConfiguration1Optional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration1 = searchConfiguration1Optional.orElseThrow();
		assertTrue(synchronizedSearchConfiguration1.isCorrupted());
		assertSynchronized(searchConfiguration1, synchronizedSearchConfiguration1, AbstractAsSortConfigurationModel.UNIQUEIDX);

		assertTrue(searchConfiguration2Optional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration2 = searchConfiguration2Optional.orElseThrow();
		assertTrue(synchronizedSearchConfiguration2.isCorrupted());
		assertSynchronized(searchConfiguration2, synchronizedSearchConfiguration2, AbstractAsSortConfigurationModel.UNIQUEIDX);

		assertTrue(searchConfiguration3Optional.isPresent());

		final AsCategoryAwareSearchConfigurationModel synchronizedSearchConfiguration3 = searchConfiguration3Optional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration3.isCorrupted());
		assertSynchronized(searchConfiguration3, synchronizedSearchConfiguration3, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}
}
