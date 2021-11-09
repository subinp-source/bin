/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.i18n.CockpitLocaleService;


@RunWith(MockitoJUnitRunner.class)
public class DefaultTypeSystemLocalizationHelperTest
{
	public static final String PRICE = "Price";
	public static final String ISO_EN = "en";
	public static final String ISO_ES_CO = "es_CO";
	public static final String EN_PRODUCT_PRICE_DESC = "enProduct.Price.desc";
	public static final String EN_PRODUCT_PRICE = "enProduct.Price";
	public static final String ES_COPRODUCT_PRICE = "es_COProduct.Price";

	private static final Locale en = Locale.ENGLISH;
	private static final Locale esCo = new Locale("es", "CO");

	@InjectMocks
	@Spy
	private DefaultTypeSystemLocalizationHelper helper;
	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private CockpitLocaleService cockpitLocaleService;
	@Mock
	private ComposedTypeModel productType;
	@Mock
	private AttributeDescriptorModel productPriceAttribute;

	@Before
	public void setUp()
	{
		doReturn(List.of(en, esCo)).when(cockpitLocaleService).getAllUILocales();

		doReturn(ProductModel._TYPECODE).when(productType).getCode();

		doReturn(PRICE).when(productPriceAttribute).getQualifier();
		doReturn(productType).when(productPriceAttribute).getEnclosingType();
	}

	@Test
	public void shouldLoadLocalizationOnFirstLocalizeType()
	{
		final SearchResult mock = new SearchResultImpl(List.of(List.of(ProductModel._TYPECODE, ISO_EN, "enProduct"),
				List.of(ProductModel._TYPECODE, ISO_ES_CO, "es_COProduct")), 2, 2, 0);

		doReturn(mock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

		final DataType.Builder builder = mock(DataType.Builder.class);

		helper.localizeType(productType, builder);

		verify(helper).loadTypeLocalization();

		final ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verify(builder).labels(captor.capture());

		assertThat(captor.getValue()).containsKeys(en, esCo);
	}

	@Test
	public void shouldLoadLocalizationOnFirstLocalizeAttribute()
	{
		final SearchResult mock = new SearchResultImpl(
				List.of(List.of(ProductModel._TYPECODE, PRICE, ISO_EN, EN_PRODUCT_PRICE, EN_PRODUCT_PRICE_DESC),
						List.of(ProductModel._TYPECODE, PRICE, ISO_ES_CO, ES_COPRODUCT_PRICE, EN_PRODUCT_PRICE_DESC)),
				2, 2, 0);

		doReturn(mock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

		final DataAttribute.Builder builder = mock(DataAttribute.Builder.class);

		helper.localizeAttribute(productPriceAttribute, builder);

		verify(helper).loadAttributeLocalization();

		final ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verify(builder).labels(captor.capture());

		assertThat(captor.getValue()).containsKeys(en, esCo);
	}

	@Test
	public void shouldLoadLocalesMap()
	{
		final Map<String, Locale> localesMap = helper.loadLocalesMap();

		assertThat(localesMap).containsKeys(ISO_EN, ISO_ES_CO);
	}

	@Test
	public void shouldLoadTypeLocalizationsOnce()
	{
		final SearchResult mock = new SearchResultImpl(List.of(List.of(ProductModel._TYPECODE, ISO_EN, "enProduct"),
				List.of(ProductModel._TYPECODE, ISO_ES_CO, "es_COProduct")), 2, 2, 0);

		doReturn(mock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

		helper.localizeType(productType, mock(DataType.Builder.class));
		helper.localizeType(productType, mock(DataType.Builder.class));

		verify(helper).loadTypeLocalization();
	}

	@Test
	public void shouldLoadAttributeLocalizationsOnce()
	{
		final SearchResult mock = new SearchResultImpl(
				List.of(List.of(ProductModel._TYPECODE, PRICE, ISO_EN, EN_PRODUCT_PRICE, EN_PRODUCT_PRICE_DESC),
						List.of(ProductModel._TYPECODE, PRICE, ISO_ES_CO, ES_COPRODUCT_PRICE, EN_PRODUCT_PRICE_DESC)),
				2, 2, 0);

		doReturn(mock).when(flexibleSearchService).search(any(FlexibleSearchQuery.class));

		helper.localizeAttribute(productPriceAttribute, mock(DataAttribute.Builder.class));
		helper.localizeAttribute(productPriceAttribute, mock(DataAttribute.Builder.class));

		verify(helper).loadAttributeLocalization();
	}
}
