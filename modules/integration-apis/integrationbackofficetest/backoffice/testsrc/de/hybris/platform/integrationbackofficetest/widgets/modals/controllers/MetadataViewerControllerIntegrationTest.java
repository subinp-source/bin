/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.controllers;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.modals.controllers.IntegrationObjectMetadataViewerController;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.odata2services.odata.schema.entity.PluralizingEntitySetNameGenerator;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

public class MetadataViewerControllerIntegrationTest extends ServicelayerTest
{
	@Resource
	private TypeService typeService;
	@Resource
	private PluralizingEntitySetNameGenerator defaultPluralizingEntitySetNameGenerator;

	private final IntegrationObjectMetadataViewerController metadataViewerController = new IntegrationObjectMetadataViewerController();

	@Before
	public void setUp()
	{
		final GenericApplicationContext applicationContext = (GenericApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

		final AbstractBeanDefinition validationDefinition = BeanDefinitionBuilder.rootBeanDefinition(ReadService.class)
		                                                                          .getBeanDefinition();
		beanFactory.registerBeanDefinition("readService", validationDefinition);
		final ReadService readService = (ReadService) Registry.getApplicationContext().getBean("readService");
		readService.setTypeService(typeService);
		metadataViewerController.setReadService(readService);
		metadataViewerController.setPluralizer(defaultPluralizingEntitySetNameGenerator);
	}

	private void setBasicJSONTest() throws ImpExException
	{
		importImpEx(
		"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; BasicJSONTest; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; itemTypeMatch(code) ",
				"; BasicJSONTest; Catalog       ; Catalog       ; ; ALL_SUB_AND_SUPER_TYPES;  ",
				"; BasicJSONTest; Product       ; Product       ; true; ALL_SUB_AND_SUPER_TYPES;  ",
				"; BasicJSONTest; CatalogVersion; CatalogVersion; ; ALL_SUB_AND_SUPER_TYPES;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; BasicJSONTest:Catalog       ; id            ; Catalog:id            ;                            ; true;  ",
				"; BasicJSONTest:Product       ; code          ; Product:code          ;                            ; true;  ",
				"; BasicJSONTest:Product       ; catalogVersion; Product:catalogVersion; BasicJSONTest:CatalogVersion; true;  ",
				"; BasicJSONTest:CatalogVersion; catalog       ; CatalogVersion:catalog; BasicJSONTest:Catalog       ; true;  ",
				"; BasicJSONTest:CatalogVersion; version       ; CatalogVersion:version;                            ; true;  "
		);
	}

	@Test
	public void testGenerateEndpointPath() throws ImpExException
	{
		setBasicJSONTest();
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode("BasicJSONTest");
		assertNotNull(integrationObjectModel);

		metadataViewerController.setSelectedIntegrationObject(integrationObjectModel);

		assertEquals("https://<your-host-name>/odata2webservices/BasicJSONTest/Products",
				metadataViewerController.generateEndpointPath());

		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class, object -> object.getCode().equals("BasicJSONTest"));
	}

	public static String loadFileAsString(final String fileLocation) throws IOException
	{
		final ClassLoader classLoader = MetadataViewerControllerIntegrationTest.class.getClassLoader();
		final URL url = classLoader.getResource(fileLocation);
		File file = null;
		if (url != null)
		{
			file = new File(url.getFile());
		}

		return Files.readString(Paths.get(file.getPath()));
	}
}