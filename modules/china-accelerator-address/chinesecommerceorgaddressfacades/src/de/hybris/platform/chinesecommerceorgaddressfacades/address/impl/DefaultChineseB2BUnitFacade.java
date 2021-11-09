/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesecommerceorgaddressfacades.address.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUnitFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * 
 * Default implementation of {@link B2BUnitFacade}
 *
 */
public class DefaultChineseB2BUnitFacade extends DefaultB2BUnitFacade implements B2BUnitFacade
{
	@Override
	public void editAddressOfUnit(final AddressData newAddress, final String unitUid)
	{
		validateParameterNotNullStandardMessage("unit Uid", unitUid);
		validateParameterNotNullStandardMessage("address Id", newAddress);
		final B2BUnitModel unitModel = getB2BUnitService().getUnitForUid(unitUid);
		final AddressModel addressModel = getB2BCommerceUnitService().getAddressForCode(unitModel, newAddress.getId());
		addressModel.setRegion(null);
		addressModel.setCity(null);
		addressModel.setCityDistrict(null);
		validateParameterNotNull(addressModel,String.format("Address not found for pk %s", newAddress.getId()));
		getAddressReverseConverter().convert(newAddress, addressModel);
		getB2BCommerceUnitService().editAddressEntry(unitModel, addressModel);
	}
}
