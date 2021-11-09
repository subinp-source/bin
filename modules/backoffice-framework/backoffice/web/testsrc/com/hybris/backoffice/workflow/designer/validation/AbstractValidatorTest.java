/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import org.mockito.Mock;

import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NetworkEntityFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;


public abstract class AbstractValidatorTest
{

	@Mock
	NodeTypeService mockedNodeTypeService;

	@Mock
	NetworkEntityFinder mockedNetworkEntityFinder;

	@Mock
	ConnectionFinder mockedConnectionFinder;

}
