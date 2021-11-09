/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ruleengineservices.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Locale;
import java.util.Set;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryToProductCodesMapperTest
{
	@Mock
	private ProductService productService;

	@Mock
	private SessionService sessionService;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private CategoryModel category;

	@Mock
	private LanguageModel language;

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private CommonI18NService commonI18NService;

	@Mock
	private I18NService i18NService;

	@InjectMocks
	private CategoryToProductCodesMapper mapper;

	@Before
	public void setUp()
	{
		when(category.getCode()).thenReturn("categoryCode");
		when(category.getCatalogVersion()).thenReturn(catalogVersion);

		when(i18NService.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		when(commonI18NService.getLanguage(Locale.ENGLISH.getLanguage())).thenReturn(language);

		when(sessionService.executeInLocalView(any(SessionExecutionBody.class))).thenAnswer(
				invocationOnMock -> ((SessionExecutionBody) invocationOnMock.getArguments()[0]).execute());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIAEOnNullParameter()
	{
		mapper.apply(null);
	}

	@Test
	public void shouldMapCategoryToCodesCorrectly()
	{
		final ProductModel product1 = mock(ProductModel.class);
		when(product1.getCode()).thenReturn("product1");

		final ProductModel product2 = mock(ProductModel.class);
		when(product2.getCode()).thenReturn("product2");

		final ProductModel product3 = mock(ProductModel.class);
		when(product3.getCode()).thenReturn("product3");

		final ProductModel product4 = mock(ProductModel.class);
		when(product4.getCode()).thenReturn("product1");

		when(productService.getProductsForCategory(category)).thenReturn(List.of(product1, product2, product3,
				product4));

		final Set<String> result = mapper.apply(category);

		assertThat(result).isNotEmpty().containsExactlyInAnyOrder("product1", "product2", "product3");

		verify(catalogVersionService).setSessionCatalogVersions(anyListOf(CatalogVersionModel.class));
		verify(commonI18NService).setCurrentLanguage(language);
		verify(productService).getProductsForCategory(category);
	}
}
