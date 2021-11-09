/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.delivery.impl;

import de.hybris.platform.acceleratorfacades.order.impl.DefaultAcceleratorCheckoutFacade;
import de.hybris.platform.chineselogisticfacades.delivery.DeliveryTimeSlotFacade;
import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;
import de.hybris.platform.chineselogisticservices.delivery.DeliveryTimeSlotService;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * 
 * Chinese specific implementation of {@link DeliveryTimeSlotFacade}
 *
 */
public class DefaultChineseDeliveryTimeSlotFacade extends DefaultAcceleratorCheckoutFacade implements DeliveryTimeSlotFacade
{
	private Converter<DeliveryTimeSlotModel, DeliveryTimeSlotData> deliveryTimeSlotConverter;
	private DeliveryTimeSlotService deliveryTimeSlotService;

	@Override
	public List<DeliveryTimeSlotData> getAllDeliveryTimeSlots()
	{
		final List<DeliveryTimeSlotModel> deliveryTimeSlotModels = deliveryTimeSlotService.getAllDeliveryTimeSlots();
		if (!CollectionUtils.isEmpty(deliveryTimeSlotModels))
		{
			return getDeliveryTimeSlotConverter().convertAll(deliveryTimeSlotModels);
		}
		return Collections.emptyList();
	}

	@Override
	public void setDeliveryTimeSlot(final String deliveryTimeSlot)
	{
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			deliveryTimeSlotService.setDeliveryTimeSlot(cartModel, deliveryTimeSlot);
		}
	}

	@Override
	public void removeDeliveryTimeSlot()
	{
		final CartModel cartModel = getCart();
		if (Objects.nonNull(cartModel))
		{
			cartModel.setDeliveryTimeSlot(null);
			getModelService().save(cartModel);
			getModelService().refresh(cartModel);
		}
	}

	@Override
	public DeliveryTimeSlotData getDeliveryTimeSlotByCode(final String code)
	{
		final DeliveryTimeSlotModel model = deliveryTimeSlotService.getDeliveryTimeSlotByCode(code);
		if (Objects.nonNull(model))
		{
			return getDeliveryTimeSlotConverter().convert(deliveryTimeSlotService.getDeliveryTimeSlotByCode(code));
		}
		return null;

	}


	protected Converter<DeliveryTimeSlotModel, DeliveryTimeSlotData> getDeliveryTimeSlotConverter()
	{
		return deliveryTimeSlotConverter;
	}

	@Required
	public void setDeliveryTimeSlotConverter(
			final Converter<DeliveryTimeSlotModel, DeliveryTimeSlotData> deliveryTimeSlotConverter)
	{
		this.deliveryTimeSlotConverter = deliveryTimeSlotConverter;
	}

	protected DeliveryTimeSlotService getDeliveryTimeSlotService()
	{
		return deliveryTimeSlotService;
	}

	@Required
	public void setDeliveryTimeSlotService(final DeliveryTimeSlotService deliveryTimeSlotService)
	{
		this.deliveryTimeSlotService = deliveryTimeSlotService;
	}


}
