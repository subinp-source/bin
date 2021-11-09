/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.converter.populators;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponNotificationData;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.util.Assert;


/**
 * Populates {@link CouponNotificationModel} to {@link CustomerCouponNotificationData}
 */
public class CustomerCouponNotificationPopulator implements Populator<CouponNotificationModel, CustomerCouponNotificationData>
{

	private Converter<CustomerModel, CustomerData> customerConverter;
	private Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;

	@Override
	public void populate(final CouponNotificationModel source, final CustomerCouponNotificationData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCoupon(getCustomerCouponConverter().convert(source.getCustomerCoupon()));
		target.setCustomer(getCustomerConverter().convert(source.getCustomer()));
		target.setStatus(source.getStatus().getCode());
	}

	protected Converter<CustomerModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	public void setCustomerConverter(final Converter<CustomerModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	protected Converter<CustomerCouponModel, CustomerCouponData> getCustomerCouponConverter()
	{
		return customerCouponConverter;
	}

	public void setCustomerCouponConverter(final Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter)
	{
		this.customerCouponConverter = customerCouponConverter;
	}


}
