/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.interceptor;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * An interceptor that validates {@link IntegrationClientCredentialsDetailsModel}'s user is not assigned to the admin user.
 */
public class IntegrationClientCredentialsUserValidateInterceptor
		implements ValidateInterceptor<IntegrationClientCredentialsDetailsModel>
{
	private static final String ERROR_MSG = "Cannot create IntegrationClientCredentialsDetails with admin user.";
	private static final String ADMIN_UID = "admin";

	@Override
	public void onValidate(final IntegrationClientCredentialsDetailsModel integrationClientCredentialsDetailsModel,
	                       final InterceptorContext interceptorContext) throws InterceptorException
	{
		final EmployeeModel user = integrationClientCredentialsDetailsModel.getUser();
		if (user != null && ADMIN_UID.equals(user.getUid()))
		{
			throw new InterceptorException(ERROR_MSG);
		}
	}
}
