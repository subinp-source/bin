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
package de.hybris.platform.ruleenginebackoffice.widgets.editor.handlers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.impl.DefaultValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaLogicHandler;


/**
 * The {@link SaveDisallowedEditorAreaLogicHandler} doesn't permit to persist changes applied to the underlying data
 * model by firing validation that always fails.
 */
public class SaveDisallowedEditorAreaLogicHandler extends DefaultEditorAreaLogicHandler
{
	private static final String MESSAGE = "rule.module.editorAreaLogicHandler.actions.save.disallowed";

	@Override
	public List<ValidationInfo> performValidation(final WidgetInstanceManager widgetInstanceManager, final Object currentObject,
				 final ValidationContext validationContext)
	{
		final DefaultValidationInfo failedValidation = new DefaultValidationInfo();
		failedValidation.setValidationSeverity(ValidationSeverity.ERROR);
		failedValidation.setValidationMessage(getLocalizedMessage());
		return newArrayList(failedValidation);

	}

	protected String getLocalizedMessage()
	{
		return org.zkoss.xel.fn.CommonFns.getLabel(MESSAGE);
	}

}
