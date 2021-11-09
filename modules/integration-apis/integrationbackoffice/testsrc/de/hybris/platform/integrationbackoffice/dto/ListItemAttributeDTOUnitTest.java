/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ListItemAttributeDTOUnitTest
{
	@Test
	public void testListItemAttributeDTOConstructor()
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");

		AttributeDescriptorModel attributeDescriptor = new AttributeDescriptorModel();
		attributeDescriptor.setAttributeType(typeModel);
		attributeDescriptor.setUnique(true);
		attributeDescriptor.setOptional(false);

		AbstractListItemDTO actual = new ListItemAttributeDTO(true, false, false,
				attributeDescriptor, ListItemStructureType.NONE, "", null);

		assertEquals(typeModel.getCode(), actual.getDescription());
	}

	@Test
	public void testListItemAttributeDTOIsRequired()
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");

		AttributeDescriptorModel attributeDescriptor = new AttributeDescriptorModel();
		attributeDescriptor.setAttributeType(typeModel);
		attributeDescriptor.setUnique(true);
		attributeDescriptor.setOptional(false);

		ListItemAttributeDTO actual = new ListItemAttributeDTO(true, false, false,
				attributeDescriptor, ListItemStructureType.NONE, "", null);

		assertTrue(actual.isRequired());

		attributeDescriptor.setUnique(false);
		assertFalse(actual.isRequired());

		attributeDescriptor.setUnique(true);
		attributeDescriptor.setOptional(true);
		assertFalse(actual.isRequired());
	}

	@Test
	public void testListItemAttributeDTOFindBaseTypeForMap()
	{
		// Case 1
		// Map of ComposedTypeModel
		// Product -> keywords
		ComposedTypeModel composedTypeModel = new ComposedTypeModel();
		composedTypeModel.setCode("Keyword");

		// Case 2
		// Map of Collections of AtomicTypeModel
		// Product -> name
		AtomicTypeModel atomicTypeModel = new AtomicTypeModel();
		atomicTypeModel.setCode("localized:java.lang.String");

		CollectionTypeModel collectionTypeModel = new CollectionTypeModel();
		collectionTypeModel.setElementType(atomicTypeModel);

		MapTypeModel mapTypeModel = new MapTypeModel();
		mapTypeModel.setCode("name");

		AttributeDescriptorModel attributeDescriptor = new AttributeDescriptorModel();
		attributeDescriptor.setUnique(true);
		attributeDescriptor.setOptional(false);

		// Case 1 test
		mapTypeModel.setReturntype(composedTypeModel);
		attributeDescriptor.setAttributeType(mapTypeModel);

		ListItemAttributeDTO actual = new ListItemAttributeDTO(true, false, false,
				attributeDescriptor, ListItemStructureType.MAP, "", null);

		assertEquals(mapTypeModel, actual.getBaseType());

		// Case 2 test
		mapTypeModel.setReturntype(collectionTypeModel);
		attributeDescriptor.setAttributeType(mapTypeModel);

		actual = new ListItemAttributeDTO(true, false, false,
				attributeDescriptor, ListItemStructureType.MAP, "", null);

		assertEquals(atomicTypeModel, actual.getBaseType());
	}

	@Test
	public void testFindMatchFound()
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");
		AttributeDescriptorModel attributeDescriptor = new AttributeDescriptorModel();
		attributeDescriptor.setAttributeType(typeModel);
		ComposedTypeModel compType1 = new ComposedTypeModel();

		ListItemAttributeDTO dtoToMatch = new ListItemAttributeDTO(true, false, false, attributeDescriptor,
				ListItemStructureType.NONE, "", typeModel);
		ListItemAttributeDTO dto2 = new ListItemAttributeDTO(true, false, false, new AttributeDescriptorModel(),
				ListItemStructureType.NONE, "", typeModel);

		Map<ComposedTypeModel, List<AbstractListItemDTO>> testMap = new HashMap<>();
		List<AbstractListItemDTO> dtos = new ArrayList<>();
		dtos.add(dtoToMatch);
		dtos.add(dto2);
		testMap.put(compType1, dtos);

		AbstractListItemDTO result = dtoToMatch.findMatch(testMap, compType1);

		assertEquals(dtoToMatch, result);
	}

	@Test
	public void testFindMatchNotFound() throws NoSuchElementException
	{
		TypeModel typeModel = new TypeModel();
		typeModel.setCode("Product");
		AttributeDescriptorModel attributeDescriptor = new AttributeDescriptorModel();
		attributeDescriptor.setAttributeType(typeModel);
		ComposedTypeModel compType1 = new ComposedTypeModel();

		ListItemAttributeDTO dtoToMatch = new ListItemAttributeDTO(true, false, false, attributeDescriptor,
				ListItemStructureType.NONE, "", typeModel);
		ListItemAttributeDTO dto2 = new ListItemAttributeDTO(true, false, false, new AttributeDescriptorModel(),
				ListItemStructureType.NONE, "", typeModel);

		Map<ComposedTypeModel, List<AbstractListItemDTO>> testMap = new HashMap<>();
		List<AbstractListItemDTO> dtos = new ArrayList<>();
		dtos.add(dto2);
		dtos.add(dto2);
		testMap.put(compType1, dtos);

		assertThatThrownBy(() -> dtoToMatch.findMatch(testMap, compType1))
				.isInstanceOf(NoSuchElementException.class)
				.hasMessage("No AttributeDescriptor was found");
	}

}
