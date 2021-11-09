/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Collection;

import org.assertj.core.util.Lists;

/**
 * Validates the {@link IntegrationObjectItemClassificationAttributeModel} is only associated to {@link de.hybris.platform.core.model.product.ProductModel} and its derivatives
 */
public class ClassificationAssociatesToProductOnlyValidateInterceptor implements ValidateInterceptor<IntegrationObjectItemClassificationAttributeModel>
{
	private static final String ERROR_MSG = "The classification attribute [%s] can only be assigned to an Integration Object Item that is a Product or its subtype.";

	@Override
	public void onValidate(final IntegrationObjectItemClassificationAttributeModel attributeModel, final InterceptorContext interceptorContext) throws InterceptorException
	{
		final Collection<ComposedTypeModel> types = getTypeHierarchy(attributeModel);
		if (!isProductType(types))
		{
			throw new InterceptorException(String.format(ERROR_MSG, attributeModel.getAttributeName()), this);
		}
	}

	private Collection<ComposedTypeModel> getTypeHierarchy(final IntegrationObjectItemClassificationAttributeModel attributeModel)
	{
		final ComposedTypeModel ioType = attributeModel.getIntegrationObjectItem().getType();
		final Collection<ComposedTypeModel> types = Lists.newArrayList(ioType);
		types.addAll(ioType.getAllSuperTypes());
		return types;
	}

	private boolean isProductType(final Collection<ComposedTypeModel> types)
	{
		return types.stream().anyMatch(typeModel -> ProductModel._TYPECODE.equals(typeModel.getCode()));
	}
}
