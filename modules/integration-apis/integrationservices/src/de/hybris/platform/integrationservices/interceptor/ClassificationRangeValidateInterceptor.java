/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * Interceptor to validate the classification attribute's {@link de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel}
 * does not have {@code range} set to true. Currently, Integration API does not support classification attribute with range.
 */
public final class ClassificationRangeValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemClassificationAttributeModel>
{
	private static final String ERROR_MSG = "Modeling classification attribute [%s] does not support range being enabled.";

	@Override
	public void onValidate(final IntegrationObjectItemClassificationAttributeModel attributeModel, final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (isRangeEnabled(attributeModel))
		{
			throw new InterceptorException(String.format(ERROR_MSG, attributeModel.getAttributeName()), this);
		}
	}

	private boolean isRangeEnabled(final IntegrationObjectItemClassificationAttributeModel attributeModel)
	{
		return attributeModel.getClassAttributeAssignment().getRange();
	}
}
