/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.interceptor;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import org.springframework.beans.factory.annotation.Required;


/**
 * This interceptor ensures that all new B2B Customers are associated with a B2BUnit {@link B2BCustomerModel},
 * {@link B2BUnitModel}
 */

public class B2BCustomerModelValidateInterceptor implements ValidateInterceptor
{
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private L10NService l10NService;

	@Override
	public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
	{

		if (model instanceof B2BCustomerModel)
		{
			final B2BCustomerModel customer = (B2BCustomerModel) model;
			final B2BUnitModel parentUnit = b2bUnitService.getParent(customer);

			if (null == parentUnit)
			{
				throw new InterceptorException(getL10NService().getLocalizedString("error.b2bcustomer.b2bunit.missing"));
			}

			if (customer.getActive().booleanValue() && !parentUnit.getActive().booleanValue())
			{
				throw new InterceptorException(getL10NService().getLocalizedString("error.b2bcustomer.enable.b2bunit.disabled"));
			}
		}
	}

	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	@Required
	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public L10NService getL10NService()
	{
		return l10NService;
	}

	@Required
	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}
}
