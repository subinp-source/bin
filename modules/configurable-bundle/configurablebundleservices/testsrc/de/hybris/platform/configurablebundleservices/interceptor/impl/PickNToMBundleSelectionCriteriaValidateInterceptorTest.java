/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.interceptor.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test to check validations applied on PickNToMSelection criteria
 */
@UnitTest
public class PickNToMBundleSelectionCriteriaValidateInterceptorTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private InterceptorContext ctx;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testNAndMshouldBeNonNegative() throws InterceptorException
	{
		thrown.expect(InterceptorException.class);
		thrown.expectMessage("N or M cannot be less than 0");

		callInterceptor(Integer.valueOf(-5), Integer.valueOf(9));

	}

	private void callInterceptor(final Integer n, final Integer m) throws InterceptorException
	{
		final PickNToMBundleSelectionCriteriaValidateInterceptor interceptor = new PickNToMBundleSelectionCriteriaValidateInterceptor();
		final PickNToMBundleSelectionCriteriaModel model = new PickNToMBundleSelectionCriteriaModel();
		model.setN(n);
		model.setM(m);
		interceptor.onValidate(model, ctx);


	}

	@Test
	public void testMshouldBeGreaterThanN() throws InterceptorException
	{
		callInterceptor(Integer.valueOf(5), Integer.valueOf(9));
	}

	@Test
	public void testNAndMCannotBeGreaterThan() throws InterceptorException
	{
		thrown.expect(InterceptorException.class);
		thrown.expectMessage("N can take values between 0 and M-1");

		callInterceptor(Integer.valueOf(5), Integer.valueOf(5));
	}
}
