/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticservices.delivery.dao.impl;

import de.hybris.platform.chineselogisticservices.delivery.dao.DeliveryTimeSlotDao;
import de.hybris.platform.chineselogisticservices.model.DeliveryTimeSlotModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class DefaultChineseDeliveryTimeSlotDao implements DeliveryTimeSlotDao
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<DeliveryTimeSlotModel> getAllDeliveryTimeSlots()
	{
		final String fsq = "SELECT {" + DeliveryTimeSlotModel.PK + "} FROM {" + DeliveryTimeSlotModel._TYPECODE + "}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(fsq);
		final SearchResult<DeliveryTimeSlotModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public DeliveryTimeSlotModel getDeliveryTimeSlotByCode(String code)
	{
		final String fsq = "SELECT {" + DeliveryTimeSlotModel.PK + "} FROM {" + DeliveryTimeSlotModel._TYPECODE + "} WHERE {"
				+ DeliveryTimeSlotModel.CODE + "}  = ?code";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(fsq);
		query.addQueryParameter("code", code);
		final SearchResult<DeliveryTimeSlotModel> result = flexibleSearchService.search(query);
		final List<DeliveryTimeSlotModel> deliveryTimeSlotModels = result.getResult();
		if (deliveryTimeSlotModels != null && !deliveryTimeSlotModels.isEmpty())
		{
			return deliveryTimeSlotModels.get(0);
		}
		return null;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


}
