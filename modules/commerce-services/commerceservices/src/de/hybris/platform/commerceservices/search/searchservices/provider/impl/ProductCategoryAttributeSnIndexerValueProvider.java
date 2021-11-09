/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnIndexerValueProvider} for product category attributes.
 */
public class ProductCategoryAttributeSnIndexerValueProvider extends
		AbstractProductSnIndexerValueProvider<ProductModel, ProductCategoryAttributeSnIndexerValueProvider.ProductCategoryData>
{
	public static final String ID = "productCategoryAttributeSnIndexerValueProvider";

	public static final String EXPRESSION_PARAM = "expression";
	public static final String EXPRESSION_DEFAULT_VALUE = CategoryModel.CODE;

	public static final String ROOT_CATEGORY_PARAM = "rootCategory";
	public static final String ROOT_CATEGORY_DEFAULT_VALUE = null;

	protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);

	private CategoryService categoryService;
	private SnExpressionEvaluator snExpressionEvaluator;

	@Override
	public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
	{
		return SUPPORTED_QUALIFIER_CLASSES;
	}

	@Override
	protected Object getFieldValue(final SnIndexerContext indexerContext, final SnIndexerFieldWrapper fieldWrapper,
			final ProductModel source, final ProductCategoryData data) throws SnIndexerException
	{
		try
		{
			final String expression = resolveExpression(fieldWrapper);
			final String productSelector = resolveProductSelector(fieldWrapper);
			final Set<ProductModel> products = data.getProducts().get(productSelector);
			final String rooCategory = resolveRootCategory(fieldWrapper);

			final Set<CategoryModel> categories = new LinkedHashSet<>();

			for (final ProductModel product : products)
			{
				categories.addAll(data.getCategories().get(product.getPk()).get(rooCategory));
			}

			if (CollectionUtils.isEmpty(categories))
			{
				return null;
			}

			if (fieldWrapper.isLocalized())
			{
				final List<Locale> locales = fieldWrapper.getQualifiers().stream().map(qualifier -> qualifier.getAs(Locale.class))
						.collect(Collectors.toList());
				return snExpressionEvaluator.evaluate(categories, expression, locales);
			}
			else
			{
				return snExpressionEvaluator.evaluate(categories, expression);
			}
		}
		catch (final SnException e)
		{
			throw new SnIndexerException(e);
		}
	}

	@Override
	protected ProductCategoryData loadData(final SnIndexerContext indexerContext,
			final Collection<SnIndexerFieldWrapper> fieldWrappers, final ProductModel source) throws SnIndexerException
	{
		final Map<String, Set<ProductModel>> products = collectProducts(fieldWrappers, source);
		final Set<ProductModel> mergedProducts = mergeProducts(products);
		final Map<PK, Map<String, Set<CategoryModel>>> categories = collectCategories(fieldWrappers, mergedProducts);

		final ProductCategoryData data = new ProductCategoryData();
		data.setProducts(products);
		data.setCategories(categories);

		return data;
	}

	protected Map<PK, Map<String, Set<CategoryModel>>> collectCategories(final Collection<SnIndexerFieldWrapper> fieldWrappers,
			final Set<ProductModel> products)
	{
		final Set<String> rootCategories = collectRootCategories(fieldWrappers);
		final Map<PK, Map<String, Set<CategoryModel>>> categories = new HashMap<>();

		for (final ProductModel product : products)
		{
			final Map<String, Set<CategoryModel>> productCategories = new HashMap<>();

			for (final String rootCategory : rootCategories)
			{
				productCategories.put(rootCategory, new HashSet<>());
			}

			doCollectCategories(productCategories, product.getSupercategories(), Collections.emptySet());

			categories.put(product.getPk(), productCategories);
		}

		return categories;
	}

	protected Set<String> collectRootCategories(final Collection<SnIndexerFieldWrapper> fieldWrappers)
	{
		final Set<String> rootCategories = new HashSet<>();
		rootCategories.add(ROOT_CATEGORY_DEFAULT_VALUE);

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			final String rootCategory = resolveRootCategory(fieldWrapper);
			rootCategories.add(rootCategory);
		}

		return rootCategories;
	}

	protected void doCollectCategories(final Map<String, Set<CategoryModel>> categories,
			final Collection<CategoryModel> parentCategories, final Set<CategoryModel> path)
	{
		if (CollectionUtils.isEmpty(parentCategories))
		{
			return;
		}

		for (final CategoryModel category : parentCategories)
		{
			if (!isBlockedCategory(category, path))
			{
				final String categoryCode = category.getCode();
				final Set<CategoryModel> dataCategories = categories.get(categoryCode);
				if (dataCategories != null)
				{
					dataCategories.addAll(path);
				}

				final Set<CategoryModel> newPath = new LinkedHashSet<>();
				newPath.add(category);
				newPath.addAll(path);

				if (categoryService.isRoot(category))
				{
					categories.get(ROOT_CATEGORY_DEFAULT_VALUE).addAll(newPath);
				}
				else
				{
					doCollectCategories(categories, category.getSupercategories(), newPath);
				}
			}
		}
	}

	protected boolean isBlockedCategory(final CategoryModel category, final Set<CategoryModel> path)
	{
		return category == null || path.contains(category);
	}

	protected String resolveExpression(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), EXPRESSION_PARAM, EXPRESSION_DEFAULT_VALUE);
	}

	protected String resolveRootCategory(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils
				.getString(fieldWrapper.getValueProviderParameters(), ROOT_CATEGORY_PARAM, ROOT_CATEGORY_DEFAULT_VALUE);
	}

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public SnExpressionEvaluator getSnExpressionEvaluator()
	{
		return snExpressionEvaluator;
	}

	@Required
	public void setSnExpressionEvaluator(final SnExpressionEvaluator snExpressionEvaluator)
	{
		this.snExpressionEvaluator = snExpressionEvaluator;
	}

	protected static class ProductCategoryData
	{
		private Map<String, Set<ProductModel>> products;
		private Map<PK, Map<String, Set<CategoryModel>>> categories;

		public Map<String, Set<ProductModel>> getProducts()
		{
			return products;
		}

		public void setProducts(final Map<String, Set<ProductModel>> products)
		{
			this.products = products;
		}

		public Map<PK, Map<String, Set<CategoryModel>>> getCategories()
		{
			return categories;
		}

		public void setCategories(final Map<PK, Map<String, Set<CategoryModel>>> categories)
		{
			this.categories = categories;
		}
	}
}
