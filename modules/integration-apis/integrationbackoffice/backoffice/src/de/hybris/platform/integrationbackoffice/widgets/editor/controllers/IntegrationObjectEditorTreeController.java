/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.SELECTED_ITEM_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorTrimmer.trimMap;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.convertIntegrationObjectToDTOMap;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createTreeItem;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.findInTreechildren;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.getReferenceClassificationAttributes;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.getStructuredAttributes;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.renameTreeitem;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorValidator.validateHasKey;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemDTOMissingDescriptorModelException;
import de.hybris.platform.integrationbackoffice.dto.ListItemStructureType;
import de.hybris.platform.integrationbackoffice.widgets.builders.TreeBuilder;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.CreateTreeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationFilterState;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationObjectPresentation;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.RenameTreeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.RetypeTreeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.SubtypeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.TreeNodeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAttributesFilteringService;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorBlacklists;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

public class IntegrationObjectEditorTreeController extends DefaultWidgetController
{
	private static final Logger LOG = Log.getLogger(IntegrationObjectEditorTreeController.class);

	private static final int MAX_DEPTH = 5;

	@WireVariable
	private transient TreeBuilder treeBuilder;
	@WireVariable
	private transient EditorAttributesFilteringService editorAttrFilterService;
	@WireVariable
	private transient IntegrationObjectPresentation editorPresentation;

	private Tree composedTypeTree;

	public void setIntegrationObjectEditorData(
			final IntegrationObjectPresentation integrationObjectPresentation)
	{
		this.editorPresentation = integrationObjectPresentation;
	}

	public Tree getComposedTypeTree()
	{
		return composedTypeTree;
	}

	public void setComposedTypeTree(final Tree composedTypeTree)
	{
		this.composedTypeTree = composedTypeTree;
	}

	@Override
	public void initialize(final Component component)
	{
		super.initialize(component);
		editorPresentation.setComposedTypeTree(composedTypeTree);
	}

	@GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
	public void handleIntegrationObjectDeletedEvent(final CockpitEvent event)
	{
		if (event.getDataAsCollection().stream().anyMatch(IntegrationObjectModel.class::isInstance))
		{
			clearSelectedIntegrationObject();
		}
	}

	@ViewEvent(componentID = "composedTypeTree", eventName = Events.ON_SELECT)
	public void composedTypeTreeOnSelect()
	{
		final TreeNodeData tnd = editorPresentation.getComposedTypeTree().getSelectedItem().getValue();
		editorPresentation.setSelectedComposedType(tnd.getComposedTypeModel());
		sendOutput(SELECTED_ITEM_OUTPUT_SOCKET, editorPresentation.getSelectedComposedType());
	}


	@SocketEvent(socketId = "loadIO")
	public void loadIntegrationObject(final String s)
	{
		editorPresentation.setAttributeDuplicationMap(new HashMap<>());
		final IntegrationObjectItemModel root = editorPresentation.getSelectedIntegrationObject().getRootItem();
		if (root != null)
		{
			try
			{
				final Map<ComposedTypeModel, List<AbstractListItemDTO>> convertedMap = convertIntegrationObjectToDTOMap(
						editorPresentation.getReadService(),
						editorPresentation.getSelectedIntegrationObject());

				final Set<SubtypeData> subtypeDataSet = editorPresentation.getDataStructureBuilder()
				                                                          .compileSubtypeDataSet(convertedMap, new HashSet<>());

				editorPresentation.setSubtypeDataSet(subtypeDataSet);
				CreateTreeData createTreeData = new CreateTreeData(root.getType(), convertedMap);
				createTree(createTreeData);
				final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = trimMap(editorPresentation, false);
				if (!("").equals(validateHasKey(trimmedMap)))
				{
					showObjectLoadedFurtherConfigurationMessage();
				}
			}
			catch (ListItemDTOMissingDescriptorModelException e)
			{
				LOG.error(e.getMessage());
				showMissingTypeMessage(e.getMessage());
			}
		}
		else
		{
			editorPresentation.clearTreeAndListbox();
			editorPresentation.getNotificationService()
			                  .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					                  NotificationEvent.Level.WARNING,
					                  getLabel("integrationbackoffice.editMode.warning.msg.invalidObjectLoaded"));
		}
	}

	@SocketEvent(socketId = "retypeTreeNodes")
	public void retypeTreeNodes(final RetypeTreeData retypeTreeData)
	{
		final ComposedTypeModel parentComposedType = retypeTreeData.getParentComposedType();
		final ComposedTypeModel newComposedType = retypeTreeData.getNewComposedType();
		final Treeitem currentTreeitem = retypeTreeData.getCurrentTreeitem();
		final ListItemAttributeDTO dto = retypeTreeData.getDto();

		// Add node for collections or circular dependency changes
		final String dtoAttributeDescriptorQualifier = dto.getAttributeDescriptor().getQualifier();
		if (editorPresentation.getReadService().isComplexType(dto.getType()))
		{
			final Treechildren nodeChildren = currentTreeitem.getTreechildren();
			if (findInTreechildren(dtoAttributeDescriptorQualifier, nodeChildren) == null)
			{
				createDynamicTreeNode(dto);
			}
		}

		// Replace nodes with new nodes containing subtype and rebuild branches of replaced nodes
		final List<Treeitem> treeitems = new ArrayList<>(editorPresentation.getComposedTypeTree().getItems());
		final Iterator<Treeitem> iterator = treeitems.iterator();
		while (iterator.hasNext())
		{
			final Treeitem treeitem = iterator.next();
			final TreeNodeData treeNodeData = treeitem.getValue();
			final ComposedTypeModel composedTypeModel = treeNodeData.getComposedTypeModel();
			if (parentComposedType.equals(composedTypeModel))
			{
				final Treechildren treechildren = treeitem.getTreechildren();
				final Treeitem childTreeitem = findInTreechildren(dtoAttributeDescriptorQualifier, treechildren);
				if (childTreeitem != null)
				{
					final TreeNodeData childTreeNodeData = childTreeitem.getValue();
					childTreeNodeData.setComposedTypeModel(newComposedType);

					final Treeitem updatedTreeitem = createTreeItem(childTreeNodeData, childTreeitem.isOpen());
					updatedTreeitem.appendChild(new Treechildren());
					treechildren.removeChild(childTreeitem);
					treechildren.appendChild(updatedTreeitem);

					editorPresentation.setAncestors(treeBuilder.determineTreeitemAncestors(updatedTreeitem));
					populateTree(updatedTreeitem, Collections.emptyMap());
					iterator.remove();
				}
			}
		}
	}

	@SocketEvent(socketId = "createTree")
	public void createTree(final CreateTreeData data)
	{
		editorPresentation.clearTreeAndListbox();
		editorPresentation.getAncestors().push(data.getRoot());

		final Treechildren rootLevel = editorPresentation.getComposedTypeTree().getTreechildren();
		final TreeNodeData tnd = new TreeNodeData(null, null, data.getRoot());
		final Treeitem rootTreeItem = createTreeItem(tnd, true);
		rootLevel.appendChild(rootTreeItem);

		editorPresentation.setCurrentAttributesMap(new HashMap<>());
		if (editorPresentation.getFilterState() == IntegrationFilterState.SHOW_ALL)
		{
			editorPresentation.setCurrentAttributesMap(
					editorPresentation.getDataStructureBuilder().populateAttributesMap(
							data.getRoot(), editorPresentation.getCurrentAttributesMap()));
			populateTree(rootTreeItem, data.getDefinitionMap());
			editorPresentation.setCurrentAttributesMap(editorPresentation
					.getDataStructureBuilder()
					.loadExistingDefinitions(data.getDefinitionMap(), editorPresentation.getCurrentAttributesMap()));
		}
		else
		{
			editorPresentation.setCurrentAttributesMap(data.getDefinitionMap());
			populateTreeInOnlySelectedMode(rootTreeItem, data.getDefinitionMap());
		}

		rootTreeItem.setSelected(true);
		Events.sendEvent(Events.ON_SELECT, editorPresentation.getComposedTypeTree(), rootTreeItem);
	}

	@SocketEvent(socketId = "checkForStructuredType")
	public void checkTreeNodeForStructuredType(final ListItemAttributeDTO dto)
	{
		if (isComplexStructuredType(dto))
		{
			final Treechildren nodeChildren = getComposedTypeTree().getSelectedItem().getTreechildren();
			final String attributeDescriptorQualifier = dto.getAttributeDescriptor().getQualifier();
			if (findInTreechildren(attributeDescriptorQualifier, nodeChildren) == null)
			{
				createDynamicTreeNode(dto);
			}
		}
	}

	@SocketEvent(socketId = "renameTreeNodes")
	public void renameTreeitemEvent(final RenameTreeData renameTreeData)
	{
		final ComposedTypeModel parentComposedType = renameTreeData.getParentComposedType();
		final AbstractListItemDTO matchedDTO = renameTreeData.getMatchedDTO();
		final String qualifier = renameTreeData.getQualifier();

		editorPresentation.getComposedTypeTree().getItems().forEach(treeitem -> {
			final TreeNodeData treeNodeData = treeitem.getValue();
			if (parentComposedType.equals(treeNodeData.getComposedTypeModel()))
			{
				final Treeitem childTreeitem = findInTreechildren(qualifier, treeitem.getTreechildren());
				renameTreeitem(childTreeitem, matchedDTO);
			}
		});
	}

	@SocketEvent(socketId = "createDynamicTreeNode")
	public void createDynamicTreeNode(final AbstractListItemDTO dto)
	{
		final String qualifier = dto.getQualifier();
		final TypeModel typeModel = dto.getType();
		final String alias = dto.getAlias();
		final ComposedTypeModel type = (ComposedTypeModel) typeModel;
		final TreeNodeData tnd = new TreeNodeData(qualifier, alias, type);
		final Treeitem parent = getComposedTypeTree().getSelectedItem();
		final Treeitem treeItem = treeBuilder.appendTreeitem(parent, tnd);
		editorPresentation.setCurrentAttributesMap(
				editorPresentation.getDataStructureBuilder()
				                  .populateAttributesMap(type, editorPresentation.getCurrentAttributesMap()));
		editorPresentation.getAncestors().clear();
		populateTree(treeItem, Collections.emptyMap());
	}

	@SocketEvent(socketId = "autoSelectAttributeRelation")
	public void autoSelectAttributeRelation(Treeitem currentTreeitem)
	{
		final Treeitem rootTreeitem = (Treeitem) getComposedTypeTree().getTreechildren().getFirstChild();
		while (currentTreeitem != rootTreeitem)
		{
			final String qualifier = ((TreeNodeData) currentTreeitem.getValue()).getQualifier();
			final Treeitem parentTreeitem = currentTreeitem.getParentItem();
			final ComposedTypeModel parentType = ((TreeNodeData) parentTreeitem.getValue()).getComposedTypeModel();
			editorPresentation.getCurrentAttributesMap().get(parentType).stream().filter(ListItemAttributeDTO.class::isInstance)
			                  .map(ListItemAttributeDTO.class::cast).forEach(listItemDTO -> {
				if (listItemDTO.getAttributeDescriptor().getQualifier().equals(qualifier))
				{
					listItemDTO.setSelected(true);
				}
			});
			currentTreeitem = parentTreeitem;
		}
	}

	/**
	 * Generate a tree iteratively.
	 *
	 * @param parent              new treeNode will be generated and added to this object and works as parent for iteration
	 * @param existingDefinitions a map contains all attributeDTO and classificationAttributeDTO for an IO. Normally selected DTO.
	 */
	private void populateTree(final Treeitem parent, final Map<ComposedTypeModel, List<AbstractListItemDTO>> existingDefinitions)
	{
		final ComposedTypeModel parentType = ((TreeNodeData) parent.getValue()).getComposedTypeModel();
		final List<AbstractListItemDTO> existingAttributes = (existingDefinitions.get(
				parentType) == null) ? Collections.emptyList()
				: existingDefinitions.get(parentType);
		final Set<AttributeDescriptorModel> filteredAttributes = editorAttrFilterService.filterAttributesForTree(parentType);
		final Set<AttributeDescriptorModel> existingCollections = getStructuredAttributes(existingAttributes);
		filteredAttributes.addAll(existingCollections);

		final Set<TreeNodeData> treeNodeDataSet = generateTreeNodeData(filteredAttributes, parentType, existingAttributes);
		treeNodeDataSet.addAll(getReferenceClassificationAttributes(existingAttributes));

		treeNodeDataSet.stream()
		               .sorted((attribute1, attribute2) -> attribute1.getAlias().compareToIgnoreCase(attribute2.getAlias()))
		               .forEach(treeNodeData -> {
			               final ComposedTypeModel composedType = treeNodeData.getComposedTypeModel();
			               if (!editorPresentation.getAncestors().contains(composedType)
					               && !EditorBlacklists.getTypesBlackList().contains(composedType.getCode()))
			               {
				               editorPresentation.getAncestors().addFirst(composedType);

				               if (!editorPresentation.getCurrentAttributesMap().containsKey(composedType))
				               {
					               editorPresentation.setCurrentAttributesMap(editorPresentation.getDataStructureBuilder()
					                                                                            .populateAttributesMap(
							                                                                            composedType,
							                                                                            editorPresentation.getCurrentAttributesMap()));
				               }

				               final Treeitem treeitem = treeBuilder.appendTreeitem(parent, treeNodeData);
				               if (treeitem.getLevel() <= MAX_DEPTH)
				               {
					               populateTree(treeitem, existingDefinitions);
				               }

				               editorPresentation.getAncestors().pollFirst();
			               }
		               });
	}

	private Set<TreeNodeData> generateTreeNodeData(final Set<AttributeDescriptorModel> filteredAttributes,
	                                               final ComposedTypeModel parentType,
	                                               final List<AbstractListItemDTO> existingAttributes)
	{
		return filteredAttributes.stream().filter(attributeDescriptor -> {
			final ComposedTypeModel attributeType = editorPresentation
					.getReadService().getComplexTypeForAttributeDescriptor(attributeDescriptor);
			return (attributeType != null);
		}).map(attributeDescriptor -> {
			final String attributeDescriptorQualifier = attributeDescriptor.getQualifier();
			final ComposedTypeModel attributeType = editorPresentation
					.getReadService().getComplexTypeForAttributeDescriptor(attributeDescriptor);
			final ComposedTypeModel attributeSubtype = editorPresentation
					.getDataStructureBuilder()
					.findSubtypeMatch(parentType, attributeDescriptorQualifier, attributeType,
							editorPresentation.getSubtypeDataSet());
			final ComposedTypeModel determinedType = attributeSubtype != null ? attributeSubtype : attributeType;
			for (final AbstractListItemDTO dto : existingAttributes)
			{
				if (dto instanceof ListItemAttributeDTO
						&& ((ListItemAttributeDTO) dto).getAttributeDescriptor().equals(attributeDescriptor))
				{
					return new TreeNodeData(attributeDescriptorQualifier, dto.getAlias(), determinedType);
				}
			}
			return new TreeNodeData(attributeDescriptorQualifier, null, determinedType);
		}).collect(Collectors.toSet());
	}

	private void populateTreeInOnlySelectedMode(final Treeitem parent,
	                                            final Map<ComposedTypeModel, List<AbstractListItemDTO>> existingDefinitions)
	{
		final TreeNodeData parentTreeNodeData = parent.getValue();
		final ComposedTypeModel parentType = parentTreeNodeData.getComposedTypeModel();
		final List<AbstractListItemDTO> existingAttributes = (existingDefinitions.get(
				parentType) == null) ? Collections.emptyList()
				: existingDefinitions.get(parentType);
		final List<TreeNodeData> treeNodeDataSet = generateTreeNodeData(parentType, existingAttributes);
		treeNodeDataSet.addAll(getReferenceClassificationAttributes(existingAttributes));
		treeNodeDataSet.stream()
		               .sorted((attribute1, attribute2) -> attribute1.getAlias().compareToIgnoreCase(attribute2.getAlias()))
		               .forEach(treeNodeData -> {
			               final ComposedTypeModel composedType = treeNodeData.getComposedTypeModel();
			               if (!editorPresentation.getAncestors().contains(composedType))
			               {
				               editorPresentation.getAncestors().addFirst(composedType);
				               final Treeitem treeitem = treeBuilder.appendTreeitem(parent, treeNodeData);
				               populateTreeInOnlySelectedMode(treeitem, existingDefinitions);
				               editorPresentation.getAncestors().pollFirst();
			               }
		               });
	}

	private List<TreeNodeData> generateTreeNodeData(final ComposedTypeModel parentType,
	                                                final List<AbstractListItemDTO> existingAttributes)
	{
		return existingAttributes.stream()
		                         .filter(ListItemAttributeDTO.class::isInstance)
		                         .map(ListItemAttributeDTO.class::cast)
		                         .filter(listItemDTO -> {
			                         final ComposedTypeModel attributeType = editorPresentation.getReadService()
			                                                                                   .getComplexTypeForAttributeDescriptor(
					                                                                                   listItemDTO.getAttributeDescriptor());
			                         return (attributeType != null);
		                         })
		                         .map(listItemDTO -> {
			                         final AttributeDescriptorModel attributeDescriptor = listItemDTO
					                         .getAttributeDescriptor();
			                         final String attributeDescriptorQualifier = attributeDescriptor
					                         .getQualifier();
			                         final ComposedTypeModel attributeType = editorPresentation.getReadService()
			                                                                                   .getComplexTypeForAttributeDescriptor(
					                                                                                   attributeDescriptor);
			                         final ComposedTypeModel attributeSubtype = editorPresentation.getDataStructureBuilder()
			                                                                                      .findSubtypeMatch(
					                                                                                      parentType,
					                                                                                      attributeDescriptorQualifier,
					                                                                                      attributeType,
					                                                                                      editorPresentation.getSubtypeDataSet());
			                         final ComposedTypeModel determinedType = attributeSubtype != null ? attributeSubtype : attributeType;
			                         return new TreeNodeData(attributeDescriptorQualifier,
					                         listItemDTO.getAlias(), determinedType);
		                         })
		                         .collect(Collectors.toList());
	}

	private void showObjectLoadedFurtherConfigurationMessage()
	{
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.WARNING,
				getLabel("integrationbackoffice.editMode.warning.msg.serviceLoadedNeedsFurtherConfig"));
	}

	private void clearSelectedIntegrationObject()
	{
		editorPresentation.clearTreeAndListbox();
		editorPresentation.setSelectedIntegrationObject(null);
		editorPresentation.getAttributeDuplicationMap().clear();
	}

	private boolean isComplexStructuredType(final ListItemAttributeDTO dto)
	{
		final boolean isStructuredType = dto.getStructureType() == ListItemStructureType.MAP
				|| dto.getStructureType() == ListItemStructureType.COLLECTION;
		return isStructuredType && editorPresentation.getReadService().isComplexType(dto.getType());
	}

	private void showMissingTypeMessage(String sourceMessage)
	{
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.WARNING,
				getLabel("integrationbackoffice.editMode.error.msg.missingType", Arrays.array(sourceMessage)));
	}
}
