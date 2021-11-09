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
 * does not have {@code multiValued} and {@code localized} set to true.
 */
public class ClassificationMultipleValueValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemClassificationAttributeModel>
{
    private static final String ERROR_MSG = "Modeling classification attribute [%s] does not support both multiValued and localized being enabled.";

    @Override
    public void onValidate(final IntegrationObjectItemClassificationAttributeModel attributeModel, final InterceptorContext interceptorContext) throws InterceptorException
    {
        if (isMultiValuedAndLocalized(attributeModel))
        {
            throw new InterceptorException(String.format(ERROR_MSG, attributeModel.getAttributeName()));
        }
    }

    private boolean isMultiValuedAndLocalized(final IntegrationObjectItemClassificationAttributeModel attributeModel)
    {
        final var classAttrAssignment = attributeModel.getClassAttributeAssignment();
        return classAttrAssignment.getMultiValued() &&
                classAttrAssignment.getLocalized();
    }
}
