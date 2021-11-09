/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.interceptor;


import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2b.mock.HybrisMokitoTest;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.workflow.interceptors.AutomatedWorkflowActionTemplateValidator;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;

import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class AutomatedWorkflowActionTemplateValidatorMockTest extends HybrisMokitoTest
{

	private final AutomatedWorkflowActionTemplateValidator actionTemplateValidator = new AutomatedWorkflowActionTemplateValidator();
	@Mock
	private InterceptorContext interceptorContextMock;
	@Mock
	private AutomatedWorkflowActionTemplateModel automatedWorkfow;

	@Test
	public void checkThatAutomatedWorkflowActionValidationIsCorrect() throws InterceptorException
	{
		when(automatedWorkfow.getJobHandler()).thenReturn("automatedWorkfow");
		actionTemplateValidator.onValidate(automatedWorkfow, interceptorContextMock);
	}

}
