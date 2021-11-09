/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.strategies;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.validation.coverage.CoverageInfo;

public interface AutoApproveProductStrategy {
	/**
	 * Auto approve the Variant and Apparel product, no validation
	 *
	 * @param processedItem
	 *           given product
	 * @return true to apply validation on product, false not apply 
	 * 			 validation on Variant and Apparel product
	 */
	CoverageInfo autoApproveVariantAndApparelProduct(ProductModel product);
}
