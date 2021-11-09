/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.indexer.service.SnIndexerValueProvider;
import de.hybris.platform.searchservices.indexer.service.impl.AbstractSnIndexerValueProvider;
import de.hybris.platform.searchservices.util.ParameterUtils;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * Base implementation of {@link SnIndexerValueProvider} for products.
 */
public abstract class AbstractProductSnIndexerValueProvider<T extends ItemModel, D> extends AbstractSnIndexerValueProvider<T, D>
{
	public static final String PRODUCT_SELECTOR_VALUE_CURRENT = "CURRENT";
	public static final String PRODUCT_SELECTOR_VALUE_CURRENT_PARENT = "CURRENT_PARENT";
	public static final String PRODUCT_SELECTOR_VALUE_BASE = "BASE";

	public static final String PRODUCT_SELECTOR_PARAM = "productSelector";
	public static final String PRODUCT_SELECTOR_PARAM_DEFAULT_VALUE = PRODUCT_SELECTOR_VALUE_CURRENT;

	/**
	 * Collects the products for the required selectors.
	 *
	 * @param fieldWrappers - the field wrappers
	 * @param product       - the source product
	 * @return the collected products
	 */
	protected Map<String, Set<ProductModel>> collectProducts(final Collection<SnIndexerFieldWrapper> fieldWrappers,
			final ProductModel product) throws SnIndexerException
	{
		final Map<String, Set<ProductModel>> products = new HashMap<>();

		for (final SnIndexerFieldWrapper fieldWrapper : fieldWrappers)
		{
			final String productSelector = resolveProductSelector(fieldWrapper);
			switch (productSelector)
			{
				case PRODUCT_SELECTOR_VALUE_CURRENT:
					products.computeIfAbsent(PRODUCT_SELECTOR_VALUE_CURRENT, key -> collectCurrentProduct(product));
					break;

				case PRODUCT_SELECTOR_VALUE_CURRENT_PARENT:
					products.computeIfAbsent(PRODUCT_SELECTOR_VALUE_CURRENT_PARENT, key -> collectCurrentParentProducts(product));
					break;

				case PRODUCT_SELECTOR_VALUE_BASE:
					products.computeIfAbsent(PRODUCT_SELECTOR_VALUE_BASE, key -> collectBaseProduct(product));
					break;

				default:
					throw new SnIndexerException("Invalid product selector: " + productSelector);
			}
		}

		return products;
	}

	protected Set<ProductModel> collectCurrentProduct(final ProductModel product)
	{
		return Set.of(product);
	}

	protected Set<ProductModel> collectCurrentParentProducts(final ProductModel product)
	{
		if (product instanceof VariantProductModel)
		{
			final Set<ProductModel> products = new LinkedHashSet<>();

			ProductModel currentProduct = product;
			while (currentProduct instanceof VariantProductModel)
			{
				products.add(currentProduct);
				currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();
			}

			products.add(currentProduct);
			return products;
		}
		else
		{
			return Set.of(product);
		}
	}

	protected Set<ProductModel> collectBaseProduct(final ProductModel product)
	{
		ProductModel currentProduct = product;
		while (currentProduct instanceof VariantProductModel)
		{
			currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();
		}

		return Set.of(currentProduct);
	}

	/**
	 * Merges the products and makes sure that they are returned in the correct order, from the most specific to the most
	 * generic.
	 *
	 * @param source - the source products
	 * @return the merged products
	 */
	protected Set<ProductModel> mergeProducts(final Map<String, Set<ProductModel>> source)
	{
		Set<ProductModel> target = source.get(PRODUCT_SELECTOR_VALUE_CURRENT_PARENT);
		if (target != null)
		{
			return target;
		}

		target = new LinkedHashSet<>();
		target.addAll(source.getOrDefault(PRODUCT_SELECTOR_VALUE_CURRENT, Collections.emptySet()));
		target.addAll(source.getOrDefault(PRODUCT_SELECTOR_VALUE_BASE, Collections.emptySet()));
		return target;
	}

	protected String resolveProductSelector(final SnIndexerFieldWrapper fieldWrapper)
	{
		return ParameterUtils
				.getString(fieldWrapper.getValueProviderParameters(), PRODUCT_SELECTOR_PARAM, PRODUCT_SELECTOR_PARAM_DEFAULT_VALUE);
	}
}
