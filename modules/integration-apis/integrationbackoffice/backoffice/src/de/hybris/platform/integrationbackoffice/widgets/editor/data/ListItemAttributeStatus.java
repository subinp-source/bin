/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.data;

import de.hybris.platform.integrationbackoffice.widgets.editor.utility.EditorConstants;

import org.zkoss.util.resource.Labels;

public enum ListItemAttributeStatus
{
	REQUIRED(Labels.getLabel("integrationbackoffice.editMode.status.text.required"),
			EditorConstants.CSS_STATUS_BADGE + " " + EditorConstants.CSS_STATUS_BADGE_GREY),
	NOT_SUPPORTED(Labels.getLabel("integrationbackoffice.editMode.status.text.notSupported"),
			EditorConstants.CSS_STATUS_BADGE + " " + EditorConstants.CSS_STATUS_BADGE_GREY),
	READ_ONLY(Labels.getLabel("integrationbackoffice.editMode.status.text.readOnly"),
			EditorConstants.CSS_STATUS_BADGE + " " + EditorConstants.CSS_STATUS_BADGE_BLUE),
	CLASSIFICATION(Labels.getLabel("integrationbackoffice.editMode.status.text.classification"),
			EditorConstants.CSS_STATUS_BADGE + " " + EditorConstants.CSS_STATUS_BADGE_BLUE),
	NONE("", ""),
	VIRTUAL(Labels.getLabel("integrationbackoffice.editMode.status.text.virtualAttribute"),
			EditorConstants.CSS_STATUS_BADGE + " " + EditorConstants.CSS_STATUS_BADGE_BLUE);

	private final String label;
	private final String styling;

	ListItemAttributeStatus(final String label, final String styling)
	{
		this.label = label;
		this.styling = styling;
	}

	public String getLabel()
	{
		return label;
	}

	public String getStyling()
	{
		return styling;
	}
}