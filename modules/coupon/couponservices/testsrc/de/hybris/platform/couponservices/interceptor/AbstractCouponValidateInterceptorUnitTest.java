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
package de.hybris.platform.couponservices.interceptor;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AbstractCouponValidateInterceptorUnitTest
{
	@InjectMocks
	private AbstractCouponValidateInterceptor validator;
	@Mock
	private InterceptorContext ctx;

	@Mock
	private L10NService l10NService;


	@Test(expected = IllegalArgumentException.class)
	public void testOnValidateModelIsNull() throws InterceptorException
	{
		when(l10NService.getLocalizedString("exception.abstractcouponvalidateinterceptor.model.cannot.null")).
				thenReturn("Coupon model cannot be NULL here");
		validator.onValidate(null, ctx);
	}

	@Test
	public void testOnValidateStartDateIsNull() throws InterceptorException
	{
		when(l10NService.getLocalizedString("exception.abstractcouponvalidateinterceptor.model.cannot.null")).
				thenReturn("Coupon model cannot be NULL here");
		final AbstractCouponModel model = getAbstractCouponModel(false);
		model.setStartDate(null);
		validator.onValidate(model, ctx);
	}

	@Test
	public void testOnValidateEndDateIsNull() throws InterceptorException
	{
		when(l10NService.getLocalizedString(anyString())).
				thenReturn("Coupon model cannot be NULL here");
		final AbstractCouponModel model = getAbstractCouponModel(false);
		model.setEndDate(null);
		validator.onValidate(model, ctx);
	}

	@Test
	public void testOnValidateTrue() throws InterceptorException
	{
		when(l10NService.getLocalizedString(anyString())).
				thenReturn("Coupon model cannot be NULL here");
		validator.onValidate(getAbstractCouponModel(false), ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateFalse() throws InterceptorException
	{
		when(l10NService.getLocalizedString(anyString())).
				thenReturn("Coupon model cannot be NULL here");
		validator.onValidate(getAbstractCouponModel(true), ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateFalseWhenEndDateInThePast() throws InterceptorException
	{
		final AbstractCouponModel abstractCouponModel = getAbstractCouponModel(true);
		final Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DAY_OF_YEAR, -1);
		abstractCouponModel.setEndDate(yesterday.getTime());
		when(l10NService.getLocalizedString("exception.abstractcouponvalidateinterceptor.enddate")).
				thenReturn("End date cannot be in the past");
		validator.onValidate(abstractCouponModel, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateFalseWhenEndDateLessThenStartDate() throws InterceptorException
	{
		final AbstractCouponModel abstractCouponModel = getAbstractCouponModel(true);
		final Calendar today = Calendar.getInstance();
		final Calendar startDate = (Calendar) today.clone();
		startDate.add(Calendar.DAY_OF_YEAR, 20);
		final Calendar endDate = (Calendar) today.clone();
		endDate.add(Calendar.DAY_OF_YEAR, 10);
		abstractCouponModel.setStartDate(startDate.getTime());
		abstractCouponModel.setEndDate(endDate.getTime());
		when(l10NService.getLocalizedString("exception.abstractcouponvalidateinterceptor.startdate.enddate")).
				thenReturn("Illegal value of startDate or endDate: endDate should be after startDate.");
		validator.onValidate(abstractCouponModel, ctx);
	}

	private AbstractCouponModel getAbstractCouponModel(final boolean exchangeDates)
	{
		final AbstractCouponModel abstractCouponModel = new AbstractCouponModel();
		final Calendar today = new GregorianCalendar();
		final Calendar startDate = (Calendar) today.clone();
		startDate.add(Calendar.DAY_OF_YEAR, -10);
		final Calendar endDate = (Calendar) today.clone();
		endDate.add(Calendar.DAY_OF_YEAR, 10);
		if (exchangeDates)
		{
			abstractCouponModel.setStartDate(endDate.getTime());
			abstractCouponModel.setEndDate(startDate.getTime());
		}
		else
		{
			abstractCouponModel.setStartDate(startDate.getTime());
			abstractCouponModel.setEndDate(endDate.getTime());
		}
		return abstractCouponModel;
	}

}
