/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.utility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ModalUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ModalUtilsTest
{
	@Mock
	private ReadService readService;

	@Test
	public void testServiceNameValidCorrect()
	{
		assertTrue(ModalUtils.isAlphaNumericName("InboundProduct42"));
	}

	@Test
	public void testServiceNameValidIncorrect()
	{
		assertFalse(ModalUtils.isAlphaNumericName(""));
		assertFalse(ModalUtils.isAlphaNumericName("$"));
		assertFalse(ModalUtils.isAlphaNumericName("/"));
		assertFalse(ModalUtils.isAlphaNumericName(" "));
	}

	@Test
	public void testServiceNameUniqueValid()
	{
		setupIOM();

		assertTrue(ModalUtils.isServiceNameUnique("OutboundProduct", readService));
	}

	@Test
	public void testServiceNameUniqueInvalid()
	{
		setupIOM();

		assertFalse(ModalUtils.isServiceNameUnique("InboundProduct42", readService));
	}

	@Test
	public void testIsAliasUniqueNoMatch()
	{
		String alias = "testAlias";
		String qualifierOfDTOToBeRename = "qualifier3";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertTrue(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsAliasUniqueAliasMatch()
	{
		String alias = "alias1";
		String qualifierOfDTOToBeRename = "qualifier3";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertFalse(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsAliasUniqueQualifierMatch()
	{
		String alias = "qualifier2";
		String qualifierOfDTOToBeRename = "qualifier3";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertFalse(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsAliasUniqueQualifierMatch2()
	{
		String alias = "qualifier2";
		String qualifierOfDTOToBeRename = "qualifier2";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertTrue(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsAliasUniqueQualifierMatch3()
	{
		String alias = "qualifier1";
		String qualifierOfDTOToBeRename = "qualifier1";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertTrue(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsAliasUniqueMatchClassAttrCode()
	{
		String alias = "attributeCode";
		String qualifierOfDTOToBeRename = "qualifier3";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertFalse(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsAliasUniqueMatchClassAttrCode2()  // add to list as long as alis equals to qualifier
	{
		String alias = "classCode";
		String qualifierOfDTOToBeRename = "classCode";
		List<AbstractListItemDTO> listItemDTOS = setupList();

		assertTrue(ModalUtils.isAliasUnique(alias, qualifierOfDTOToBeRename, listItemDTOS));
	}

	@Test
	public void testIsClassificationAttributeCodeAlphaNumericName()
	{
		final ClassAttributeAssignmentModel assignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationAttributeModel attribute = mock(ClassificationAttributeModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		given(assignment.getClassificationAttribute()).willReturn(attribute);
		given(assignment.getClassificationClass()).willReturn(classificationClass);
		given(assignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.STRING);
		given(classificationClass.getCode()).willReturn("code");
		given(attribute.getCode()).willReturn("Attribute %Code");

		final ListItemClassificationAttributeDTO dto = new ListItemClassificationAttributeDTO(true, false, false, assignment,
				StringUtils.EMPTY);

		assertTrue(ModalUtils.isAlphaNumericName(dto.getClassificationAttributeCode()));
	}

	private void setupIOM()
	{
		final List<IntegrationObjectModel> integrationObjectModels = new ArrayList<>();
		final IntegrationObjectModel inboundProduct42 = new IntegrationObjectModel();
		inboundProduct42.setCode("InboundProduct42");
		integrationObjectModels.add(inboundProduct42);
		when(readService.getIntgrationObjectModelByCode("InboundProduct42")).thenReturn(integrationObjectModels);
	}

	private List<AbstractListItemDTO> setupList()
	{
		AttributeDescriptorModel adm1 = new AttributeDescriptorModel();
		TypeModel tm1 = new TypeModel();
		tm1.setCode("Type1");
		adm1.setQualifier("qualifier1");
		adm1.setAttributeType(tm1);
		AttributeDescriptorModel adm2 = new AttributeDescriptorModel();
		adm2.setQualifier("qualifier2");
		adm2.setAttributeType(tm1);

		ClassAttributeAssignmentModel attributeAssignmentModel = new ClassAttributeAssignmentModel();
		ClassificationAttributeModel attributeModel = new ClassificationAttributeModel();
		ClassificationClassModel classificationClassModel = new ClassificationClassModel();
		attributeModel.setCode("attributeCode");
		attributeAssignmentModel.setClassificationAttribute(attributeModel);
		classificationClassModel.setCode("classCode");
		attributeAssignmentModel.setClassificationClass(classificationClassModel);
		attributeAssignmentModel.setAttributeType(ClassificationAttributeTypeEnum.STRING);
		attributeAssignmentModel.setMultiValued(false);

		String alias = "classificationAttribute";

		ListItemAttributeDTO attr1 = new ListItemAttributeDTO(true, false, false, adm1, ListItemStructureType.NONE, "alias1",
				null);
		ListItemAttributeDTO attr2 = new ListItemAttributeDTO(true, false, false, adm2, ListItemStructureType.NONE, "alias2",
				null);
		ListItemAttributeDTO attr3 = new ListItemAttributeDTO(true, false, false, adm1, ListItemStructureType.NONE, "qualifier1",
				null);
		ListItemClassificationAttributeDTO attr4 = new ListItemClassificationAttributeDTO(true, false, false,
				attributeAssignmentModel, alias);

		List<AbstractListItemDTO> attrList = new ArrayList<>();
		attrList.add(attr1);
		attrList.add(attr2);
		attrList.add(attr3);
		attrList.add(attr4);

		return attrList;
	}
}