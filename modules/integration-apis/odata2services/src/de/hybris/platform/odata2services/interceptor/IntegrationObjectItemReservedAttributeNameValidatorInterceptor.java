/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2services.interceptor;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.LOCALIZED_ATTRIBUTE_NAME;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * An interceptor that validates {@link IntegrationObjectItemAttributeModel}'s attribute name does not equal the reserved names
 * "localizedAttributes" and "integrationKey".
 */
public class IntegrationObjectItemReservedAttributeNameValidatorInterceptor implements ValidateInterceptor<IntegrationObjectItemAttributeModel>
{
	private static final String ERROR_MSG = "[%s] attribute name is reserved. Please rename this attribute.";

	@Override
	public void onValidate(final IntegrationObjectItemAttributeModel attributeModel, final InterceptorContext interceptorContext) throws InterceptorException
	{
		final String attributeName = attributeModel.getAttributeName();
		if (isReservedName(attributeName))
		{
			throw new InterceptorException(String.format(ERROR_MSG, attributeName), this);
		}
	}

	private boolean isReservedName(final String attributeName){
		return LOCALIZED_ATTRIBUTE_NAME.equals(attributeName) || INTEGRATION_KEY_PROPERTY_NAME.equals(attributeName);
	}
}
