/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import static de.hybris.platform.integrationservices.interceptor.AttributeValidationUtils.isClassificationAttributeNameDuplicated;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * An interceptor that validates the {@link IntegrationObjectItemClassificationAttributeModel}'s attribute name does not appear in the
 * {@link de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel}s of the
 * {@link IntegrationObjectItemModel} associated to the attribute.
 */
public class IntegrationClassificationAttributeUniqueNameValidateInterceptor
		implements ValidateInterceptor<IntegrationObjectItemClassificationAttributeModel>
{
	private static final String ERROR_MSG = "The attribute [%s] already exists in this integration object item. Please provide a different name for one of the [%s] attributes.";

	@Override
	public void onValidate(final IntegrationObjectItemClassificationAttributeModel attributeModel,
	                       final InterceptorContext interceptorContext) throws InterceptorException
	{
		final IntegrationObjectItemModel item = attributeModel.getIntegrationObjectItem();
		if (item != null && isClassificationAttributeNameDuplicated(attributeModel.getAttributeName(), item))
		{
			final String attributeName = attributeModel.getAttributeName();
			throw new InterceptorException(String.format(ERROR_MSG, attributeName, attributeName), this);
		}
	}
}
