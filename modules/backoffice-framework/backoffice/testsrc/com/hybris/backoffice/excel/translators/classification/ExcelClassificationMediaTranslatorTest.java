/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.translators.classification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.translators.media.MediaFolderProvider;


@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationMediaTranslatorTest
{

	@Mock
	TypeService typeService;
	@Mock
	KeyGenerator mediaCodeGenerator;
	@Mock
	MediaFolderProvider mediaFolderProvider;
	@InjectMocks
	ExcelClassificationMediaTranslator excelClassificationMediaTranslator;


	@Test
	public void shouldHandleRequestWhenAttributeIsMedia()
	{
		// given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ComposedTypeModel composedType = mock(ComposedTypeModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);
		given(classAttributeAssignment.getReferenceType()).willReturn(composedType);
		given(composedType.getCode()).willReturn(MediaModel._TYPECODE);
		given(typeService.isAssignableFrom(MediaModel._TYPECODE, MediaModel._TYPECODE)).willReturn(true);

		// when
		final boolean canHandle = excelClassificationMediaTranslator.canHandleAttribute(attribute);

		// then
		assertThat(canHandle).isTrue();
	}

	@Test
	public void shouldNotHandleRequestWhenAttributeIsNotMedia()
	{
		// given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ComposedTypeModel composedType = mock(ComposedTypeModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);
		given(classAttributeAssignment.getReferenceType()).willReturn(composedType);
		given(composedType.getCode()).willReturn(ProductModel._TYPECODE);
		given(typeService.isAssignableFrom(MediaModel._TYPECODE, ProductModel._TYPECODE)).willReturn(false);

		// when
		final boolean canHandle = excelClassificationMediaTranslator.canHandleAttribute(attribute);

		// then
		assertThat(canHandle).isFalse();
	}

	@Test
	public void shouldGenerateReferenceFormatWithoutUrl()
	{
		// given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);
		excelClassificationMediaTranslator.setExportUrl(false);

		// when
		final String referenceFormat = excelClassificationMediaTranslator.singleReferenceFormat(attribute);

		// then
		assertThat(referenceFormat).isEqualTo("filePath:code:catalog:version:folder");
	}

	@Test
	public void shouldGenerateReferenceFormatWithUrl()
	{
		// given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);
		excelClassificationMediaTranslator.setExportUrl(true);

		// when
		final String referenceFormat = excelClassificationMediaTranslator.singleReferenceFormat(attribute);

		// then
		assertThat(referenceFormat).isEqualTo("filePath:code:catalog:version:folder:url");
	}

	@Test
	public void shouldExportEmptyResponseWhenValueIsNotMedia()
	{
		// given
		final FeatureValue featureValue = new FeatureValue("not a media");
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);

		// when
		final Optional<String> exportedValue = excelClassificationMediaTranslator.exportSingle(attribute, featureValue);

		// then
		assertThat(exportedValue).isEmpty();
	}

	@Test
	public void shouldExportResponseWithoutUrlWhenValueIsMedia()
	{
		// given
		final MediaModel media = mock(MediaModel.class);
		final MediaFolderModel mediaFolder = mock(MediaFolderModel.class);
		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		final CatalogModel catalog = mock(CatalogModel.class);
		final FeatureValue featureValue = new FeatureValue(media);
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);

		given(media.getCatalogVersion()).willReturn(catalogVersion);
		given(catalogVersion.getCatalog()).willReturn(catalog);
		given(media.getFolder()).willReturn(mediaFolder);
		given(mediaFolder.getQualifier()).willReturn("Public");
		given(catalogVersion.getVersion()).willReturn("Online");
		given(catalog.getId()).willReturn("Default");
		given(media.getCode()).willReturn("img1");
		excelClassificationMediaTranslator.setExportUrl(false);

		// when
		final Optional<String> exportedValue = excelClassificationMediaTranslator.exportSingle(attribute, featureValue);

		// then
		assertThat(exportedValue).isPresent();
		assertThat(exportedValue.get()).isEqualTo(":img1:Default:Online:Public");
	}

	@Test
	public void shouldExportResponseWithUrlWhenValueIsMedia()
	{
		// given
		final MediaModel media = mock(MediaModel.class);
		final MediaFolderModel mediaFolder = mock(MediaFolderModel.class);
		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		final CatalogModel catalog = mock(CatalogModel.class);
		final FeatureValue featureValue = new FeatureValue(media);
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
		attribute.setAttributeAssignment(classAttributeAssignment);

		given(media.getCatalogVersion()).willReturn(catalogVersion);
		given(catalogVersion.getCatalog()).willReturn(catalog);
		given(media.getFolder()).willReturn(mediaFolder);
		given(mediaFolder.getQualifier()).willReturn("Public");
		given(catalogVersion.getVersion()).willReturn("Online");
		given(catalog.getId()).willReturn("Default");
		given(media.getCode()).willReturn("img1");
		given(media.getDownloadURL()).willReturn("https://localhost:9002/media/img1.png");
		excelClassificationMediaTranslator.setExportUrl(true);

		// when
		final Optional<String> exportedValue = excelClassificationMediaTranslator.exportSingle(attribute, featureValue);

		// then
		assertThat(exportedValue).isPresent();
		assertThat(exportedValue.get()).isEqualTo(":img1:Default:Online:Public:\"https://localhost:9002/media/img1.png\"");
	}

	@Test
	public void shouldGenerateEmptyImpexWhenAttributeIsNotClassificationAttribute()
	{
		// given
		final ExcelAttribute excelAttribute = mock(ExcelAttribute.class);
		final ImportParameters importParameters = mock(ImportParameters.class);
		final ExcelImportContext excelImportContext = mock(ExcelImportContext.class);

		// when
		final Impex impex = excelClassificationMediaTranslator.importData(excelAttribute, importParameters, excelImportContext);

		// then
		assertThat(impex.getImpexes()).isEmpty();
	}

	@Test
	public void shouldGenerateImpexHeaderForNotLocalizedField()
	{
		// given
		final ExcelClassificationAttribute classificationAttribute = mockClassificationAttribute();

		// when
		final ImpexHeaderValue mediaHeader = excelClassificationMediaTranslator.createMediaHeader(classificationAttribute);

		// then
		assertThat(mediaHeader.getName()).isEqualTo(
				"@images(code,catalogVersion(version, catalog(id)))[system='Default',attributeType='null',version='Online',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
	}

	@Test
	public void shouldGenerateImpexHeaderForLocalizedField()
	{
		// given
		final ExcelClassificationAttribute classificationAttribute = mockClassificationAttribute();
		given(classificationAttribute.getIsoCode()).willReturn("en");

		// when
		final ImpexHeaderValue mediaHeader = excelClassificationMediaTranslator.createMediaHeader(classificationAttribute);

		// then
		assertThat(mediaHeader.getName()).isEqualTo(
				"@images(code,catalogVersion(version, catalog(id)))[system='Default',attributeType='null',version='Online',lang=en,translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
	}

	@Test
	public void shouldGenerateImpexForSingleValue()
	{
		// given
		final ExcelClassificationAttribute attribute = mockClassificationAttribute();
		final ImportParameters importParameters = mock(ImportParameters.class);
		final ExcelImportContext excelImportContext = mock(ExcelImportContext.class);
		final Map<String, String> params = new HashMap<>();
		params.put("code", "img1");
		params.put("filePath", "img.jpg");
		params.put("catalog", "Default");
		params.put("version", "Online");
		given(importParameters.getMultiValueParameters()).willReturn(Collections.singletonList(params));
		given(importParameters.getTypeCode()).willReturn(ProductModel._TYPECODE);

		// when
		final Impex impex = excelClassificationMediaTranslator.importData(attribute, importParameters, excelImportContext);

		// then
		assertThat(impex.getImpexes()).hasSize(2);
		verifyProductImpex(impex);
		verifyMediaImpex(impex);
	}

	private void verifyProductImpex(final Impex impex)
	{
		final ImpexForType productImpex = impex.findUpdates(ProductModel._TYPECODE);
		assertThat(productImpex.getImpexTable().columnKeySet()).hasSize(1);
		final List<ImpexHeaderValue> impexProductHeader = new ArrayList(productImpex.getImpexTable().row(0).keySet());
		final List<Object> impexProductValue = new ArrayList(productImpex.getImpexTable().row(0).values());
		assertThat(impexProductHeader.get(0).getName()).isEqualTo(
				"@images(code,catalogVersion(version, catalog(id)))[system='Default',attributeType='null',version='Online',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
		assertThat(impexProductValue.get(0)).isEqualTo("img1:Online:Default");
	}

	private void verifyMediaImpex(final Impex impex)
	{
		final ImpexForType mediaImpex = impex.findUpdates(MediaModel._TYPECODE);
		assertThat(mediaImpex.getImpexTable().columnKeySet()).hasSize(4);
		assertThat(mediaImpex.getImpexTable().columnKeySet()).extracting("name").contains("code",
				"catalogVersion(version,catalog(id))", "folder(qualifier)", "@media");
		assertThat(mediaImpex.getImpexTable().columnKeySet()).extracting("translator").contains(null, null, null,
				"de.hybris.platform.impex.jalo.media.MediaDataTranslator");
		assertThat(mediaImpex.getImpexTable().values()).contains("img1", "Online:Default", "", "img.jpg");
	}

	private ExcelClassificationAttribute mockClassificationAttribute()
	{
		final ExcelClassificationAttribute classificationAttribute = mock(ExcelClassificationAttribute.class);
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		final ClassificationSystemModel classificationSystem = mock(ClassificationSystemModel.class);
		final ClassificationSystemVersionModel classificationSystemVersion = mock(ClassificationSystemVersionModel.class);
		given(classificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
		given(classAttributeAssignment.getClassificationClass()).willReturn(classificationClass);
		given(classificationClass.getCatalogVersion()).willReturn(classificationSystemVersion);
		given(classificationAttribute.getQualifier()).willReturn("images");
		given(classificationSystemVersion.getCatalog()).willReturn(classificationSystem);
		given(classificationSystem.getId()).willReturn("Default");
		given(classificationSystemVersion.getVersion()).willReturn("Online");
		return classificationAttribute;
	}
}
