/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.coupon.backoffice.cockpitng.editor.tab.populator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.coupon.backoffice.data.AppliedCouponData;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.CouponService;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultAppliedCouponPopulatorTest
{
	private static final String COUPON_CODE = "VALID_CODE";
	private static final String COUPON_NAME = "VALID_COUPON_NAME";
	@InjectMocks
	private DefaultAppliedCouponPopulator populator;
	@Mock
	private CouponService couponService;
	@Mock
	private AbstractCouponModel coupon;
	private AppliedCouponData appliedCoupon;

	@Before
	public void setUp() throws Exception
	{
		appliedCoupon = new AppliedCouponData();
		given(coupon.getName()).willReturn(COUPON_NAME);
	}

	@Test
	public void shouldPopulateCouponCodeAndName() throws Exception
	{
		//given
		final Optional<AbstractCouponModel> couponOptional = Optional.of(coupon);
		given(couponService.getCouponForCode(COUPON_CODE)).willReturn(couponOptional);
		//when
		populator.populate(COUPON_CODE,appliedCoupon);
		//then
		assertThat(appliedCoupon.getCode()).isEqualTo(COUPON_CODE);
		assertThat(appliedCoupon.getName()).isEqualTo(COUPON_NAME);
	}

	@Test
	public void shouldPopulateCouponCodeOnlyIfCorrespondingCouponIsNotPresent() throws Exception
	{
		//given
		final Optional<AbstractCouponModel> couponOptional = Optional.empty();
		given(couponService.getCouponForCode(COUPON_CODE)).willReturn(couponOptional);
		//when
		populator.populate(COUPON_CODE,appliedCoupon);
		//then
		assertThat(appliedCoupon.getCode()).isEqualTo(COUPON_CODE);
		assertThat(appliedCoupon.getName()).isNull();
	}

}
