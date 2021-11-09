/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.productinterest;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestRelationData;
import de.hybris.platform.notificationservices.enums.NotificationType;

import java.util.List;
import java.util.Optional;


/**
 * Facade to deal with the ProductInterests
 */
public interface ProductInterestFacade
{
	/**
	 * Saves the new or edited interest
	 *
	 * @param productInterest
	 *           the ProductInterest to be saved
	 */
	void saveProductInterest(ProductInterestData productInterest);

	/**
	 * Removes the product interest
	 *
	 * @param productInterest
	 *           the ProductInterest to be removed
	 */
	void removeProductInterest(ProductInterestData productInterest);

	/**
	 * Gets the particular interest of the current customer according to the product code and the notificationType
	 *
	 * @param productcode
	 *           the code of the product to be found
	 * @param notificationType
	 *           the notificationType of the wanted ProductInterest
	 * @return the ProductInterestData
	 */
	Optional<ProductInterestData> getProductInterestDataForCurrentCustomer(String productcode,
			NotificationType notificationType);

	/**
	 * Gets the expired and valid particular interest of the current customer according to the product code and the
	 * notificationType
	 *
	 * @param productcode
	 *           the code of the product to be found
	 * @param notificationType
	 *           the notificationType of the wanted ProductInterest
	 * @return the ProductInterestData
	 */
	List<ProductInterestData> getProductInterestsForNotificationType(String productcode, NotificationType notificationType);

	/**
	 * Removes all interests for specific product for current customer
	 *
	 * @param productCode
	 *           the code of the product
	 */
	void removeAllProductInterests(final String productCode);

	/**
	 * Finds interests watched by current customer
	 *
	 * @param pageableData
	 *           the pagination data
	 * @return Map whose key is ProductMode and value is the Map of NotificationType as key and creation time as value.
	 */
	List<ProductInterestRelationData> getProductsByCustomerInterests(final PageableData pageableData);

	/**
	 * Finds product interest relation for specific product for current customer
	 *
	 * @param productCode
	 *           the code of the product
	 * @return product interests relation data.
	 */
	ProductInterestRelationData getProductInterestRelation(final String productCode);

	/**
	 * Finds interests watched by current customer
	 * 
	 * @param params
	 *           the conditions for searching product interests
	 * @param searchPageData
	 *           the search page data
	 * @return sorted and paged product interests relation data
	 */
	SearchPageData<ProductInterestRelationData> getPaginatedProductInterestsByCustomer(final SearchPageData searchPageData);

	/**
	 * Finds interests watched by current customer
	 * 
	 * @param productCode
	 *           the code of the product
	 * @param notificationType
	 *           the notificationType of the wanted ProductInterest
	 * @param searchPageData
	 *           the search page data
	 * @return sorted and paged product interests relation data
	 */
	SearchPageData<ProductInterestRelationData> getPaginatedProductInterestsForNotificationType(String productCode, NotificationType notificationType,
			final SearchPageData searchPageData);


	/**
	 * Finds the total size of search result
	 *
	 * @param pageableData
	 *           the pagination data
	 * @return the total size of search result
	 */
	int getProductsCountByCustomerInterests(PageableData pageableData);
}
