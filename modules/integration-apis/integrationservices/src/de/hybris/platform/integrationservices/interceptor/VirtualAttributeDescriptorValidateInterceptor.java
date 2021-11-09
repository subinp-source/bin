/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor;

import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;
import de.hybris.platform.integrationservices.util.AtomicTypeModelRetriever;
import de.hybris.platform.integrationservices.scripting.LogicLocation;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.List;

/**
 * Interceptor to validate that only {@link IntegrationObjectVirtualAttributeDescriptorModel}
 * with primitive {@code type} are allowed.
 */
public class VirtualAttributeDescriptorValidateInterceptor
		implements ValidateInterceptor<IntegrationObjectVirtualAttributeDescriptorModel>
{
	private static final String LOCATION_MSG = "URI '[%s]' provided does not meet the pattern model://logicLocation";
	private static final List<String> SUPPORTED_PRIMITIVES = List.of("java.lang.Integer", "java.lang.Boolean", "java.lang.Byte",
			"java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.String", "java.lang.Character",
			"java.util.Date", "java.math.BigInteger", "java.math.BigDecimal");
	private static final String UNSUPPORTED_TYPE_MSG = "Invalid [%s] type for virtual attribute descriptor with code [%s]. " +
			"Virtual attribute descriptor must have supported primitive type from the following list: " + SUPPORTED_PRIMITIVES;


	private final AtomicTypeModelRetriever atomicTypeRetriever;

	public VirtualAttributeDescriptorValidateInterceptor(
			final AtomicTypeModelRetriever atomicTypeRetriever)
	{
		this.atomicTypeRetriever = atomicTypeRetriever;
	}

	@Override
	public void onValidate(
			final IntegrationObjectVirtualAttributeDescriptorModel virtualAttributeDescriptorModel,
			final InterceptorContext interceptorContext) throws InterceptorException
	{
		setTypeIfNecessary(virtualAttributeDescriptorModel);
		if (!isSupportedPrimitive(virtualAttributeDescriptorModel))
		{
			throw new InterceptorException(
					String.format(UNSUPPORTED_TYPE_MSG, virtualAttributeDescriptorModel.getType().getCode(),
							virtualAttributeDescriptorModel.getCode()), this);
		}
		if (!isValidFormat(virtualAttributeDescriptorModel))
		{
			throw new InterceptorException(String.format(LOCATION_MSG, virtualAttributeDescriptorModel.getLogicLocation()), this);
		}
	}

	private boolean isSupportedPrimitive(final IntegrationObjectVirtualAttributeDescriptorModel virtualAttributeDescriptorModel)
	{
		return SUPPORTED_PRIMITIVES.contains(virtualAttributeDescriptorModel.getType().getCode());
	}

	private boolean isValidFormat(final IntegrationObjectVirtualAttributeDescriptorModel virtualAttributeDescriptor)
	{
		return LogicLocation.isValid(virtualAttributeDescriptor.getLogicLocation());
	}

	private void setTypeIfNecessary(final IntegrationObjectVirtualAttributeDescriptorModel virtualAttributeDescriptorModel)
	{
		if(virtualAttributeDescriptorModel.getType() == null)
		{
			virtualAttributeDescriptorModel.setType(atomicTypeRetriever.get("java.lang.String"));
		}
	}
}
