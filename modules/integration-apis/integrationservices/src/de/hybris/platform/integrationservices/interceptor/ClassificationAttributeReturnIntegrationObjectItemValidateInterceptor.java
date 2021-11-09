/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import static de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum.REFERENCE;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * Interceptor to validate the classification attribute's {@link IntegrationObjectItemClassificationAttributeModel}
 * provides a non-null returnIntegrationObjectItem if its attribute type {@link ClassificationAttributeTypeEnum}
 * is a non-primitive type.
 */
public class ClassificationAttributeReturnIntegrationObjectItemValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemClassificationAttributeModel>
{
	private static final String ERROR_MSG = "Missing returnIntegrationObjectItem " +
			"for classification attribute [%s] of 'reference' attributeType.";

	@Override
	public void onValidate(final IntegrationObjectItemClassificationAttributeModel attributeModel, final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (isReferenceAttribute(attributeModel) && attributeModel.getReturnIntegrationObjectItem() == null)
		{
			throw new InterceptorException(String.format(ERROR_MSG, attributeModel.getAttributeName()), this);
		}
	}

	private boolean isReferenceAttribute(final IntegrationObjectItemClassificationAttributeModel attributeModel)
	{
		return REFERENCE.equals(attributeModel.getClassAttributeAssignment().getAttributeType());
	}
}
