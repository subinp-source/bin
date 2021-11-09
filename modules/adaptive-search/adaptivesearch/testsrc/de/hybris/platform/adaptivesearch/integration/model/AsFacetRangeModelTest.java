/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.integration.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetValueModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class AsFacetRangeModelTest extends ServicelayerTransactionalTest
{
	private static final String CATALOG_ID = "hwcatalog";
	private static final String VERSION_STAGED = "Staged";
	private static final String VERSION_ONLINE = "Online";

	private static final String FACET_RANGE_QUALIFIER = "EUR";

	private static final String FACET_RANGE1_ID = "FacetRange1";
	private static final String FACET_RANGE1_UID = "e81de964-b6b8-4031-bf1a-2eeb99b606ac";
	private static final String FACET_RANGE1_FROM = Integer.toString(0);
	private static final String FACET_RANGE1_TO = Integer.toString(100);
	private static final String FACET_RANGE2_ID = "FacetRange2";
	private static final String FACET_RANGE2_UID = "e3780f3f-5e60-4174-b85d-52c84b34ee38";
	private static final String FACET_RANGE2_FROM = Integer.toString(100);
	private static final String FACET_RANGE2_TO = Integer.toString(200);

	private static final String FACET_CONFIGURATION_ID = "facetConfigurationID";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private AsConfigurationService asConfigurationService;

	private CatalogVersionModel onlineCatalogVersion;
	private AbstractAsFacetConfigurationModel facetConfiguration;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/adaptivesearch/test/integration/model/asFacetRangeModelTest.impex", StandardCharsets.UTF_8.name());

		onlineCatalogVersion = catalogVersionService.getCatalogVersion(CATALOG_ID, VERSION_ONLINE);

		final Optional<AbstractAsFacetConfigurationModel> facetConfigurationOptional = asConfigurationService
				.getConfigurationForUid(AbstractAsFacetConfigurationModel.class, onlineCatalogVersion, FACET_CONFIGURATION_ID);
		facetConfiguration = facetConfigurationOptional.get();
	}

	@Test
	public void createMultipleFacetRangesWithoutUid()
	{
		// given
		final AsFacetRangeModel facetRange1 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange1.setCatalogVersion(onlineCatalogVersion);
		facetRange1.setFacetConfiguration(facetConfiguration);
		facetRange1.setId(FACET_RANGE1_ID);
		facetRange1.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange1.setFrom(FACET_RANGE1_FROM);
		facetRange1.setTo(FACET_RANGE1_TO);


		final AsFacetRangeModel facetRange2 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange2.setCatalogVersion(onlineCatalogVersion);
		facetRange2.setFacetConfiguration(facetConfiguration);
		facetRange2.setId(FACET_RANGE2_ID);
		facetRange2.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange2.setFrom(FACET_RANGE2_FROM);
		facetRange2.setTo(FACET_RANGE2_TO);

		// when
		asConfigurationService.saveConfiguration(facetRange1);
		asConfigurationService.saveConfiguration(facetRange2);

		// then
		assertEquals(onlineCatalogVersion, facetRange1.getCatalogVersion());
		assertNotNull(facetRange1.getUid());
		assertFalse(facetRange1.getUid().isEmpty());

		assertEquals(onlineCatalogVersion, facetRange1.getCatalogVersion());
		assertNotNull(facetRange1.getUid());
		assertFalse(facetRange1.getUid().isEmpty());
	}

	@Test
	public void createFacetRange()
	{
		// given
		final AsFacetRangeModel facetRange1 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange1.setCatalogVersion(onlineCatalogVersion);
		facetRange1.setFacetConfiguration(facetConfiguration);
		facetRange1.setId(FACET_RANGE1_ID);
		facetRange1.setUid(FACET_RANGE1_UID);
		facetRange1.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange1.setFrom(FACET_RANGE1_FROM);
		facetRange1.setTo(FACET_RANGE1_TO);

		// when
		asConfigurationService.saveConfiguration(facetRange1);

		// then
		assertEquals(onlineCatalogVersion, facetRange1.getCatalogVersion());
		assertEquals(FACET_RANGE1_UID, facetRange1.getUid());
		assertEquals(FACET_RANGE1_ID, facetRange1.getId());
		assertEquals(FACET_RANGE_QUALIFIER, facetRange1.getQualifier());
		assertEquals(FACET_RANGE1_FROM, facetRange1.getFrom());
		assertEquals(FACET_RANGE1_TO, facetRange1.getTo());
	}

	@Test
	public void failToCreateMultipleFacetRangesWithSameId()
	{
		// given
		final AsFacetRangeModel facetRange1 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange1.setCatalogVersion(onlineCatalogVersion);
		facetRange1.setFacetConfiguration(facetConfiguration);
		facetRange1.setId(FACET_RANGE1_ID);
		facetRange1.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange1.setFrom(Integer.toString(0));
		facetRange1.setTo(Integer.toString(100));


		final AsFacetRangeModel facetRange2 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange2.setCatalogVersion(onlineCatalogVersion);
		facetRange2.setFacetConfiguration(facetConfiguration);
		facetRange2.setId(FACET_RANGE1_ID);
		facetRange2.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange2.setFrom(Integer.toString(100));
		facetRange2.setTo(Integer.toString(200));

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		asConfigurationService.saveConfiguration(facetRange1);
		asConfigurationService.saveConfiguration(facetRange2);
	}

	@Test
	public void failToCreateMultipleFacetRangesWithSameUId()
	{
		// given
		final AsFacetRangeModel facetRange1 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange1.setCatalogVersion(onlineCatalogVersion);
		facetRange1.setFacetConfiguration(facetConfiguration);
		facetRange1.setId(FACET_RANGE1_ID);
		facetRange1.setUid(FACET_RANGE1_UID);
		facetRange1.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange1.setFrom(Integer.toString(0));
		facetRange1.setTo(Integer.toString(100));


		final AsFacetRangeModel facetRange2 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange2.setCatalogVersion(onlineCatalogVersion);
		facetRange2.setFacetConfiguration(facetConfiguration);
		facetRange2.setId(FACET_RANGE2_ID);
		facetRange2.setUid(FACET_RANGE1_UID);
		facetRange2.setQualifier(FACET_RANGE_QUALIFIER);
		facetRange2.setFrom(Integer.toString(100));
		facetRange2.setTo(Integer.toString(200));

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		asConfigurationService.saveConfiguration(facetRange1);
		asConfigurationService.saveConfiguration(facetRange2);
	}

	@Test
	public void failToCreateMultipleFacetRangesWithoutMandatoryFields()
	{
		// given
		final AsFacetRangeModel facetRange1 = asConfigurationService.createConfiguration(AsFacetRangeModel.class);
		facetRange1.setCatalogVersion(onlineCatalogVersion);
		facetRange1.setFacetConfiguration(facetConfiguration);
		facetRange1.setId(FACET_RANGE1_ID);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		asConfigurationService.saveConfiguration(facetRange1);
	}
}
