/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.translators.generic.factory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.parser.DefaultImportParameterParser;
import com.hybris.backoffice.excel.importing.parser.ParsedValues;
import com.hybris.backoffice.excel.importing.parser.matcher.DefaultExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;


public class DefaultImportImpexFactoryTest
{

	private final ReferenceImportImpexFactoryStrategy referenceImportImpexFactoryStrategy = new ReferenceImportImpexFactoryStrategy();

	@Test
	public void shouldPrepareImpexHeaderForCatalogVersion()
	{
		// given
		final ImportParameters importParameters = mock(ImportParameters.class);

		// when
		final ImpexHeaderValue impexHeaderValue = referenceImportImpexFactoryStrategy
				.prepareImpexHeader(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(true, true), importParameters);

		// then
		assertThat(impexHeaderValue.isUnique()).isTrue();
		assertThat(impexHeaderValue.getName()).isEqualTo("catalogVersion(version,catalog(id))");
		assertThat(impexHeaderValue.getLang()).isEqualTo(null);
	}

	@Test
	public void shouldPrepareImpexHeaderForLocalizedReference()
	{
		// given
		final ImportParameters importParameters = mock(ImportParameters.class);
		given(importParameters.getIsoCode()).willReturn("en");

		// when
		final ImpexHeaderValue impexHeaderValue = referenceImportImpexFactoryStrategy
				.prepareImpexHeader(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(true, true), importParameters);

		// then
		assertThat(impexHeaderValue.isUnique()).isTrue();
		assertThat(impexHeaderValue.getName()).isEqualTo("catalogVersion(version,catalog(id))");
		assertThat(impexHeaderValue.getLang()).isEqualTo("en");
	}

	@Test
	public void shouldPrepareImpexHeaderForSupercategories()
	{
		// given
		final ImportParameters importParameters = mock(ImportParameters.class);

		// when
		final ImpexHeaderValue impexHeaderValue = referenceImportImpexFactoryStrategy
				.prepareImpexHeader(RequiredAttributeTestFactory.prepareStructureForSupercategories(false, true), importParameters);

		// then
		assertThat(impexHeaderValue.isUnique()).isFalse();
		assertThat(impexHeaderValue.getName()).isEqualTo("supercategories(code,catalogVersion(version,catalog(id)))");
	}

	@Test
	public void shouldPrepareImpexValueForCatalogVersion()
	{
		// given

		final ParsedValues parsedValues = createDefaultImportParameterParser().parseValue("CatalogVersion.version:Catalog.id", "",
				"Online:Default");
		final ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null,
				parsedValues.getParameters());

		// when
		final String impexValue = referenceImportImpexFactoryStrategy
				.prepareImpexValue(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(), importParameters);

		// then
		assertThat(impexValue).isEqualTo("Online:Default");
	}

	@Test
	public void shouldPrepareImpexValueForSupercategories()
	{
		// given
		final ParsedValues parsedValues = createDefaultImportParameterParser().parseValue(
				"Category.code:CatalogVersion.version:Catalog.id", ":Online:Default",
				"First:Online:Default,Second:Online:,Third::Default");
		final ImportParameters importParameters = new ImportParameters(null, null, parsedValues.getCellValue(), null,
				parsedValues.getParameters());

		// when
		final String impexValue = referenceImportImpexFactoryStrategy
				.prepareImpexValue(RequiredAttributeTestFactory.prepareStructureForSupercategories(), importParameters);

		// then
		assertThat(impexValue).isEqualTo("First:Online:Default,Second:Online:Default,Third:Online:Default");
	}

	private DefaultImportParameterParser createDefaultImportParameterParser()
	{
		final DefaultImportParameterParser parser = new DefaultImportParameterParser();
		parser.setMatcher(new DefaultExcelParserMatcher());
		parser.setSplitter(new DefaultExcelParserSplitter());
		return parser;
	}
}
