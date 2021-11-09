/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;

import com.hybris.cockpitng.annotations.SocketEvent;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAttributesFilteringService;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.CreateIntegrationObjectModalData;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ModalUtils;
import de.hybris.platform.odata2webservices.enums.IntegrationType;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public final class CreateIntegrationObjectModalController extends DefaultWidgetController
{

	@WireVariable
	private transient ReadService readService;
	@WireVariable
	private transient EditorAttributesFilteringService editorAttrFilterService;

	private Textbox integrationObjectName;
	private Combobox rootTypeComboBox;
	private Combobox integrationTypeComboBox;

	public void setReadService(final ReadService readService)
	{
		this.readService = readService;
	}

	@SocketEvent(socketId = "showModal")
	public void loadCreateIntegrationObjectModal(String message)
	{
		loadComposedTypes();
		loadIntegrationTypes();
		rootTypeComboBox.setAutocomplete(false);
		rootTypeComboBox.setAutodrop(true);
		integrationTypeComboBox.setAutocomplete(false);
		integrationTypeComboBox.setAutodrop(true);
	}

	@ViewEvent(componentID = "createButton", eventName = Events.ON_CLICK)
	public void createIntegrationObject()
	{
		final String serviceName = integrationObjectName.getValue();
		if (("").equals(serviceName))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.emptyService"),
					getLabel("integrationbackoffice.editMode.error.title.emptyService"), 1, Messagebox.ERROR);
		}
		else if (!ModalUtils.isAlphaNumericName(serviceName))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.invalidService"),
					getLabel("integrationbackoffice.editMode.error.title.invalidService"), 1, Messagebox.ERROR);
		}
		else if (!ModalUtils.isServiceNameUnique(serviceName, readService))
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.serviceExists"),
					getLabel("integrationbackoffice.editMode.error.title.serviceExists"), 1, Messagebox.ERROR);
		}
		else if (rootTypeComboBox.getSelectedItem() == null)
		{
			Messagebox.show(getLabel("integrationbackoffice.editMode.error.msg.invalidRootType"),
					getLabel("integrationbackoffice.editMode.error.title.invalidRootType"), 1, Messagebox.ERROR);
		}
		else
		{
			final ComposedTypeModel root = rootTypeComboBox.getSelectedItem().getValue();
			final IntegrationType type;

			if (integrationTypeComboBox.getSelectedItem() != null)
			{
				type = integrationTypeComboBox.getSelectedItem().getValue();
			}
			else
			{
				type = IntegrationType.INBOUND; //Defaults to INBOUND
			}
			sendOutput("createButtonClick", new CreateIntegrationObjectModalData(serviceName, root, type));
		}
	}

	private void loadComposedTypes()
	{
		final List<ComposedTypeModel> unsortedComposedTypeModels = readService.getAvailableTypes();
		final List<ComposedTypeModel> sortedComposedTypeModels = sortComposedTypeModels(unsortedComposedTypeModels);
		sortedComposedTypeModels.forEach(type -> {
			if (!editorAttrFilterService.filterAttributesForAttributesMap(type).isEmpty())
			{
				rootTypeComboBox.appendChild(createComboItem(type.getCode(), type));
			}
		});
	}

	private void loadIntegrationTypes()
	{
		final List<IntegrationType> unsortedIntegrationTypes = readService.getIntegrationTypes();
		final List<IntegrationType> sortedIntegrationTypes = sortIntegrationTypes(unsortedIntegrationTypes);
		sortedIntegrationTypes.forEach((type -> integrationTypeComboBox
				.appendChild(createComboItem(type.getCode(), type))));
	}

	private List<ComposedTypeModel> sortComposedTypeModels(List<ComposedTypeModel> unsortedComposedTypeModels)
	{
		return unsortedComposedTypeModels.stream()
		                                 .sorted(Comparator.comparing(TypeModel::getCode))
		                                 .collect(Collectors.toList());
	}

	private List<IntegrationType> sortIntegrationTypes(List<IntegrationType> unsortedIntegrationTypes)
	{
		return unsortedIntegrationTypes.stream()
		                               .sorted(Comparator.comparing(IntegrationType::getCode))
		                               .collect(Collectors.toList());
	}

	private List<ComposedTypeModel> filterComposedTypeModels(String typeEnteredString,
	                                                         List<ComposedTypeModel> unfilteredComposedTypeModels)
	{
		return unfilteredComposedTypeModels
				.stream()
				.filter(p -> p.getCode().startsWith(typeEnteredString)).collect(Collectors.toList());
	}

	private List<IntegrationType> filterIntegrationTypes(String typeEnteredString,
	                                                     List<IntegrationType> unfilteredIntegrationTypes)
	{
		return unfilteredIntegrationTypes
				.stream()
				.filter(p -> p.getCode().startsWith(typeEnteredString)).collect(Collectors.toList());
	}

	@ViewEvent(componentID = "rootTypeComboBox", eventName = Events.ON_BLUR)
	public void onRootTypeComboBoxBlur()
	{
		if (!rootTypeComboBox.getItems().isEmpty())
		{
			rootTypeComboBox.setSelectedIndex(0);
		}
	}

	@ViewEvent(componentID = "integrationTypeComboBox", eventName = Events.ON_BLUR)
	public void onComposedTypeComboBoxBlur()
	{
		if (!integrationTypeComboBox.getItems().isEmpty())
		{
			integrationTypeComboBox.setSelectedIndex(0);
		}
	}

	@ViewEvent(componentID = "rootTypeComboBox", eventName = Events.ON_CHANGE)
	public void onRootTypeComboBoxChanging()
	{
		populateRootTypeComboBoxComboBox(rootTypeComboBox.getValue());
	}

	@ViewEvent(componentID = "integrationTypeComboBox", eventName = Events.ON_CHANGE)
	public void onComposedTypeComboBoxChanging()
	{
		populateIntegrationTypeComboBox(integrationTypeComboBox.getValue());
	}

	public void populateRootTypeComboBoxComboBox(String textInput)
	{
		List<ComposedTypeModel> unsortedRootTypes = readService.getAvailableTypes();
		if (!"".equals(textInput))
		{
			unsortedRootTypes = filterComposedTypeModels(textInput, unsortedRootTypes);
		}
		final List<ComposedTypeModel> sortedIntegrationObjectModels = sortComposedTypeModels(unsortedRootTypes);
		rootTypeComboBox.getItems().clear();
		sortedIntegrationObjectModels.forEach(type -> {
			if (!editorAttrFilterService.filterAttributesForAttributesMap(type).isEmpty())
			{
				rootTypeComboBox.appendChild(createComboItem(type.getCode(), type));
			}
		});
	}

	public void populateIntegrationTypeComboBox(String textInput)
	{
		List<IntegrationType> unsortedIntegrationTypes = readService.getIntegrationTypes();
		if (!"".equals(textInput))
		{
			unsortedIntegrationTypes = filterIntegrationTypes(integrationTypeComboBox.getValue(), unsortedIntegrationTypes);
		}
		final List<IntegrationType> sortedComposedTypeModels = sortIntegrationTypes(unsortedIntegrationTypes);
		integrationTypeComboBox.getItems().clear();
		sortedComposedTypeModels.forEach((type -> integrationTypeComboBox
				.appendChild(createComboItem(type.getCode(), type))));
	}

}
