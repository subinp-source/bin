/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.model;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.searchservices.model.SnCollectionPatternConstraintModel;
import de.hybris.platform.searchservices.model.SnSynonymEntryModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;
import de.hybris.platform.validation.services.ValidationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SnSynonymEntryModelValidationTest extends ServicelayerTransactionalTest
{
	private static final String ID1 = "id1";
	private static final String SYNONYMENTRY_INPUT_QUALIFIER = String.join(".", SnSynonymEntryModel._TYPECODE,
			SnSynonymEntryModel.INPUT);
	private static final String SYNONYMENTRY_SYNONYMS_QUALIFIER = String.join(".", SnSynonymEntryModel._TYPECODE,
			SnSynonymEntryModel.SYNONYMS);

	@Resource
	private ModelService modelService;

	@Resource
	private ValidationService validationService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/impex/essentialdata-searchservices-validation.impex", "UTF-8");
		validationService.reloadValidationEngine();
	}

	@Test
	public void testEntryWithoutInputAndSynonymsInput()
	{
		//given
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(ID1);

		//when
		final Set<HybrisConstraintViolation> validations = validationService.validate(synonymEntry);

		//then
		Assert.assertTrue("The violation set should not be null", validations != null);
		Assert.assertEquals("There should be no violation of constraints", 0, validations.size());
	}

	@Test
	public void testEntryWithInputAndSynonymsInput()
	{
		//given
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(ID1);
		synonymEntry.setInput(Collections.singletonList("limousine"));
		synonymEntry.setSynonyms(Arrays.asList("car", "automobile"));

		//when
		final Set<HybrisConstraintViolation> validations = validationService.validate(synonymEntry);

		//then
		Assert.assertTrue("The violation set should not be null", validations != null);
		Assert.assertEquals("There should be no violation of constraints", 0, validations.size());
	}

	@Test
	public void testInvalidInput()
	{
		//given
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(ID1);
		synonymEntry.setInput(Collections.singletonList("l,imousine"));
		synonymEntry.setSynonyms(Arrays.asList("car", "automobile"));

		//when
		final Set<HybrisConstraintViolation> validations = validationService.validate(synonymEntry);

		//then
		Assert.assertTrue("The violation set should not be null", validations != null);
		Assert.assertEquals("There should be one violation of constraint", 1, validations.size());
		Assert.assertThat(getConstraints(validations),
				Matchers.everyItem(Matchers.instanceOf(SnCollectionPatternConstraintModel.class)));
		Assert.assertThat(getQualifiers(validations), Matchers.containsInAnyOrder(SYNONYMENTRY_INPUT_QUALIFIER));
	}


	@Test
	public void testInvalidSynonyms()
	{
		//given
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(ID1);
		synonymEntry.setInput(Collections.singletonList("limousine"));
		synonymEntry.setSynonyms(Arrays.asList("car", "a=utomobile"));

		//when
		final Set<HybrisConstraintViolation> validations = validationService.validate(synonymEntry);

		//then
		Assert.assertTrue("The violation set should not be null", validations != null);
		Assert.assertEquals("There should be one violation of constraint", 1, validations.size());
		Assert.assertThat(getConstraints(validations),
				Matchers.everyItem(Matchers.instanceOf(SnCollectionPatternConstraintModel.class)));
		Assert.assertThat(getQualifiers(validations), Matchers.containsInAnyOrder(SYNONYMENTRY_SYNONYMS_QUALIFIER));
	}

	@Test
	public void testInvalidInputAndSynonyms()
	{
		//given
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(ID1);
		synonymEntry.setInput(Collections.singletonList("l>imousine"));
		synonymEntry.setSynonyms(Arrays.asList("ca,r", "a=utomobile"));

		//when
		final Set<HybrisConstraintViolation> validations = validationService.validate(synonymEntry);

		//then
		Assert.assertTrue("The violation set should not be null", validations != null);
		Assert.assertEquals("There should be two violation of constraints", 2, validations.size());
		Assert.assertThat(getConstraints(validations),
				Matchers.everyItem(Matchers.instanceOf(SnCollectionPatternConstraintModel.class)));
		Assert.assertThat(getQualifiers(validations),
				Matchers.containsInAnyOrder(SYNONYMENTRY_INPUT_QUALIFIER, SYNONYMENTRY_SYNONYMS_QUALIFIER));
	}

	private List<String> getQualifiers(final Set<HybrisConstraintViolation> validations)
	{
		return validations.stream().map(HybrisConstraintViolation::getQualifier).collect(Collectors.toList());
	}


	private List<AbstractConstraintModel> getConstraints(final Set<HybrisConstraintViolation> validations)
	{
		return validations.stream().map(HybrisConstraintViolation::getConstraintModel).collect(Collectors.toList());
	}
}
