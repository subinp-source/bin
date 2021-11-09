/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsfacades.strategy.preprocessor;

import de.hybris.platform.xyformsservices.enums.YFormDataActionEnum;
import de.hybris.platform.xyformsservices.exception.YFormServiceException;

import java.util.Map;


/**
 * Preprocessor that applies changes to YForm Items before rendering them.
 */
public interface YFormPreprocessorStrategy
{
	/**
	 * Preprocesses a form
	 *
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @throws YFormServiceException
	 * @throws YFormProcessorException
	 */
	public void process(String applicationId, String formId, YFormDataActionEnum action, String formDataId)
			throws YFormServiceException, YFormProcessorException;

	/**
	 * Preprocesses a form
	 *
	 * @param applicationId
	 * @param formId
	 * @param action
	 * @param formDataId
	 * @param params
	 * @throws YFormServiceException
	 * @throws YFormProcessorException
	 */
	public void process(String applicationId, String formId, YFormDataActionEnum action, String formDataId,
			Map<String, Object> params) throws YFormServiceException, YFormProcessorException;

}
