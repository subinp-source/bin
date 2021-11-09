/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.admin.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.searchservices.admin.dao.SnIndexConfigurationDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnSynonymDictionaryModel;

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
public class SnIndexConfigurationModelTest extends ServicelayerTransactionalTest
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
	private SnIndexConfigurationDao snIndexConfigurationDao;

	@Resource
	private CommonI18NService commonI18NService;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
	}

	@Test
	public void getNonExistingIndexConfiguration()
	{
		// when
		final Optional<SnIndexConfigurationModel> indexConfigurationOptional = snIndexConfigurationDao
				.findIndexConfigurationById(ID1);

		// then
		assertFalse(indexConfigurationOptional.isPresent());
	}

	@Test
	public void createIndexConfiguration()
	{
		// given
		final LanguageModel languageEN = commonI18NService.getLanguage("en");
		final CurrencyModel currencyGBP = commonI18NService.getCurrency("GBP");
		final SnSynonymDictionaryModel synonymDictionary = modelService.create(SnSynonymDictionaryModel.class);
		synonymDictionary.setId(ID1);
		synonymDictionary.setName(NAME1);
		synonymDictionary.setLanguages(List.of(languageEN));
		final SnIndexConfigurationModel indexConfiguration = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration.setId(ID1);
		indexConfiguration.setName(NAME1);
		indexConfiguration.setLanguages(List.of(languageEN));
		indexConfiguration.setCurrencies(List.of(currencyGBP));
		indexConfiguration.setSynonymDictionaries(Collections.singletonList(synonymDictionary));

		// when
		modelService.save(indexConfiguration);

		final Optional<SnIndexConfigurationModel> createdIndexConfigurationOptional = snIndexConfigurationDao
				.findIndexConfigurationById(ID1);

		// then
		assertTrue(createdIndexConfigurationOptional.isPresent());
		final SnIndexConfigurationModel createdIndexConfiguration = createdIndexConfigurationOptional.get();
		assertEquals(ID1, createdIndexConfiguration.getId());
		assertEquals(NAME1, createdIndexConfiguration.getName());

		Assertions.assertThat(createdIndexConfiguration.getLanguages()).hasSize(1);
		Assertions.assertThat(createdIndexConfiguration.getLanguages()).containsOnly(languageEN);
		Assertions.assertThat(createdIndexConfiguration.getCurrencies()).hasSize(1);
		Assertions.assertThat(createdIndexConfiguration.getCurrencies().stream().map(CurrencyModel::getIsocode))
				.containsOnly("GBP");

		Assertions.assertThat(createdIndexConfiguration.getSynonymDictionaries()).hasSize(1);
		Assertions.assertThat(createdIndexConfiguration.getSynonymDictionaries()).containsOnly(synonymDictionary);
	}

	@Test
	public void failToCreateIndexConfigurationWithInvalidId()
	{
		// given
		final SnIndexConfigurationModel indexConfiguration = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration.setId(INVALID_ID);
		indexConfiguration.setName(NAME1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexConfiguration);
	}

	@Test
	public void failToCreateIndexConfigurationWithoutId()
	{
		// given
		final SnIndexConfigurationModel indexConfiguration = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration.setName(NAME1);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexConfiguration);
	}

	@Test
	public void createMultipleIndexConfigurations()
	{
		// given
		final LanguageModel languageEN = commonI18NService.getLanguage("en");
		final CurrencyModel currencyGBP = commonI18NService.getCurrency("GBP");

		final SnIndexConfigurationModel indexConfiguration1 = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration1.setId(ID1);
		indexConfiguration1.setName(NAME1);
		indexConfiguration1.setLanguages(List.of(languageEN));
		indexConfiguration1.setCurrencies(List.of(currencyGBP));

		final LanguageModel languageDE = commonI18NService.getLanguage("de");
		final CurrencyModel currencyEUR = commonI18NService.getCurrency("EUR");
		final CurrencyModel currencyCHF = commonI18NService.getCurrency("CHF");

		final SnIndexConfigurationModel indexConfiguration2 = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration2.setId(ID2);
		indexConfiguration2.setName(NAME2);
		indexConfiguration2.setLanguages(List.of(languageDE));
		indexConfiguration2.setCurrencies(List.of(currencyEUR, currencyCHF));

		// when
		modelService.save(indexConfiguration1);
		modelService.save(indexConfiguration2);

		final Optional<SnIndexConfigurationModel> createdIndexConfiguration1Optional = snIndexConfigurationDao
				.findIndexConfigurationById(ID1);
		final Optional<SnIndexConfigurationModel> createdIndexConfiguration2Optional = snIndexConfigurationDao
				.findIndexConfigurationById(ID2);

		// then
		assertTrue(createdIndexConfiguration1Optional.isPresent());
		final SnIndexConfigurationModel createdIndexConfiguration1 = createdIndexConfiguration1Optional.get();
		assertEquals(ID1, createdIndexConfiguration1.getId());
		assertEquals(NAME1, createdIndexConfiguration1.getName());
		Assertions.assertThat(createdIndexConfiguration1.getLanguages()).hasSize(1);
		Assertions.assertThat(createdIndexConfiguration1.getLanguages().stream().map(LanguageModel::getIsocode)).containsOnly("en");
		Assertions.assertThat(createdIndexConfiguration1.getCurrencies()).hasSize(1);
		Assertions.assertThat(createdIndexConfiguration1.getCurrencies().stream().map(CurrencyModel::getIsocode))
				.containsOnly("GBP");

		assertTrue(createdIndexConfiguration2Optional.isPresent());
		final SnIndexConfigurationModel createdIndexConfiguration2 = createdIndexConfiguration2Optional.get();
		assertEquals(ID2, createdIndexConfiguration2.getId());
		assertEquals(NAME2, createdIndexConfiguration2.getName());
		Assertions.assertThat(createdIndexConfiguration2.getLanguages()).hasSize(1);
		Assertions.assertThat(createdIndexConfiguration2.getLanguages().stream().map(LanguageModel::getIsocode)).containsOnly("de");
		Assertions.assertThat(createdIndexConfiguration2.getCurrencies()).hasSize(2);
		Assertions.assertThat(createdIndexConfiguration2.getCurrencies().stream().map(CurrencyModel::getIsocode))
				.containsOnly("EUR", "CHF");
	}

	@Test
	public void failToCreateMultipleIndexConfigurationsWithSameId()
	{
		// given
		final SnIndexConfigurationModel indexConfiguration1 = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration1.setId(ID1);
		indexConfiguration1.setName(NAME1);

		final SnIndexConfigurationModel indexConfiguration2 = modelService.create(SnIndexConfigurationModel.class);
		indexConfiguration2.setId(ID1);
		indexConfiguration2.setName(NAME2);

		// expect
		expectedException.expect(ModelSavingException.class);

		// when
		modelService.save(indexConfiguration1);
		modelService.save(indexConfiguration2);
	}
}
