/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationbackoffice.services.ReadService;
import de.hybris.platform.integrationbackoffice.widgets.editor.data.SubtypeData;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Textbox;

import java.util.ArrayList;
import java.util.List;

import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorBlacklists.getTypesBlackList;
import static de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorUtils.createComboItem;

public class RetypeAttributeModalController extends DefaultWidgetController
{
	@WireVariable
	private transient ReadService readService;

	private Textbox attributeName;
	private Textbox attributeBaseType;
	private Textbox attributeCurrentSubtype;
	private Combobox subtypeComboBox;
	private Button confirmButton;
	private Comboitem selectedItem;
	private transient SubtypeData subtypeData;

	@SocketEvent(socketId = "showModal")
	public void loadRetypeModal(final SubtypeData subtypeData)
	{
		this.subtypeData = subtypeData;
		attributeName.setValue(subtypeData.getAttributeAlias());
		attributeBaseType.setValue(subtypeData.getBaseType().getCode());
		attributeCurrentSubtype.setValue(subtypeData.getSubtype().getCode());
		confirmButton.setDisabled(true);
		populateCombobox(subtypeData);
	}

	private void populateCombobox(final SubtypeData subtypeData)
	{
		subtypeComboBox.getItems().clear();

		final ComposedTypeModel baseType = readService.getComposedTypeModelFromTypeModel(subtypeData.getBaseType());
		final List<ComposedTypeModel> allSubtypes = new ArrayList<>(baseType.getAllSubTypes());

		subtypeComboBox.appendChild(createComboItem(baseType.getCode(), baseType));
		allSubtypes.forEach(type -> {
			if (!getTypesBlackList().contains(type.getCode()))
			{
				subtypeComboBox.appendChild(createComboItem(type.getCode(), type));
			}
		});
	}

	@ViewEvent(componentID = "subtypeComboBox", eventName = Events.ON_CHANGE)
	public void subTypeComboBoxOnChange()
	{
		selectedItem = subtypeComboBox.getSelectedItem();
		confirmButton.setDisabled(selectedItem == null);
	}

	@ViewEvent(componentID = "confirmButton", eventName = Events.ON_CLICK)
	public void updateAttributeType()
	{
		if (selectedItem != null)
		{
			final TypeModel newSubtype = selectedItem.getValue();
			if (newSubtype != null && !newSubtype.getCode().equals(attributeCurrentSubtype.getValue()))
			{
				subtypeData.setSubtype(newSubtype);
			}
			sendOutput("confirmButtonClick", subtypeData);
		}
	}

}