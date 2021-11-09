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
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedItemModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedSortModel;
import de.hybris.platform.adaptivesearch.model.AsFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedItemModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedSortModel;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchProfileModel;
import de.hybris.platform.adaptivesearch.model.AsSortModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
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
public class AsSimpleSearchConfigurationSynchronizationTest extends AbstractAsSynchronizationTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String SEARCH_PROFILE_CODE = "searchProfile";

	private static final String UID1 = "c5be51d4-5649-4a7f-b27d-c18758c5dfff";

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
	private AsSearchProfileService asSearchProfileService;

	@Resource
	private AsSearchConfigurationService asSearchConfigurationService;

	@Resource
	private AsConfigurationService asConfigurationService;

	private CatalogVersionModel onlineCatalogVersion;
	private CatalogVersionModel stagedCatalogVersion;
	private AsSimpleSearchProfileModel searchProfile;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/asBase.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchProfile.impex", StandardCharsets.UTF_8.name());

		stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final Optional<AsSimpleSearchProfileModel> searchProfileOptional = asSearchProfileService
				.getSearchProfileForCode(stagedCatalogVersion, SEARCH_PROFILE_CODE);
		searchProfile = searchProfileOptional.orElseThrow();
	}

	@Test
	public void searchConfigurationNotFoundBeforeSynchronization()
	{
		// given
		final AsSimpleSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsSimpleSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setUid(UID1);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);

		final Optional<AsSimpleSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertFalse(searchConfigurationOptional.isPresent());
	}

	@Test
	public void synchronizeNewSearchConfiguration()
	{
		// given
		final AsSimpleSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsSimpleSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setUid(UID1);
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

		final Optional<AsSimpleSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsSimpleSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeUpdatedSearchConfiguration()
	{
		// given
		final AsSimpleSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsSimpleSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setUid(UID1);
		searchConfiguration.setGroupMergeMode(AsGroupMergeMode.REPLACE);
		searchConfiguration.setGroupExpression(INDEX_PROPERTY2);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		asConfigurationService.refreshConfiguration(searchConfiguration);

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
		asConfigurationService.refreshConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsSimpleSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertTrue(searchConfigurationOptional.isPresent());

		final AsSimpleSearchConfigurationModel synchronizedSearchConfiguration = searchConfigurationOptional.orElseThrow();
		assertFalse(synchronizedSearchConfiguration.isCorrupted());
		assertSynchronized(searchConfiguration, synchronizedSearchConfiguration, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeRemovedSearchConfiguration()
	{
		// given
		final AsSimpleSearchConfigurationModel searchConfiguration = asConfigurationService
				.createConfiguration(AsSimpleSearchConfigurationModel.class);
		searchConfiguration.setCatalogVersion(stagedCatalogVersion);
		searchConfiguration.setSearchProfile(searchProfile);
		searchConfiguration.setUid(UID1);

		// when
		asConfigurationService.saveConfiguration(searchConfiguration);
		asConfigurationService.refreshConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		asConfigurationService.removeConfiguration(searchConfiguration);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsSimpleSearchConfigurationModel> searchConfigurationOptional = asSearchConfigurationService
				.getSearchConfigurationForUid(onlineCatalogVersion, UID1);

		// then
		assertFalse(searchConfigurationOptional.isPresent());
	}
}
