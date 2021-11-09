/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.profiletagaddon.interceptors.beforeview;

import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Extends the {@link de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler} to include a list of category ids
 * in the placeholder 'categoryIdList' whenever a product or a category is viewed so that affinity of the user can be incremented for multiple
 * categories rather than just the immediate category being viewed.
 */
public class CategoryCollectionBeforeViewHandler implements BeforeViewHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryCollectionBeforeViewHandler.class);

    private static final String PRODUCT_KEY = "product";
    private static final String CATEGORIES_KEY = "categoryIdList";
    private static final String PRODUCT_VIEW_NAME = "pages/product/productLayout2Page";
    private static final String CATEGORY_VIEW = "/category/";
    private static final String SEARCH_PAGE_DATA_KEY = "searchPageData";

    private ProductFacade productFacade;

    private CommerceCategoryService commerceCategoryService;

    @Override
    public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView) {

        if (modelAndView != null) {
            final ModelMap model = modelAndView.getModelMap();
            final String viewName = modelAndView.getViewName();
            if(model != null && viewName != null){
                if(viewName.equals(PRODUCT_VIEW_NAME)){
                    Optional.ofNullable(model.get(PRODUCT_KEY))
                            .filter(ProductData.class::isInstance)
                            .map(ProductData.class::cast)
                            .map(ProductData::getCode)
                            // then add categories
                            .ifPresent(productCode -> {
                                final ProductData productWithCategories = productFacade.getProductForCodeAndOptions(productCode, Collections.singleton(ProductOption.CATEGORIES));
                                final List<String> categoryList = productWithCategories.getCategories().stream()
                                        .map(categoryModel -> categoryModel.getCode())
                                        .collect(Collectors.toList());
                                modelAndView.addObject(CATEGORIES_KEY, categoryList);
                                LOG.debug("Added categories from product {} to {}.", productCode, CATEGORIES_KEY);
                            });
                } else if (viewName.contains(CATEGORY_VIEW)){
                    final Set<String> categoryIds = new HashSet<>();
                    final Optional<ProductCategorySearchPageData> searchData = Optional.ofNullable(model.get(SEARCH_PAGE_DATA_KEY))
                            .filter(ProductCategorySearchPageData.class::isInstance)
                            .map(ProductCategorySearchPageData.class::cast);
                    if(searchData.isPresent()){
                        // Add the category being viewed
                        final String categoryCode = searchData.get().getCategoryCode();
                        categoryIds.add(categoryCode);
                        // Add the super categories as they are also part of the broader range of categories to which the user may be interested.
                        final CategoryModel categoryModel = this.commerceCategoryService.getCategoryForCode(categoryCode);
                        categoryModel.getSupercategories().forEach(categoryData -> categoryIds.add(categoryData.getCode()));
                        LOG.debug("Added super categories of category {} to {}.", categoryCode, CATEGORIES_KEY);
                    }
                    modelAndView.addObject(CATEGORIES_KEY, categoryIds);
                } else {
                    LOG.debug("Cannot add categories to view {} as its not supported.", viewName);
                }

            } else {
                LOG.debug("Cannot add categories as model or viewName is null.");
            }

        } else {
            LOG.debug("Cannot add categories to a null ModelAndView.");
        }
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public void setCommerceCategoryService(CommerceCategoryService commerceCategoryService){
        this.commerceCategoryService = commerceCategoryService;
    }
}
