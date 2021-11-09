/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.product;

import de.hybris.platform.commerceservices.product.data.ReferenceData;

import java.util.List;


/**
 * Defines an API for product reference
 *
 * @param <TYPE>
 *           the product reference type
 * @param <TARGET>
 *           the target product
 */
public interface CommerceProductReferenceService<TYPE, TARGET>
{
	/**
	 * @deprecated Since 5.0. Use getProductReferencesForCode(final String code, final List<TYPE> referenceTypes, final
	 *             Integer limit); instead.
	 *
	 * @param code
	 *           the product code
	 * @param referenceType
	 *           the product reference type
	 * @param limit
	 *           maximum number of references to retrieve. If null, all available references will be retrieved.
	 * @return a collection product references
	 */
	@Deprecated(since = "5.0", forRemoval = true)
	List<ReferenceData<TYPE, TARGET>> getProductReferencesForCode(final String code, final TYPE referenceType,
			final Integer limit);

	/**
	 * Retrieves product references for a given product and product reference type.
	 *
	 * @param code
	 *           the product code
	 * @param referenceTypes
	 *           the product reference types to return
	 * @param limit
	 *           maximum number of references to retrieve. If null, all available references will be retrieved.
	 * @return a collection product references.
	 */
	List<ReferenceData<TYPE, TARGET>> getProductReferencesForCode(final String code, final List<TYPE> referenceTypes,
			final Integer limit);
}
