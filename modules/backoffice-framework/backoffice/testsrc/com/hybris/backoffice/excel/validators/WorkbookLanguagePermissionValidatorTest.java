/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.importing.ExcelAttributeTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelClassificationTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelTypeSystemService;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;


@RunWith(MockitoJUnitRunner.class)
public class WorkbookLanguagePermissionValidatorTest
{
	@Mock
	private ExcelHeaderService excelHeaderService;
	@Mock
	private ExcelSheetService excelSheetService;
	@Mock
	private ExcelTypeSystemService excelTypeSystemService;
	@Mock
	private ExcelClassificationTypeSystemService excelClassificationTypeSystemService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private ServicelayerManager servicelayerManager;

	@Mock
	private ModelService modelService;
	@Mock
	private Workbook workbook;
	@Mock
	private Sheet productSheet;
	@Mock
	private Sheet categorySheet;

	@Spy
	@InjectMocks
	private WorkbookLanguagePermissionValidator validator;

	private static final String ENGLISH_ISO_CODE = "en";
	private static final String GERMAN_ISO_CODE = "de";
	private final Language enLanguage = mock(Language.class);
	private final LanguageModel enLangModel = mock(LanguageModel.class);
	private final Locale enLocale = new Locale(ENGLISH_ISO_CODE);
	private final Locale deLocale = new Locale(GERMAN_ISO_CODE);
	private final List<String> productAttributeNames = new ArrayList<>();
	private final List<String> categoryAttributeNames = new ArrayList<>();

	private final ExcelAttributeTypeSystemService.ExcelTypeSystem excelTypeSystem = mock(
			ExcelAttributeTypeSystemService.ExcelTypeSystem.class);
	private final ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem excelClassificationTypeSystem = mock(
			ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem.class);

	@Before
	public void setup()
	{
		when(excelSheetService.getSheets(workbook)).thenReturn(Arrays.asList(productSheet, categorySheet));
		when(productSheet.getWorkbook()).thenReturn(workbook);
		when(categorySheet.getWorkbook()).thenReturn(workbook);
		when(excelTypeSystemService.loadTypeSystem(workbook)).thenReturn(excelTypeSystem);
		when(excelClassificationTypeSystemService.loadTypeSystem(workbook)).thenReturn(excelClassificationTypeSystem);

		when(excelTypeSystem.findRow(any())).thenReturn(Optional.empty());
		when(excelClassificationTypeSystem.findRow(any())).thenReturn(Optional.empty());

		when(validator.getServicelayerManager()).thenReturn(servicelayerManager);
		when(excelHeaderService.getHeaderDisplayNames(productSheet)).thenReturn(productAttributeNames);
		when(excelHeaderService.getHeaderDisplayNames(categorySheet)).thenReturn(categoryAttributeNames);
		when(modelService.get(enLanguage)).thenReturn(enLangModel);
		when(commonI18NService.getLocaleForLanguage(enLangModel)).thenReturn(enLocale);
		when(commonI18NService.getLocaleForIsoCode(ENGLISH_ISO_CODE)).thenReturn(enLocale);
		when(commonI18NService.getLocaleForIsoCode(GERMAN_ISO_CODE)).thenReturn(deLocale);
	}

	@Test
	public void shouldNotReturnValidationErrorsWhenUserHaveSufficientLanguagePermission()
	{
		// given
		productAttributeNames.addAll(Arrays.asList("description[en]", "code", "testClass.testFeature[en]"));
		mockTypeSystemRow("description", ENGLISH_ISO_CODE);
		mockTypeSystemRow("code", null);
		mockClassificationTypeSystemRow("testClass", "testFeature", ENGLISH_ISO_CODE);

		when(servicelayerManager.getAllWritableLanguages()).thenReturn(Set.of(enLanguage));
		when(servicelayerManager.getAllReadableLanguages()).thenReturn(Set.of(enLanguage));

		// when
		final List<ExcelValidationResult> validationResults = validator.validate(workbook);

		// then
		assertThat(validationResults).isEmpty();
	}

	@Test
	public void shouldNotReturnValidationErrorsWhenAttributesUnknown()
	{
		// given
		productAttributeNames.addAll(Arrays.asList("test1[en]", "test2", "testClass.testFeature[de]"));

		when(servicelayerManager.getAllWritableLanguages()).thenReturn(null);
		when(servicelayerManager.getAllReadableLanguages()).thenReturn(null);

		// when
		final List<ExcelValidationResult> validationResults = validator.validate(workbook);

		// then
		assertThat(validationResults).isEmpty();
	}

	@Test
	public void shouldNotReturnValidationErrorsWhenNoLocalizedAttributeExist()
	{
		// given
		productAttributeNames.addAll(Arrays.asList("code", "testClass.testFeature"));
		categoryAttributeNames.addAll(Arrays.asList("Identifier"));

		mockTypeSystemRow("code", null);
		mockTypeSystemRow("Identifier", null);
		mockClassificationTypeSystemRow("testClass", "testFeature", null);

		when(servicelayerManager.getAllWritableLanguages()).thenReturn(null);
		when(servicelayerManager.getAllReadableLanguages()).thenReturn(null);

		// when
		final List<ExcelValidationResult> validationResults = validator.validate(workbook);

		// then
		assertThat(validationResults).isEmpty();
	}

	@Test
	public void shouldReturnValidationErrorsWhenNoLanguagePermission()
	{
		// given
		productAttributeNames.addAll(Arrays.asList("description[en]", "testClass.testFeature[de]"));
		categoryAttributeNames.addAll(Arrays.asList("name[de]"));

		mockTypeSystemRow("description", ENGLISH_ISO_CODE);
		mockTypeSystemRow("name", GERMAN_ISO_CODE);
		mockClassificationTypeSystemRow("testClass", "testFeature", GERMAN_ISO_CODE);

		when(servicelayerManager.getAllWritableLanguages()).thenReturn(null);
		when(servicelayerManager.getAllReadableLanguages()).thenReturn(null);

		// when
		final List<ExcelValidationResult> validationResults = validator.validate(workbook);

		// then
		assertValidationResult(validationResults, 2, Set.of(ENGLISH_ISO_CODE, GERMAN_ISO_CODE));
	}

	@Test
	public void shouldReturnValidationErrorsWhenNoLanguageWritablePermission()
	{
		// given
		productAttributeNames.addAll(Arrays.asList("description[en]"));

		mockTypeSystemRow("description", ENGLISH_ISO_CODE);
		when(servicelayerManager.getAllWritableLanguages()).thenReturn(null);
		when(servicelayerManager.getAllReadableLanguages()).thenReturn(Set.of(enLanguage));

		// when
		final List<ExcelValidationResult> validationResults = validator.validate(workbook);

		// then
		assertValidationResult(validationResults, 1, Set.of(ENGLISH_ISO_CODE));
	}

	@Test
	public void shouldReturnValidationErrorsWhenNoLanguageReadablePermission()
	{
		// given
		productAttributeNames.addAll(Arrays.asList("description[en]"));
		mockTypeSystemRow("description", ENGLISH_ISO_CODE);
		when(servicelayerManager.getAllWritableLanguages()).thenReturn(Set.of(enLanguage));
		when(servicelayerManager.getAllReadableLanguages()).thenReturn(Set.of());

		// when
		final List<ExcelValidationResult> validationResults = validator.validate(workbook);

		// then
		assertValidationResult(validationResults, 1, Set.of(ENGLISH_ISO_CODE));
	}

	private void assertValidationResult(final List<ExcelValidationResult> validationResults, final int expectedErrorLength,
			final Set<Serializable> expectedLangSet)
	{
		assertThat(validationResults).hasSize(1);
		assertThat(validationResults.get(0).getHeader().getMessageKey())
				.isEqualTo(validator.EXCEL_IMPORT_VALIDATION_WORKBOOK_LANGUAGE_PERMISSION_HEADER);
		final List<ValidationMessage> validationMessages = validationResults.get(0).getValidationErrors();
		assertThat(validationMessages).hasSize(expectedErrorLength);
		validationMessages.stream().forEach(message -> {
			assertThat(message.getMessageKey()).isEqualTo(validator.EXCEL_IMPORT_VALIDATION_WORKBOOK_LANGUAGE_PERMISSION_DETAIL);
			assertThat(message.getParams()).hasSize(1);
			assertThat(expectedLangSet).contains(message.getParams()[0]);
		});
	}

	private void mockTypeSystemRow(final String name, final String isoCode)
	{
		final String fullName = isoCode == null ? name : name + "[" + isoCode + "]";
		final TypeSystemRow typeSystemRow = new TypeSystemRow();
		typeSystemRow.setAttrDisplayName(fullName);
		typeSystemRow.setAttrLocLang(isoCode);
		given(excelTypeSystem.findRow(fullName)).willReturn(Optional.of(typeSystemRow));
	}

	private void mockClassificationTypeSystemRow(final String clazz, final String attribute, final String isoCode)
	{
		final String fullName = isoCode == null ? clazz + "." + attribute : clazz + "." + attribute + "[" + isoCode + "]";
		final ClassificationTypeSystemRow row = new ClassificationTypeSystemRow();
		row.setClassificationClass(clazz);
		row.setClassificationAttribute(attribute);
		row.setIsoCode(isoCode);
		given(excelClassificationTypeSystem.findRow(fullName)).willReturn(Optional.of(row));
	}
}
