/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.ConfigurationVariantUtil;
import de.hybris.platform.sap.productconfig.services.model.MockVariantProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;


/**
 * Default implementation of the {@link ConfigurationVariantUtil}.
 */
public class SimpleConfigurationVariantUtilImpl implements ConfigurationVariantUtil
{

	@Override
	public boolean isCPQBaseProduct(final ProductModel productModel)
	{
		final VariantTypeModel variantType = productModel.getVariantType();
		if (variantType != null)
		{
			return variantType.getCode().equals(MockVariantProductModel._TYPECODE);
		}
		return false;
	}

	@Override
	public boolean isCPQVariantProduct(final ProductModel productModel)
	{
		if (productModel instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
			if (null != baseProduct)
			{
				return isCPQBaseProduct(baseProduct);
			}
		}
		return false;
	}

	@Override
	public String getBaseProductCode(final ProductModel variantProductModel)
	{
		return ((VariantProductModel) variantProductModel).getBaseProduct().getCode();
	}

	@Override
	public boolean isCPQChangeableVariantProduct(final ProductModel productModel)
	{
		return false;
	}

	@Override
	public boolean isCPQNotChangeableVariantProduct(final ProductModel productModel)
	{
		return productModel instanceof MockVariantProductModel;
	}
}
