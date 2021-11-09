/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.strategies.impl;

import de.hybris.platform.customerinterestsfacades.data.ProductInterestRelationData;
import de.hybris.platform.customerinterestsfacades.strategies.CollectionSortStrategy;

import java.util.List;
import static java.util.Objects.nonNull;


/**
 * Default implementation of {@link CollectionSortStrategy}
 */
public class SortByProductNameStrategy implements CollectionSortStrategy<List<ProductInterestRelationData>>
{

	@Override
	public void ascendingSort(final List<ProductInterestRelationData> list)
	{
		list.sort((a, b) -> (nonNull(a.getProduct().getName()) ? a.getProduct().getName() : "").compareTo(nonNull(b.getProduct()
				.getName()) ? b.getProduct().getName() : ""));
	}
}
