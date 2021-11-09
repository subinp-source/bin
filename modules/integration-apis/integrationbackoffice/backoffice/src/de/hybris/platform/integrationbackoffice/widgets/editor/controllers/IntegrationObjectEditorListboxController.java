/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.ASCENDING;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.AUTO_SELECT_RELATION_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.CHECK_FOR_STRUCTURE_TYPE_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.DESCENDING;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.ENABLE_SAVE_BUTTON_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_ATTR_DETAILS_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_CLASSIFICATION_ATTR_DETAILS_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_RENAME_MODAL_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_RETYPE_MODAL_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_VIRTUAL_ATTR_DETAILS_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.RENAME_TREE_NODES_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.RETYPE_TREE_NODES_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.compileDuplicationMap;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createListItem;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.markRowsWithDuplicateNames;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemVirtualAttributeDTO;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationObjectPresentation;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.RenameTreeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.RetypeTreeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.SubtypeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.RenameAttributeModalData;
import de.hybris.platform.integrationservices.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.zkoss.lang.Strings;
import org.zkoss.zhtml.Button;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

public class IntegrationObjectEditorListboxController extends DefaultWidgetController
{
	private static final Logger LOG = Log.getLogger(IntegrationObjectEditorListboxController.class);

	@WireVariable
	private transient IntegrationObjectPresentation editorPresentation;

	private Listbox attributesListBox;
	private Listheader attributeNameListheader;
	private Listheader descriptionListheader;
	private List<String> attributeMenuPopupLabels;

	@Override
	public void initialize(final Component component)
	{
		super.initialize(component);
		setAttributesListBox(attributesListBox);
		initializeAttributeMenuPopupLabels();
	}

	@SocketEvent(socketId = "selectedItem")
	public void populateListBox(final ComposedTypeModel key)
	{
		clearListBox();
		addToListBox(key);
		checkDuplicates();
	}

	@SocketEvent(socketId = "focusOnListitem")
	public void focusOnListitem(final String alias)
	{
		for (final Listitem listitem : getAttributesListBox().getItems())
		{
			final AbstractListItemDTO dto = listitem.getValue();
			if (dto.getAlias().equals(alias))
			{
				Clients.scrollIntoView(listitem);
				break;
			}
		}
	}

	@SocketEvent(socketId = "retypeAttributeEvent")
	public void retypeAttribute(final SubtypeData subtypeData)
	{
		final String alias = subtypeData.getAttributeAlias();
		final ComposedTypeModel parentComposedType = subtypeData.getParentNodeType();
		final ComposedTypeModel newComposedType = editorPresentation.getReadService()
		                                                            .getComposedTypeModelFromTypeModel(subtypeData.getSubtype());
		final Treeitem currentTreeitem = editorPresentation.getComposedTypeTree().getSelectedItem();
		final ListItemAttributeDTO dto;
		Optional<ListItemAttributeDTO> optionalListItemAttributeDTO = editorPresentation.getCurrentAttributesMap()
		                                                                                .get(parentComposedType)
		                                                                                .stream()
		                                                                                .filter(ListItemAttributeDTO.class::isInstance)
		                                                                                .map(ListItemAttributeDTO.class::cast)
		                                                                                .filter(listItemDTO -> listItemDTO.getAlias()
		                                                                                                                  .equals(alias))
		                                                                                .findFirst();
		dto = optionalListItemAttributeDTO
				.orElseThrow(() -> new NoSuchElementException(
						String.format("No ListItemAttribute was found for attribute with alias %s", alias)));


		// Update data structures
		dto.setType(newComposedType);
		dto.setSelected(true);
		if (dto.isAutocreate() && newComposedType.getAbstract())
		{
			dto.setAutocreate(false);
			editorPresentation.getNotificationService().notifyUser(IntegrationbackofficeConstants.EXTENSIONNAME,
					IntegrationbackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.WARNING,
					getLabel("integrationbackoffice.editMode.warning.msg.abstractTypeAutocreate", Arrays.array(dto.getAlias())));
		}
		sendOutput(AUTO_SELECT_RELATION_OUTPUT_SOCKET, currentTreeitem);
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> attributesMap = editorPresentation.getDataStructureBuilder()
		                                                                                          .populateAttributesMap(
				                                                                                          newComposedType,
				                                                                                          editorPresentation.getCurrentAttributesMap());
		editorPresentation.setCurrentAttributesMap(attributesMap);
		editorPresentation.getSubtypeDataSet().add(subtypeData);

		final RetypeTreeData retypeTreeData = new RetypeTreeData(parentComposedType, newComposedType, currentTreeitem, dto);
		sendOutput(RETYPE_TREE_NODES_OUTPUT_SOCKET, retypeTreeData);

		LOG.info("Base type {} updated to subtype {} under parent of type {}", subtypeData.getBaseType().getCode(),
				newComposedType.getCode(), parentComposedType.getCode());
		Events.sendEvent(Events.ON_SELECT, editorPresentation.getComposedTypeTree(), currentTreeitem);
		focusOnListitem(alias);
		enableSaveButton();
	}

	@SocketEvent(socketId = "renameAttributeEvent")
	public void renameAttribute(final RenameAttributeModalData renameAttributeModalData)
	{
		final ComposedTypeModel parentComposedType = renameAttributeModalData.getParent();
		final AbstractListItemDTO updatedDTO = renameAttributeModalData.getDto();
		final String alias = updatedDTO.getAlias();
		final AbstractListItemDTO matchedDTO;

		matchedDTO = getMatchingDTO(parentComposedType, updatedDTO, alias);

		if (matchedDTO.isComplexType(editorPresentation.getReadService()))
		{
			notifyTreeForRename(parentComposedType, matchedDTO);
		}

		sendOutput(AUTO_SELECT_RELATION_OUTPUT_SOCKET, editorPresentation.getComposedTypeTree().getSelectedItem());

		if (editorPresentation.getReadService().isProductType(editorPresentation.getSelectedComposedType().getCode()))
		{
			final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> compiledDuplicates = compileDuplicationMap(
					editorPresentation.getSelectedComposedType(),
					editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType()),
					editorPresentation.getAttributeDuplicationMap());

			editorPresentation.setAttributeDuplicationMap(compiledDuplicates);
		}

		populateListBox(parentComposedType);
		focusOnListitem(alias);
		enableSaveButton();
	}

	private void checkDuplicates()
	{
		if (markRowsWithDuplicateNames(getAttributesListBox().getItems(),
				getAttributeDuplicationMap().get(getSelectedComposedType())))
		{
			getNotificationService().notifyUser(IntegrationbackofficeConstants.EXTENSIONNAME,
					IntegrationbackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.WARNING,
					getLabel("integrationbackoffice.editMode.warning.msg.nameDuplication"));
		}
	}

	private void addToListBox(final ComposedTypeModel key)
	{
		final String attributeNamesSortingDirection = attributeNameListheader.getSortDirection();
		final String descriptionsSortingDirection = descriptionListheader.getSortDirection();
		sortItems(key, attributeNamesSortingDirection, descriptionsSortingDirection).forEach((AbstractListItemDTO dto) -> {
			final Listitem listItem = setupListItem(key, dto);
			getAttributesListBox().appendChild(listItem);
		});
	}

	private Stream<AbstractListItemDTO> sortItems(final ComposedTypeModel key, final String attributeNamesSortingDirection,
	                                              final String descriptionsSortingDirection)
	{
		return getCurrentAttributesMap().get(key).stream().sorted((dto1, dto2) -> {
			if (ASCENDING.equals(attributeNamesSortingDirection))
			{
				return dto1.getAlias().compareToIgnoreCase(dto2.getAlias());
			}
			else if (DESCENDING.equals(attributeNamesSortingDirection))
			{
				return dto2.getAlias().compareToIgnoreCase(dto1.getAlias());
			}
			else if (ASCENDING.equals(descriptionsSortingDirection))
			{
				return dto1.getDescription().compareToIgnoreCase(dto2.getDescription());
			}
			else if (DESCENDING.equals(descriptionsSortingDirection))
			{
				return dto2.getDescription().compareToIgnoreCase(dto1.getDescription());
			}
			else
			{
				return dto1.getAlias().compareToIgnoreCase(dto2.getAlias());
			}
		});
	}

	private void notifyTreeForRename(final ComposedTypeModel parentComposedType, final AbstractListItemDTO matchedDTO)
	{
		final RenameTreeData renameTreeData;
		final String qualifier = matchedDTO.getQualifier();

		sendOutput(CHECK_FOR_STRUCTURE_TYPE_OUTPUT_SOCKET, matchedDTO);
		renameTreeData = new RenameTreeData(parentComposedType, matchedDTO, qualifier);
		sendOutput(RENAME_TREE_NODES_OUTPUT_SOCKET, renameTreeData);
	}

	private AbstractListItemDTO getMatchingDTO(final ComposedTypeModel parentComposedType, final AbstractListItemDTO updatedDTO,
	                                           final String alias)
	{
		final AbstractListItemDTO matchedDTO;

		matchedDTO = updatedDTO.findMatch(editorPresentation.getCurrentAttributesMap(), parentComposedType);
		matchedDTO.setAlias(alias);
		matchedDTO.setSelected(true);
		return matchedDTO;
	}

	private void clearListBox()
	{
		getAttributesListBox().getItems().clear();
	}

	private Listitem setupListItem(final ComposedTypeModel key, final AbstractListItemDTO abstractListItemDTO)
	{
		boolean isComplex = false;
		boolean hasSubtypes = false;
		if (abstractListItemDTO instanceof ListItemAttributeDTO)
		{
			final ListItemAttributeDTO dto = (ListItemAttributeDTO) abstractListItemDTO;
			isComplex = dto.isComplexType(editorPresentation.getReadService());
			if (isComplex)
			{
				hasSubtypes = !getReadService().getComposedTypeModelFromTypeModel(dto.getBaseType()).getSubtypes().isEmpty();
			}
		}
		final Listitem listItem = createListItem(abstractListItemDTO, isComplex, hasSubtypes, attributeMenuPopupLabels,
				getEditModeFlag(), getReadService());
		final Button optionsBtn = (Button) listItem.getLastChild().getFirstChild();
		final List<AbstractListItemDTO> itemAttributeDTOs = new ArrayList<>(getCurrentAttributesMap().get(key));
		itemAttributeDTOs.remove(abstractListItemDTO);
		final RenameAttributeModalData renameAttributeModalData = new RenameAttributeModalData(itemAttributeDTOs,
				abstractListItemDTO, key);

		if (getEditModeFlag())
		{
			if (abstractListItemDTO instanceof ListItemAttributeDTO)
			{
				addListItemEvents((ListItemAttributeDTO) abstractListItemDTO, listItem);
			}
			else if (abstractListItemDTO instanceof ListItemClassificationAttributeDTO || abstractListItemDTO instanceof ListItemVirtualAttributeDTO)
			{
				listItem.addEventListener(Events.ON_CLICK, event -> {
					if (!listItem.isSelected())
					{
						getAutoCreateCheckbox(listItem).setChecked(false);
					}
					updateAttribute(listItem);
				});
			}
			addCheckboxEvents(listItem);
			addDuplicationMarkerEvent(listItem);
		}
		else
		{
			addMaintainStateEvents(abstractListItemDTO, listItem);
		}

		addButtonEvents(listItem, optionsBtn);
		addMenuItemEvents(renameAttributeModalData, optionsBtn);

		if (!getEditModeFlag())
		{
			getUniqueCheckbox(listItem).setDisabled(true);
			getAutoCreateCheckbox(listItem).setDisabled(true);
			getRenameAttribute(optionsBtn).setVisible(false);
		}
		return listItem;
	}

	private void addMenuItemEvents(final RenameAttributeModalData ramd, final Button optionsBtn)
	{
		final Menuitem viewDetails = getViewDetails(optionsBtn);
		final Menuitem renameAttribute = getRenameAttribute(optionsBtn);
		final Menuitem retypeAttribute = getRetypeAttribute(optionsBtn);
		renameAttribute.addEventListener(Events.ON_CLICK, event -> sendOutput(OPEN_RENAME_MODAL_OUTPUT_SOCKET, ramd));

		if (ramd.getDto() instanceof ListItemAttributeDTO)
		{
			final ListItemAttributeDTO ramdDTO = (ListItemAttributeDTO) ramd.getDto();
			final SubtypeData data = new SubtypeData(ramd.getParent(), ramdDTO.getType(), ramdDTO.getBaseType(),
					ramd.getDto().getAlias(), ramdDTO.getAttributeDescriptor().getQualifier());
			retypeAttribute.addEventListener(Events.ON_CLICK, event -> sendOutput(OPEN_RETYPE_MODAL_OUTPUT_SOCKET, data));

			viewDetails.addEventListener(Events.ON_CLICK, event -> sendOutput(OPEN_ATTR_DETAILS_OUTPUT_SOCKET, ramd.getDto()));
		}
		else if (ramd.getDto() instanceof ListItemClassificationAttributeDTO)
		{
			viewDetails.addEventListener(Events.ON_CLICK,
					event -> sendOutput(OPEN_CLASSIFICATION_ATTR_DETAILS_OUTPUT_SOCKET, ramd.getDto()));
		}
		else
		{
			viewDetails.addEventListener(Events.ON_CLICK,
					event -> sendOutput(OPEN_VIRTUAL_ATTR_DETAILS_OUTPUT_SOCKET, ramd.getDto()));
		}
	}


	private void addCheckboxEvents(final Listitem listItem)
	{
		final Checkbox uniqueCheckbox = getUniqueCheckbox(listItem);
		final Checkbox autocreateCheckbox = getAutoCreateCheckbox(listItem);
		uniqueCheckbox.addEventListener(Events.ON_CHECK, event -> checkboxEventActions(listItem, uniqueCheckbox));
		autocreateCheckbox.addEventListener(Events.ON_CHECK, event -> checkboxEventActions(listItem, autocreateCheckbox));
	}

	private void checkboxEventActions(final Listitem listItem, final Checkbox checkbox)
	{
		if (!listItem.isDisabled())
		{
			if (checkbox.isChecked())
			{
				listItem.setSelected(true);
				if (listItem.getValue() instanceof ListItemAttributeDTO)
				{
					sendOutput(CHECK_FOR_STRUCTURE_TYPE_OUTPUT_SOCKET, listItem.getValue());
				}
			}
			updateAttribute(listItem);
		}
	}

	private void addButtonEvents(final Listitem listItem, final Button detailsBtn)
	{
		detailsBtn.addEventListener(Events.ON_CLICK, event -> {
			if (!listItem.isDisabled())
			{
				final Menupopup menuPopup = (Menupopup) listItem.getLastChild().getFirstChild().getFirstChild();
				menuPopup.open(detailsBtn);
			}
		});
	}

	private void addListItemEvents(final ListItemAttributeDTO dto, final Listitem listItem)
	{
		final Checkbox uniqueCheckbox = getUniqueCheckbox(listItem);
		final Checkbox autocreateCheckbox = getAutoCreateCheckbox(listItem);
		if (dto.isRequired())
		{
			listItem.addEventListener(Events.ON_CLICK, event ->
					maintainSelectionState(listItem, true));
		}
		else if (!dto.isSupported())
		{
			listItem.addEventListener(Events.ON_CLICK, event -> {
				maintainSelectionState(listItem, false);
				getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
						NotificationEvent.Level.WARNING,
						getLabel("integrationbackoffice.editMode.warning.msg.illegalMapTypeAttribute",
								Arrays.array(dto.getAttributeDescriptor().getQualifier())));
			});
		}
		else if (!listItem.isDisabled())
		{
			listItem.addEventListener(Events.ON_CLICK, event -> {
				if (listItem.isSelected())
				{
					sendOutput(CHECK_FOR_STRUCTURE_TYPE_OUTPUT_SOCKET, dto);
				}
				else
				{
					if (!dto.getAttributeDescriptor().getUnique())
					{
						uniqueCheckbox.setChecked(false);
					}
					autocreateCheckbox.setChecked(false);
				}
				updateAttribute(listItem);
			});
		}
	}

	private void addMaintainStateEvents(final AbstractListItemDTO dto, final Listitem listItem)
	{
		if (dto.isSelected())
		{
			listItem.addEventListener(Events.ON_CLICK, event -> maintainSelectionState(listItem, true));
		}
		else
		{
			listItem.addEventListener(Events.ON_CLICK, event -> maintainSelectionState(listItem, false));
		}
	}

	private void maintainSelectionState(final Listitem listItem, final boolean selected)
	{
		listItem.setSelected(selected);
	}

	private void addDuplicationMarkerEvent(final Listitem listItem)
	{
		listItem.addEventListener(Events.ON_CLICK, event -> {
				checkForDuplicates();
		});
	}

	private void checkForDuplicates()
	{
		final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> tempAttributeDuplicationMap = getAttributeDuplicationMap();
		final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> newAttributeDuplicationMap = compileDuplicationMap(
				getSelectedComposedType(),
				getCurrentAttributesMap().get(getSelectedComposedType()), tempAttributeDuplicationMap);
		setAttributeDuplicationMap(newAttributeDuplicationMap);
		markRowsWithDuplicateNames(getAttributesListBox().getItems(),
				getAttributeDuplicationMap().get(getSelectedComposedType()));
	}

	private void updateAttribute(final Listitem listitem)
	{
		final AbstractListItemDTO dto = listitem.getValue();
		final List<Component> components = listitem.getChildren();
		final Checkbox uCheckbox = ((Checkbox) components.get(EditorConstants.UNIQUE_CHECKBOX_COMPONENT_INDEX).getFirstChild());
		final Checkbox aCheckbox = ((Checkbox) components.get(EditorConstants.AUTOCREATE_CHECKBOX_COMPONENT_INDEX)
		                                                 .getFirstChild());
		final Listcell attributeLabel = (Listcell) components.get(EditorConstants.ATTRIBUTE_NAME_COMPONENT_INDEX);
		dto.setAlias(attributeLabel.getLabel());
		dto.setSelected(listitem.isSelected());
		dto.setCustomUnique(uCheckbox.isChecked());
		dto.setAutocreate(aCheckbox.isChecked());
		if (getEditModeFlag())
		{
			enableSaveButton();
		}
	}

	private void enableSaveButton()
	{
		if (!isModified())
		{
			setModified(true);
			sendOutput(ENABLE_SAVE_BUTTON_OUTPUT_SOCKET, true);
		}
	}

	private void initializeAttributeMenuPopupLabels()
	{
		attributeMenuPopupLabels = new ArrayList<>();
		attributeMenuPopupLabels.add(getLabel("integrationbackoffice.editMode.menuItem.viewDetails"));
		attributeMenuPopupLabels.add(getLabel("integrationbackoffice.editMode.menuItem.renameAttribute"));
		attributeMenuPopupLabels.add(getLabel("integrationbackoffice.editMode.menuItem.retypeAttribute"));
	}

	private Menuitem getRetypeAttribute(final Button optionsBtn)
	{
		return (Menuitem) optionsBtn.getFirstChild().getLastChild();
	}

	private Menuitem getRenameAttribute(final Button optionsBtn)
	{
		return (Menuitem) optionsBtn.getFirstChild().getFirstChild().getNextSibling();
	}

	private Menuitem getViewDetails(final Button optionsBtn)
	{
		return (Menuitem) optionsBtn.getFirstChild().getFirstChild();
	}

	private Checkbox getAutoCreateCheckbox(final Listitem listItem)
	{
		return (Checkbox) listItem.getChildren()
		                          .get(EditorConstants.AUTOCREATE_CHECKBOX_COMPONENT_INDEX)
		                          .getFirstChild();
	}

	private Checkbox getUniqueCheckbox(final Listitem listItem)
	{
		return (Checkbox) listItem.getChildren()
		                          .get(EditorConstants.UNIQUE_CHECKBOX_COMPONENT_INDEX)
		                          .getFirstChild();
	}

	//IntegrationEditorData
	private Map<ComposedTypeModel, List<AbstractListItemDTO>> getCurrentAttributesMap()
	{
		return editorPresentation.getCurrentAttributesMap();
	}

	private NotificationService getNotificationService()
	{
		return editorPresentation.getNotificationService();
	}

	private ReadService getReadService()
	{
		return editorPresentation.getReadService();
	}

	private boolean getEditModeFlag()
	{
		return editorPresentation.isEditModeFlag();
	}

	private Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> getAttributeDuplicationMap()
	{
		return editorPresentation.getAttributeDuplicationMap();
	}

	private void setAttributeDuplicationMap(
			final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> attributeDuplicationMap)
	{
		editorPresentation.setAttributeDuplicationMap(attributeDuplicationMap);
	}

	private ComposedTypeModel getSelectedComposedType()
	{
		return editorPresentation.getSelectedComposedType();
	}

	private Listbox getAttributesListBox()
	{
		return editorPresentation.getAttributesListBox();
	}

	private void setModified(final boolean isModified)
	{
		editorPresentation.setModified(isModified);
	}

	private boolean isModified()
	{
		return editorPresentation.isModified();
	}

	private Tree getComposedTypeTree()
	{
		return editorPresentation.getComposedTypeTree();
	}

	private void setAttributesListBox(final Listbox attributesListBox)
	{
		editorPresentation.setAttributesListBox(attributesListBox);
	}
}