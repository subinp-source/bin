/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticfacades.delivery;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.chineselogisticfacades.delivery.data.DeliveryTimeSlotData;

import java.util.List;


/**
 * Facade for DeliveryTimeSlot
 */
public interface DeliveryTimeSlotFacade extends AcceleratorCheckoutFacade
{
	/**
	 * Gets all of the DeliveryTimeSlots.
	 *
	 * @return List<DeliveryTimeSlotData>
	 */
	List<DeliveryTimeSlotData> getAllDeliveryTimeSlots();

	/**
	 * Sets the DeliveryTimeSlot into the cartmodel.
	 *
	 * @param deliveryTimeSlot
	 *           the code of the delivery time slot
	 */
	void setDeliveryTimeSlot(String deliveryTimeSlot);

	/**
	 * Gets the DeliveryTimeSlot by code.
	 *
	 * @param code
	 *           the code of the delivery time slot
	 * @return delivery timeslot model
	 */
	DeliveryTimeSlotData getDeliveryTimeSlotByCode(String code);

	/**
	 * Removes delivery time slot from current cart.
	 */
	void removeDeliveryTimeSlot();


}
