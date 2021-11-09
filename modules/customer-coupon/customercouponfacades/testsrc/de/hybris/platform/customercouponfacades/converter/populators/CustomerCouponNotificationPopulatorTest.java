/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponfacades.converter.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponData;
import de.hybris.platform.customercouponfacades.customercoupon.data.CustomerCouponNotificationData;
import de.hybris.platform.customercouponservices.enums.CouponNotificationStatus;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link CustomerCouponNotificationPopulator}
 */
@UnitTest
public class CustomerCouponNotificationPopulatorTest
{

	private CustomerCouponNotificationPopulator populator;

	@Mock
	private CouponNotificationModel notificationModel;
	@Mock
	private CustomerCouponModel couponModel;
	@Mock
	private CustomerCouponData couponData;
	@Mock
	private CustomerModel customerModel;
	@Mock
	private CustomerData customerData;
	@Mock
	private Converter<CustomerModel, CustomerData> customerConverter;
	@Mock
	private Converter<CustomerCouponModel, CustomerCouponData> customerCouponConverter;

	private CustomerCouponNotificationData notificationData;


	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		populator = new CustomerCouponNotificationPopulator();
		notificationData = new CustomerCouponNotificationData();
		populator.setCustomerConverter(customerConverter);
		populator.setCustomerCouponConverter(customerCouponConverter);

		Mockito.when(notificationModel.getCustomerCoupon()).thenReturn(couponModel);
		Mockito.when(notificationModel.getCustomer()).thenReturn(customerModel);
		Mockito.when(notificationModel.getStatus()).thenReturn(CouponNotificationStatus.EFFECTIVESENT);
		Mockito.when(customerConverter.convert(customerModel)).thenReturn(customerData);
		Mockito.when(customerCouponConverter.convert(couponModel)).thenReturn(couponData);
	}

	@Test
	public void testPopulate()
	{
		populator.populate(notificationModel, notificationData);


		Assert.assertEquals(customerData, notificationData.getCustomer());
		Assert.assertEquals(couponData, notificationData.getCoupon());
		Assert.assertEquals(CouponNotificationStatus.EFFECTIVESENT.getCode(), notificationData.getStatus());
	}

}
