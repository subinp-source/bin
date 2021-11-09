/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryPageModelToDataRenderingPopulatorTest
{
	@Mock
	private Predicate<ItemModel> categoryPageTypePredicate;
	@InjectMocks
	private CategoryPageModelToDataRenderingPopulator populator;

	@Mock
	private CMSCategoryRestrictionModel categoryRestriction1;
	@Mock
	private CMSCategoryRestrictionModel categoryRestriction2;
	@Mock
	private CategoryModel categoryA;
	@Mock
	private CategoryModel categoryB;
	@Mock
	private CategoryModel categoryC;

	@Test
	public void shouldPopulateCategoryCodesForCategoryPage()
	{
		final Map<String, Object> targetMap = new HashMap<>();
		final CategoryPageModel categoryPage = new CategoryPageModel();
		categoryPage.setRestrictions(Arrays.asList(categoryRestriction1, categoryRestriction2));
		doReturn(Boolean.TRUE).when(categoryPageTypePredicate).test(categoryPage);
		doReturn(Arrays.asList(categoryA, categoryB)).when(categoryRestriction1).getCategories();
		doReturn(Arrays.asList(categoryC)).when(categoryRestriction2).getCategories();
		doReturn("catA").when(categoryA).getCode();
		doReturn("catB").when(categoryB).getCode();
		doReturn("catC").when(categoryC).getCode();

		populator.populate(categoryPage, targetMap);

		assertThat((Collection<String>) targetMap.get("categoryCodes"), hasItems("catA", "catB", "catC"));
	}

	@Test
	public void shouldNotPopulateCategoryCodesForNonCategoryPage()
	{
		final Map<String, Object> targetMap = new HashMap<>();
		final AbstractPageModel page = new AbstractPageModel();
		doReturn(Boolean.FALSE).when(categoryPageTypePredicate).test(page);

		populator.populate(page, targetMap);

		assertThat(targetMap.get("categoryCodes"), nullValue());
	}

}
