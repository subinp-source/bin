/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.interceptor.impl;

import de.hybris.platform.configurablebundleservices.model.PickExactlyNBundleSelectionCriteriaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;


/**
 * Validate that the selection criteria has at least 1 selection
 */
public class PickExactlyNBundleSelectionCriteriaValidateInterceptor implements ValidateInterceptor
{

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model.getClass().equals(PickExactlyNBundleSelectionCriteriaModel.class))
		{
			final PickExactlyNBundleSelectionCriteriaModel pickExactlyNCriteria = (PickExactlyNBundleSelectionCriteriaModel) model;

			if (pickExactlyNCriteria.getN() != null && pickExactlyNCriteria.getN().intValue() < 1)
			{
				throw new InterceptorException("Selection Criteria " + pickExactlyNCriteria.getId()
						+ "pick number must be greater than or equal to 1");
			}
		}

	}

}
