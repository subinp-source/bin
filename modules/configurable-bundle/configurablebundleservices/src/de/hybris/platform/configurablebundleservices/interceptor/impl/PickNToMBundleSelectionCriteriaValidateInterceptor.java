/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.configurablebundleservices.model.PickNToMBundleSelectionCriteriaModel;

/**
 * Validates that the selection criteria has between integer n and integer m selections n>=0 and m >=0
 * 
 * and that n can take values between 0 and m-1
 */
public class PickNToMBundleSelectionCriteriaValidateInterceptor implements ValidateInterceptor
{

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (!(model instanceof PickNToMBundleSelectionCriteriaModel))
		{
			return;
		}

		final PickNToMBundleSelectionCriteriaModel pickNToMSelection = (PickNToMBundleSelectionCriteriaModel) model;
		if (isLessThanZero(pickNToMSelection.getN()) || isLessThanZero(pickNToMSelection.getM()))
		{
			throw new InterceptorException("N or M cannot be less than 0");
		}

		if (pickNToMSelection.getN() != null && pickNToMSelection.getM() != null
				&& pickNToMSelection.getN() >= pickNToMSelection.getM())
		{
			throw new InterceptorException("N can take values between 0 and M-1");
		}
	}

	protected boolean isLessThanZero(Integer value)
	{
		return value != null && value < 0;
	}
}
