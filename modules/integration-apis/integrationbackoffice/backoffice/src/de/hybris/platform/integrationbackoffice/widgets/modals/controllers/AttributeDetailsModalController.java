/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

import de.hybris.platform.integrationbackoffice.dto.ListItemAttributeDTO;

import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

public class AttributeDetailsModalController extends DefaultWidgetController
{
	private Textbox attributeName;
	private Textbox baseType;
	private Radiogroup partOfRadio;
	private Radiogroup autocreateRadio;
	private Radiogroup optionalRadio;
	private Radiogroup uniqueRadio;


	@SocketEvent(socketId = "showModal")
	public void showDetails(final ListItemAttributeDTO dto)
	{
		attributeName.setValue(dto.getAttributeDescriptor().getQualifier());
		baseType.setValue(dto.getBaseType().getCode());

		final boolean partOf = dto.getAttributeDescriptor().getPartOf();
		final boolean autocreate = dto.getAttributeDescriptor().getAutocreate();
		final boolean optional = dto.getAttributeDescriptor().getOptional();
		final boolean unique = dto.getAttributeDescriptor().getUnique();

		partOfRadio.setSelectedIndex(partOf ? 0 : 1);
		autocreateRadio.setSelectedIndex(autocreate ? 0 : 1);
		optionalRadio.setSelectedIndex(optional ? 0 : 1);
		uniqueRadio.setSelectedIndex(unique ? 0 : 1);

		disableRadioButtons();
	}

	private void disableRadioButtons()
	{
		partOfRadio.getItems().forEach(item -> item.setDisabled(true));
		autocreateRadio.getItems().forEach(item -> item.setDisabled(true));
		optionalRadio.getItems().forEach(item -> item.setDisabled(true));
		uniqueRadio.getItems().forEach(item -> item.setDisabled(true));
	}

}
