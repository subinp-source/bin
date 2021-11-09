/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;


/**
 * Empty strategy for processing Forms. No changes are applied to current form content before it is rendered
 */
public class EmptyYFormPreprocessorStrategy implements YFormPreprocessorStrategy
{
	@Override
	public void process(final String applicationId, final String formId, final YFormDataActionEnum action, final String formDataId,
			final Map<String, Object> params) throws YFormServiceException, YFormProcessorException
	{
		// Empty preprocessing
	}

	@Override
	public void process(final String applicationId, final String formId, final YFormDataActionEnum action, final String formDataId)
			throws YFormServiceException, YFormProcessorException
	{
		// Empty preprocessing
	}
}
