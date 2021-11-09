/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.delivery.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.chineselogisticservices.delivery.DeliveryTimeSlotService;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultChineseDeliveryTimeSlotFacadeTest
{

	private static final String TIME_SLOT_CODE = "0001";
	private static final String TIME_SLOT_NAME = "testname";
	private static final String DELIVERY_TIME_SLOT = "any days";

	private DefaultChineseDeliveryTimeSlotFacade facade;
	private CartModel cart;
	private List<DeliveryTimeSlotModel> deliveryTimeSlotModels;
	@Mock
	private DeliveryTimeSlotService deliveryTimeSlotService;
	@Mock
	private DeliveryTimeSlotModel deliveryTimeSlotModel;
	@Mock
	private CartFacade cartFacade;
	@Mock
	private CartService cartService;
	@Mock
	private ModelService modelService;
	@Mock
	private Converter<DeliveryTimeSlotModel, DeliveryTimeSlotData> deliveryTimeSlotConverter;

	private DeliveryTimeSlotData deliveryTimeSlotData;
	private List deliveryTimeSlotDataList;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		facade = new DefaultChineseDeliveryTimeSlotFacade();
		facade.setDeliveryTimeSlotConverter(deliveryTimeSlotConverter);
		facade.setDeliveryTimeSlotService(deliveryTimeSlotService);
		facade.setCartFacade(cartFacade);
		facade.setCartService(cartService);
		facade.setModelService(modelService);

		cart = new CartModel();
		deliveryTimeSlotModels = new ArrayList<>();
		deliveryTimeSlotModels.add(deliveryTimeSlotModel);
		deliveryTimeSlotData = new DeliveryTimeSlotData();
		deliveryTimeSlotData.setCode(TIME_SLOT_CODE);
		deliveryTimeSlotData.setName(TIME_SLOT_NAME);
		deliveryTimeSlotDataList = Arrays.asList(deliveryTimeSlotData);

		BDDMockito.given(deliveryTimeSlotConverter.convertAll(Mockito.any())).willReturn(deliveryTimeSlotDataList);
		BDDMockito.given(deliveryTimeSlotConverter.convert(Mockito.any())).willReturn(deliveryTimeSlotData);
		BDDMockito.given(deliveryTimeSlotModel.getCode()).willReturn(TIME_SLOT_CODE);
		BDDMockito.given(deliveryTimeSlotModel.getName()).willReturn(TIME_SLOT_NAME);
		BDDMockito.given(cartFacade.hasSessionCart()).willReturn(true);
		BDDMockito.given(cartService.getSessionCart()).willReturn(cart);
	}

	@Test
	public void testGetAllDeliveryTimeSlots_empty()
	{
		BDDMockito.given(deliveryTimeSlotService.getAllDeliveryTimeSlots()).willReturn(Collections.EMPTY_LIST);

		final List<DeliveryTimeSlotData> deliveryTimeSlots = facade.getAllDeliveryTimeSlots();
		Assert.assertEquals(0, deliveryTimeSlots.size());
	}

	@Test
	public void testGetAllDeliveryTimeSlots()
	{
		BDDMockito.given(deliveryTimeSlotService.getAllDeliveryTimeSlots()).willReturn(deliveryTimeSlotModels);

		final List<DeliveryTimeSlotData> deliveryTimeSlots = facade.getAllDeliveryTimeSlots();
		Assert.assertEquals(1, deliveryTimeSlots.size());
		Assert.assertEquals(TIME_SLOT_CODE, deliveryTimeSlots.get(0).getCode());
		Assert.assertEquals(TIME_SLOT_NAME, deliveryTimeSlots.get(0).getName());
	}

	@Test
	public void testSetDeliveryTimeSlot()
	{
		Mockito.doNothing().when(deliveryTimeSlotService).setDeliveryTimeSlot(cart, DELIVERY_TIME_SLOT);
		facade.setDeliveryTimeSlot(DELIVERY_TIME_SLOT);
		BDDMockito.verify(deliveryTimeSlotService, Mockito.times(1)).setDeliveryTimeSlot(cart, DELIVERY_TIME_SLOT);
	}

	@Test
	public void testGetDeliveryTimeSlot_NotNull()
	{
		BDDMockito.given(deliveryTimeSlotService.getDeliveryTimeSlotByCode(TIME_SLOT_CODE)).willReturn(deliveryTimeSlotModel);
		Assert.assertEquals(deliveryTimeSlotData, facade.getDeliveryTimeSlotByCode(TIME_SLOT_CODE));
	}

	@Test
	public void testGetDeliveryTimeSlot_Null()
	{
		BDDMockito.given(deliveryTimeSlotService.getDeliveryTimeSlotByCode(TIME_SLOT_CODE)).willReturn(null);
		Assert.assertNull(facade.getDeliveryTimeSlotByCode(TIME_SLOT_CODE));
	}

	@Test
	public void testRemoveDeliveryTimeSlot()
	{
		cart.setDeliveryTimeSlot(deliveryTimeSlotModel);
		Mockito.doNothing().when(modelService).save(cart);
		Mockito.doNothing().when(modelService).refresh(cart);
		facade.removeDeliveryTimeSlot();
		Assert.assertNull(cart.getDeliveryTimeSlot());
		BDDMockito.verify(modelService, Mockito.times(1)).save(cart);
		BDDMockito.verify(modelService, Mockito.times(1)).refresh(cart);
	}
}
