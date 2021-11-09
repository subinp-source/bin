/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * Interceptor to validate the classification attribute's {@link de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel}
 * does not have {@code reference} as the attribute type and {@code localized} set to true.
 */
public final class ClassificationLocalizedReferenceValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemClassificationAttributeModel>
{
	private static final String ERROR_MSG = "Modeling classification attribute [%s] does not support localized reference.";

	@Override
	public void onValidate(
			final IntegrationObjectItemClassificationAttributeModel attributeModel,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		if (isLocalizedReference(attributeModel))
		{
			throw new InterceptorException(String.format(ERROR_MSG, attributeModel.getAttributeName()));
		}
	}

	private boolean isLocalizedReference(final IntegrationObjectItemClassificationAttributeModel attributeModel)
	{
		final var classAttrAssignment = attributeModel.getClassAttributeAssignment();
		return Boolean.TRUE.equals(classAttrAssignment.getLocalized()) &&
				ClassificationAttributeTypeEnum.REFERENCE.equals(classAttrAssignment.getAttributeType());
	}
}