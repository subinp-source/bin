/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.TestUtils;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.TreeNodeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Treeitem;

import spock.lang.Issue;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EditorUtilsUnitTest
{
	private static final String COLLECTION_TYPE = "CollectionItem";
	private static final String MAP_TYPE = "MapItem";
	private static final int AUTOCREATE_CHECKBOX_INDEX = 4;

	private static final List<String> labels = Arrays.asList("does", "not", "matter");

	@Mock
	private ReadService readService;

	@Before
	public void setup()
	{
		doReturn(true).when(readService).isCollectionType(COLLECTION_TYPE);
		doReturn(true).when(readService).isMapType(MAP_TYPE);
	}

	@Test
	public void convertIntegrationObjectToDTOMap()
	{
		final IntegrationObjectModel object = integrationObject(
				item("MapTypeExample",
						attribute(descriptor(MAP_TYPE, false), false, false)),
				item("TypeSystemUnique",
						attribute(descriptor("String", true), false, false),
						attribute(descriptor("ReferencedItem", false), false, true),
						attribute(descriptor(COLLECTION_TYPE, false), false, false)),
				item("CustomUnique",
						attribute(descriptor("String", false), true, false),
						attribute(descriptor("Integer", null), true, false)),
				item("DoubleUnique",
						attribute(descriptor("String", true), true, false)),
				item("NonUnique",
						attribute(descriptor("Integer", null), null, null)));

		final Map<ComposedTypeModel, List<AbstractListItemDTO>> dtoMap = EditorUtils.convertIntegrationObjectToDTOMap(readService,
				object);

		assertThat(dtoMap.keySet())
				.extracting("code")
				.containsExactlyInAnyOrder("MapTypeExample", "TypeSystemUnique", "CustomUnique", "DoubleUnique", "NonUnique");

		// extracting("structureType") returns boolean. So have to test in this way
		assertEquals(((ListItemAttributeDTO)extractDtoForItemType(dtoMap, "MapTypeExample").get(0)).getStructureType(), ListItemStructureType.MAP);
		assertThat(extractDtoForItemType(dtoMap, "MapTypeExample"))
				.extracting("attributeDescriptor.attributeType.itemtype", "customUnique", "selected",
						"autocreate")
				.containsExactlyInAnyOrder(
						tuple(MAP_TYPE, false, true, false));

		extractDtoForItemType(dtoMap, "TypeSystemUnique").stream()
		                                                 .filter(ListItemAttributeDTO.class::isInstance)
		                                                 .map(ListItemAttributeDTO.class::cast)
		                                                 .forEach(item -> {
		                                                 	String type = item.getAttributeDescriptor().getAttributeType().getItemtype();
		                                                 	if(type.equals("String") || type.equals("ReferencedItem")){
		                                                 		assertEquals(ListItemStructureType.NONE, item.getStructureType());
		                                                    }
		                                                 	else{
			                                                    assertEquals(ListItemStructureType.COLLECTION, item.getStructureType());
		                                                    }
		                                                 });
		assertThat(extractDtoForItemType(dtoMap, "TypeSystemUnique"))
				.extracting("attributeDescriptor.attributeType.itemtype", "customUnique", "selected",
						"autocreate")
				.containsExactlyInAnyOrder(
						tuple("String", false, true, false),
						tuple("ReferencedItem", false, true, true),
						tuple(COLLECTION_TYPE, false, true, false));

		assertEquals(ListItemStructureType.NONE, ((ListItemAttributeDTO)extractDtoForItemType(dtoMap, "CustomUnique").get(0)).getStructureType());
		assertEquals(ListItemStructureType.NONE, ((ListItemAttributeDTO)extractDtoForItemType(dtoMap, "CustomUnique").get(1)).getStructureType());
		assertThat(extractDtoForItemType(dtoMap, "CustomUnique"))
				.extracting("attributeDescriptor.attributeType.itemtype", "customUnique", "selected",
						"autocreate")
				.containsOnly(
						tuple("String", true, true, false),
						tuple("Integer", true, true, false));

		assertEquals(ListItemStructureType.NONE, ((ListItemAttributeDTO)extractDtoForItemType(dtoMap, "DoubleUnique").get(0)).getStructureType());
		assertThat(extractDtoForItemType(dtoMap, "DoubleUnique"))
				.extracting("attributeDescriptor.attributeType.itemtype", "customUnique", "selected",
						"autocreate")
				.containsExactlyInAnyOrder(tuple("String", false, true, false));

		assertEquals(ListItemStructureType.NONE, ((ListItemAttributeDTO)extractDtoForItemType(dtoMap, "NonUnique").get(0)).getStructureType());
		assertThat(extractDtoForItemType(dtoMap, "NonUnique"))
				.extracting("attributeDescriptor.attributeType.itemtype", "customUnique", "selected",
						"autocreate")
				.containsExactlyInAnyOrder(tuple("Integer", false, true, false));
	}

	@Test
	@Issue("IAPI-5157")
	public void autoCreateCheckboxIsEnabledForRequiredAttribute()
	{
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		when(typeModel.getCode()).thenReturn("Catalog");
		when(typeModel.getAbstract()).thenReturn(false);

		final AbstractListItemDTO dto = TestUtils.createListItemAttributeDTO("catalog", false,true, false,
				false, ListItemStructureType.NONE, typeModel);

		final Listitem listItem = EditorUtils.createListItem(dto, true, false, labels, true, readService);

		assertFalse(extractCheckbox(listItem).isDisabled());
	}

	@Test
	@Issue("IAPI-5157")
	public void autoCreateCheckboxIsDisabledForPrimitiveAttribute()
	{
		final AtomicTypeModel typeModel = mock(AtomicTypeModel.class);
		when(typeModel.getCode()).thenReturn("code");

		final AbstractListItemDTO dto = TestUtils.createListItemAttributeDTO("code", false,true, true,
				false, ListItemStructureType.NONE, typeModel);
		final Listitem listItem = EditorUtils.createListItem(dto, false, false, labels, true, readService);

		assertTrue(extractCheckbox(listItem).isDisabled());
	}

	@Test
	@Issue("IAPI-5157")
	public void autoCreateCheckboxIsDisabledForAbstractAttribute()
	{
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		when(typeModel.getCode()).thenReturn("AbstractCatalog");
		when(typeModel.getAbstract()).thenReturn(true);

		final AbstractListItemDTO dto = TestUtils.createListItemAttributeDTO("catalog", false,true, true,
				false, ListItemStructureType.NONE, typeModel);
		final Listitem listItem = EditorUtils.createListItem(dto, true, false, labels, true, readService);

		assertTrue(extractCheckbox(listItem).isDisabled());
	}

	private Checkbox extractCheckbox(final Listitem listitem){
		return (Checkbox) listitem.getChildren().get(AUTOCREATE_CHECKBOX_INDEX).getFirstChild();
	}

	private IntegrationObjectModel integrationObject(final IntegrationObjectItemModel... items)
	{
		final IntegrationObjectModel object = mock(IntegrationObjectModel.class);
		doReturn(asSet(items)).when(object).getItems();
		return object;
	}

	private IntegrationObjectItemModel item(final String code, final IntegrationObjectItemAttributeModel... attributes)
	{
		final IntegrationObjectItemModel item = mock(IntegrationObjectItemModel.class);
		doReturn(composedTypeModel(code)).when(item).getType();
		doReturn(asSet(attributes)).when(item).getAttributes();
		return item;
	}

	private ComposedTypeModel composedTypeModel(final String code)
	{
		final ComposedTypeModel model = mock(ComposedTypeModel.class);
		doReturn(code).when(model).getCode();
		return model;
	}

	private IntegrationObjectItemAttributeModel attribute(final AttributeDescriptorModel descriptor, final Boolean unique,
	                                                      final Boolean create)
	{
		final IntegrationObjectItemAttributeModel attribute = mock(IntegrationObjectItemAttributeModel.class);
		doReturn(descriptor).when(attribute).getAttributeDescriptor();
		doReturn(unique).when(attribute).getUnique();
		doReturn(create).when(attribute).getAutoCreate();
		return attribute;
	}

	private AttributeDescriptorModel descriptor(final String type, final Boolean unique)
	{

		final TypeModel typeModel;
		if (COLLECTION_TYPE.equals(type))
		{
			typeModel = collectionType(type);
		}
		else if (MAP_TYPE.equals(type))
		{
			typeModel = mapType(type);
		}
		else
		{
			typeModel = mapType(type);
		}
		final AttributeDescriptorModel descriptor = mock(AttributeDescriptorModel.class);
		doReturn(typeModel).when(descriptor).getAttributeType();
		doReturn(unique).when(descriptor).getUnique();
		return descriptor;
	}

	private CollectionTypeModel collectionType(final String type)
	{
		final CollectionTypeModel model = mock(CollectionTypeModel.class);
		doReturn(type).when(model).getItemtype();
		doReturn(typeModel(type)).when(model).getElementType();
		return model;
	}

	private MapTypeModel mapType(final String type)
	{

		final MapTypeModel model = mock(MapTypeModel.class);
		doReturn(type).when(model).getItemtype();
		doReturn(typeModel(type)).when(model).getReturntype();
		return model;
	}

	private TypeModel typeModel(final String type)
	{
		final TypeModel model = mock(TypeModel.class);
		doReturn(type).when(model).getCode();
		doReturn(type).when(model).getItemtype();
		return model;
	}

	@SafeVarargs
	private <T> Set<T> asSet(final T... items)
	{
		return new HashSet<>(Arrays.asList(items));
	}

	private List<AbstractListItemDTO> extractDtoForItemType(final Map<ComposedTypeModel, List<AbstractListItemDTO>> map,
	                                                        final String type)
	{
		return map.entrySet().stream()
		          .filter(entry -> entry.getKey().getCode().equals(type))
		          .findAny()
		          .map(Map.Entry::getValue)
		          .orElse(Collections.emptyList());
	}

	@Test
	public void renameTreeitemTest()
	{
		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		when(composedTypeModel.getCode()).thenReturn("Media");

		final TreeNodeData treeNodeData = new TreeNodeData("icon", null, composedTypeModel);
		final Treeitem treeitem = new Treeitem();
		treeitem.setValue(treeNodeData);

		final ListItemAttributeDTO dto = mock(ListItemAttributeDTO.class);
		final String alias = "aliasOfIcon";
		when(dto.getAlias()).thenReturn(alias);

		final Treeitem actualTreeitem = EditorUtils.renameTreeitem(treeitem, dto);

		assertEquals(alias + " [" + treeNodeData.getComposedTypeModel().getCode() + "]", actualTreeitem.getLabel());
	}

	@Test
	public void constructDuplicateMapNoDuplicatesTest()
	{
		ComposedTypeModel product = composedTypeModel("Product");
		ComposedTypeModel variantProduct = composedTypeModel("VariantProduct");
		ListItemClassificationAttributeDTO dto1 = TestUtils.createClassificationAttributeDTO("name1", "q1",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");
		ListItemClassificationAttributeDTO dto2 = TestUtils.createClassificationAttributeDTO("name2", "q2",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");
		ListItemClassificationAttributeDTO dto3 = TestUtils.createClassificationAttributeDTO("name3", "q3",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");

		dto1.setSelected(true);
		dto2.setSelected(true);
		dto3.setSelected(true);

		List<AbstractListItemDTO> dtos = new ArrayList<>();
		dtos.add(dto1);
		dtos.add(dto2);
		dtos.add(dto3);

		Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> duplicationMap = new HashMap<>();
		duplicationMap = EditorUtils.compileDuplicationMap(product, dtos, duplicationMap);
		duplicationMap = EditorUtils.compileDuplicationMap(variantProduct, dtos, duplicationMap);

		assertTrue(duplicationMap.isEmpty());
	}

	@Test
	public void constructDuplicateMapPresentDuplicatesTest()
	{
		ComposedTypeModel product = composedTypeModel("Product");
		ComposedTypeModel variantProduct = composedTypeModel("VariantProduct");
		ListItemClassificationAttributeDTO dto1 = TestUtils.createClassificationAttributeDTO("name1", "q1",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");
		ListItemClassificationAttributeDTO dto2 = TestUtils.createClassificationAttributeDTO("name1", "q2",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");
		ListItemClassificationAttributeDTO dto3 = TestUtils.createClassificationAttributeDTO("name3", "q3",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");
		ListItemClassificationAttributeDTO dto4 = TestUtils.createClassificationAttributeDTO("name4", "q4",
				ClassificationAttributeTypeEnum.BOOLEAN, "type");


		dto1.setSelected(true);
		dto2.setSelected(true);
		dto3.setSelected(true);
		dto4.setSelected(true);

		List<AbstractListItemDTO> dtos1 = new ArrayList<>();
		dtos1.add(dto1);
		dtos1.add(dto2);
		dtos1.add(dto3);

		List<AbstractListItemDTO> dtos2 = new ArrayList<>();
		dtos2.add(dto1);
		dtos2.add(dto3);
		dtos2.add(dto4);

		Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> duplicationMap = new HashMap<>();
		duplicationMap = EditorUtils.compileDuplicationMap(product, dtos1, duplicationMap);
		duplicationMap = EditorUtils.compileDuplicationMap(variantProduct, dtos2, duplicationMap);

		assertEquals(1, duplicationMap.keySet().size());
		assertEquals(1, duplicationMap.get(product).keySet().size());
		assertEquals(2, duplicationMap.get(product).get("name1").size());

		ListItemClassificationAttributeDTO firstDupEntry = (ListItemClassificationAttributeDTO) duplicationMap.get(product)
		                                                                                                      .get("name1")
		                                                                                                      .get(0);
		ListItemClassificationAttributeDTO secondDupEntry = (ListItemClassificationAttributeDTO) duplicationMap.get(product)
		                                                                                                       .get("name1")
		                                                                                                       .get(1);

		assertEquals("q1", firstDupEntry.getClassificationAttributeCode());
		assertEquals("q2", secondDupEntry.getClassificationAttributeCode());
	}

	@Test
	public void isClassificationAttributePresentTest()
	{
		final ClassAttributeAssignmentModel assignment = new ClassAttributeAssignmentModel();
		final ClassificationAttributeModel checkedAttribute = new ClassificationAttributeModel();
		final ClassificationClassModel classificationClassModel = new ClassificationClassModel();

		checkedAttribute.setCode("Built-in speakers, 2433");

		classificationClassModel.setCode("2910");

		assignment.setClassificationAttribute(checkedAttribute);
		assignment.setClassificationClass(classificationClassModel);

		final ListItemClassificationAttributeDTO dto1 = TestUtils.createClassificationAttributeDTO("name1", "Builtinspeakers2433",
				ClassificationAttributeTypeEnum.BOOLEAN, "2910");
		final ListItemClassificationAttributeDTO dto2 = TestUtils.createClassificationAttributeDTO("name2", "Built-in microphone, 1025",
				ClassificationAttributeTypeEnum.BOOLEAN, "2910");
		final ListItemClassificationAttributeDTO dto3 = TestUtils.createClassificationAttributeDTO("name3", "Audio system, 442",
				ClassificationAttributeTypeEnum.BOOLEAN, "2910");

		final List<AbstractListItemDTO> dtos1 = new ArrayList<>();
		dtos1.add(dto1);
		dtos1.add(dto2);
		dtos1.add(dto3);

		final List<AbstractListItemDTO> dtos2 = new ArrayList<>();
		dtos2.add(dto2);
		dtos2.add(dto3);

		assertTrue(EditorUtils.isClassificationAttributePresent(assignment, dtos1));
		assertFalse(EditorUtils.isClassificationAttributePresent(assignment, dtos2));
	}
}
