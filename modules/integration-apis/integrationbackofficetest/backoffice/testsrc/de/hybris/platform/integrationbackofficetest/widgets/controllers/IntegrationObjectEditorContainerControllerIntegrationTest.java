/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.controllers;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.builders.DefaultDataStructureBuilder;
import de.hybris.platform.integrationbackoffice.widgets.editor.controllers.IntegrationObjectEditorContainerController;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationObjectPresentation;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.SubtypeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAttributesFilteringService;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

public class IntegrationObjectEditorContainerControllerIntegrationTest extends ServicelayerTest
{
	@Resource
	private TypeService typeService;
	private EditorAttributesFilteringService editorAttrFilterService;

	private ReadService readService;
	private DefaultDataStructureBuilder dataStructureBuilder;
	private IntegrationObjectPresentation integrationObjectPresentation;

	final private IntegrationObjectEditorContainerController controller = new IntegrationObjectEditorContainerController();

	@Before
	public void setUp() throws Exception
	{
		integrationObjectPresentation = new IntegrationObjectPresentation();
		final GenericApplicationContext applicationContext = (GenericApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

		final AbstractBeanDefinition validationDefinition = BeanDefinitionBuilder.rootBeanDefinition(ReadService.class)
		                                                                         .getBeanDefinition();
		beanFactory.registerBeanDefinition("readService", validationDefinition);
		readService = (ReadService) Registry.getApplicationContext().getBean("readService");
		readService.setTypeService(typeService);
		integrationObjectPresentation.setReadService(readService);
		dataStructureBuilder = new DefaultDataStructureBuilder(readService, editorAttrFilterService);
		integrationObjectPresentation.setDataStructureBuilder(dataStructureBuilder);
		controller.setEditorPresentation(integrationObjectPresentation);
		setCompileSubtypeSetTestImpex();
	}

	@After
	public void tearDown()
	{
		IntegrationTestUtil.removeSafely(IntegrationObjectModel.class,
				object -> object.getCode().equals("CompileSubtypeSetTestImpex"));
	}

	private void setCompileSubtypeSetTestImpex() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; CompileSubtypeSetTestImpex; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false] ",
				"; CompileSubtypeSetTestImpex; Order   ; Order   ;   true;",
				"; CompileSubtypeSetTestImpex; Customer; Customer;       ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; CompileSubtypeSetTestImpex:Order   ; user      ; Order:user         ; CompileSubtypeSetTestImpex:Customer;       ;",
				"; CompileSubtypeSetTestImpex:Order   ; code      ; Order:code         ;                                    ;   true;",
				"; CompileSubtypeSetTestImpex:Customer; name      ; Customer:name      ;                                    ;       ;",
				"; CompileSubtypeSetTestImpex:Customer; customerID; Customer:customerID;                                    ;   true;"
		);
	}

	@Test
	public void readServiceTest()
	{
		assertTrue(Objects.nonNull(readService));
	}

	@Test
	public void compileSubtypeDataSetTest()
	{
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CompileSubtypeSetTestImpex");
		assertNotNull(integrationObjectModel);

		final Map<ComposedTypeModel, List<AbstractListItemDTO>> convertedMap = EditorUtils.convertIntegrationObjectToDTOMap(
				readService, integrationObjectModel);
		integrationObjectPresentation.setSubtypeDataSet(dataStructureBuilder.compileSubtypeDataSet(convertedMap, new HashSet<>()));

		final SubtypeData subtypeData = (SubtypeData) integrationObjectPresentation.getSubtypeDataSet().toArray()[0];

		assertEquals("CompileSubtypeSetTestImpex", integrationObjectModel.getCode());
		assertEquals(1, integrationObjectPresentation.getSubtypeDataSet().size());
		assertEquals("User", subtypeData.getBaseType().getCode());
		assertEquals("Customer", subtypeData.getSubtype().getCode());
		assertEquals("Order", subtypeData.getParentNodeType().getCode());
		assertEquals("user", subtypeData.getAttributeAlias());
	}

	@Test
	public void findSubtypeMatchTest()
	{
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CompileSubtypeSetTestImpex");
		assertNotNull(integrationObjectModel);

		final Map<ComposedTypeModel, List<AbstractListItemDTO>> convertedMap = EditorUtils.convertIntegrationObjectToDTOMap(
				readService, integrationObjectModel);
		integrationObjectPresentation.setSubtypeDataSet(
				dataStructureBuilder.compileSubtypeDataSet(convertedMap, new HashSet<>()));

		ComposedTypeModel order = integrationObjectModel.getRootItem().getType();
		SubtypeData subtypeData = (SubtypeData) integrationObjectPresentation.getSubtypeDataSet().toArray()[0];
		ComposedTypeModel attributeType = (ComposedTypeModel) subtypeData.getBaseType();
		String qualifier = "user";
		ComposedTypeModel expectedSubtype = (ComposedTypeModel) subtypeData.getSubtype();

		ComposedTypeModel actualSubtype = dataStructureBuilder.findSubtypeMatch(order, qualifier, attributeType,
				integrationObjectPresentation.getSubtypeDataSet());

		assertEquals("CompileSubtypeSetTestImpex", integrationObjectModel.getCode());
		assertEquals(expectedSubtype, actualSubtype);
	}
}