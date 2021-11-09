/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storefinder.data.StoreFinderStockSearchPageData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceStockData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;


/**
 * Store finder stock facade. It is used for retrieving store specific stock data.
 *
 * @param <ITEM>
 *           type extending {@link PointOfServiceStockData}
 * @param <RESULT>
 *           result type extending {@link StoreFinderStockSearchPageData}
 */
public interface StoreFinderStockFacade<ITEM extends PointOfServiceStockData, RESULT extends StoreFinderStockSearchPageData<ITEM>>
{
	/**
	 * Returns result set with point of services with stock levels for the given location free text search term
	 *
	 * @param location
	 * @param productData
	 * @param pageableData
	 * @return {@link StoreFinderStockSearchPageData} with {@link PointOfServiceStockData}
	 */
	RESULT productSearch(String location, ProductData productData, PageableData pageableData);

	/**
	 * Returns result set with point of services with stock levels for the given point of service name
	 *
	 * @param posName
	 * @param productData
	 * @param pageableData
	 * @return {@link StoreFinderStockSearchPageData} with {@link PointOfServiceStockData}
	 */
	RESULT productPOSSearch(String posName, ProductData productData, PageableData pageableData);

	/**
	 * Returns result set with point of services with stock levels for the given GPS coordinates
	 *
	 * @param geoPoint
	 * @param productData
	 * @param pageableData
	 * @return {@link StoreFinderStockSearchPageData} with {@link PointOfServiceStockData}
	 */
	RESULT productSearch(GeoPoint geoPoint, ProductData productData, PageableData pageableData);
}
