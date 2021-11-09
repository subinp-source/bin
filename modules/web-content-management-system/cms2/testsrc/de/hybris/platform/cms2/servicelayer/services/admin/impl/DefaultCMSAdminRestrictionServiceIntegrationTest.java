/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.services.admin.impl;

import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminRestrictionService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCMSAdminRestrictionServiceIntegrationTest extends ServicelayerTest
{
	@Resource
	private CMSAdminRestrictionService cmsAdminRestrictionService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private CMSAdminSiteService cmsAdminSiteService;
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private ProductService productService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	private PreviewDataModel previewContext;

	private CatalogVersionModel catalogVersion;
	private AbstractPageModel page;
	private AbstractRestrictionModel restriction;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultUsers();
		createDefaultCatalog();

		catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		createPreviewContext();

		cmsAdminSiteService.setActiveCatalogVersion(catalogVersion);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.admin.impl.DefaultCMSAdminRestrictionService#getCategories(de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel, de.hybris.platform.cms2.model.preview.PreviewDataModel)}
	 * .
	 */
	@Test
	public void shouldGetCategoriesForRestriction()
	{
		// given
		final List<CategoryModel> categories = new ArrayList<CategoryModel>();
		categories.add(categoryService.getCategoryForCode("testCategory0"));
		categories.add(categoryService.getCategoryForCode("testCategory1"));
		categories.add(categoryService.getCategoryForCode("testCategory2"));
		final CMSCategoryRestrictionModel categoryRestriction = modelService.create(CMSCategoryRestrictionModel.class);
		categoryRestriction.setUid("FooBar");
		categoryRestriction.setCategories(categories);
		modelService.save(categoryRestriction);

		// when
		final List<CategoryModel> restrictedCategories = cmsAdminRestrictionService.getCategories(categoryRestriction,
				previewContext);

		// then
		assertThat(restrictedCategories).hasSize(3);
		assertThat(CollectionUtils.isEqualCollection(restrictedCategories, categories)).isTrue();
		restrictedCategories.get(0).getCatalogVersion().equals(catalogVersion);
		restrictedCategories.get(1).getCatalogVersion().equals(catalogVersion);
		restrictedCategories.get(2).getCatalogVersion().equals(catalogVersion);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.cms2.servicelayer.services.admin.impl.DefaultCMSAdminRestrictionService#getProducts(de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel, de.hybris.platform.cms2.model.preview.PreviewDataModel)}
	 * .
	 */
	@Test
	public void shouldGetProductsForRestriction()
	{
		// given
		final List<ProductModel> products = new ArrayList<ProductModel>();
		products.add(productService.getProductForCode("testProduct0"));
		products.add(productService.getProductForCode("testProduct1"));
		products.add(productService.getProductForCode("testProduct2"));
		final CMSProductRestrictionModel productRestriction = modelService.create(CMSProductRestrictionModel.class);
		productRestriction.setUid("FooBar");
		productRestriction.setProducts(products);
		modelService.save(productRestriction);

		// when
		final List<ProductModel> restrictedProducts = cmsAdminRestrictionService.getProducts(productRestriction, previewContext);

		// then
		assertThat(restrictedProducts).hasSize(3);
		assertThat(CollectionUtils.isEqualCollection(restrictedProducts, products)).isTrue();
		restrictedProducts.get(0).getCatalogVersion().equals(catalogVersion);
		restrictedProducts.get(1).getCatalogVersion().equals(catalogVersion);
		restrictedProducts.get(2).getCatalogVersion().equals(catalogVersion);
	}

	@Test
	public void shouldFindRestrictionsForPage()
	{
		createPageWithRestriction();

		final Collection<AbstractRestrictionModel> result = cmsAdminRestrictionService.getRestrictionsForPage(page);

		assertThat(result).contains(restriction);
	}

	@Test
	public void hasPageRestrictions()
	{
		createPageWithRestriction();

		final boolean result = cmsAdminRestrictionService.hasPageRestrictions(page);

		Assert.assertTrue(result);
	}

	protected void createPageWithRestriction()
	{
		final PageTemplateModel pageTemplate = new PageTemplateModel();
		pageTemplate.setUid("test-template");
		pageTemplate.setCatalogVersion(catalogVersion);

		page = new CategoryPageModel();
		page.setUid("test-page");
		page.setCatalogVersion(catalogVersion);
		page.setMasterTemplate(pageTemplate);

		restriction = modelService.create(CMSTimeRestrictionModel.class);
		restriction.setUid("FooBar");
		restriction.setCatalogVersion(catalogVersion);
		restriction.setPages(Arrays.asList(page));

		modelService.saveAll(pageTemplate, page, restriction);
	}

	protected void createPreviewContext()
	{
		final Collection<CatalogVersionModel> catalogVersions = new ArrayList<CatalogVersionModel>();
		catalogVersions.add(catalogVersion);
		previewContext = modelService.create(PreviewDataModel.class);
		previewContext.setCatalogVersions(catalogVersions);
		previewContext.setUser(userService.getUserForUID("ahertz"));
		previewContext.setLanguage(commonI18NService.getLanguage("en"));
		modelService.save(previewContext);
	}

}
