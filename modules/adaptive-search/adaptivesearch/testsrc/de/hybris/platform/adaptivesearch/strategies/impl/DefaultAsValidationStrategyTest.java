/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AsSimpleSearchProfileModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.services.ValidationService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;


@IntegrationTest
public class DefaultAsValidationStrategyTest extends ServicelayerTransactionalTest
{
	private final static String CODE = "searchProfile";
	private final static String INDEX_TYPE = "testIndex1";

	@Resource
	private ModelService modelService;

	@Resource
	private ValidationService validationService;

	@Resource
	private DefaultAsValidationStrategy defaultAsValidationStrategy;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/impex/essentialdata-adaptivesearch-validation.impex", StandardCharsets.UTF_8.name());

		validationService.reloadValidationEngine();
	}

	@Test
	public void isValid()
	{
		// given
		final AsSimpleSearchProfileModel searchProfile = modelService.create(AsSimpleSearchProfileModel.class);
		searchProfile.setCode(CODE);
		searchProfile.setIndexType(INDEX_TYPE);

		// when
		final boolean valid = defaultAsValidationStrategy.isValid(searchProfile);

		// then
		assertTrue(valid);
	}

	@Test
	public void isNotValid()
	{
		// given
		final AsSimpleSearchProfileModel searchProfile = modelService.create(AsSimpleSearchProfileModel.class);
		searchProfile.setIndexType(INDEX_TYPE);

		// when
		final boolean valid = defaultAsValidationStrategy.isValid(searchProfile);

		// then
		assertFalse(valid);
	}
}
