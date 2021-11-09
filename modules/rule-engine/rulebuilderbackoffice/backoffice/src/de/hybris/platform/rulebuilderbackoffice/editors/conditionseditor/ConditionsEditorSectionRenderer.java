/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.rulebuilderbackoffice.editors.conditionseditor;

import de.hybris.platform.rulebuilderbackoffice.editors.AbstractEditorSectionRenderer;


public class ConditionsEditorSectionRenderer extends AbstractEditorSectionRenderer
{
	protected static final String EDITOR_ID = "conditionsEditor";
	protected static final String ATTRIBUTE = "conditions";

	@Override
	public String getEditorId()
	{
		return EDITOR_ID;
	}

	@Override
	protected String getAttribute()
	{
		return ATTRIBUTE;
	}
}
