/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.integrationbackoffice.widgets.modals.controllers.IntegrationObjectMetadataViewerController;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MetadataViewerControllerUnitTest
{

	@Test
	public void testGenerateServicePath()
	{
		final IntegrationObjectModel integrationObject = mock(IntegrationObjectModel.class);
		when(integrationObject.getCode()).thenReturn("Product1");

		final IntegrationObjectMetadataViewerController controller = new IntegrationObjectMetadataViewerController();

		controller.setSelectedIntegrationObject(integrationObject);

		assertEquals("https://<your-host-name>/odata2webservices/Product1/$metadata", controller.generateServicePath());
	}

}
