/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.acceleratorservices.payment.data.CustomerShipToData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class CustomerShipToDataPopulator extends AbstractResultPopulator<CartModel, CustomerShipToData>
{
	@Override
	public void populate(final CartModel source, final CustomerShipToData target) throws ConversionException
	{
		validateParameterNotNull(source, "Parameter [CartModel] source cannot be null");
		validateParameterNotNull(target, "Parameter [CustomerShipToData] target cannot be null");

		final AddressModel deliveryAddress = source.getDeliveryAddress();
		if (deliveryAddress != null)
		{
			target.setShipToCity(deliveryAddress.getTown());
			target.setShipToCompany(deliveryAddress.getCompany());
			target.setShipToCountry(deliveryAddress.getCountry().getIsocode());
			if (deliveryAddress.getRegion() != null)
			{
				target.setShipToState(deliveryAddress.getRegion().getIsocodeShort());
			}
			target.setShipToFirstName(deliveryAddress.getFirstname());
			target.setShipToLastName(deliveryAddress.getLastname());
			target.setShipToPhoneNumber(deliveryAddress.getPhone1());
			target.setShipToPostalCode(deliveryAddress.getPostalcode());
			target.setShipToStreet1(deliveryAddress.getLine1());
			target.setShipToStreet2(deliveryAddress.getLine2());
		}

		final DeliveryModeModel deliveryMode = source.getDeliveryMode();
		if (deliveryMode == null)
		{
			target.setShipToShippingMethod("none");
		}
		else
		{
			target.setShipToShippingMethod(deliveryMode.getCode());
		}
	}
}
