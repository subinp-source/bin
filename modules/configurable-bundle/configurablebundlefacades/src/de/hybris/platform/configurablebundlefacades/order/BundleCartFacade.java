/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import javax.annotation.Nonnull;


/**
 * Bundle Cart facade interface. Service is responsible for getting and updating all necessary information for a bundle
 * cart.
 */
public interface BundleCartFacade
{
	/**
	 * Start new bundle in cart based on the given bundle template and add the product to it.
	 *
	 * @see de.hybris.platform.core.order.EntryGroup
	 * @see de.hybris.platform.configurablebundleservices.jalo.BundleSelectionCriteria
	 *
	 * @param bundleTemplateId a component to add the product to. He whole bundle structure
	 *                          - starting from the root of the component - will be added to cart groups
	 * @param productCode a product which will be added to the component
	 * @param quantity quantity for the product. Is limited by selection criteria of the component
	 * @return information about the new cart entry
	 * @throws CommerceCartModificationException if the operation is not possible
	 */
	CartModificationData startBundle(@Nonnull String bundleTemplateId, @Nonnull String productCode, long quantity)
			throws CommerceCartModificationException;

	/**
	 * Add a product to an existing bundle.
	 *
	 * @see de.hybris.platform.core.order.EntryGroup
	 *
	 * @param productCode product to add
	 * @param quantity quantity of the product
	 * @param groupNumber entry group number, that defines the bundle and the component within the bundle
	 * @return information about the new cart entry
	 * @throws CommerceCartModificationException if the operation is not possible
	 */
	CartModificationData addToCart(@Nonnull String productCode, long quantity, int groupNumber)
			throws CommerceCartModificationException;

	/**
	 * Constructs pageable list of products available for entry group of type {@link de.hybris.platform.core.enums.GroupType#CONFIGURABLEBUNDLE}
	 *
	 * @param groupNumber
	 *           entry group number related to the component
	 * @param searchQuery
	 *           the search query
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 * @throws IllegalArgumentException 
	 *           if group is not found or group type is not {@link de.hybris.platform.core.enums.GroupType#CONFIGURABLEBUNDLE}
	 */
	@Nonnull
	ProductSearchPageData<SearchStateData, ProductData> getAllowedProducts(
			@Nonnull Integer groupNumber, String searchQuery, @Nonnull PageableData pageableData);
}
