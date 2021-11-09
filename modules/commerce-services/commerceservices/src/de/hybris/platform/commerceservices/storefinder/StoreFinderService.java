/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.storefinder;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;


/**
 * Store finder service. It is used for retrieving data for store finder related data.
 *
 * @param <ITEM>
 * 		distance to point of service
 * @param <RESULT>
 * 		search page result
 */
public interface StoreFinderService<ITEM extends PointOfServiceDistanceData, RESULT extends StoreFinderSearchPageData<ITEM>>
{
	/**
	 * Gets the locations retrieved based on the given arguments.
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned locations belongs to
	 * @param locationText
	 * 		text that will be used for locations search
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	RESULT locationSearch(BaseStoreModel baseStore, String locationText, PageableData pageableData);

	/**
	 * Gets the locations retrieved based on the given arguments.
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned locations belongs to
	 * @param locationText
	 * 		text that will be used for locations search
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @param maxRadius
	 * 		the maximum radius from the location that results should be returned for.
	 * @return locations found for the given parameters
	 */
	RESULT locationSearch(BaseStoreModel baseStore, String locationText, PageableData pageableData, double maxRadius);


	/**
	 * Gets the locations retrieved basing on the given arguments.
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned locations belongs to
	 * @param geoPoint
	 * 		geographical point that search is performed for
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	RESULT positionSearch(BaseStoreModel baseStore, GeoPoint geoPoint, PageableData pageableData);

	/**
	 * Gets the locations retrieved basing on the given arguments.
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned locations belongs to
	 * @param geoPoint
	 * 		geographical point that search is performed for
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @param maxRadius
	 * 		the maximum radius from the location that results should be returned for.
	 * @return locations found for the given parameters
	 */
	RESULT positionSearch(BaseStoreModel baseStore, GeoPoint geoPoint, PageableData pageableData, double maxRadius);

	/**
	 * Gets the {@link PointOfServiceModel} for the given name that should be unique
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned location belongs to
	 * @param name
	 * 		the requested point of service name
	 * @return {@link PointOfServiceModel} for the given parameter
	 */
	PointOfServiceModel getPointOfServiceForName(BaseStoreModel baseStore, String name);


	/**
	 * Gets the location for the given name that should be unique. Additionally the distance between the location and
	 * given coordinates is calculated.
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned location belongs to
	 * @param name
	 * 		name of the requested point of service
	 * @param geoPoint
	 * 		geographical point of the origin point
	 * @return location for the given parameter
	 */
	ITEM getPointOfServiceDistanceForName(BaseStoreModel baseStore, String name, GeoPoint geoPoint);

	/**
	 * Gets the locations retrieved basing on the given arguments.
	 *
	 * @param baseStore
	 * 		{@link BaseStoreModel} instance that returned locations belongs to
	 * @param pageableData
	 * 		{@link PageableData} object that contains basing information for search result set
	 * @return locations found for the given parameters
	 */
	RESULT getAllPos(BaseStoreModel baseStore, PageableData pageableData);

	/**
	 * Gets a list of {@link PointOfServiceModel} for all points of service in a given country
	 *
	 * @param countryIsoCode
	 * 		{@link CountryModel:ISOCODE}
	 * @param baseStore
	 * 		the active {@link BaseStoreModel}
	 * @return list of {@link PointOfServiceModel}
	 */
	List<PointOfServiceModel> getAllPosForCountry(String countryIsoCode, BaseStoreModel baseStore);

	/**
	 * Gets a list of {@link PointOfServiceModel} for all points of service in a given country and region
	 *
	 * @param countryIsoCode
	 * 		{@link CountryModel:ISOCODE}
	 * @param regionIsoCode
	 * 		{@link RegionModel:ISOCODE}
	 * @param baseStore
	 * 		the active {@link BaseStoreModel}
	 * @return list of {@link PointOfServiceModel}
	 */
	List<PointOfServiceModel> getAllPosForRegion(String countryIsoCode, String regionIsoCode, BaseStoreModel baseStore);
}
