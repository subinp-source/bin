/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel.URL;
import static de.hybris.platform.cmsfacades.rendering.populators.LinkComponentModelToDataPopulator.CATEGORY_PREFIX;
import static de.hybris.platform.cmsfacades.rendering.populators.LinkComponentModelToDataPopulator.PRODUCT_PREFIX;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Map;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class LinkComponentModelToDataPopulatorTest
{
	private static final String PAGE_LABEL = "/faq";
	private static final String PRODUCT_CODE = "prod111";
	private static final String CATEGORY_CODE = "cat2222";

	@Mock
	private Predicate<CMSItemModel> linkComponentPredicate;
	@Mock
	private Map<String, Object> stringObjectMap;
	@Mock
	private CMSLinkComponentModel cmsLinkComponent;
	@Mock
	private ContentPageModel contentPage;
	@Mock
	private ProductModel product;
	@Mock
	private CategoryModel category;

	@InjectMocks
	private LinkComponentModelToDataPopulator populator;

	@Before
	public void setUp()
	{
		when(linkComponentPredicate.test(cmsLinkComponent)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void shouldNotPopulateUrl()
	{
		populator.populate(cmsLinkComponent, stringObjectMap);

		verify(stringObjectMap, never()).put(anyString(), anyObject());
	}

	@Test
	public void shouldNotPopulateUrlForExternalLink()
	{
		//External link could be a full path to an external page or a relative path to another category or product page
		when(cmsLinkComponent.getUrl()).thenReturn("/Brands/c/category-code");

		populator.populate(cmsLinkComponent, stringObjectMap);

		verify(stringObjectMap, never()).put(anyString(), anyObject());
	}

	@Test
	public void shouldPopulateUrlWithContentPageLabel()
	{
		when(cmsLinkComponent.getContentPage()).thenReturn(contentPage);
		when(contentPage.getLabel()).thenReturn(PAGE_LABEL);

		populator.populate(cmsLinkComponent, stringObjectMap);

		verify(stringObjectMap).put(CMSLinkComponentModel.URL, PAGE_LABEL);
	}

	@Test
	public void shouldPopulateUrlWithProductCode()
	{
		when(cmsLinkComponent.getProduct()).thenReturn(product);
		when(product.getCode()).thenReturn(PRODUCT_CODE);

		populator.populate(cmsLinkComponent, stringObjectMap);

		verify(stringObjectMap).put(URL, PRODUCT_PREFIX + PRODUCT_CODE);
	}

	@Test
	public void shouldPopulateUrlWithCategoryCode()
	{
		when(cmsLinkComponent.getCategory()).thenReturn(category);
		when(category.getCode()).thenReturn(CATEGORY_CODE);

		populator.populate(cmsLinkComponent, stringObjectMap);

		verify(stringObjectMap).put(URL, CATEGORY_PREFIX + CATEGORY_CODE);
	}

}
