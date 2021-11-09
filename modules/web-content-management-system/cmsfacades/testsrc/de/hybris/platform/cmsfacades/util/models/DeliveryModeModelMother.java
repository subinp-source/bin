/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cmsfacades.util.builder.DeliveryModeModelBuilder;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.DeliveryModeDao;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class DeliveryModeModelMother extends AbstractModelMother<DeliveryModeModel>
{
	public static final String CODE_PICKUP = "pickup";
	public static final String CODE_REGULAR = "regular";
	public static final String CODE_STANDARD_SHIPMENT = "standard-shipment";
	public static final String DELIVERY_MODE_SHIPING = "free-standard-shipping";

	private DeliveryModeDao deliveryModeDao;

	public DeliveryModeModel Pickup()
	{
		return getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode(CODE_PICKUP),
				() -> DeliveryModeModelBuilder.aModel().withCode(CODE_PICKUP).withActive(Boolean.TRUE)
						.withName("Pickup", Locale.ENGLISH).build());
	}

	public DeliveryModeModel Regular()
	{
		return getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode(CODE_REGULAR),
				() -> DeliveryModeModelBuilder.aModel().withCode(CODE_REGULAR).withActive(Boolean.TRUE)
						.withName("Regular Delivery", Locale.ENGLISH).build());
	}

	public DeliveryModeModel standardShipment()
	{

		return getFromCollectionOrSaveAndReturn(() -> getDeliveryModeDao().findDeliveryModesByCode(CODE_STANDARD_SHIPMENT),
				() -> DeliveryModeModelBuilder.aModel().withCode(CODE_STANDARD_SHIPMENT)
						.withName(DELIVERY_MODE_SHIPING, Locale.ENGLISH).withActive(Boolean.TRUE).build());
	}

	public DeliveryModeDao getDeliveryModeDao()
	{
		return deliveryModeDao;
	}

	@Required
	public void setDeliveryModeDao(final DeliveryModeDao deliveryModeDao)
	{
		this.deliveryModeDao = deliveryModeDao;
	}

}