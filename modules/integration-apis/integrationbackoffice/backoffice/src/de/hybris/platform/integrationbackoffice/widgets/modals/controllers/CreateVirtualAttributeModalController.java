/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;

import de.hybris.platform.integrationbackoffice.dto.AbstractListItemDTO;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorAttributesFilteringService;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.CreateVirtualAttributeModalData;
import de.hybris.platform.integrationbackoffice.widgets.modals.data.VirtualAttributesSupportedTypes;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ModalUtils;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;
import de.hybris.platform.integrationservices.util.AtomicTypeModelRetriever;
import de.hybris.platform.scripting.model.ScriptModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

public final class CreateVirtualAttributeModalController extends DefaultWidgetController
{
	private static final String LOGIC_LOCATION_TEMPLATE = "model://%s";
	private static final String DEFAULT_VADM_TYPE = "java.lang.String";

	@WireVariable
	private transient ReadService readService;
	@WireVariable
	private transient EditorAttributesFilteringService editorAttrFilterService;
	@WireVariable
	private transient AtomicTypeModelRetriever atomicTypeRetriever;

	private Textbox virtualAttributeAlias;
	private Textbox virtualAttributeDescriptorCode;
	private Combobox scriptLocationComboBox;
	private Combobox descriptorTypeComboBox;
	private Combobox virtualAttributeDescriptorComboBox;
	private Button createButton;
	private Button clearButton;

	private transient List<AbstractListItemDTO> currentListboxAttributes;

	public void setReadService(final ReadService readService)
	{
		this.readService = readService;
	}

	@SocketEvent(socketId = "openVirtualAttributeModelEvent")
	public void loadCreateIntegrationObjectModal(final List<AbstractListItemDTO> attributes)
	{
		this.currentListboxAttributes = attributes;
		loadScriptLocation();
		loadDescriptorType();
		loadExistingVADescriptor();

		createButton.setVisible(false);
		clearButton.addEventListener(Events.ON_CLICK, event -> resetVADescriptor());

		scriptLocationComboBox.setAutocomplete(false);
		scriptLocationComboBox.setAutodrop(true);

		virtualAttributeAlias.addEventListener(Events.ON_BLUR, event -> setCreateButtonStatus());
		scriptLocationComboBox.addEventListener(Events.ON_BLUR, event -> setCreateButtonStatus());
		virtualAttributeDescriptorCode.addEventListener(Events.ON_BLUR, event -> setCreateButtonStatus());
	}

	@ViewEvent(componentID = "createButton", eventName = Events.ON_CLICK)
	public void createVirtualAttribute()
	{
		String alias = virtualAttributeAlias.getValue() == null ? "" : virtualAttributeAlias.getValue();
		final String descriptorCode = virtualAttributeDescriptorCode.getValue() == null ? "" : virtualAttributeDescriptorCode.getValue();
		boolean isCodeUsedAsAlias = false;
		if ("".equals(alias))
		{
			isCodeUsedAsAlias = true;
			alias = descriptorCode;
		}
		if (isAliasAndCodeValid(descriptorCode, alias, isCodeUsedAsAlias))
		{
			final String type;
			if (descriptorTypeComboBox.getSelectedItem() != null)
			{
				type = descriptorTypeComboBox.getSelectedItem().getLabel();
			}
			else
			{
				type = VirtualAttributesSupportedTypes.STRING.getLabel(); //Defaults to String
			}

			final String logicLocation = scriptLocationComboBox.getSelectedItem().getLabel();
			final boolean isDescriptorPersisted = virtualAttributeDescriptorComboBox.getSelectedItem() != null;

			sendOutput("createButtonClick",
					new CreateVirtualAttributeModalData(alias, descriptorCode, logicLocation, type, isDescriptorPersisted));
		}
	}

	private boolean isAliasAndCodeValid(final String descriptorCode, final String alias, final boolean isCodeUsedAsAlias)
	{
		if (("").equals(descriptorCode))
		{
			Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.emptyDescriptorCode"),
					getLabel("integrationbackoffice.createVirtualAttribute.error.title.emptyDescriptorCode"), 1,
					Messagebox.ERROR);
			return false;
		}
		else if (!ModalUtils.isAlphaNumericName(descriptorCode))
		{
			Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.invalidDescriptorCode"),
					getLabel("integrationbackoffice.createVirtualAttribute.error.title.invalidDescriptorCode"), 1,
					Messagebox.ERROR);
			return false;
		}
		else if (!ModalUtils.isAlphaNumericName(alias))
		{
			Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.invalidAlias"),
					getLabel("integrationbackoffice.createVirtualAttribute.error.title.invalidAlias"), 1, Messagebox.ERROR);
			return false;
		}
		else if (!ModalUtils.isAliasUnique(alias, descriptorCode, currentListboxAttributes))
		{
			if (!isCodeUsedAsAlias)
			{
				Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.aliasExists"),
						getLabel("integrationbackoffice.createVirtualAttribute.error.title.aliasExists"), 1,
						Messagebox.ERROR);
			}
			else
			{
				Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.codeAsAliasExists"),
						getLabel("integrationbackoffice.createVirtualAttribute.error.title.aliasExists"), 1,
						Messagebox.ERROR);
			}
			return false;

		}
		else if (virtualAttributeDescriptorComboBox.getSelectedItem() == null &&
				!ModalUtils.isVADMCodeUnique(descriptorCode, readService))
		{
			Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.descriptorCodeExists"),
					getLabel("integrationbackoffice.createVirtualAttribute.error.title.descriptorCodeExists"), 1,
					Messagebox.ERROR);
			return false;
		}
		else
		{
			return true;
		}
	}

	private void loadScriptLocation()
	{
		scriptLocationComboBox.getItems().clear();
		final List<ScriptModel> unsortedScriptModels = readService.getScriptModels();
		final List<ScriptModel> sortedComposedTypeModels = sortScriptModels(unsortedScriptModels);
		sortedComposedTypeModels.stream()
		                        .filter(ScriptModel::getActive)
		                        .forEach(
									type -> scriptLocationComboBox.appendChild(
											createComboItem(String.format(LOGIC_LOCATION_TEMPLATE, type.getCode()), type)));
	}

	private void loadDescriptorType()
	{
		descriptorTypeComboBox.getItems().clear();
		List<VirtualAttributesSupportedTypes> enumList = new ArrayList<>(
				Arrays.asList(VirtualAttributesSupportedTypes.values()));
		enumList = sortVirtualAttributesSupportedTypes(enumList);
		enumList.forEach(
				oneEnum -> descriptorTypeComboBox.appendChild(
						createComboItem(oneEnum.getLabel(), oneEnum)));
	}

	private void loadExistingVADescriptor()
	{
		virtualAttributeDescriptorComboBox.getItems().clear();

		final List<IntegrationObjectVirtualAttributeDescriptorModel> virtualAttributeDescriptorModelList = sortVirtualAttributeDescriptors(
				readService.getVirtualAttributeDescriptorModels());
		virtualAttributeDescriptorModelList.forEach(descriptorModel -> virtualAttributeDescriptorComboBox.appendChild(
				createComboItem(descriptorModel.getCode(), descriptorModel)));

		virtualAttributeDescriptorComboBox.addEventListener(Events.ON_SELECT, this::setExistingVADescriptor);
	}

	private void setExistingVADescriptor(final Event event)
	{
		final Comboitem selectedItem = virtualAttributeDescriptorComboBox.getSelectedItem();
		if (selectedItem == null)
		{
			return;
		}

		// set VA descriptor code
		final IntegrationObjectVirtualAttributeDescriptorModel descriptorModel = selectedItem.getValue();
		virtualAttributeDescriptorCode.setValue(descriptorModel.getCode());
		virtualAttributeDescriptorCode.setDisabled(true);

		// set VA script
		final Comboitem scriptItem = getScriptFromScriptCombobox(descriptorModel);
		if (scriptItem != null)
		{
			scriptLocationComboBox.setSelectedItem(scriptItem);
			scriptLocationComboBox.setDisabled(true);
		}
		else
		{
			// notificationService: selected vaDesciptor's script is invalid or removed.
			Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.scriptNotExists"),
					getLabel("integrationbackoffice.createVirtualAttribute.error.title.scriptNotExists"), 1, Messagebox.ERROR);
		}

		// set descriptor type
		if (descriptorModel.getType() == null)
		{
			descriptorTypeComboBox.setSelectedItem(null);
			descriptorTypeComboBox.getItems().forEach(item -> {
				if (item.getLabel().equals(DEFAULT_VADM_TYPE))
				{
					descriptorTypeComboBox.setSelectedItem(item);
				}
			});
			descriptorTypeComboBox.setDisabled(true);
		}
		else
		{
			final Comboitem typeItem = getTypeFromTypeCombobox(descriptorModel);
			if (typeItem != null)
			{
				descriptorTypeComboBox.setSelectedItem(typeItem);
				descriptorTypeComboBox.setDisabled(true);
			}
			else
			{
				// notificationService: the vaDescriptor has a type that is not supported yet.
				Messagebox.show(getLabel("integrationbackoffice.createVirtualAttribute.error.msg.typeNotExists"),
						getLabel("integrationbackoffice.createVirtualAttribute.error.title.typeNotExists"), 1, Messagebox.ERROR);
			}
		}

		setCreateButtonStatus();
	}

	private Comboitem getScriptFromScriptCombobox(final IntegrationObjectVirtualAttributeDescriptorModel descriptorModel)
	{
		for (final var item : scriptLocationComboBox.getItems())
		{
			if (item.getLabel().equals(descriptorModel.getLogicLocation()))
			{
				return item;
			}
		}
		return null;
	}

	private Comboitem getTypeFromTypeCombobox(final IntegrationObjectVirtualAttributeDescriptorModel descriptorModel)
	{
		for (final var item : descriptorTypeComboBox.getItems())
		{
			if (descriptorModel.getType() != null && item.getLabel().equals(descriptorModel.getType().getCode()))
			{
				return item;
			}
		}
		return null;
	}

	private void setCreateButtonStatus()
	{
		if (virtualAttributeDescriptorCode.getValue() != null && !"".equals(virtualAttributeDescriptorCode.getValue().trim()) &&
				scriptLocationComboBox.getSelectedItem() != null)
		{
			createButton.setVisible(true);
			return;
		}
		createButton.setVisible(false);

	}

	private List<ScriptModel> sortScriptModels(final List<ScriptModel> unsortedComposedTypeModels)
	{
		return unsortedComposedTypeModels.stream()
		                                 .sorted(Comparator.comparing(ScriptModel::getCode))
		                                 .collect(Collectors.toList());
	}

	private List<VirtualAttributesSupportedTypes> sortVirtualAttributesSupportedTypes(
			final List<VirtualAttributesSupportedTypes> unsortedList)
	{
		return unsortedList.stream()
		                   .sorted(Comparator.comparing(VirtualAttributesSupportedTypes::getLabel))
		                   .collect(Collectors.toList());
	}


	private List<IntegrationObjectVirtualAttributeDescriptorModel> sortVirtualAttributeDescriptors(
			final List<IntegrationObjectVirtualAttributeDescriptorModel> unsortedList)
	{
		return unsortedList.stream()
		                   .sorted(Comparator.comparing(IntegrationObjectVirtualAttributeDescriptorModel::getCode))
		                   .collect(Collectors.toList());
	}

	private void resetVADescriptor()
	{
		virtualAttributeDescriptorComboBox.setSelectedItem(null);

		virtualAttributeDescriptorCode.setDisabled(false);
		virtualAttributeDescriptorCode.setValue("");

		scriptLocationComboBox.setDisabled(false);
		scriptLocationComboBox.setSelectedItem(null);

		descriptorTypeComboBox.setDisabled(false);
		descriptorTypeComboBox.setSelectedItem(null);

		createButton.setVisible(false);
	}

}
