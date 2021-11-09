/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.populator;

import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOCALIZED;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.ATTRIBUTE_LOC_LANG;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_ATTRIBUTE;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_CLASS;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_SYSTEM;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.CLASSIFICATION_VERSION;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.FULL_NAME;
import static com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns.MANDATORY;
import static org.junit.Assert.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants.ClassificationTypeSystemColumns;


@RunWith(MockitoJUnitRunner.class)
public class ClassificationTypeSystemSheetCompressorTest
{
	@Mock
	CollectionFormatter mockedCollectionFormatter;
	@InjectMocks
	ClassificationTypeSystemSheetCompressor compressor;

	@Test
	public void shouldCompressClassificationRowWhenAttributeIsSameButLangIsDifferent()
	{
		// given
		final Collection<Map<ClassificationTypeSystemColumns, String>> rows = new ArrayList<>();
		rows.add(prepareRow("fullName1", "classificationClass", "en"));
		rows.add(prepareRow("fullName2", "classificationClass", "de"));

		given(mockedCollectionFormatter.formatToString("en", "de")).willReturn("{en},{de}");
		given(mockedCollectionFormatter.formatToString("fullName1", "fullName2")).willReturn("{fullName1},{fullName2}");

		// when
		final Collection<Map<ClassificationTypeSystemColumns, String>> compressionResult = compressor.compress(rows);

		// then
		assertThat(compressionResult).hasSize(1);
		final Map<ClassificationTypeSystemColumns, String> firstRow = compressionResult.iterator().next();
		assertThat(firstRow).includes(entry(FULL_NAME, "{fullName1},{fullName2}"));
		assertThat(firstRow).includes(entry(CLASSIFICATION_SYSTEM, "classificationSystem"));
		assertThat(firstRow).includes(entry(CLASSIFICATION_VERSION, "classificationVersion"));
		assertThat(firstRow).includes(entry(CLASSIFICATION_CLASS, "classificationClass"));
		assertThat(firstRow).includes(entry(CLASSIFICATION_ATTRIBUTE, "classificationAttribute"));
		assertThat(firstRow).includes(entry(ATTRIBUTE_LOCALIZED, "true"));
		assertThat(firstRow).includes(entry(ATTRIBUTE_LOC_LANG, "{en},{de}"));
		assertThat(firstRow).includes(entry(MANDATORY, "true"));
	}

	@Test
	public void shouldCompressClassificationRowWhenAttributeIsSameButClassIsDifferent()
	{
		// given
		final Collection<Map<ClassificationTypeSystemColumns, String>> rows = new ArrayList<>();
		rows.add(prepareRow("fullName", "classificationClass1", "en"));
		rows.add(prepareRow("fullName", "classificationClass2", "en"));

		// when
		final Collection<Map<ClassificationTypeSystemColumns, String>> compressionResult = compressor.compress(rows);

		// then
		final Map<ClassificationTypeSystemColumns, String> firstRow = compressionResult.stream()
				.filter(row -> row.get(CLASSIFICATION_CLASS).equals("classificationClass1")).findFirst().orElse(null);
		final Map<ClassificationTypeSystemColumns, String> secondRow = compressionResult.stream()
				.filter(row -> row.get(CLASSIFICATION_CLASS).equals("classificationClass2")).findFirst().orElse(null);

		assertThat(compressionResult).hasSize(2);
		assertNotNull(firstRow);
		assertNotNull(secondRow);
	}

	private Map<ClassificationTypeSystemColumns, String> prepareRow(final String fullName, final String classificationClass,
			final String language)
	{
		final Map<ClassificationTypeSystemColumns, String> row = new HashMap<ClassificationTypeSystemColumns, String>()
		{
			{
				put(FULL_NAME, fullName);
				put(CLASSIFICATION_SYSTEM, "classificationSystem");
				put(CLASSIFICATION_VERSION, "classificationVersion");
				put(CLASSIFICATION_CLASS, classificationClass);
				put(CLASSIFICATION_ATTRIBUTE, "classificationAttribute");
				put(ATTRIBUTE_LOCALIZED, "true");
				put(ATTRIBUTE_LOC_LANG, language);
				put(MANDATORY, "true");
			}
		};
		return row;
	}
}
