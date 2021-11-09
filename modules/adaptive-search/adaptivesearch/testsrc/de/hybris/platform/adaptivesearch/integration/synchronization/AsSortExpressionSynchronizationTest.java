/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.synchronization;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.enums.AsSortOrder;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
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
public class AsSortExpressionSynchronizationTest extends AbstractAsSynchronizationTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String SORT_CONFIGURATION_UID = "sort";

	private static final String UID1 = "cde588ec-d453-48bd-a3b1-b9aa00402256";

	private static final String INDEX_PROPERTY1 = "property1";

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
	private AbstractAsSortConfigurationModel sortConfiguration;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/asBase.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchProfile.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSimpleSearchConfiguration.impex", StandardCharsets.UTF_8.name());
		importCsv("/adaptivesearch/test/integration/asSorts.impex", StandardCharsets.UTF_8.name());

		stagedCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_STAGED);
		onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final Optional<AbstractAsSortConfigurationModel> sortConfigurationOptional = asConfigurationService
				.getConfigurationForUid(AbstractAsSortConfigurationModel.class, stagedCatalogVersion, SORT_CONFIGURATION_UID);
		sortConfiguration = sortConfigurationOptional.orElseThrow();
	}

	@Test
	public void synchronizeNewSortExpression()
	{
		// given
		final AsSortExpressionModel sortExpression = asConfigurationService.createConfiguration(AsSortExpressionModel.class);
		sortExpression.setCatalogVersion(stagedCatalogVersion);
		sortExpression.setUid(UID1);
		sortExpression.setSortConfiguration(sortConfiguration);
		sortExpression.setExpression(INDEX_PROPERTY1);
		sortExpression.setOrder(AsSortOrder.ASCENDING);

		// when
		asConfigurationService.saveConfiguration(sortExpression);
		modelService.refresh(sortExpression);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsSortExpressionModel> synchronizedSortExpressionOptional = asConfigurationService
				.getConfigurationForUid(AsSortExpressionModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedSortExpressionOptional.isPresent());

		final AsSortExpressionModel synchronizedSortExpression = synchronizedSortExpressionOptional.orElseThrow();
		assertFalse(synchronizedSortExpression.isCorrupted());
		assertSynchronized(sortExpression, synchronizedSortExpression, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeUpdatedSortExpression()
	{
		// given
		final AsSortExpressionModel sortExpression = asConfigurationService.createConfiguration(AsSortExpressionModel.class);
		sortExpression.setCatalogVersion(stagedCatalogVersion);
		sortExpression.setUid(UID1);
		sortExpression.setSortConfiguration(sortConfiguration);
		sortExpression.setExpression(INDEX_PROPERTY1);
		sortExpression.setOrder(AsSortOrder.ASCENDING);

		// when
		asConfigurationService.saveConfiguration(sortExpression);
		modelService.refresh(sortExpression);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		sortExpression.setOrder(AsSortOrder.DESCENDING);

		asConfigurationService.saveConfiguration(sortExpression);
		modelService.refresh(sortExpression);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsSortExpressionModel> synchronizedSortExpressionOptional = asConfigurationService
				.getConfigurationForUid(AsSortExpressionModel.class, onlineCatalogVersion, UID1);

		// then
		assertTrue(synchronizedSortExpressionOptional.isPresent());

		final AsSortExpressionModel synchronizedSortExpression = synchronizedSortExpressionOptional.orElseThrow();
		assertFalse(synchronizedSortExpression.isCorrupted());
		assertSynchronized(sortExpression, synchronizedSortExpression, AbstractAsSortConfigurationModel.UNIQUEIDX);
	}

	@Test
	public void synchronizeRemovedSortExpression()
	{
		// given
		final AsSortExpressionModel sortExpression = asConfigurationService.createConfiguration(AsSortExpressionModel.class);
		sortExpression.setCatalogVersion(stagedCatalogVersion);
		sortExpression.setUid(UID1);
		sortExpression.setSortConfiguration(sortConfiguration);
		sortExpression.setExpression(INDEX_PROPERTY1);
		sortExpression.setOrder(AsSortOrder.ASCENDING);

		// when
		asConfigurationService.saveConfiguration(sortExpression);
		modelService.refresh(sortExpression);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		asConfigurationService.removeConfiguration(sortExpression);

		catalogSynchronizationService.synchronizeFully(stagedCatalogVersion, onlineCatalogVersion);

		final Optional<AsSortExpressionModel> synchronizedSortExpressionOptional = asConfigurationService
				.getConfigurationForUid(AsSortExpressionModel.class, onlineCatalogVersion, UID1);

		// then
		assertFalse(synchronizedSortExpressionOptional.isPresent());
	}
}
