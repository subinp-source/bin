/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;

@IntegrationTest
public class DestinationValidateInterceptorIntegrationTest extends ServicelayerTransactionalTest
{

	@Resource
	private ModelService modelService;


	@Test(expected = ModelSavingException.class)
	public void consumedDestinationValidationInterceptorWithInvalidCredentialTest()
	{

		final ConsumedDestinationModel consumedDestinationModel = new ConsumedDestinationModel();

		consumedDestinationModel.setCredential(new ExposedOAuthCredentialModel());
		modelService.save(consumedDestinationModel);
	}

	@Test(expected = ModelSavingException.class)
	public void exposedDestinationValidationInterceptorWithInvalidCredentialTest()
	{

		final ExposedDestinationModel exposedDestinationModel = new ExposedDestinationModel();
		exposedDestinationModel.setCredential(new ConsumedOAuthCredentialModel());
		modelService.save(exposedDestinationModel);
	}
}
