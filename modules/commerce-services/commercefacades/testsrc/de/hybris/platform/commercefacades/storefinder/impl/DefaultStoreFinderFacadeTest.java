/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storefinder.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.storefinder.StoreFinderService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.verify;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultStoreFinderFacadeTest
{
	private static final String COUNTRY_ISO_CODE = "CA";
	private static final String REGION_ISO_CODE = "CA-QC";

	@InjectMocks
	private DefaultStoreFinderFacade defaultStoreFinderFacade;

	@Mock
	private BaseStoreService baseStoreService;

	@Mock
	private StoreFinderService storeFinderService;

	@Mock
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	private BaseStoreModel baseStore = new BaseStoreModel();

	@Test
	public void testGetPointsOfServiceForCountry()
	{
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStore);
		given(storeFinderService.getAllPosForCountry(COUNTRY_ISO_CODE, baseStore)).willReturn(new ArrayList<PointOfServiceModel>());
		given(pointOfServiceConverter.convertAll(any(Collection.class))).willReturn(new ArrayList<PointOfServiceData>());

		defaultStoreFinderFacade.getPointsOfServiceForCountry(COUNTRY_ISO_CODE);

		verify(baseStoreService).getCurrentBaseStore();
		verify(storeFinderService).getAllPosForCountry(COUNTRY_ISO_CODE, baseStore);
		verify(pointOfServiceConverter).convertAll(anyCollection());
	}

	@Test
	public void testGetPointsOfServiceForRegion()
	{
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStore);
		given(storeFinderService.getAllPosForRegion(COUNTRY_ISO_CODE, REGION_ISO_CODE, baseStore)).willReturn(new ArrayList<PointOfServiceModel>());
		given(pointOfServiceConverter.convertAll(any(Collection.class))).willReturn(new ArrayList<PointOfServiceData>());

		defaultStoreFinderFacade.getPointsOfServiceForRegion(COUNTRY_ISO_CODE, REGION_ISO_CODE);

		verify(baseStoreService).getCurrentBaseStore();
		verify(storeFinderService).getAllPosForRegion(COUNTRY_ISO_CODE, REGION_ISO_CODE, baseStore);
		verify(pointOfServiceConverter).convertAll(anyCollection());
	}
}
