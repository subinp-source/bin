/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

import java.util.Map;



/**
 * No transformation is applied to the formData content
 */
public class EmptyTransformerYFormPreprocessorStrategy extends TransformerYFormPreprocessorStrategy
{
	@Override
	protected String transform(final String xmlContent, final Map<String, Object> params) throws YFormProcessorException
	{
		return xmlContent;
	}
}
