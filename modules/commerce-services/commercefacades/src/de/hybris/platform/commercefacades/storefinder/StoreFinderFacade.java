/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder;

import de.hybris.platform.commercefacades.store.data.StoreCountData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;

import java.util.List;

import java.util.List;


/**
 * Store finder facade. It is used for retrieving data for store finder related data.
 */
public interface StoreFinderFacade
{

	/**
	 * Gets the search page data object parametrized with {@link PointOfServiceData} and contains location search results
	 * for the given location text.
	 *
	 * @param locationText
	 * 		the text that location search is performed for
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	StoreFinderSearchPageData<PointOfServiceData> locationSearch(String locationText, PageableData pageableData);

	/**
	 * Gets the search page data object parametrized with {@link PointOfServiceData} and contains location search results
	 * for the given location text.
	 *
	 * @param locationText
	 * 		the text that location search is performed for
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @param maxRadius
	 * 		the maximum radius from the location that results should be returned for.
	 * @return locations found for the given parameters
	 */
	StoreFinderSearchPageData<PointOfServiceData> locationSearch(String locationText, PageableData pageableData, double maxRadius);


	/**
	 * Gets the search page data object parametrized with {@link PointOfServiceData} and contains location search results
	 * for the given coordinates
	 *
	 * @param geoPoint
	 * 		geographical point that search is performed for
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	StoreFinderSearchPageData<PointOfServiceData> positionSearch(GeoPoint geoPoint, PageableData pageableData);

	/**
	 * Gets the search page data object parametrized with {@link PointOfServiceData} and contains location search results
	 * for the given coordinates
	 *
	 * @param geoPoint
	 * 		geographical point that search is performed for
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @param maxRadius
	 * 		the maximum radius from the location that results should be returned for.
	 * @return locations found for the given parameters
	 */
	StoreFinderSearchPageData<PointOfServiceData> positionSearch(GeoPoint geoPoint, PageableData pageableData, double maxRadius);


	/**
	 * Gets the {@link PointOfServiceData} for the given name that should be unique
	 *
	 * @param name
	 * 		the point of service name
	 * @return {@link PointOfServiceData} for the given parameter
	 */
	PointOfServiceData getPointOfServiceForName(String name);


	/**
	 * Gets the {@link PointOfServiceData} for the given name that should be unique. Additionally the distance between
	 * the {@link PointOfServiceData} and given coordinates is calculated.
	 *
	 * @param name
	 * 		- name of the requested point of service
	 * @param geoPoint
	 * 		- geographical location of the origin point
	 * @return {@link PointOfServiceData} for the given parameter
	 */
	PointOfServiceData getPointOfServiceForNameAndPosition(String name, GeoPoint geoPoint);

	/**
	 * Gets the search page data object parametrized with {@link PointOfServiceData} and contains all stores for current
	 * base store
	 *
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	StoreFinderSearchPageData<PointOfServiceData> getAllPointOfServices(PageableData pageableData);

	/**
	 * Gets the store's counts per country and its regions
	 *
	 * @return {@link List<StoreCountData>}
	 */
	List<StoreCountData> getStoreCounts();

	/**
	 * Gets a list of {@link PointOfServiceData} for all points of service in a given country
	 *
	 * @param countryIsoCode
	 * 		{@link CountryModel:ISOCODE}
	 * @return list of {@link PointOfServiceData}
	 */
	List<PointOfServiceData> getPointsOfServiceForCountry(String countryIsoCode);

	/**
	 * Gets a list of {@link PointOfServiceData} for all points of service in a given country and region
	 *
	 * @param countryIsoCode
	 * 		{@link CountryModel:ISOCODE}
	 * @param regionIsoCode
	 * 		{@link RegionModel:ISOCODE}
	 * @return list of {@link PointOfServiceData}
	 */
	List<PointOfServiceData> getPointsOfServiceForRegion(String countryIsoCode, String regionIsoCode);
}
