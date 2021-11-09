/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.synchronization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchConfigurationService;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class AsPromotedFacetValueSynchronizationTest extends AbstractAsSynchronizationTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String FACET_CONFIGURATION_UID = "facet";

	private static final String UID1 = "cde588ec-d453-48bd-a3b1-b9aa00402256";

	private static final String VALUE1 = "value1";
	private static final String VALUE2 = "value2";

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
	private AbstractAsFacetConfigurationModel facetConfiguration;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/asBase.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchProfile.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchConfiguration.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asFacets.impex", StandardCharsets.UTF_8.name());

		stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final Optional<AbstractAsFacetConfigurationModel> facetConfigurationOptional = asConfigurationService
				.getConfigurationForUid(AbstractAsFacetConfigurationModel.class, stagedCatalogVersion, FACET_CONFIGURATION_UID);
		facetConfiguration = facetConfigurationOptional.orElseThrow();
	}

	@Test
	public void promotedFacetValueNotFoundBeforeSynchronization()
	{
		// given
		final AsPromotedFacetValueModel promotedValue = asConfigurationService.createConfiguration(AsPromotedFacetValueModel.class);
		promotedValue.setCatalogVersion(stagedCatalogVersion);
		promotedValue.setUid(UID1);
		promotedValue.setFacetConfiguration(facetConfiguration);
		promotedValue.setValue(VALUE1);

		// when
		asConfigurationService.saveConfiguration(promotedValue);

		final Optional<AsPromotedFacetValueModel> synchronizedPromotedValueOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedFacetValueModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedPromotedValueOptional.isPresent());
	}

	@Test
	public void synchronizeNewPromotedFacetValue()
	{
		// given
		final AsPromotedFacetValueModel promotedValue = asConfigurationService.createConfiguration(AsPromotedFacetValueModel.class);
		promotedValue.setCatalogVersion(stagedCatalogVersion);
		promotedValue.setUid(UID1);
		promotedValue.setFacetConfiguration(facetConfiguration);
		promotedValue.setValue(VALUE1);

		// when
		asConfigurationService.saveConfiguration(promotedValue);
		asConfigurationService.refreshConfiguration(promotedValue);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedFacetValueModel> synchronizedPromotedValueOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedFacetValueModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedPromotedValueOptional.isPresent());

		final AsPromotedFacetValueModel synchronizedPromotedValue = synchronizedPromotedValueOptional.orElseThrow();
		assertFalse(synchronizedPromotedValue.isCorrupted());
		assertSynchronized(promotedValue, synchronizedPromotedValue, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeUpdatedPromotedFacetValue()
	{
		// given
		final AsPromotedFacetValueModel promotedValue = asConfigurationService.createConfiguration(AsPromotedFacetValueModel.class);
		promotedValue.setCatalogVersion(stagedCatalogVersion);
		promotedValue.setUid(UID1);
		promotedValue.setFacetConfiguration(facetConfiguration);
		promotedValue.setValue(VALUE1);

		// when
		asConfigurationService.saveConfiguration(promotedValue);
		asConfigurationService.refreshConfiguration(promotedValue);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		promotedValue.setValue(VALUE2);

		asConfigurationService.saveConfiguration(promotedValue);
		asConfigurationService.refreshConfiguration(promotedValue);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedFacetValueModel> synchronizedPromotedValueOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedFacetValueModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedPromotedValueOptional.isPresent());

		final AsPromotedFacetValueModel synchronizedPromotedValue = synchronizedPromotedValueOptional.orElseThrow();
		assertFalse(synchronizedPromotedValue.isCorrupted());
		assertSynchronized(promotedValue, synchronizedPromotedValue, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeRemovedPromotedFacetValue()
	{
		// given
		final AsPromotedFacetValueModel promotedValue = asConfigurationService.createConfiguration(AsPromotedFacetValueModel.class);
		promotedValue.setCatalogVersion(stagedCatalogVersion);
		promotedValue.setUid(UID1);
		promotedValue.setFacetConfiguration(facetConfiguration);
		promotedValue.setValue(VALUE1);

		// when
		asConfigurationService.saveConfiguration(promotedValue);
		asConfigurationService.refreshConfiguration(promotedValue);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		asConfigurationService.removeConfiguration(promotedValue);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsPromotedFacetValueModel> synchronizedPromotedValueOptional = asConfigurationService
				.getConfigurationForUid(AsPromotedFacetValueModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedPromotedValueOptional.isPresent());
	}
}
