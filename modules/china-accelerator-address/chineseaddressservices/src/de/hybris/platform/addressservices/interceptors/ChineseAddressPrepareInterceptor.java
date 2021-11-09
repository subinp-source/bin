/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.interceptors;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;


/**
 * Interceptor to set address.town with Chinese city name.
 */
public class ChineseAddressPrepareInterceptor implements PrepareInterceptor<AddressModel>
{

	@Override
	public void onPrepare(final AddressModel address, final InterceptorContext ctx) throws InterceptorException
	{
		if (address.getCity() != null)
		{
			address.setTown(address.getCity().getName());
		}
	}

}
