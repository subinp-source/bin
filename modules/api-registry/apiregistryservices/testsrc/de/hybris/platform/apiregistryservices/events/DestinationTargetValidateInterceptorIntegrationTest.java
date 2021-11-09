/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.events;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DestinationTargetValidateInterceptorIntegrationTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	private DestinationTargetModel destinationTargetModel;

	@Before
   public void setUp()
	{
		destinationTargetModel = modelService.create(DestinationTargetModel.class);
		destinationTargetModel.setId("Testing Purpose");
		destinationTargetModel.setTemplate(true);
		modelService.save(destinationTargetModel);

	}

	@Test(expected = ModelSavingException.class)
	public void testValidationInterceptor()
	{
       destinationTargetModel.setTemplate(false);
       modelService.save(destinationTargetModel);
	}


}
