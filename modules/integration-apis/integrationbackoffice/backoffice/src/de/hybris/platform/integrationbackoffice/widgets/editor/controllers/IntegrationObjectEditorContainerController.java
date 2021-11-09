/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.CREATE_DYNAMIC_TREE_NODE_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.CREATE_TREE_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.ENABLE_SAVE_BUTTON_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.FILTER_STATE_CHANGE_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.LOAD_IO_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_CLONE_MODAL_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.OPEN_ITEM_TYPE_MODAL_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants.SELECTED_ITEM_OUTPUT_SOCKET;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorTrimmer.trimMap;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.compileDuplicationMap;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.convertIntegrationObjectToDTOMap;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.isClassificationAttributePresent;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorValidator.validateDefinitions;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorValidator.validateHasKey;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorValidator.validateHasNoDuplicateAttributeNames;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationbackoffice.constants.IntegrationbackofficeConstants;
import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;
import de.hybris.platform.integrationbackoffice.dto.ListItemDTOMissingDescriptorModelException;
import de.hybris.platform.integrationbackoffice.dto.ListItemVirtualAttributeDTO;
import de.hybris.platform.integrationbackoffice.utility.ItemTypeMatchSelector;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.CreateTreeData;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationFilterState;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.IntegrationObjectPresentation;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAccessRights;
import de.hybris.platform.integrationbackoffice.widgets.modals.builders.AuditReportBuilder;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.CreateIntegrationObjectModalData;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.CreateVirtualAttributeModalData;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.SelectedClassificationAttributesData;
import de.hybris.platform.integrationbackoffice.widgets.providers.ClassificationAttributeQualifierProvider;
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.lang.Strings;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;

/**
 * Controls the functionality of the editor
 */
public final class IntegrationObjectEditorContainerController extends DefaultWidgetController
{
	private static final Logger LOG = Log.getLogger(IntegrationObjectEditorContainerController.class);

	@WireVariable
	private transient CockpitEventQueue cockpitEventQueue;
	@WireVariable
	private transient EditorAccessRights editorAccessRights;
	@WireVariable
	private transient AuditReportBuilder auditReportBuilder;
	@WireVariable
	private transient ClassificationAttributeQualifierProvider classificationAttributeQualifierProvider;
	@Resource
	private transient ItemTypeMatchSelector itemTypeMatchSelector;
	@WireVariable
	private transient IntegrationObjectPresentation editorPresentation;

	@Override
	public void initialize(final Component component)
	{
		super.initialize(component);
		editorPresentation.setEditModeFlag(editorAccessRights.isUserAdmin());
		editorPresentation.resetState();
	}

	public void setDefaultAuditReportBuilder(final AuditReportBuilder auditReportBuilder)
	{
		this.auditReportBuilder = auditReportBuilder;
	}

	@Required
	public void setEditorPresentation(final IntegrationObjectPresentation editorPresentation)
	{
		this.editorPresentation = editorPresentation;
	}

	public void setEditorAccessRights(final EditorAccessRights editorAccessRights)
	{
		this.editorAccessRights = editorAccessRights;
	}

	@SocketEvent(socketId = "receiveIntegrationObjectComboBox")
	public void loadIntegrationObject(final IntegrationObjectModel integrationObject)
	{
		editorPresentation.setSelectedIntegrationObject(integrationObject);
		sendOutput(LOAD_IO_OUTPUT_SOCKET, "");
		editorPresentation.setModified(false);
	}

	@SocketEvent(socketId = "createIntegrationObjectEvent")
	public void createNewIntegrationObject(final CreateIntegrationObjectModalData data)
	{
		editorPresentation.setModified(false);
		editorPresentation.setAttributeDuplicationMap(new HashMap<>());
		editorPresentation.setFilterState(IntegrationFilterState.SHOW_ALL);
		editorPresentation.setSubtypeDataSet(new HashSet<>());
		sendOutput(FILTER_STATE_CHANGE_OUTPUT_SOCKET, IntegrationFilterState.SHOW_ALL);

		CreateTreeData createTreeData = new CreateTreeData(data.getComposedTypeModel(), Collections.emptyMap());
		sendOutput(CREATE_TREE_OUTPUT_SOCKET, createTreeData);
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = createIntegrationObject(data);

		if (!("").equals(validateHasKey(trimmedMap)))
		{
			editorPresentation.getNotificationService()
			                  .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					                  NotificationEvent.Level.WARNING,
					                  getLabel("integrationbackoffice.editMode.warning.msg.serviceCreatedNeedsFurtherConfig"));
		}
		else
		{
			editorPresentation.getNotificationService()
			                  .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					                  NotificationEvent.Level.SUCCESS,
					                  getLabel("integrationbackoffice.editMode.info.msg.serviceCreated"));
		}
	}

	@SocketEvent(socketId = "saveEvent")
	public void updateIntegrationObject(final String message)
	{
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedAttributesMap = trimMap(editorPresentation, false);
		if (validation(trimmedAttributesMap))
		{

			IntegrationObjectModel selectedIO = editorPresentation.getSelectedIntegrationObject();
			editorPresentation.setItemTypeMatchMap(getItemTypeMatchForIntegrationObjectItem(selectedIO));
			final IntegrationObjectModel ioModel = editorPresentation.getWriteService()
			                                                         .createDefinitions(selectedIO,
					                                                         trimmedAttributesMap,
					                                                         selectedIO.getRootItem()
					                                                                   .getCode());
			setItemTypeMatchForIntegrationObject(ioModel);
			editorPresentation.getItemTypeMatchMap().clear();

			if (editorPresentation.hasRoot())
			{
				persistenceSetup(ioModel);
			}
			else
			{
				Messagebox.show(getLabel("integrationbackoffice.editMode.warning.msg.saveConfirmation"),
						getLabel("integrationbackoffice.editMode.warning.title.saveConfirmation"), new Messagebox.Button[]
								{ Messagebox.Button.OK, Messagebox.Button.CANCEL }, new String[]
								{ getLabel("integrationbackoffice.editMode.button.saveDefinition") }, null, null, clickEvent -> {
							if (clickEvent.getButton() == Messagebox.Button.OK)
							{
								persistenceSetup(ioModel);
								editorPresentation.setModified(true);
							}
						});
			}
		}
	}

	@SocketEvent(socketId = "refreshEvent")
	public void refreshButtonOnClick(final String message)
	{
		if (editorPresentation.getSelectedIntegrationObject() != null)
		{
			loadIntegrationObject(editorPresentation.getSelectedIntegrationObject());
			editorPresentation.setModified(false);
			editorPresentation.getAttributeDuplicationMap().clear();
		}
	}

	@SocketEvent(socketId = "receiveDelete")
	public void deleteActionOnPerform()
	{
		if (editorPresentation.getSelectedIntegrationObject() != null)
		{
			Messagebox.show(
					getLabel("integrationbackoffice.editMode.info.msg.deleteConfirmation",
							Arrays.array(editorPresentation.getSelectedIntegrationObject().getCode())),
					getLabel("integrationbackoffice.editMode.info.title.deleteConfirmation"), new Messagebox.Button[]
							{ Messagebox.Button.OK, Messagebox.Button.CANCEL }, null, null, null, clickEvent -> {
						if (clickEvent.getButton() == Messagebox.Button.OK)
						{							
							processIntegrationObjectDeletionAction();
						}
					});
		}
		else
		{
			showNoServiceSelectedMessage();
		}
	}

	@SocketEvent(socketId = "metadataModalEvent")
	public void metaDataModelRequestHandler(final String message)
	{
		sendCurrentIntegrationObject("openMetadataViewer");
	}

	@SocketEvent(socketId = "openItemTypeIOIModalEvent")
	public void itemTypeIOIModalRequestHandler(final String message)
	{
		sendCurrentIntegrationObject(OPEN_ITEM_TYPE_MODAL_OUTPUT_SOCKET);
	}

	@SocketEvent(socketId = "openVirtualAttributeModelEvent")
	public void virtualAttributeModalRequestHandler(final String message)
	{
		if (editorPresentation.isValidIOSelected())
		{
			sendOutput("openVirtualAttributeModelEvent",
					editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType()));
		}
		else
		{
			showNoServiceSelectedMessage();
		}
	}

	@SocketEvent(socketId = "auditReportEvent")
	public void downloadIntegrationObjectReport()
	{
		if (editorPresentation.isValidIOSelected())
		{
			final Map<String, InputStream> auditReportMapRes = auditReportBuilder.generateAuditReport(
					editorPresentation.getSelectedIntegrationObject());
			if (!auditReportMapRes.isEmpty())
			{
				LOG.info("Audit Report has been Created Successfully!");
				auditReportBuilder.downloadAuditReport(auditReportMapRes);
			}
			else
			{
				LOG.info("Audit Report Creation Failed!");
			}
		}
		else
		{
			showNoServiceSelectedMessage();
		}
	}

	@SocketEvent(socketId = "receiveClone")
	public void cloneActionOnPerform()
	{
		if (editorPresentation.isValidIOSelected())
		{
			if (!editorPresentation.isModified())
			{
				sendOutput(OPEN_CLONE_MODAL_OUTPUT_SOCKET, editorPresentation.getSelectedIntegrationObject());
			}
			else
			{
				editorPresentation.getNotificationService()
				                  .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
						                  NotificationEvent.Level.WARNING,
						                  getLabel("integrationbackoffice.editMode.warning.msg.saveBeforeCloning"));
			}
		}
		else
		{
			showNoServiceSelectedMessage();
		}
	}

	@SocketEvent(socketId = "saveButtonItemTypeMatch")
	public void itemTypeMatchSaveHandler(final Collection<IntegrationObjectItemModel> integrationObjectItemModels)
	{
		LOG.debug("The number of items to be saved {}", integrationObjectItemModels.size());
		editorPresentation.getWriteService().persistIntegrationObjectItems(integrationObjectItemModels);
		cockpitEventQueue
				.publishEvent(
						new DefaultCockpitEvent(ObjectFacade.OBJECTS_UPDATED_EVENT,
								editorPresentation.getSelectedIntegrationObject(),
								null));
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.SUCCESS, getLabel("integrationbackoffice.itemTypeMatchIOIModal.msg.save"));
	}

	@SocketEvent(socketId = "cloneIntegrationObjectEvent")
	public void cloneIntegrationObject(final CreateIntegrationObjectModalData data)
	{
		final NotificationService notificationService = editorPresentation.getNotificationService();

		if (data.getComposedTypeModel() != null)
		{
			filterStateChange(IntegrationFilterState.SHOW_ALL);
			sendOutput(FILTER_STATE_CHANGE_OUTPUT_SOCKET, IntegrationFilterState.SHOW_ALL);
			editorPresentation.setModified(false);

			editorPresentation.setItemTypeMatchMap(
					getItemTypeMatchForIntegrationObjectItem(editorPresentation.getSelectedIntegrationObject()));
			final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = createIntegrationObject(data);
			if (!("").equals(validateHasKey(trimmedMap)))
			{
				notificationService.notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
						NotificationEvent.Level.WARNING,
						getLabel("integrationbackoffice.editMode.warning.msg.serviceClonedNeedsFurtherConfig"));
			}
			else
			{
				notificationService.notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
						NotificationEvent.Level.SUCCESS, getLabel("integrationbackoffice.editMode.info.msg.serviceCloned"));
			}
		}
		else
		{
			notificationService.notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					NotificationEvent.Level.WARNING,
					getLabel("integrationbackoffice.editMode.warning.msg.integrationContextLost"));
		}
	}

	@SocketEvent(socketId = "filterStateChangeInput")
	public void filterStateChange(final IntegrationFilterState state)
	{
		editorPresentation.setFilterState(state);
		final IntegrationObjectItemModel root = editorPresentation.getSelectedIntegrationObject().getRootItem();
		if (root != null)
		{
			final CreateTreeData createTreeData;

			if (editorPresentation.isEditModeFlag())
			{
				createTreeData = new CreateTreeData(root.getType(), trimMap(editorPresentation, false));
			}
			else
			{
				try
				{
					createTreeData = new CreateTreeData(root.getType(),
							convertIntegrationObjectToDTOMap(editorPresentation.getReadService(),
									editorPresentation.getSelectedIntegrationObject()));
				}
				catch (ListItemDTOMissingDescriptorModelException e){
					LOG.error(e.getMessage());
					showMissingTypeMessage(e.getMessage());
					return;
				}
			}

			sendOutput(CREATE_TREE_OUTPUT_SOCKET, createTreeData);
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

	@SocketEvent(socketId = "addClassificationAttributesEvent")
	public void addClassificationAttributes(final SelectedClassificationAttributesData selectedClassificationAttributesData)
	{
		if (!editorPresentation.getReadService().isProductType(editorPresentation.getSelectedComposedType().getCode()))
		{
			editorPresentation.getNotificationService().notifyUser(IntegrationbackofficeConstants.EXTENSIONNAME,
					IntegrationbackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.WARNING,
					getLabel("integrationbackoffice.editMode.warning.msg.cannotAddClassificationAttributeToNotProductType"));
			return;
		}

		boolean attributeAlreadyPresent = false;
		final boolean useFullClassificationQualifier = selectedClassificationAttributesData.isUseFullQualifier();
		for (final ClassAttributeAssignmentModel assignment : selectedClassificationAttributesData.getAssignments())
		{
			if (!isClassificationAttributePresent(assignment,
					editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType())))
			{
				final String alias = useFullClassificationQualifier ? classificationAttributeQualifierProvider.provide(
						assignment) : "";
				final ListItemClassificationAttributeDTO dto = new ListItemClassificationAttributeDTO(true, false, false,
						assignment, alias);
				editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType()).add(dto);
				if (ClassificationAttributeTypeEnum.REFERENCE.equals(assignment.getAttributeType()))
				{
					sendOutput(CREATE_DYNAMIC_TREE_NODE_OUTPUT_SOCKET, dto);
				}
			}
			else if (!assignment.getMandatory())
			{
				attributeAlreadyPresent = true;
			}
		}
		if (attributeAlreadyPresent)
		{
			editorPresentation.getNotificationService().notifyUser(IntegrationbackofficeConstants.EXTENSIONNAME,
					IntegrationbackofficeConstants.NOTIFICATION_TYPE, NotificationEvent.Level.WARNING,
					getLabel("integrationbackoffice.editMode.warning.msg.attributeAlreadyPresent"));
		}

		final Map<ComposedTypeModel, Map<String, List<AbstractListItemDTO>>> duplicatesMap = compileDuplicationMap(
				editorPresentation.getSelectedComposedType(),
				editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType()),
				editorPresentation.getAttributeDuplicationMap());

		editorPresentation.setAttributeDuplicationMap(duplicatesMap);
		sendOutput(SELECTED_ITEM_OUTPUT_SOCKET, editorPresentation.getSelectedComposedType());
		sendOutput(ENABLE_SAVE_BUTTON_OUTPUT_SOCKET, true);
	}

	@SocketEvent(socketId = "addVirtualAttributeEvent")
	public void addVirtualAttribute(CreateVirtualAttributeModalData data)
	{
		final IntegrationObjectVirtualAttributeDescriptorModel descriptorModel;
		if (data.getIsDescriptorPersisted())
		{
			List<IntegrationObjectVirtualAttributeDescriptorModel> descriptorModelList =
					editorPresentation.getReadService().getVirtualAttributeDescriptorModelsByCode(data.getVadmCode());
			if (descriptorModelList.isEmpty())
			{
				showNoVirtualAttributeDescriptorMessage();
				return;
			}
			descriptorModel = descriptorModelList.get(0);
		}
		else
		{
			descriptorModel = editorPresentation.getWriteService()
			                                    .persistVirtualAttributeDescriptor(data.getVadmCode(), data.getScriptLocation(),
					                                    data.getType());
			editorPresentation.getNotificationService()
			                  .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
					                  NotificationEvent.Level.SUCCESS,
					                  getLabel("integrationbackoffice.virtualAttributeDescriptorCreate.info.msg.VADMCreated"));
		}
		final ListItemVirtualAttributeDTO dto = new ListItemVirtualAttributeDTO(true, false, false,
				descriptorModel, data.getAlias());
		editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType()).add(dto);
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.SUCCESS,
				getLabel("integrationbackoffice.virtualAttributeDescriptorCreate.info.msg.VAAddedToListbox",
						Arrays.array(data.getAlias())));

		editorPresentation.setAttributeDuplicationMap(compileDuplicationMap(
				editorPresentation.getSelectedComposedType(),
				editorPresentation.getCurrentAttributesMap().get(editorPresentation.getSelectedComposedType()),
				editorPresentation.getAttributeDuplicationMap()));

		sendOutput(SELECTED_ITEM_OUTPUT_SOCKET, editorPresentation.getSelectedComposedType());
		sendOutput(ENABLE_SAVE_BUTTON_OUTPUT_SOCKET, true);
	}

	private void sendCurrentIntegrationObject(final String socketID)
	{
		if (editorPresentation.isValidIOSelected())
		{
			final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = trimMap(editorPresentation, false);
			if (!("").equals(validateHasKey(trimmedMap)))
			{
				showObjectLoadedFurtherConfigurationMessage();
			}
			else
			{
				sendOutput(socketID, editorPresentation.getSelectedIntegrationObject());
			}
		}
		else
		{
			showNoServiceSelectedMessage();
		}
	}

	private void persistenceSetup(final IntegrationObjectModel ioModel)
	{
		persistIntegrationObject(ioModel);
		editorPresentation.setModified(false);
		sendOutput(ENABLE_SAVE_BUTTON_OUTPUT_SOCKET, false);
	}

	private void persistIntegrationObject(final IntegrationObjectModel ioModel)
	{
		editorPresentation.getWriteService().persistIntegrationObject(ioModel);
		cockpitEventQueue
				.publishEvent(
						new DefaultCockpitEvent(ObjectFacade.OBJECTS_UPDATED_EVENT,
								editorPresentation.getSelectedIntegrationObject(),
								null));
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.SUCCESS, getLabel("integrationbackoffice.editMode.info.msg.save"));
	}

	private Map<ComposedTypeModel, List<AbstractListItemDTO>> createIntegrationObject(final CreateIntegrationObjectModalData data)
	{
		final IntegrationObjectModel selectedIO = editorPresentation.getWriteService()
		                                                            .createIntegrationObject(data.getName(), data.getType());
		editorPresentation.setSelectedIntegrationObject(selectedIO);
		final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedMap = trimMap(editorPresentation, false);
		final IntegrationObjectModel ioModel = editorPresentation.getWriteService()
		                                                         .createDefinitions(
				                                                         editorPresentation.getSelectedIntegrationObject(),
				                                                         trimmedMap, data.getComposedTypeModel().getCode());
		setItemTypeMatchForIntegrationObject(ioModel);
		editorPresentation.getWriteService().persistIntegrationObject(ioModel);
		cockpitEventQueue.publishEvent(
				new DefaultCockpitEvent(ObjectFacade.OBJECT_CREATED_EVENT, editorPresentation.getSelectedIntegrationObject(),
						null));
		return trimmedMap;
	}

	private IntegrationObjectModel setItemTypeMatchForIntegrationObject(final IntegrationObjectModel integrationObjectModel)
	{
		integrationObjectModel.getItems().forEach(this::setItemTypeMatchForIntegrationObjectItem);
		return integrationObjectModel;
	}

	private void setItemTypeMatchForIntegrationObjectItem(final IntegrationObjectItemModel item)
	{
		if (item.getItemTypeMatch() == null)
		{
			if (editorPresentation.getItemTypeMatchMap().containsKey(item.getType()))
			{
				item.setItemTypeMatch(editorPresentation.getItemTypeMatchMap().get(item.getType()));
			}
			else
			{
				item.setItemTypeMatch(getDefaultItemTypeMatchEnum(item));
			}
		}
	}

	private Map<ComposedTypeModel, ItemTypeMatchEnum> getItemTypeMatchForIntegrationObjectItem(
			final IntegrationObjectModel integrationObjectModel)
	{
		return integrationObjectModel.getItems().stream()
		                             .filter(it -> it.getItemTypeMatch() != null)
		                             .collect(Collectors.toMap(IntegrationObjectItemModel::getType,
				                             IntegrationObjectItemModel::getItemTypeMatch));
	}

	private ItemTypeMatchEnum getDefaultItemTypeMatchEnum(final IntegrationObjectItemModel integrationObjectItemModel)
	{
		return ItemTypeMatchEnum.valueOf(itemTypeMatchSelector.getToSelectItemTypeMatch(integrationObjectItemModel).name());
	}

	private boolean validation(final Map<ComposedTypeModel, List<AbstractListItemDTO>> trimmedAttributesMap)
	{
		final String VALIDATION_MESSAGE_TITLE = getLabel("integrationbackoffice.editMode.error.title.validation");
		final String VALIDATION_DUPLICATION_TITLE = getLabel("integrationbackoffice.editMode.error.title.duplicationValidation");
		String validationError;
		validationError = validateDefinitions(trimmedAttributesMap);
		if (!("").equals(validationError))
		{
			Messagebox.show(
					getLabel("integrationbackoffice.editMode.error.msg.definitionValidation", Arrays.array(validationError)),
					VALIDATION_MESSAGE_TITLE, 1, Messagebox.ERROR);
			return false;
		}
		validationError = validateHasKey(trimmedAttributesMap);
		if (!("").equals(validationError))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.uniqueValidation", Arrays.array(validationError)),
					VALIDATION_MESSAGE_TITLE, 1, Messagebox.ERROR);
			return false;
		}
		validationError = validateHasNoDuplicateAttributeNames(editorPresentation.getAttributeDuplicationMap());
		if (!("").equals(validationError))
		{
			Messagebox.show(
					getLabel("integrationbackoffice.editMode.error.msg.duplicationValidation", Arrays.array(validationError)),
					VALIDATION_DUPLICATION_TITLE, 1, Messagebox.ERROR);
			return false;
		}

		return true;
	}

	private void showNoServiceSelectedMessage()
	{
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.WARNING, getLabel("integrationbackoffice.editMode.warning.msg.noServiceSelected"));
	}

	private void showObjectLoadedFurtherConfigurationMessage()
	{
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.WARNING,
				getLabel("integrationbackoffice.editMode.warning.msg.serviceLoadedNeedsFurtherConfig"));
	}

	private void showNoVirtualAttributeDescriptorMessage()
	{
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.WARNING,
				getLabel("integrationbackoffice.virtualAttributeDescriptorCreate.error.msg.noDescriptorFound"));
	}

	private void showMissingTypeMessage(String sourceMessage)
	{
		editorPresentation.getNotificationService().notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
				NotificationEvent.Level.WARNING,
				getLabel("integrationbackoffice.editMode.error.msg.missingType", Arrays.array(sourceMessage)));
	}

	private void processIntegrationObjectDeletionAction()
	{
		try
		{
			editorPresentation.getWriteService()
							  .deleteIntegrationObject(editorPresentation.getSelectedIntegrationObject());
			editorPresentation.getNotificationService()
					          .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
										NotificationEvent.Level.SUCCESS,
										getLabel("integrationbackoffice.editMode.info.msg.delete"));
			cockpitEventQueue.publishEvent(
					new DefaultCockpitEvent(ObjectFacade.OBJECTS_DELETED_EVENT,
											editorPresentation.getSelectedIntegrationObject(), null));
		}
		catch(final ModelRemovalException e)
		{
			editorPresentation.getNotificationService()
							  .notifyUser(Strings.EMPTY, IntegrationbackofficeConstants.NOTIFICATION_TYPE,
										NotificationEvent.Level.FAILURE,
										getLabel("integrationbackoffice.editMode.info.msg.deleteFailMessage",
										Arrays.array(editorPresentation.getSelectedIntegrationObject().getCode())));
		}
	}
}
