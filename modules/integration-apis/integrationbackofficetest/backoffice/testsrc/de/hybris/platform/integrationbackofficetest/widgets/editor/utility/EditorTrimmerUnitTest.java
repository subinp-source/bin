/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.editor.utility;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationObjectPresentation;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.TreeNodeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorTrimmer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditorTrimmerUnitTest
{
	@Mock
	private ReadService readService;
	@Mock
	private Tree tree;
	@Mock
	private Treeitem rootTreeItem;
	@Mock
	private Treechildren treechildren;
	@Mock
	private Treeitem productTreeItem;
	@Mock
	private Treeitem warehouseTreeItem;

	private ComposedTypeModel rootType;
	private ComposedTypeModel product;
	private ComposedTypeModel warehouse;
	private TreeNodeData rootNodeData;
	private TreeNodeData warehouseNodeData;
	private TreeNodeData productNodeData;
	private IntegrationObjectPresentation integrationObjectPresentation;

	@Before
	public void setUp()
	{
		rootType = new ComposedTypeModel();
		rootNodeData = new TreeNodeData(null, null, rootType);

		product = new ComposedTypeModel();
		product.setCode("Product");
		productNodeData = new TreeNodeData("product", null, product);

		warehouse = new ComposedTypeModel();
		warehouse.setCode("Warehouse");
		warehouseNodeData = new TreeNodeData("warehouse", null, warehouse);

		integrationObjectPresentation = new IntegrationObjectPresentation();
		integrationObjectPresentation.setReadService(readService);
		integrationObjectPresentation.setComposedTypeTree(tree);
	}

	@Test
	public void testTrimmer()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> attributesMap = prepareMockMap();

		assertEquals(3, attributesMap.get(rootType).size());
		assertEquals(1, attributesMap.get(warehouse).size());
		assertEquals(0, attributesMap.get(product).size());

		integrationObjectPresentation.setCurrentAttributesMap(attributesMap);
		integrationObjectPresentation.setComposedTypeTree(tree);

		// Stock level
		//      -> product (not selected, trimmed)
		//      -> products (not selected, trimmed)
		//      -> warehouse (selected)
		//          -> name (selected)

		when(tree.getItems()).thenReturn(Collections.singletonList(rootTreeItem));
		when(rootTreeItem.getValue()).thenReturn(rootNodeData);
		when(rootTreeItem.getTreechildren()).thenReturn(treechildren);
		when(warehouseTreeItem.getValue()).thenReturn(warehouseNodeData);

		final List<Component> children = new ArrayList<>();
		children.add(productTreeItem);
		children.add(warehouseTreeItem);

		when(treechildren.getChildren()).thenReturn(children);
		when(productTreeItem.getValue()).thenReturn(productNodeData);
		when(productTreeItem.getLabel()).thenReturn("product [Product]");
		when(warehouseTreeItem.getValue()).thenReturn(warehouseNodeData);
		when(warehouseTreeItem.getLabel()).thenReturn("warehouse [Warehouse]");

		when(readService.isComplexType(warehouse)).thenReturn(true);

		final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = EditorTrimmer.trimMap(integrationObjectPresentation,
				false);

		assertNotNull(trimmedMap);
		assertEquals(1, trimmedMap.get(rootType).size());
		assertEquals(1, trimmedMap.get(warehouse).size());
		assertNull(trimmedMap.get(product));

		final List<AbstractListItemDTO> stockLevelAttributes = trimmedMap.get(rootType);
		final List<AbstractListItemDTO> warehouseAttributes = trimmedMap.get(warehouse);
		assertTrue(stockLevelAttributes.get(0).isSelected());
		assertEquals(warehouse, ((ListItemAttributeDTO) stockLevelAttributes.get(0)).getType());
		assertEquals("warehouse", ((ListItemAttributeDTO) stockLevelAttributes.get(0)).getAttributeDescriptor().getQualifier());
		assertTrue(warehouseAttributes.get(0).isSelected());
		assertEquals("java.lang.String", ((ListItemAttributeDTO) warehouseAttributes.get(0)).getType().getCode());
		assertEquals("name", ((ListItemAttributeDTO) warehouseAttributes.get(0)).getAttributeDescriptor().getQualifier());
	}

	@Test
	public void testTrimmerConsiderSubtypes()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> attributesMap = prepareMockMap();

		ListItemAttributeDTO productAttrInStockLevel = null;
		for (AbstractListItemDTO dto : attributesMap.get(rootType))
		{
			if (dto instanceof ListItemAttributeDTO && dto.getAlias().equals("product"))
			{
				productAttrInStockLevel = (ListItemAttributeDTO) dto;
			}
		}

		assert productAttrInStockLevel != null;
		productAttrInStockLevel.setType(warehouse);  // Assigning Warehouse as the new subtype of Product

		assertEquals(3, attributesMap.get(rootType).size());
		assertEquals(1, attributesMap.get(warehouse).size());
		assertEquals(0, attributesMap.get(product).size());

		integrationObjectPresentation.setCurrentAttributesMap(attributesMap);
		integrationObjectPresentation.setComposedTypeTree(tree);

		// Stock level
		//      -> product (not selected, NOT trimmed (due to subtype))
		//      -> products (not selected, trimmed)
		//      -> warehouse (selected)
		//          -> name (selected)

		when(tree.getItems()).thenReturn(Collections.singletonList(rootTreeItem));
		when(rootTreeItem.getValue()).thenReturn(rootNodeData);
		when(rootTreeItem.getTreechildren()).thenReturn(treechildren);
		when(warehouseTreeItem.getValue()).thenReturn(warehouseNodeData);
		when(productTreeItem.getValue()).thenReturn((productNodeData));

		final List<Component> children = new ArrayList<>();
		children.add(productTreeItem);
		children.add(warehouseTreeItem);

		when(treechildren.getChildren()).thenReturn(children);
		when(productTreeItem.getLabel()).thenReturn("product [Product]");
		when(warehouseTreeItem.getLabel()).thenReturn("warehouse [Warehouse]");

		when(readService.isComplexType(warehouse)).thenReturn(true);
		when(readService.isComplexType(product)).thenReturn(true);

		final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = EditorTrimmer.trimMap(integrationObjectPresentation,
				true);

		assertNotNull(trimmedMap);
		assertEquals(2, trimmedMap.get(rootType).size());
		assertEquals(1, trimmedMap.get(warehouse).size());
		assertNotNull(trimmedMap.get(product));

		final List<AbstractListItemDTO> stockLevelAttributes = trimmedMap.get(rootType);
		final List<AbstractListItemDTO> warehouseAttributes = trimmedMap.get(warehouse);
		assertFalse(stockLevelAttributes.get(0).isSelected());
		assertEquals(warehouse, ((ListItemAttributeDTO) stockLevelAttributes.get(0)).getType());
		assertEquals("product", ((ListItemAttributeDTO) stockLevelAttributes.get(0)).getAttributeDescriptor().getQualifier());
		assertEquals("warehouse", ((ListItemAttributeDTO) stockLevelAttributes.get(1)).getAttributeDescriptor().getQualifier());
		assertTrue(warehouseAttributes.get(0).isSelected());
		assertEquals("java.lang.String", ((ListItemAttributeDTO) warehouseAttributes.get(0)).getType().getCode());
		assertEquals("name", ((ListItemAttributeDTO) warehouseAttributes.get(0)).getAttributeDescriptor().getQualifier());
	}

	private Map<ComposedTypeModel, List<AbstractListItemDTO>> prepareMockMap()
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> attributesMap = new HashMap<>();

		// StockLevel attributes
		final List<AbstractListItemDTO> stockLevelAttributes = new ArrayList<>();

		final AttributeDescriptorModel productAttribute = new AttributeDescriptorModel();
		productAttribute.setAttributeType(product);
		productAttribute.setQualifier("product");

		final AttributeDescriptorModel productsAttribute = new AttributeDescriptorModel();
		final CollectionTypeModel collectionTypeModel = new CollectionTypeModel();
		collectionTypeModel.setElementType(product);
		productsAttribute.setAttributeType(collectionTypeModel);
		productsAttribute.setQualifier("products");

		final AttributeDescriptorModel warehouseAttribute = new AttributeDescriptorModel();
		warehouseAttribute.setAttributeType(warehouse);
		warehouseAttribute.setQualifier("warehouse");

		stockLevelAttributes.add(
				new ListItemAttributeDTO(false, false, false, productAttribute, ListItemStructureType.NONE, "", null));
		stockLevelAttributes.add(
				new ListItemAttributeDTO(false, false, false, productsAttribute, ListItemStructureType.COLLECTION, "", null));
		stockLevelAttributes.add(
				new ListItemAttributeDTO(true, true, false, warehouseAttribute, ListItemStructureType.NONE, "", null));

		// Warehouse attributes
		final List<AbstractListItemDTO> warehouseAttributes = new ArrayList<>();

		final AttributeDescriptorModel nameAttribute = new AttributeDescriptorModel();
		final AtomicTypeModel atomicTypeModel = new AtomicTypeModel();
		atomicTypeModel.setCode("java.lang.String");
		nameAttribute.setAttributeType(atomicTypeModel);
		nameAttribute.setQualifier("name");

		warehouseAttributes.add(new ListItemAttributeDTO(true, true, false, nameAttribute, ListItemStructureType.NONE, "", null));

		attributesMap.put(rootType, stockLevelAttributes);
		attributesMap.put(product, Collections.emptyList());
		attributesMap.put(warehouse, warehouseAttributes);
		return attributesMap;
	}
}
