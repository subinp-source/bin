/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.interceptor;

import de.hybris.platform.inboundservices.model.IntegrationClientCredentialsDetailsModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Set;

import javax.validation.constraints.NotNull;

/**
 * An interceptor that validates {@link IntegrationClientCredentialsDetailsModel}'s authorizedGrantTypes is equal to the expected
 * default value.
 */
public class IntegrationClientCredentialsAuthorizedGrantTypesValidateInterceptor
		implements ValidateInterceptor<IntegrationClientCredentialsDetailsModel>
{
	private static final String VALID_GRANT_TYPE = "client_credentials";
	private static final String ERROR_MSG = "Cannot create IntegrationClientCredentialsDetails with a provided value for authorizedGrantTypes.";

	@Override
	public void onValidate(@NotNull final IntegrationClientCredentialsDetailsModel integrationClientCredentialsDetailsModel,
	                       final InterceptorContext interceptorContext) throws InterceptorException
	{
		final Set<String> grantTypes = integrationClientCredentialsDetailsModel.getAuthorizedGrantTypes();
		if (grantTypes != null && (grantTypes.size() != 1 || !VALID_GRANT_TYPE.equals(grantTypes.iterator().next())))
		{
			throw new InterceptorException(ERROR_MSG);
		}
	}
}
