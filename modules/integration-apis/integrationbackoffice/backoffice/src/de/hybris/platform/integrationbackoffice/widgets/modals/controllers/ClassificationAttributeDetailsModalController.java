/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.controllers;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;

import de.hybris.platform.integrationbackoffice.dto.ListItemClassificationAttributeDTO;

import org.zkoss.zul.Textbox;

/**
 * Modal displays details for classification attribute: catalog version, category, attribute name and its type.
 */
public final class ClassificationAttributeDetailsModalController extends DefaultWidgetController
{
	private Textbox catalogVersion;
	private Textbox category;
	private Textbox attributeName;
	private Textbox attributeType;

	@SocketEvent(socketId = "showModal")
	public void showDetails(final ListItemClassificationAttributeDTO dto)
	{
		catalogVersion.setValue(
				String.format("%s - %s", dto.getClassAttributeAssignmentModel().getSystemVersion().getCatalog().getId(),
						dto.getClassAttributeAssignmentModel().getClassificationClass().getCatalogVersion().getVersion()));
		category.setValue(dto.getCategoryCode());
		attributeName.setValue(dto.getClassificationAttributeCode());
		attributeType.setValue(dto.getClassAttributeAssignmentModel().getAttributeType().getCode());
	}
}