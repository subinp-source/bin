/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.types.controller;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.cmsfacades.types.AbstractComponentTypeFacadeIntegrationTest;
import de.hybris.platform.cmsfacades.types.ComponentTypeNotFoundException;
import de.hybris.platform.cmsfacades.types.impl.DefaultComponentTypeFacade;
import de.hybris.platform.cmsfacades.types.service.ComponentTypeStructureService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class ComponentTypeFacadeWithGenericServiceIntegrationTest extends ServicelayerTest
{

	private final AbstractComponentTypeFacadeIntegrationTest abstractIntegrationTest = new AbstractComponentTypeFacadeIntegrationTest();

	private final boolean READ_ONLY_MODE = true;
	private final boolean NON_READ_ONLY_MODE = false;

	@Resource
	protected DefaultComponentTypeFacade componentTypeFacade;

	@Resource
	private ComponentTypeStructureService genericComponentTypeStructureService;

	@Resource
	private UserService userService;

	@Resource
	private TypeService typeService;

	@Before
	public void setup() throws ImpExException
	{
		importCsv("/cmswebservices/test/impex/essentialTestDataAuth.impex", "utf-8");

		final UserModel cmsmanager = userService.getUserForUID("cmsmanager");
		userService.setCurrentUser(cmsmanager);

		abstractIntegrationTest.setComponentTypeFacade(this.componentTypeFacade);
		abstractIntegrationTest.getComponentTypeFacade().setComponentTypeStructureService(genericComponentTypeStructureService);
		abstractIntegrationTest.setup();
	}

	@Test
	public void shouldGetCategoryPageComponentType_FromAllTypes()
	{
		abstractIntegrationTest.shouldGetCategoryPageComponentType_FromAllTypes();
	}

	@Test
	public void shouldGetCategoryPageComponentType_FromSingleType() throws ComponentTypeNotFoundException
	{
		abstractIntegrationTest.shouldGetCategoryPageComponentType_FromSingleType();
	}

	@Test
	public void shouldGetContentPageComponentType_FromSingleType() throws ComponentTypeNotFoundException
	{
		abstractIntegrationTest.shouldGetContentPageComponentType_FromSingleType();
	}

	@Test
	public void shouldFilterAnyAbstractComponentType()
	{
		//execute
		final List<ComponentTypeData> componentTypes = componentTypeFacade.getAllCmsItemTypes(Arrays.asList(StructureTypeCategory.values()), NON_READ_ONLY_MODE);
		final List<String> collectedTypes = componentTypes.stream().map(ComponentTypeData::getCode)
				.filter(typeCode -> typeService.getComposedTypeForCode(typeCode).getAbstract()).collect(toList());

		//assert
		assertThat("AbstractComponentTypeFacadeIntegrationTest should filter all abstract classes", collectedTypes,
				emptyIterable());
	}

	@Test
	public void shouldGetAllCmsItemTypesWithNonEditableAttributes()
	{
		// WHEN
		final List<ComponentTypeData> allItemTypes = componentTypeFacade
				.getAllCmsItemTypes(Arrays.asList(StructureTypeCategory.values()), READ_ONLY_MODE);

		// THEN
		final Set<ComponentTypeAttributeData> allAttributes = allItemTypes.stream().flatMap(type -> type.getAttributes().stream())
				.collect(toSet());
		final boolean allInReadOnly = allAttributes.stream().noneMatch(ComponentTypeAttributeData::isEditable);
		assertTrue(allInReadOnly);
	}

	@Test
	public void shouldGetEmptyListIfCategoriesNotProvided()
	{
		// WHEN
		final List<ComponentTypeData> allItemTypes = componentTypeFacade.getAllCmsItemTypes(Arrays.asList(), READ_ONLY_MODE);

		// THEN
		assertThat(allItemTypes, empty());
	}

	@Test
	public void shouldGetSomeCmsItemTypesWithEditableAttributes()
	{
		// WHEN
		final List<ComponentTypeData> allItemTypes = componentTypeFacade
				.getAllCmsItemTypes(Arrays.asList(StructureTypeCategory.values()), NON_READ_ONLY_MODE);

		// THEN
		final Set<ComponentTypeAttributeData> allAttributes = allItemTypes.stream().flatMap(type -> type.getAttributes().stream())
				.collect(toSet());
		final boolean someEditable = allAttributes.stream().anyMatch(ComponentTypeAttributeData::isEditable);
		assertTrue(someEditable);
	}

	@Test
	public void shouldGetCmsItemTypeWithNonEditableAttributes() throws ComponentTypeNotFoundException
	{
		// WHEN
		final ComponentTypeData typeData = componentTypeFacade
				.getCmsItemTypeByCodeAndMode(CMSParagraphComponentModel._TYPECODE, null, READ_ONLY_MODE);

		// THEN
		final List<ComponentTypeAttributeData> attributes = typeData.getAttributes();
		final boolean allInReadOnly = attributes.stream().noneMatch(ComponentTypeAttributeData::isEditable);
		assertTrue(allInReadOnly);
	}

	@Test
	public void shouldGetCmsItemTypeWithEditableAttributes() throws ComponentTypeNotFoundException
	{
		// WHEN
		final ComponentTypeData typeData = componentTypeFacade
				.getCmsItemTypeByCodeAndMode(CMSParagraphComponentModel._TYPECODE, null, NON_READ_ONLY_MODE);

		// THEN
		final List<ComponentTypeAttributeData> attributes = typeData.getAttributes();
		final boolean someEditable = attributes.stream().anyMatch(ComponentTypeAttributeData::isEditable);
		assertTrue(someEditable);
	}
}
