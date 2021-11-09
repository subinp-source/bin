/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingservices.interceptor;

import de.hybris.platform.consignmenttrackingservices.model.CarrierModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * An implementation of {@link PrepareInterceptor}
 */
public class ConsignmentPrepareInterceptor implements PrepareInterceptor<ConsignmentModel>
{

	@Override
	public void onPrepare(ConsignmentModel consignment, InterceptorContext ctx) throws InterceptorException
	{
		final CarrierModel carrier = consignment.getCarrierDetails();
		final String carrierCode = carrier == null ? null : carrier.getCode();
		consignment.setCarrier(carrierCode);
	}

}
