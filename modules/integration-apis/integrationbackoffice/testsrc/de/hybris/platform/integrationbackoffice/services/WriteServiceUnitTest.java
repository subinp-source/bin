/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
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
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.odata2webservices.enums.IntegrationType;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class WriteServiceUnitTest
{
	@Mock
	private ReadService readService;
	@Mock
	private ModelService modelService;

	@InjectMocks
	private WriteService writeService;

	@Before
	public void setUp()
	{
		when(modelService.create(IntegrationObjectItemModel.class)).thenReturn(new IntegrationObjectItemModel());
		when(modelService.create(IntegrationObjectItemAttributeModel.class)).thenReturn(
				new IntegrationObjectItemAttributeModel());
		when(modelService.create(IntegrationObjectItemClassificationAttributeModel.class)).thenReturn(
				new IntegrationObjectItemClassificationAttributeModel());
	}

	@Test
	public void testCreateIntegrationObject()
	{
		final String name = "InboundStockLevel";
		final IntegrationObjectModel io = spy(IntegrationObjectModel.class);
		when(modelService.create(IntegrationObjectModel.class)).thenReturn(io);

		final IntegrationObjectModel actual = writeService.createIntegrationObject(name, IntegrationType.INBOUND);

		assertEquals(name, actual.getCode());
		assertEquals(IntegrationType.INBOUND, actual.getIntegrationType());
		assertEquals(0, actual.getItems().size());
		verify(modelService, times(1)).create(IntegrationObjectModel.class);
	}

	@Test
	public void testPersistDefinitionsClear()
	{
		final IntegrationObjectModel io = mockIntegrationObject();

		writeService.createDefinitions(io, Collections.emptyMap(), "");

		assertEquals(0, io.getItems().size());
	}

	@Test
	public void testPersistDefinitionsClearWithPK()
	{
		final IntegrationObjectItemModel integrationObjectItemModel = mock(IntegrationObjectItemModel.class);
		final PK pk = PK.fromLong(1);
		when(integrationObjectItemModel.getPk()).thenReturn(pk);

		final IntegrationObjectModel io = new IntegrationObjectModel();
		final Set<IntegrationObjectItemModel> ioItems = new HashSet<>();
		ioItems.add(integrationObjectItemModel);
		io.setItems(ioItems);

		writeService.clearIntegrationObject(io);

		assertNull(io.getItems());
	}

	@Test
	public void testCreateDefinitions()
	{
		final IntegrationObjectModel integrationObjectModel = new IntegrationObjectModel();
		integrationObjectModel.setItems(Collections.emptySet());
		final String rootCode = "product";

		// Classification Attribute
		final ListItemClassificationAttributeDTO classificationAttribute = TestUtils.createClassificationAttributeDTO("",
				"weight",
				ClassificationAttributeTypeEnum.STRING, "dimensions");

		// Regular attribute
		final AttributeDescriptorModel attributeDescriptorModel = new AttributeDescriptorModel();
		attributeDescriptorModel.setQualifier("code");
		attributeDescriptorModel.setAttributeType(new TypeModel());
		attributeDescriptorModel.setUnique(true);
		final ListItemAttributeDTO attribute = new ListItemAttributeDTO(true, false, false, attributeDescriptorModel,
				ListItemStructureType.NONE, "", null);

		// Map of attributes
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> map = new HashMap<>();
		final ComposedTypeModel composedTypeModel = mock(ComposedTypeModel.class);
		when(composedTypeModel.getCode()).thenReturn(rootCode);
		final List<AbstractListItemDTO> list = new ArrayList<>();
		list.add(attribute);
		list.add(classificationAttribute);
		map.put(composedTypeModel, list);

		final IntegrationObjectModel actual = writeService.createDefinitions(integrationObjectModel, map, rootCode);
		final IntegrationObjectItemModel rootItem = actual.getItems().iterator().next();

		assertTrue(rootItem.getRoot());
		assertEquals(1, rootItem.getAttributes().size());
		assertEquals(1, rootItem.getClassificationAttributes().size());
	}

	@Test
	public void testPersistIntegrationObject()
	{
		final IntegrationObjectModel integrationObjectModel = new IntegrationObjectModel();
		writeService.persistIntegrationObject(integrationObjectModel);
		verify(modelService, times(1)).save(integrationObjectModel);
	}

	@Test
	public void testPersistIntegrationObjectItem()
	{
		final Set<IntegrationObjectItemModel> set = Set.of(new IntegrationObjectItemModel());
		writeService.persistIntegrationObjectItems(set);
		verify(modelService).saveAll(set);
	}

	@Test
	public void testBuildIntegrationObjectItem()
	{
		final String code = "StockLevel";
		final IntegrationObjectModel ioModel = mock(IntegrationObjectModel.class);
		final ComposedTypeModel ctm = mock(ComposedTypeModel.class);
		when(ctm.getCode()).thenReturn(code);

		final IntegrationObjectItemModel actual = writeService.buildIntegrationObjectItem(ioModel, ctm, "");

		verify(modelService, times(1)).create(IntegrationObjectItemModel.class);
		assertEquals(code, actual.getCode());
		assertEquals(ioModel, actual.getIntegrationObject());
		assertEquals(ctm, actual.getType());
	}

	@Test
	public void testBuildIntegrationObjectItemAttribute()
	{
		final AttributeDescriptorModel attributeDescriptor1 = new AttributeDescriptorModel();
		attributeDescriptor1.setQualifier("q1");
		attributeDescriptor1.setAttributeType(new TypeModel());
		attributeDescriptor1.setUnique(true);
		final AttributeDescriptorModel attributeDescriptor2 = new AttributeDescriptorModel();
		attributeDescriptor2.setQualifier("q2");
		attributeDescriptor2.setAttributeType(new TypeModel());
		attributeDescriptor2.setUnique(false);
		final AttributeDescriptorModel attributeDescriptor3 = new AttributeDescriptorModel();
		attributeDescriptor3.setQualifier("q3");
		attributeDescriptor3.setAttributeType(new TypeModel());
		attributeDescriptor3.setUnique(true);
		final AttributeDescriptorModel attributeDescriptor4 = new AttributeDescriptorModel();
		attributeDescriptor4.setQualifier("q4");
		attributeDescriptor4.setAttributeType(new TypeModel());
		attributeDescriptor4.setUnique(false);

		final ListItemAttributeDTO dto1 = new ListItemAttributeDTO(true, false, false, attributeDescriptor1,
				ListItemStructureType.NONE, "", null);
		final ListItemAttributeDTO dto2 = new ListItemAttributeDTO(true, false, false, attributeDescriptor2,
				ListItemStructureType.NONE, "", null);
		final ListItemAttributeDTO dto3 = new ListItemAttributeDTO(true, true, false, attributeDescriptor3,
				ListItemStructureType.NONE, "", null);
		final ListItemAttributeDTO dto4 = new ListItemAttributeDTO(true, true, false, attributeDescriptor4,
				ListItemStructureType.NONE, "", null);

		final Set<ListItemAttributeDTO> dtos = new HashSet<>();
		dtos.add(dto1);
		dtos.add(dto2);
		dtos.add(dto3);
		dtos.add(dto4);

		final IntegrationObjectItemModel ioItem = mock(IntegrationObjectItemModel.class);

		final Set<IntegrationObjectItemAttributeModel> actual = writeService.buildIntegrationObjectItemAttribute(dtos, ioItem);

		actual.forEach(ioia -> dtos.forEach(dto -> {
			final AttributeDescriptorModel attributeDescriptor = dto.getAttributeDescriptor();
			if (attributeDescriptor.equals(ioia.getAttributeDescriptor()))
			{
				assertEquals(attributeDescriptor.getQualifier(), ioia.getAttributeName());
				assertEquals(ioItem, ioia.getIntegrationObjectItem());
				assertEquals(attributeDescriptor.getUnique() || dto.isCustomUnique(), ioia.getUnique());
				assertEquals(dto.isAutocreate(), ioia.getAutoCreate());
				assertNull(ioia.getReturnIntegrationObjectItem());
			}
		}));
	}

	@Test
	public void testBuildIntegrationObjectItemClassificationAttribute()
	{
		final ClassificationAttributeModel classificationAttributeModel = new ClassificationAttributeModel();
		classificationAttributeModel.setCode("classAttrCode");

		final ClassificationClassModel classificationClassModel = new ClassificationClassModel();
		classificationClassModel.setCode("classCode");

		final ClassAttributeAssignmentModel classAttributeAssignmentModel = new ClassAttributeAssignmentModel();
		final ClassificationAttributeTypeEnum type = ClassificationAttributeTypeEnum.STRING;
		classAttributeAssignmentModel.setAttributeType(type);
		classAttributeAssignmentModel.setClassificationAttribute(classificationAttributeModel);
		classAttributeAssignmentModel.setClassificationClass(classificationClassModel);
		classAttributeAssignmentModel.setMultiValued(false);

		final String attributeName = "name";
		final ListItemClassificationAttributeDTO dto1 = new ListItemClassificationAttributeDTO(true, false, false,
				classAttributeAssignmentModel, attributeName);
		final Set<ListItemClassificationAttributeDTO> dtos = new HashSet<>();
		dtos.add(dto1);

		final IntegrationObjectItemModel ioItem = mock(IntegrationObjectItemModel.class);

		final Set<IntegrationObjectItemClassificationAttributeModel> actual = writeService.buildIntegrationObjectItemClassificationAttribute(
				dtos, ioItem);

		actual.forEach(ioica -> dtos.forEach(dto -> {
			assertEquals(dto.getClassAttributeAssignmentModel(), ioica.getClassAttributeAssignment());
			assertEquals(dto.getAlias(), dto.getAlias());
		}));
	}

	@Test
	public void testPersistReturnIntegrationObjectItem()
	{
		final IntegrationObjectModel mockIO = mockIntegrationObject();
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> convertedMap = mockDTOMap(mockIO);

		mockIO.getItems().forEach(ioi -> ioi.getAttributes().forEach(ioia -> assertNull(ioia.getReturnIntegrationObjectItem())));

		when(readService.isCollectionType(anyString())).thenReturn(false);
		when(readService.isEnumerationMetaType(anyString())).thenReturn(false);
		when(readService.isComposedType("ComposedType")).thenReturn(true);

		final IntegrationObjectModel actual = writeService.setReturnIntegrationObjectItem(mockIO, convertedMap);

		IntegrationObjectItemModel product = null;
		IntegrationObjectItemModel catalogVersion = null;
		for (IntegrationObjectItemModel itemModel : actual.getItems())
		{
			if (itemModel.getCode().equals("Product"))
			{
				product = itemModel;
			}
			else if (itemModel.getCode().equals("CatalogVersion"))
			{
				catalogVersion = itemModel;
			}
		}
		assert product != null;
		for (IntegrationObjectItemAttributeModel attributeModel : product.getAttributes())
		{
			if (attributeModel.getAttributeName().equals("CatalogVersion"))
			{
				assertEquals(catalogVersion, attributeModel.getReturnIntegrationObjectItem());
			}
		}
	}

	@Test
	public void testPersistReturnIntegrationObjectItemWithSubtype()
	{
		IntegrationObjectModel mockIO = mockIntegrationObject();
		Map<ComposedTypeModel, List<AbstractListItemDTO>> convertedMap = mockDTOMap(mockIO);

		mockIO = addIOIToExistingIO(mockIO);
		convertedMap = addMapEntry(mockIO, convertedMap);

		mockIO.getItems().forEach(ioi -> ioi.getAttributes().forEach(ioia -> assertNull(ioia.getReturnIntegrationObjectItem())));

		final IntegrationObjectModel actual = writeService.setReturnIntegrationObjectItem(mockIO, convertedMap);

		IntegrationObjectItemModel product = null;
		IntegrationObjectItemModel classificationClass = null;
		for (IntegrationObjectItemModel itemModel : actual.getItems())
		{
			if (itemModel.getCode().equals("Product"))
			{
				product = itemModel;
			}
			else if (itemModel.getCode().equals("ClassificationClass"))
			{
				classificationClass = itemModel;
			}
		}

		assert product != null;
		for (IntegrationObjectItemAttributeModel attributeModel : product.getAttributes())
		{
			if (attributeModel.getAttributeName().equals("ClassificationClass"))
			{
				assertEquals(classificationClass, attributeModel.getReturnIntegrationObjectItem());
			}
		}
	}

	@Test
	public void testDeleteIntegrationObject()
	{
		IntegrationObjectModel integrationObject = mock(IntegrationObjectModel.class);
		PK integrationObjectPK = PK.fromLong(123);
		when(integrationObject.getPk()).thenReturn(integrationObjectPK);
		writeService.deleteIntegrationObject(integrationObject);
		verify(modelService, times(1)).remove(integrationObjectPK);
	}

	@Test
	public void testGetMatchingListItemDTOType()
	{
		final AttributeDescriptorModel descriptor = new AttributeDescriptorModel();
		final TypeModel expected = new TypeModel();
		descriptor.setAttributeType(expected);

		final List<AbstractListItemDTO> relatedDTOs = new ArrayList<>();
		relatedDTOs.add(new ListItemAttributeDTO(true, false, false, descriptor, ListItemStructureType.NONE, "Product", null));
		relatedDTOs.add(
				TestUtils.createClassificationAttributeDTO("", "weight", ClassificationAttributeTypeEnum.STRING, "dimensions"));

		final TypeModel actual = writeService.getMatchingListItemDTOType(relatedDTOs, "Product");

		assertEquals(expected, actual);
	}

	@Test
	public void testDetermineAttributeCodeAtomic()
	{
		final AtomicTypeModel typeModel = mock(AtomicTypeModel.class);
		when(typeModel.getItemtype()).thenReturn("AtomicType");
		when(readService.isCollectionType("AtomicType")).thenReturn(false);
		when(readService.isMapType("AtomicType")).thenReturn(false);
		when(readService.isComposedType("AtomicType")).thenReturn(false);
		when(readService.isEnumerationMetaType("AtomicType")).thenReturn(false);
		final String result = writeService.determineAttributeCode(typeModel);

		assertNull(result);
	}

	@Test
	public void testDetermineAttributeCodeCollection()
	{
		final CollectionTypeModel typeModel = mock(CollectionTypeModel.class);
		final ComposedTypeModel elementType = mock(ComposedTypeModel.class);
		when(typeModel.getItemtype()).thenReturn("CollectionType");
		when(readService.isCollectionType("CollectionType")).thenReturn(true);
		when(typeModel.getElementType()).thenReturn(elementType);
		when(elementType.getCode()).thenReturn("TestCode");
		final String result = writeService.determineAttributeCode(typeModel);

		assertEquals("TestCode", result);
	}

	@Test
	public void testDetermineAttributeCodeComposed()
	{
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		when(typeModel.getItemtype()).thenReturn("ComposedType");
		when(readService.isCollectionType("ComposedType")).thenReturn(false);
		when(readService.isMapType("ComposedType")).thenReturn(false);
		when(readService.isComposedType("ComposedType")).thenReturn(true);
		when(typeModel.getCode()).thenReturn("TestCode");
		final String result = writeService.determineAttributeCode(typeModel);

		assertEquals("TestCode", result);
	}

	@Test
	public void testDetermineAttributeCodeEnumeration()
	{
		final EnumerationMetaTypeModel typeModel = mock(EnumerationMetaTypeModel.class);
		when(typeModel.getCode()).thenReturn("TestCode");
		when(typeModel.getItemtype()).thenReturn("EnumerationMetaType");
		when(readService.isCollectionType("EnumerationMetaType")).thenReturn(false);
		when(readService.isMapType("EnumerationMetaType")).thenReturn(false);
		when(readService.isComposedType("EnumerationMetaType")).thenReturn(false);
		when(readService.isEnumerationMetaType("EnumerationMetaType")).thenReturn(true);
		final String result = writeService.determineAttributeCode(typeModel);

		assertEquals("TestCode", result);
	}

	@Test
	public void testDetermineAttributeCodeMapOfCollection()
	{
		final MapTypeModel typeModel = mock(MapTypeModel.class);
		final CollectionTypeModel returnType = mock(CollectionTypeModel.class);
		final ComposedTypeModel elementType = mock(ComposedTypeModel.class);
		when(typeModel.getItemtype()).thenReturn("MapType");
		when(readService.isCollectionType("MapType")).thenReturn(false);
		when(readService.isMapType("MapType")).thenReturn(true);
		when(typeModel.getReturntype()).thenReturn(returnType);
		when(returnType.getItemtype()).thenReturn("CollectionType");
		when(readService.isCollectionType("CollectionType")).thenReturn(true);
		when(returnType.getElementType()).thenReturn(elementType);
		when(elementType.getCode()).thenReturn("TestCode");
		final String result = writeService.determineAttributeCode(typeModel);

		assertEquals("TestCode", result);
	}

	@Test
	public void testDetermineAttributeCodeMapOfAtomic()
	{
		final MapTypeModel typeModel = mock(MapTypeModel.class);
		final AtomicTypeModel returnType = mock(AtomicTypeModel.class);
		when(typeModel.getItemtype()).thenReturn("MapType");
		when(readService.isCollectionType("MapType")).thenReturn(false);
		when(readService.isMapType("MapType")).thenReturn(true);
		when(typeModel.getReturntype()).thenReturn(returnType);
		when(returnType.getItemtype()).thenReturn("AtomicType");
		when(readService.isCollectionType("AtomicType")).thenReturn(false);
		when(readService.isMapType("AtomicType")).thenReturn(false);
		when(readService.isComposedType("AtomicType")).thenReturn(false);
		when(readService.isEnumerationMetaType("AtomicType")).thenReturn(false);
		final String result = writeService.determineAttributeCode(typeModel);

		assertNull(result);
	}

	@Test
	public void testDetermineAttributeCodeNull()
	{
		assertNull(writeService.determineAttributeCode(null));
	}

	private IntegrationObjectModel mockIntegrationObject()
	{
		IntegrationObjectModel io = new IntegrationObjectModel();

		// 'Product' integration object item (top-level item)
		IntegrationObjectItemModel ioItemProduct = spy(IntegrationObjectItemModel.class);
		ComposedTypeModel productComposedType = new ComposedTypeModel();
		String productCode = "Product";
		ioItemProduct.setCode(productCode);
		productComposedType.setCode(productCode);
		ioItemProduct.setType(productComposedType);

		// 'CatalogVersion' integration object item
		IntegrationObjectItemModel ioItemCatalogVersion = spy(IntegrationObjectItemModel.class);
		ComposedTypeModel catalogVersionComposedType = new ComposedTypeModel();
		String catalogVersionCode = "CatalogVersion";
		ioItemCatalogVersion.setCode(catalogVersionCode);
		catalogVersionComposedType.setCode(catalogVersionCode);
		ioItemCatalogVersion.setType(catalogVersionComposedType);

		// 'Language' integration object item
		IntegrationObjectItemModel ioItemLanguage = spy(IntegrationObjectItemModel.class);
		ComposedTypeModel languageComposedType = new ComposedTypeModel();
		String languageCode = "Language";
		ioItemLanguage.setCode(languageCode);
		languageComposedType.setCode(languageCode);
		ioItemLanguage.setType(languageComposedType);

		// 'CatalogVersion' integration object item attribute (contained by 'Product')
		AttributeDescriptorModel catalogVersionDescriptor = new AttributeDescriptorModel();
		catalogVersionDescriptor.setAttributeType(catalogVersionComposedType);

		IntegrationObjectItemAttributeModel ioiAttributeCatalogVersion = new IntegrationObjectItemAttributeModel();
		ioiAttributeCatalogVersion.setAttributeDescriptor(catalogVersionDescriptor);
		ioiAttributeCatalogVersion.setAttributeName("catalogVersion");

		// 'code' integration object item attribute (contained by 'CatalogVersion' and 'Language')
		TypeModel mockType2 = mock(TypeModel.class);
		when(mockType2.getItemtype()).thenReturn("AtomicType");

		AttributeDescriptorModel codeDescriptor = new AttributeDescriptorModel();
		codeDescriptor.setAttributeType(mockType2);

		IntegrationObjectItemAttributeModel ioiAttributeCode = new IntegrationObjectItemAttributeModel();
		ioiAttributeCode.setAttributeDescriptor(codeDescriptor);
		ioiAttributeCode.setAttributeName("code");

		// 'languages' integration object item attribute (contained by 'CatalogVersion')
		TypeModel elementMockType = mock(TypeModel.class);
		when(elementMockType.getCode()).thenReturn("Language");

		CollectionTypeModel mockType3 = mock(CollectionTypeModel.class);
		when(mockType3.getItemtype()).thenReturn("CollectionType");
		when(mockType3.getElementType()).thenReturn(elementMockType);

		AttributeDescriptorModel languagesDescriptor = new AttributeDescriptorModel();
		languagesDescriptor.setAttributeType(mockType3);

		IntegrationObjectItemAttributeModel ioiAttributeLanguages = new IntegrationObjectItemAttributeModel();
		ioiAttributeLanguages.setAttributeDescriptor(languagesDescriptor);
		ioiAttributeLanguages.setAttributeName("languages");

		// Relationships
		Set<IntegrationObjectItemAttributeModel> productAttributes = new HashSet<>();
		productAttributes.add(ioiAttributeCatalogVersion);
		ioItemProduct.setAttributes(productAttributes);

		Set<IntegrationObjectItemAttributeModel> catalogVersionAttributes = new HashSet<>();
		catalogVersionAttributes.add(ioiAttributeCode);
		catalogVersionAttributes.add(ioiAttributeLanguages);
		ioItemCatalogVersion.setAttributes(catalogVersionAttributes);

		Set<IntegrationObjectItemAttributeModel> languageAttributes = new HashSet<>();
		languageAttributes.add(ioiAttributeCode);
		ioItemLanguage.setAttributes(languageAttributes);

		Set<IntegrationObjectItemModel> ioItems = new HashSet<>();
		ioItems.add(ioItemProduct);
		ioItems.add(ioItemCatalogVersion);
		ioItems.add(ioItemLanguage);

		for (IntegrationObjectItemModel item : ioItems)
		{
			item.setClassificationAttributes(Collections.emptySet());
		}

		io.setItems(ioItems);
		return io;
	}

	private Map<ComposedTypeModel, List<AbstractListItemDTO>> mockDTOMap(IntegrationObjectModel ioModel)
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> mockMap = new HashMap<>();

		ComposedTypeModel mockType1 = null;
		for (IntegrationObjectItemModel item : ioModel.getItems())
		{
			if (item.getCode().equals("Product"))
			{
				mockType1 = item.getType();
			}
		}

		ComposedTypeModel mockType2 = null;
		for (IntegrationObjectItemModel item : ioModel.getItems())
		{
			if (item.getCode().equals("CatalogVersion"))
			{
				mockType2 = item.getType();
			}
		}

		ComposedTypeModel mockType3 = null;
		for (IntegrationObjectItemModel item : ioModel.getItems())
		{
			if (item.getCode().equals("Language"))
			{
				mockType3 = item.getType();
			}
		}

		CollectionTypeModel mockType3b = mock(CollectionTypeModel.class);
		when(mockType3b.getCode()).thenReturn("Language");

		TypeModel mockType4 = mock(TypeModel.class);
		when(mockType4.getItemtype()).thenReturn("AtomicType");

		AttributeDescriptorModel catalogVersionDescriptor = new AttributeDescriptorModel();
		catalogVersionDescriptor.setAttributeType(mockType2);

		AttributeDescriptorModel languageDescriptor = new AttributeDescriptorModel();
		languageDescriptor.setAttributeType(mockType3b);
		when(mockType3b.getElementType()).thenReturn(mockType3);

		AttributeDescriptorModel codeDescriptor = new AttributeDescriptorModel();
		codeDescriptor.setAttributeType(mockType4);

		// Product list items
		ListItemAttributeDTO productDto1 = new ListItemAttributeDTO(true, false, false, catalogVersionDescriptor,
				ListItemStructureType.NONE, "catalogVersion", null);
		ListItemAttributeDTO productDto2 = new ListItemAttributeDTO(true, false, false, codeDescriptor,
				ListItemStructureType.NONE, "code", null);
		List<AbstractListItemDTO> productList = new ArrayList<>();
		productList.add(productDto1);
		productList.add(productDto2);

		// CatalogVersion list items
		ListItemAttributeDTO catalogVersionDto1 = new ListItemAttributeDTO(true, false, false, codeDescriptor,
				ListItemStructureType.NONE, "code", null);
		ListItemAttributeDTO catalogVersionDto2 = new ListItemAttributeDTO(true, false, false, languageDescriptor,
				ListItemStructureType.COLLECTION, "languages", null);
		List<AbstractListItemDTO> catalogVersionList = new ArrayList<>();
		catalogVersionList.add(catalogVersionDto1);
		catalogVersionList.add(catalogVersionDto2);

		// Language list items
		ListItemAttributeDTO languageDto1 = new ListItemAttributeDTO(true, false, false, codeDescriptor,
				ListItemStructureType.NONE, "code", null);
		List<AbstractListItemDTO> languageList = new ArrayList<>();
		languageList.add(languageDto1);

		// Compile map
		mockMap.put(mockType1, productList);
		mockMap.put(mockType2, catalogVersionList);
		mockMap.put(mockType3, languageList);

		return mockMap;
	}

	private IntegrationObjectModel addIOIToExistingIO(IntegrationObjectModel io)
	{
		// Add a subtype IOI to the IO
		IntegrationObjectItemModel ioItemClassificationClass = spy(IntegrationObjectItemModel.class);
		ComposedTypeModel classificationClassComposedType = mock(ComposedTypeModel.class);
		String classificationClassCode = "ClassificationClass";
		ioItemClassificationClass.setCode(classificationClassCode);
		when(classificationClassComposedType.getItemtype()).thenReturn(classificationClassCode);
		doReturn(classificationClassComposedType).when(ioItemClassificationClass).getType();

		// 'ClassificationClass' integration object item attribute (contained by 'Product')
		TypeModel mockType = mock(TypeModel.class);
		when(mockType.getItemtype()).thenReturn("ComposedType");
		when(mockType.getCode()).thenReturn("ClassificationClass");

		AttributeDescriptorModel classificationClassDescriptor = new AttributeDescriptorModel();
		classificationClassDescriptor.setAttributeType(mockType);

		IntegrationObjectItemAttributeModel ioiAttributeCategory = new IntegrationObjectItemAttributeModel();
		ioiAttributeCategory.setAttributeDescriptor(classificationClassDescriptor);
		ioiAttributeCategory.setAttributeName("Category");

		// 'hmcXML' integration object item attribute (contained by 'ClassificationClass')
		TypeModel mockType2 = mock(TypeModel.class);
		when(mockType2.getItemtype()).thenReturn("AtomicType");

		AttributeDescriptorModel hmcXMLDescriptor = new AttributeDescriptorModel();
		hmcXMLDescriptor.setAttributeType(mockType2);

		IntegrationObjectItemAttributeModel ioiAttributeCode = new IntegrationObjectItemAttributeModel();
		ioiAttributeCode.setAttributeDescriptor(hmcXMLDescriptor);
		ioiAttributeCode.setAttributeName("hmcXML");

		// Relationships
		Set<IntegrationObjectItemAttributeModel> classificationClassAttributes = new HashSet<>();
		classificationClassAttributes.add(ioiAttributeCode);
		ioItemClassificationClass.setAttributes(classificationClassAttributes);
		ioItemClassificationClass.setClassificationAttributes(Collections.emptySet());

		Set<IntegrationObjectItemAttributeModel> productAttributes;
		for (IntegrationObjectItemModel item : io.getItems())
		{
			if (item.getCode().equals("Product"))
			{
				productAttributes = item.getAttributes();
				productAttributes.add(ioiAttributeCategory);
				item.setAttributes(productAttributes);
			}
		}

		// Update IO
		Set<IntegrationObjectItemModel> items = io.getItems();
		items.add(ioItemClassificationClass);
		io.setItems(items);
		return io;
	}

	private Map<ComposedTypeModel, List<AbstractListItemDTO>> addMapEntry(IntegrationObjectModel ioModel,
	                                                                      Map<ComposedTypeModel, List<AbstractListItemDTO>> dtoMap)
	{
		ComposedTypeModel mockType = null;
		for (IntegrationObjectItemModel item : ioModel.getItems())
		{
			if (item.getCode().equals("ClassificationClass"))
			{
				mockType = item.getType();
			}
		}

		CollectionTypeModel mockTypeB = mock(CollectionTypeModel.class);
		when(mockTypeB.getCode()).thenReturn("ClassificationClass");

		AttributeDescriptorModel classificationClassDescriptor = new AttributeDescriptorModel();
		classificationClassDescriptor.setAttributeType(mockTypeB);
		when(mockTypeB.getElementType()).thenReturn(mockType);

		TypeModel mockType2 = mock(TypeModel.class);
		when(mockType2.getItemtype()).thenReturn("AtomicType");

		AttributeDescriptorModel hmcXMLDescriptor = new AttributeDescriptorModel();
		hmcXMLDescriptor.setAttributeType(mockType2);

		// Product list items
		for (ComposedTypeModel type : dtoMap.keySet())
		{
			if (type.getCode().equals("Product"))
			{
				List<AbstractListItemDTO> productList = dtoMap.get(type);
				ListItemAttributeDTO productDto1 = new ListItemAttributeDTO(true, false, false, classificationClassDescriptor,
						ListItemStructureType.COLLECTION, "supercategories", null);
				productList.add(productDto1);
			}
		}

		// ClassificationClass list items
		List<AbstractListItemDTO> classificationClassList = new ArrayList<>();
		ListItemAttributeDTO classificationClassDto1 = new ListItemAttributeDTO(true, false, false, hmcXMLDescriptor,
				ListItemStructureType.NONE, "hmcXML", null);
		classificationClassList.add(classificationClassDto1);

		dtoMap.put(mockType, classificationClassList);

		return dtoMap;
	}
}
