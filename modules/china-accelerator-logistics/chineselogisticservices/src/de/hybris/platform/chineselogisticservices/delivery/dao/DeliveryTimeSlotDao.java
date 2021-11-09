/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticservices.delivery.dao;

import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;


/**
 * Looking up DeliveryTimeSlot in the database
 */
public interface DeliveryTimeSlotDao extends Dao
{
	/**
	 * Getting all of theDeliveryTimeSlots
	 *
	 * @return List<DeliveryTimeSlotModel>
	 */
	List<DeliveryTimeSlotModel> getAllDeliveryTimeSlots();

	/**
	 * Getting the DeliveryTimeSlot by code
	 *
	 * @param code
	 * @return DeliveryTimeSlotModel
	 */
	DeliveryTimeSlotModel getDeliveryTimeSlotByCode(String code);
}
