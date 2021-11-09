/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order.converters.comparator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

import org.springframework.beans.factory.annotation.Required;


/**
 * The class of OrderEntryProductComparator.
 * 
 * @deprecated since 1905: The comparator compares only deprecated fields, so it is deprecated, too.
 */
@Deprecated(since = "1905", forRemoval= true)
public class OrderEntryProductComparator extends AbstractBundleOrderEntryComparator<OrderEntryData> // NOSONAR
{

	private transient ProductService productService;

	@Override
	@SuppressWarnings({"squid:MethodCyclomaticComplexity","squid:ExpressionComplexityCheck"})
	protected int doCompare(OrderEntryData o1, OrderEntryData o2)
	{

		ProductModel o1Product = null;
		ProductModel o2Product = null;
		
		if (o1.getProduct() != null && o1.getProduct().getCode() != null)
		{
			o1Product = getProductService().getProductForCode(o1.getProduct().getCode());
		}

		if (o2 != null && o2.getProduct() != null && o2.getProduct().getCode() != null)
		{
			o2Product = getProductService().getProductForCode(o2.getProduct().getCode());
		}

		if (o1Product != null && o2Product != null)
		{
			return o1Product.getCode().compareTo(o2Product.getCode());
		}

		return 0;
	}

	@Override
	@SuppressWarnings("squid:MethodCyclomaticComplexity")
	protected boolean comparable(OrderEntryData o1, OrderEntryData o2)
	{
		return o1 != null && o2 != null;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(ProductService productService)
	{
		this.productService = productService;
	}
}
