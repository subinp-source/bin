/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.services;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.config.ReadOnlyAttributesConfiguration;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.odata2services.odata.schema.SchemaGenerator;
import de.hybris.platform.odata2webservices.enums.IntegrationType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ReadServiceUnitTest
{

	@Mock
	private FlexibleSearchService flexibleSearchService;
	@Mock
	private TypeService typeService;
	@Mock
	private SchemaGenerator schemaGenerator;
	@Mock
	private ReadOnlyAttributesConfiguration readOnlyAttributesConfiguration;

	private ReadService readService;

	@Before
	public void setUp()
	{
		readService = new ReadService();
		readService.setFlexibleSearchService(flexibleSearchService);
		readService.setTypeService(typeService);
		readService.setODataDefaultSchemaGenerator(schemaGenerator);
		readService.setReadOnlyAttributesConfiguration(readOnlyAttributesConfiguration);
		when(typeService.isAssignableFrom("CollectionType", "CollectionType")).thenReturn(true);
		when(typeService.isAssignableFrom("ComposedType", "ComposedType")).thenReturn(true);
		when(typeService.isAssignableFrom("EnumerationMetaType", "EnumerationMetaType")).thenReturn(true);
		when(typeService.isAssignableFrom("AtomicType", "AtomicType")).thenReturn(true);
		when(typeService.isAssignableFrom("MapType", "MapType")).thenReturn(true);
	}

	@Test
	public void testComplexType()
	{
		final TypeModel typeModel1 = mock(TypeModel.class);
		when(typeModel1.getItemtype()).thenReturn("CollectionType");
		final TypeModel typeModel2 = mock(TypeModel.class);
		when(typeModel2.getItemtype()).thenReturn("ComposedType");
		final TypeModel typeModel3 = mock(TypeModel.class);
		when(typeModel3.getItemtype()).thenReturn("EnumerationMetaType");
		final TypeModel typeModel4 = mock(TypeModel.class);
		when(typeModel4.getItemtype()).thenReturn("AtomicType");
		final TypeModel typeModel5 = mock(TypeModel.class);
		when(typeModel5.getItemtype()).thenReturn("MapType");
		assertFalse(readService.isComplexType(typeModel1));
		assertTrue(readService.isComplexType(typeModel2));
		assertTrue(readService.isComplexType(typeModel3));
		assertFalse(readService.isComplexType(typeModel4));
		assertFalse(readService.isComplexType(typeModel5));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorComposedType()
	{
		final AttributeDescriptorModel attributeDescriptorModel1 = new AttributeDescriptorModel();
		final ComposedTypeModel typeModel1 = mock(ComposedTypeModel.class);
		when(typeModel1.getItemtype()).thenReturn("ComposedType");
		attributeDescriptorModel1.setAttributeType(typeModel1);

		assertEquals(typeModel1, readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel1));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorAtomicType()
	{
		final AttributeDescriptorModel attributeDescriptorModel2 = new AttributeDescriptorModel();
		final AtomicTypeModel typeModel2 = mock(AtomicTypeModel.class);
		when(typeModel2.getItemtype()).thenReturn("AtomicType");
		attributeDescriptorModel2.setAttributeType(typeModel2);

		assertNull(readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel2));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorCollectionOfAtomicTypes()
	{
		final AttributeDescriptorModel attributeDescriptorModel3 = new AttributeDescriptorModel();
		final CollectionTypeModel typeModel3 = mock(CollectionTypeModel.class);
		when(typeModel3.getItemtype()).thenReturn("CollectionType");
		final AtomicTypeModel elementType1 = mock(AtomicTypeModel.class);
		when(elementType1.getItemtype()).thenReturn("AtomicType");
		when(typeModel3.getElementType()).thenReturn(elementType1);
		attributeDescriptorModel3.setAttributeType(typeModel3);

		assertNull(readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel3));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorCollectionOfComposedTypes()
	{
		final AttributeDescriptorModel attributeDescriptorModel4 = new AttributeDescriptorModel();
		final CollectionTypeModel typeModel4 = mock(CollectionTypeModel.class);
		when(typeModel4.getItemtype()).thenReturn("CollectionType");
		final ComposedTypeModel elementType2 = mock(ComposedTypeModel.class);
		when(elementType2.getItemtype()).thenReturn("ComposedType");
		when(typeModel4.getElementType()).thenReturn(elementType2);
		attributeDescriptorModel4.setAttributeType(typeModel4);

		assertEquals(elementType2, readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel4));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorMapOfMapTypes()
	{
		final AttributeDescriptorModel attributeDescriptorModel5 = new AttributeDescriptorModel();
		final MapTypeModel typeModel5 = mock(MapTypeModel.class);
		when(typeModel5.getItemtype()).thenReturn("MapType");
		final MapTypeModel returnType1 = mock(MapTypeModel.class);
		when(returnType1.getItemtype()).thenReturn("MapType");
		when(typeModel5.getReturntype()).thenReturn(returnType1);
		attributeDescriptorModel5.setAttributeType(typeModel5);

		assertNull(readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel5));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorMapOfCollectionOfComposedTypes()
	{
		final AttributeDescriptorModel attributeDescriptorModel6 = new AttributeDescriptorModel();
		final MapTypeModel typeModel6 = mock(MapTypeModel.class);
		when(typeModel6.getItemtype()).thenReturn("MapType");
		final CollectionTypeModel returnType2 = mock(CollectionTypeModel.class);
		when(returnType2.getItemtype()).thenReturn("CollectionType");
		when(typeModel6.getReturntype()).thenReturn(returnType2);
		final ComposedTypeModel elementType3 = mock(ComposedTypeModel.class);
		when(elementType3.getItemtype()).thenReturn("ComposedType");
		when(returnType2.getElementType()).thenReturn(elementType3);
		attributeDescriptorModel6.setAttributeType(typeModel6);

		assertEquals(elementType3, readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel6));
	}

	@Test
	public void testGetComplexTypeForAttributeDescriptorMapOfCollectionOfAtomicTypes()
	{
		final AttributeDescriptorModel attributeDescriptorModel7 = new AttributeDescriptorModel();
		final MapTypeModel typeModel7 = mock(MapTypeModel.class);
		when(typeModel7.getItemtype()).thenReturn("MapType");
		final CollectionTypeModel returnType3 = mock(CollectionTypeModel.class);
		when(returnType3.getItemtype()).thenReturn("CollectionType");
		when(typeModel7.getReturntype()).thenReturn(returnType3);
		final AtomicTypeModel elementType4 = mock(AtomicTypeModel.class);
		when(elementType4.getItemtype()).thenReturn("AtomicType");
		when(returnType3.getElementType()).thenReturn(elementType4);
		attributeDescriptorModel7.setAttributeType(typeModel7);

		assertNull(readService.getComplexTypeForAttributeDescriptor(attributeDescriptorModel7));
	}

	@Test
	public void testGetIntegrationObjectModels()
	{
		final SearchResult<IntegrationObjectModel> searchResult = mock(SearchResult.class);
		when(flexibleSearchService.<IntegrationObjectModel>search("SELECT PK FROM {IntegrationObject}")).thenReturn(searchResult);
		readService.getIntegrationObjectModels();
		verify(flexibleSearchService, times(1)).<IntegrationObjectModel>search("SELECT PK FROM {IntegrationObject}");
	}

	@Test
	public void testGetAvailableTypes()
	{
		final List<ComposedTypeModel> unfilteredList = new ArrayList<>();

		final ComposedTypeModel concreteComposedType = mock(ComposedTypeModel.class);
		when(concreteComposedType.getAbstract()).thenReturn(false);
		unfilteredList.add(concreteComposedType);

		final ComposedTypeModel abstractComposedType = mock(ComposedTypeModel.class);
		when(concreteComposedType.getAbstract()).thenReturn(true);
		unfilteredList.add(abstractComposedType);

		final SearchResult<ComposedTypeModel> searchResult = mock(SearchResult.class);
		when(flexibleSearchService.<ComposedTypeModel>search(
				"SELECT PK FROM {composedtype} WHERE (p_sourcetype is null AND p_generate =1) OR p_sourcetype = 8796093382738"))
				.thenReturn(searchResult);
		when(searchResult.getResult()).thenReturn(unfilteredList);

		final List<ComposedTypeModel> actual = readService.getAvailableTypes();

		verify(flexibleSearchService, times(1)).search(
				"SELECT PK FROM {composedtype} WHERE (p_sourcetype is null AND p_generate =1) OR p_sourcetype = 8796093382738");
		for (ComposedTypeModel composedTypeModel : actual)
		{
			assertEquals(false, composedTypeModel.getAbstract());
		}
	}

	@Test
	public void testGetAttributesForType()
	{
		ComposedTypeModel composedType = mock(ComposedTypeModel.class);
		readService.getAttributesForType(composedType);
		verify(typeService, times(1)).getAttributeDescriptorsForType(composedType);
	}

	@Test
	public void testGetIntegrationTypes()
	{
		assertEquals(IntegrationType.INBOUND, readService.getIntegrationTypes().get(0));
	}

	@Test
	public void testGetEDMX()
	{
		IntegrationObjectModel integrationObject = new IntegrationObjectModel();
		integrationObject.setItems(new HashSet<>());
		Schema schema = new Schema().setNamespace("namespace");
		when(schemaGenerator.generateSchema(integrationObject.getItems())).thenReturn(schema);
		assertNotNull(readService.getEDMX(integrationObject));
	}

	@Test
	public void testGetEDMXException()
	{
		assertNull(readService.getEDMX(new IntegrationObjectModel()));
	}

	@Test
	public void testGetScriptModels()
	{
		final SearchResult<ScriptModel> searchResult = mock(SearchResult.class);
		when(flexibleSearchService.<ScriptModel>search("SELECT PK FROM {Script}")).thenReturn(searchResult);
		readService.getScriptModels();
		verify(flexibleSearchService, times(1)).search(
				"SELECT PK FROM {Script}");

	}

}