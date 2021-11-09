/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.hybris.platform.servicelayer.type.TypeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributeTestFactory;


public class ExcelGenericReferenceValidatorTest
{

	@Mock
	TypeService typeService;

	@InjectMocks
	ExcelGenericReferenceValidator excelGenericReferenceValidator;

	@Before
	public void setUpMockito()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldNotHandleRequestWhenTypeIsBlacklisted()
	{
		// given
		final List<String> blacklistedTypes = Collections.singletonList(MediaModel._TYPECODE);
		final ImportParameters importParameters = mock(ImportParameters.class);
		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		final ComposedTypeModel mediaComposedType = mock(ComposedTypeModel.class);
		given(attributeDescriptorModel.getAttributeType()).willReturn(mediaComposedType);
		given(mediaComposedType.getCode()).willReturn(MediaModel._TYPECODE);
		given(importParameters.isCellValueNotBlank()).willReturn(true);
		given(typeService.isAssignableFrom(MediaModel._TYPECODE, MediaModel._TYPECODE)).willReturn(true);
		excelGenericReferenceValidator.setBlacklistedTypes(blacklistedTypes);
		
		// when
		final boolean result = excelGenericReferenceValidator.canHandle(importParameters, attributeDescriptorModel);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldHandleRequestWhenTypeIsNotBlacklisted()
	{
		// given
		final List<String> blacklistedTypes = Collections.singletonList(MediaModel._TYPECODE);
		final ImportParameters importParameters = mock(ImportParameters.class);
		final AttributeDescriptorModel attributeDescriptorModel = mock(AttributeDescriptorModel.class);
		final ComposedTypeModel productComposedType = mock(ComposedTypeModel.class);
		given(attributeDescriptorModel.getAttributeType()).willReturn(productComposedType);
		given(productComposedType.getCode()).willReturn(ProductModel._TYPECODE);
		given(importParameters.isCellValueNotBlank()).willReturn(true);
		given(typeService.isAssignableFrom(MediaModel._TYPECODE, MediaModel._TYPECODE)).willReturn(true);
		excelGenericReferenceValidator.setBlacklistedTypes(blacklistedTypes);

		// when
		final boolean result = excelGenericReferenceValidator.canHandle(importParameters, attributeDescriptorModel);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldBuildFlexibleQueryToCheckWhetherCatalogExist()
	{
		// given
		final Map<String, String> params = new HashMap<>();
		params.put("Catalog.id", "Default");

		// when
		final Optional<FlexibleSearchQuery> flexibleSearchQuery = excelGenericReferenceValidator
				.buildFlexibleSearchQuery(RequiredAttributeTestFactory.prepareStructureForCatalog(), params, new HashMap<>());

		// then
		assertThat(flexibleSearchQuery).isPresent();
		assertThat(flexibleSearchQuery.get().getQuery()).isEqualToIgnoringCase("SELECT {pk} FROM {Catalog} WHERE {id} = ?id");
		assertThat(flexibleSearchQuery.get().getQueryParameters()).containsKeys("id");
		assertThat(flexibleSearchQuery.get().getQueryParameters()).containsValues("Default");
	}

	@Test
	public void shouldBuildFlexibleQueryToCheckWhetherCatalogVersionExist()
	{
		// given
		final long catalogPk = 123L;
		final Map<String, String> params = new HashMap<>();
		params.put("CatalogVersion.version", "Online");
		params.put("Catalog.id", "Default");
		final HashMap context = new HashMap();
		context.put("Catalog_Default", prepareCatalog(catalogPk));

		// when
		final Optional<FlexibleSearchQuery> flexibleSearchQuery = excelGenericReferenceValidator
				.buildFlexibleSearchQuery(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(), params, context);

		// then
		assertThat(flexibleSearchQuery).isPresent();
		assertThat(flexibleSearchQuery.get().getQuery())
				.isEqualToIgnoringCase("SELECT {pk} FROM {CatalogVersion} WHERE {version} = ?version AND {catalog} = ?catalog");
		assertThat(flexibleSearchQuery.get().getQueryParameters()).containsKeys("version", "catalog");
		assertThat(flexibleSearchQuery.get().getQueryParameters()).containsValues("Online", catalogPk);
	}

	@Test
	public void shouldBuildFlexibleQueryToCheckWhetherSupercategoryExist()
	{
		// given
		final long catalogPk = 123L;
		final long catalogVersionPk = 987L;
		final Map<String, String> params = new HashMap<>();
		params.put("Category.code", "Hardware");
		params.put("CatalogVersion.version", "Online");
		params.put("Catalog.id", "Default");
		final HashMap<String, Object> context = new HashMap<>();
		context.put("Catalog_Default", prepareCatalog(catalogPk));
		context.put("CatalogVersion_Online_Default", prepareCatalogVersion(catalogVersionPk));

		// when
		final Optional<FlexibleSearchQuery> flexibleSearchQuery = excelGenericReferenceValidator
				.buildFlexibleSearchQuery(RequiredAttributeTestFactory.prepareStructureForSupercategories(), params, context);

		// then
		assertThat(flexibleSearchQuery).isPresent();
		assertThat(flexibleSearchQuery.get().getQuery())
				.isEqualToIgnoringCase("SELECT {pk} FROM {Category} WHERE {code} = ?code AND {catalogVersion} = ?catalogVersion");
		assertThat(flexibleSearchQuery.get().getQueryParameters()).containsKeys("code", "catalogVersion");
		assertThat(flexibleSearchQuery.get().getQueryParameters()).containsValues("Hardware", catalogVersionPk);
	}

	private CatalogModel prepareCatalog(final long pkValue)
	{
		final CatalogModel catalogModel = Mockito.mock(CatalogModel.class);
		given(catalogModel.getPk()).willReturn(PK.fromLong(pkValue));
		return catalogModel;
	}

	private CatalogVersionModel prepareCatalogVersion(final long pkValue)
	{
		final CatalogVersionModel catalogVersionModel = Mockito.mock(CatalogVersionModel.class);
		given(catalogVersionModel.getPk()).willReturn(PK.fromLong(pkValue));
		return catalogVersionModel;
	}
}
