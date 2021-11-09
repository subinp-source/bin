/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import static de.hybris.platform.integrationservices.interceptor.AttributeValidationUtils.isVirtualAttributeNameDuplicated;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * Interceptor to validate the type {@link IntegrationObjectItemVirtualAttributeModel}
 */
public class IntegrationObjectItemVirtualAttributeModelInterceptor
		implements ValidateInterceptor<IntegrationObjectItemVirtualAttributeModel>
{
	private static final String RETURN_OBJ_MUST_BE_NULL_MSG = "Virtual attribute cannot have a returnIntegrationObjectItem. Only " +
			"primitive virtual attributes are currently supported. Found [%s] attribute with a returnIntegrationObjectItem set to [%s].";

	private static final String DUPLICATE_ERROR_MSG = "The attribute [%s] already exists in this integration object item. Please provide a different name for one of the [%s] attributes.";


	@Override
	public void onValidate(final IntegrationObjectItemVirtualAttributeModel attributeModel,
	                       final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (attributeModel.getReturnIntegrationObjectItem() != null)
		{
			throw new InterceptorException(String.format(RETURN_OBJ_MUST_BE_NULL_MSG, attributeModel.getAttributeName(),
					attributeModel.getReturnIntegrationObjectItem().getCode()), this);
		}

		final IntegrationObjectItemModel item = attributeModel.getIntegrationObjectItem();
		if (item != null && isVirtualAttributeNameDuplicated(attributeModel.getAttributeName(), item))
		{
			throw new InterceptorException(
					String.format(DUPLICATE_ERROR_MSG, attributeModel.getAttributeName(), attributeModel.getAttributeName()));
		}
	}
}
