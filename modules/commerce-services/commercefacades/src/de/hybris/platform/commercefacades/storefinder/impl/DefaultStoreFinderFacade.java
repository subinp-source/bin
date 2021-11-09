/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder.impl;

import de.hybris.platform.commercefacades.store.data.StoreCountData;
import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.StoreFinderService;
import de.hybris.platform.commerceservices.storefinder.data.PointOfServiceDistanceData;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.pojo.StoreCountInfo;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link StoreFinderFacade}
 */
public class DefaultStoreFinderFacade implements StoreFinderFacade
{
	private StoreFinderService<PointOfServiceDistanceData, StoreFinderSearchPageData<PointOfServiceDistanceData>> storeFinderService;
	private BaseStoreService baseStoreService;
	private PointOfServiceService pointOfServiceService;
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	private Converter<StoreCountInfo, StoreCountData> storeCountConverter;
	private Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> searchPagePointOfServiceDistanceConverter;
	private Converter<PointOfServiceDistanceData, PointOfServiceData> pointOfServiceDistanceConverter;

	protected StoreFinderService<PointOfServiceDistanceData, StoreFinderSearchPageData<PointOfServiceDistanceData>> getStoreFinderService()
	{
		return storeFinderService;
	}

	@Required
	public void setStoreFinderService(
			final StoreFinderService<PointOfServiceDistanceData, StoreFinderSearchPageData<PointOfServiceDistanceData>> storeFinderService)
	{
		this.storeFinderService = storeFinderService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected Converter<PointOfServiceModel, PointOfServiceData> getPointOfServiceConverter()
	{
		return pointOfServiceConverter;
	}

	@Required
	public void setPointOfServiceConverter(final Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter)
	{
		this.pointOfServiceConverter = pointOfServiceConverter;
	}

	protected Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> getSearchPagePointOfServiceDistanceConverter()
	{
		return searchPagePointOfServiceDistanceConverter;
	}

	@Required
	public void setSearchPagePointOfServiceDistanceConverter(
			final Converter<StoreFinderSearchPageData<PointOfServiceDistanceData>, StoreFinderSearchPageData<PointOfServiceData>> searchPagePointOfServiceDistanceConverter)
	{
		this.searchPagePointOfServiceDistanceConverter = searchPagePointOfServiceDistanceConverter;
	}

	protected Converter<PointOfServiceDistanceData, PointOfServiceData> getPointOfServiceDistanceConverter()
	{
		return pointOfServiceDistanceConverter;
	}

	@Required
	public void setPointOfServiceDistanceConverter(
			final Converter<PointOfServiceDistanceData, PointOfServiceData> pointOfServiceDistanceConverter)
	{
		this.pointOfServiceDistanceConverter = pointOfServiceDistanceConverter;
	}

	protected PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	@Required
	public void setPointOfServiceService(final PointOfServiceService pointOfServiceService)
	{
		this.pointOfServiceService = pointOfServiceService;
	}

	protected Converter<StoreCountInfo, StoreCountData> getStoreCountConverter()
	{
		return storeCountConverter;
	}

	@Required
	public void setStoreCountConverter(final Converter<StoreCountInfo, StoreCountData> storeCountConverter)
	{
		this.storeCountConverter = storeCountConverter;
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String locationText, final PageableData pageableData)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = getStoreFinderService()
				.locationSearch(currentBaseStore, locationText, pageableData);
		return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String locationText, final PageableData pageableData,
			final double maxRadius)
	{

		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = getStoreFinderService()
				.locationSearch(currentBaseStore, locationText, pageableData, maxRadius);
		return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceData> positionSearch(final GeoPoint geoPoint, final PageableData pageableData)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = getStoreFinderService()
				.positionSearch(currentBaseStore, geoPoint, pageableData);
		return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceData> positionSearch(final GeoPoint geoPoint, final PageableData pageableData,
			final double maxRadius)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = getStoreFinderService()
				.positionSearch(currentBaseStore, geoPoint, pageableData, maxRadius);
		return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
	}

	@Override
	public PointOfServiceData getPointOfServiceForName(final String name)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final PointOfServiceModel pointOfService = getStoreFinderService().getPointOfServiceForName(currentBaseStore, name);
		return getPointOfServiceConverter().convert(pointOfService);
	}

	@Override
	public PointOfServiceData getPointOfServiceForNameAndPosition(final String name, final GeoPoint geoPoint)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final PointOfServiceDistanceData pointOfServiceDistance = getStoreFinderService()
				.getPointOfServiceDistanceForName(currentBaseStore, name, geoPoint);
		return getPointOfServiceDistanceConverter().convert(pointOfServiceDistance);
	}

	@Override
	public StoreFinderSearchPageData<PointOfServiceData> getAllPointOfServices(final PageableData pageableData)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final StoreFinderSearchPageData<PointOfServiceDistanceData> searchPageData = getStoreFinderService()
				.getAllPos(currentBaseStore, pageableData);
		return getSearchPagePointOfServiceDistanceConverter().convert(searchPageData);
	}

	@Override
	public List<PointOfServiceData> getPointsOfServiceForCountry(final String countryIsoCode)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		return getPointOfServiceConverter()
				.convertAll(getStoreFinderService().getAllPosForCountry(countryIsoCode, currentBaseStore));
	}


	@Override
	public List<PointOfServiceData> getPointsOfServiceForRegion(final String countryIsoCode, final String regionIsoCode)
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		return getPointOfServiceConverter()
				.convertAll(getStoreFinderService().getAllPosForRegion(countryIsoCode, regionIsoCode, currentBaseStore));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.yacceleratorfacades.storefinder.StoreFinderFacade#getStoreCounts()
	 */
	@Override
	public List<StoreCountData> getStoreCounts()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		final List<StoreCountInfo> storeCountInfoList = getPointOfServiceService().getPointOfServiceCounts(currentBaseStore);
		return getStoreCountConverter().convertAll(storeCountInfoList);
	}

}
