/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.searchservices.admin.dao.SnSynonymDictionaryDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;
import de.hybris.platform.searchservices.model.SnSynonymEntryModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class SnSynonymDictionaryModelTest extends ServicelayerTransactionalTest
{
	private static final String INVALID_ID = "__id1__";

	private static final String ID1 = "id1";
	private static final String ID2 = "id2";

	private static final String NAME1 = "name1";
	private static final String NAME2 = "name2";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Resource
	private ModelService modelService;

	@Resource
	private SnSynonymDictionaryDao snSynonymDictionaryDao;

	@Resource
	private CommonI18NService commonI18NService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
	}

	@Test
	public void getNonExistingSynonymDictionary()
	{
		// when
		final Optional<SnSynonymDictionaryModel> synonymDictionaryOptional = snSynonymDictionaryDao.findSynonymDictionaryById(ID1);

		// then
		assertFalse(synonymDictionaryOptional.isPresent());
	}

	@Test
	public void createSynonymDictionary()
	{
		// given
		final List<LanguageModel> languagesEN = Collections.singletonList(commonI18NService.getLanguage("en"));
		final SnSynonymDictionaryModel synonymDictionary = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary.setId(ID1);
		synonymDictionary.setName(NAME1);
		synonymDictionary.setLanguages(languagesEN);
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(ID1);
		synonymEntry.setInput(Collections.singletonList("limousine"));
		synonymEntry.setSynonyms(Arrays.asList("car", "automobile"));
		synonymEntry.setSynonymDictionary(synonymDictionary);
		synonymDictionary.setEntries(Collections.singletonList(synonymEntry));

		// when
		modelService.save(synonymDictionary);

		final Optional<SnSynonymDictionaryModel> createdSynonymDictionaryOptional = snSynonymDictionaryDao
				.findSynonymDictionaryById(ID1);

		// then
		assertTrue(createdSynonymDictionaryOptional.isPresent());
		final SnSynonymDictionaryModel createdSynonymDictionary = createdSynonymDictionaryOptional.get();
		assertEquals(ID1, createdSynonymDictionary.getId());
		assertEquals(NAME1, createdSynonymDictionary.getName());
		assertEquals(languagesEN, createdSynonymDictionary.getLanguages());

		final List<SnSynonymEntryModel> synonymEntries = createdSynonymDictionary.getEntries();
		Assertions.assertThat(synonymEntries.stream().flatMap(e -> e.getInput().stream())).containsOnly("limousine");
		Assertions.assertThat(synonymEntries.stream().flatMap(e -> e.getSynonyms().stream())).containsOnly("car", "automobile");
	}

	@Test
	public void failToCreateSynonymDictionaryWithInvalidId()
	{
		// given
		final SnSynonymDictionaryModel synonymDictionary = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary.setId(INVALID_ID);
		synonymDictionary.setName(NAME1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(synonymDictionary);
	}

	@Test
	public void failToCreateSynonymEntryWithInvalidId()
	{
		// given
		final SnSynonymDictionaryModel synonymDictionary = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary.setId(ID1);
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setId(INVALID_ID);
		synonymEntry.setSynonyms(Arrays.asList("car", "automobile"));
		synonymEntry.setSynonymDictionary(synonymDictionary);
		synonymDictionary.setEntries(Collections.singletonList(synonymEntry));

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(synonymDictionary);
	}

	@Test
	public void failToCreateSynonymEntryWithoutId()
	{
		// given
		final SnSynonymDictionaryModel synonymDictionary = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary.setId(ID1);
		final SnSynonymEntryModel synonymEntry = modelService.create(SnSynonymEntryModel.class);
		synonymEntry.setSynonyms(Arrays.asList("car", "automobile"));
		synonymEntry.setSynonymDictionary(synonymDictionary);
		synonymDictionary.setEntries(Collections.singletonList(synonymEntry));

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(synonymDictionary);
	}

	@Test
	public void failToCreateSynonymDictionaryWithoutId()
	{
		// given
		final SnSynonymDictionaryModel synonymDictionary = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary.setName(NAME1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(synonymDictionary);
	}

	@Test
	public void createMultipleSynonymDictionaries()
	{
		// given
		final List<LanguageModel> languagesEN = Collections.singletonList(commonI18NService.getLanguage("en"));
		final SnSynonymDictionaryModel synonymDictionary1 = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary1.setId(ID1);
		synonymDictionary1.setName(NAME1);
		synonymDictionary1.setLanguages(languagesEN);

		final List<LanguageModel> languagesDE = Collections.singletonList(commonI18NService.getLanguage("de"));

		final SnSynonymDictionaryModel synonymDictionary2 = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary2.setId(ID2);
		synonymDictionary2.setName(NAME2);
		synonymDictionary2.setLanguages(languagesDE);

		// when
		modelService.save(synonymDictionary1);
		modelService.save(synonymDictionary2);

		final Optional<SnSynonymDictionaryModel> createdSynonymDictionary1Optional = snSynonymDictionaryDao
				.findSynonymDictionaryById(ID1);
		final Optional<SnSynonymDictionaryModel> createdSynonymDictionary2Optional = snSynonymDictionaryDao
				.findSynonymDictionaryById(ID2);

		// then
		assertTrue(createdSynonymDictionary1Optional.isPresent());
		final SnSynonymDictionaryModel createdSynonymDictionary1 = createdSynonymDictionary1Optional.get();
		assertEquals(ID1, createdSynonymDictionary1.getId());
		assertEquals(NAME1, createdSynonymDictionary1.getName());
		assertEquals(languagesEN, createdSynonymDictionary1.getLanguages());

		assertTrue(createdSynonymDictionary2Optional.isPresent());
		final SnSynonymDictionaryModel createdSynonymDictionary2 = createdSynonymDictionary2Optional.get();
		assertEquals(ID2, createdSynonymDictionary2.getId());
		assertEquals(NAME2, createdSynonymDictionary2.getName());
		assertEquals(languagesDE, createdSynonymDictionary2.getLanguages());
	}

	@Test
	public void failToCreateMultipleSynonymDictionariesWithSameId()
	{
		// given
		final SnSynonymDictionaryModel synonymDictionary1 = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary1.setId(ID1);
		synonymDictionary1.setName(NAME1);

		final SnSynonymDictionaryModel synonymDictionary2 = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary2.setId(ID1);
		synonymDictionary2.setName(NAME2);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(synonymDictionary1);
		modelService.save(synonymDictionary2);
	}
}
