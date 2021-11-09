/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.TestUtils;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorValidator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.testframework.Assert.assertEquals;

@UnitTest
public class EditorValidatorUnitTest
{
	private final ComposedTypeModel c1 = new ComposedTypeModel();
	private final ComposedTypeModel c2 = new ComposedTypeModel();
	private final ComposedTypeModel c3 = new ComposedTypeModel();

	private final AttributeDescriptorModel a1 = new AttributeDescriptorModel();
	private final AttributeDescriptorModel baseADM = new AttributeDescriptorModel();

	@Before
	public void setup()
	{
		c1.setCode("Comp1");
		c2.setCode("Comp2");
		c3.setCode("Comp3");

		final TypeModel t = new TypeModel();

		t.setCode("");
		a1.setUnique(true);
		a1.setAttributeType(t);
		baseADM.setAttributeType(t);
		baseADM.setUnique(false);
	}

	@Test
	public void validateDefinitionsValidTest()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> validMap = new HashMap<>();

		final ListItemAttributeDTO itemSelect = new ListItemAttributeDTO(true, false, false, baseADM, ListItemStructureType.NONE,
				"", null);
		final ListItemAttributeDTO itemNotSelect = new ListItemAttributeDTO(false, false, false, baseADM,
				ListItemStructureType.NONE, "", null);

		final List<AbstractListItemDTO> l1 = new ArrayList<>();
		final List<AbstractListItemDTO> l2 = new ArrayList<>();
		final List<AbstractListItemDTO> l3 = new ArrayList<>();

		l1.add(itemNotSelect);
		l1.add(itemNotSelect);
		l1.add(itemNotSelect);
		l1.add(itemSelect);

		l2.add(itemNotSelect);
		l2.add(itemNotSelect);
		l2.add(itemSelect);
		l2.add(itemSelect);

		l3.add(itemSelect);
		l3.add(itemNotSelect);
		l3.add(itemNotSelect);
		l3.add(itemSelect);

		validMap.put(c1, l1);
		validMap.put(c2, l2);
		validMap.put(c3, l3);

		assertEquals("", EditorValidator.validateDefinitions(validMap));
	}

	@Test
	public void validateDefinitionsInvalidTest()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> invalidMap = new HashMap<>();

		final ListItemAttributeDTO itemSelect = new ListItemAttributeDTO(true, false, false, baseADM, ListItemStructureType.NONE,
				"", null);
		final ListItemAttributeDTO itemNotSelect = new ListItemAttributeDTO(false, false, false, baseADM,
				ListItemStructureType.NONE, "", null);

		final List<AbstractListItemDTO> l1 = new ArrayList<>();
		final List<AbstractListItemDTO> l2 = new ArrayList<>();
		final List<AbstractListItemDTO> l3 = new ArrayList<>();

		//l1 has nothing

		l2.add(itemNotSelect);
		l2.add(itemNotSelect);
		l2.add(itemSelect);
		l2.add(itemSelect);

		l3.add(itemSelect);
		l3.add(itemNotSelect);
		l3.add(itemNotSelect);
		l3.add(itemSelect);

		invalidMap.put(c1, l1);
		invalidMap.put(c2, l2);
		invalidMap.put(c3, l3);

		assertEquals("Comp1", EditorValidator.validateDefinitions(invalidMap));
	}

	@Test
	public void validateHasKeyValidTest()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> validMap = new HashMap<>();

		final ListItemAttributeDTO itemUnique = new ListItemAttributeDTO(true, false, false, a1, ListItemStructureType.NONE, "",
				null);
		final ListItemAttributeDTO itemCustomUnique = new ListItemAttributeDTO(true, true, false, baseADM,
				ListItemStructureType.NONE, "", null);
		final ListItemAttributeDTO itemSelect = new ListItemAttributeDTO(true, false, false, baseADM, ListItemStructureType.NONE,
				"", null);
		final ListItemAttributeDTO itemNotSelect = new ListItemAttributeDTO(false, false, false, baseADM,
				ListItemStructureType.NONE, "", null);

		final List<AbstractListItemDTO> l1 = new ArrayList<>();
		final List<AbstractListItemDTO> l2 = new ArrayList<>();
		final List<AbstractListItemDTO> l3 = new ArrayList<>();

		l1.add(itemNotSelect);
		l1.add(itemNotSelect);
		l1.add(itemNotSelect);
		l1.add(itemUnique);

		l2.add(itemNotSelect);
		l2.add(itemNotSelect);
		l2.add(itemSelect);
		l2.add(itemSelect);
		l2.add(itemCustomUnique);

		l3.add(itemSelect);
		l3.add(itemNotSelect);
		l3.add(itemNotSelect);
		l3.add(itemSelect);
		l3.add(itemUnique);
		l3.add(itemCustomUnique);

		validMap.put(c1, l1);
		validMap.put(c2, l2);
		validMap.put(c3, l3);

		assertEquals("", EditorValidator.validateHasKey(validMap));
	}

	@Test
	public void validateHasKeyInvalidTest()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> validMap = new HashMap<>();

		final ListItemAttributeDTO itemUnique = new ListItemAttributeDTO(true, false, false, a1, ListItemStructureType.NONE, "",
				null);
		final ListItemAttributeDTO itemCustomUnique = new ListItemAttributeDTO(true, true, false, baseADM,
				ListItemStructureType.NONE, "", null);
		final ListItemAttributeDTO itemSelect = new ListItemAttributeDTO(true, false, false, baseADM, ListItemStructureType.NONE,
				"", null);
		final ListItemAttributeDTO itemNotSelect = new ListItemAttributeDTO(false, false, false, baseADM,
				ListItemStructureType.NONE, "", null);

		final List<AbstractListItemDTO> l1 = new ArrayList<>();
		final List<AbstractListItemDTO> l2 = new ArrayList<>();
		final List<AbstractListItemDTO> l3 = new ArrayList<>();

		l1.add(itemNotSelect);
		l1.add(itemNotSelect);
		l1.add(itemNotSelect);
		l1.add(itemUnique);

		l2.add(itemNotSelect);
		l2.add(itemNotSelect);
		l2.add(itemSelect);
		l2.add(itemSelect);

		l3.add(itemSelect);
		l3.add(itemNotSelect);
		l3.add(itemNotSelect);
		l3.add(itemSelect);
		l3.add(itemUnique);
		l3.add(itemCustomUnique);

		validMap.put(c1, l1);
		validMap.put(c2, l2);
		validMap.put(c3, l3);

		assertEquals("Comp2", EditorValidator.validateHasKey(validMap));
	}

	@Test
	public void validateHasNoDuplicateAttributeNamesTest()
	{
		final ComposedTypeModel c1 = new ComposedTypeModel();
		final ComposedTypeModel c2 = new ComposedTypeModel();

		c1.setCode("Product");
		c2.setCode("ProductSubtype");

		final ClassificationAttributeTypeEnum e = ClassificationAttributeTypeEnum.NUMBER;
		final ListItemClassificationAttributeDTO dto1 = TestUtils.createClassificationAttributeDTO("attr1", "q1", e, "category");
		dto1.setAlias("attrAlias");

		final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> dupeMap = new HashMap<>();
		final Map<String, List<AbstractListItemDTO>> innerMap = new HashMap<>();

		List<AbstractListItemDTO> list = new ArrayList<>();
		list.add(dto1);
		list.add(dto1);

		innerMap.put("q1", list);
		dupeMap.put(c1, innerMap);
		dupeMap.put(c2, innerMap);

		final List<String> expected = new ArrayList<>();
		expected.add("Product");
		expected.add("ProductSubtype");

		final String result = EditorValidator.validateHasNoDuplicateAttributeNames(dupeMap);
		final List<String> actual = Arrays.asList(result.split(", "));

		assert (actual.containsAll(expected));
	}
}
