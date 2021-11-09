/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticservices.delivery;

import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * Providing some services about DeliveryTimeSlot
 */
public interface DeliveryTimeSlotService
{
	/**
	 * Getting all of the DeliveryTimeSlots
	 *
	 * @return List<DeliveryTimeSlotModel>
	 */
	List<DeliveryTimeSlotModel> getAllDeliveryTimeSlots();

	/**
	 * Getting the DeliveryTimeSlot by code
	 *
	 * @param code
	 *           the code of the DeliveryTimeSlotModel
	 * @return delivery timeslot model
	 */
	DeliveryTimeSlotModel getDeliveryTimeSlotByCode(String code);

	/**
	 * Setting the DeliveryTimeSlot into the cartmodel
	 *
	 * @param cartModel
	 *           cart model
	 * @param deliveryTimeSlot
	 *           delivery timeslot
	 */
	void setDeliveryTimeSlot(CartModel cartModel, String deliveryTimeSlot);
}
