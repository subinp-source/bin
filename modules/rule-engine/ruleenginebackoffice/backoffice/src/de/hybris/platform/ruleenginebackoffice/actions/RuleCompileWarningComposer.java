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
package de.hybris.platform.ruleenginebackoffice.actions;

import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;


/**
 * The class is responsible for handling of warning dialog box for Rule Compile actons.
 */
public class RuleCompileWarningComposer extends ViewAnnotationAwareComposer
{
	private Window window;
	private Label warnLabel;
	private String message;
	private Button okBtn;

	@ViewEvent(eventName = Events.ON_CREATE)
	public void onCreate(@SuppressWarnings("unused") final CreateEvent event)
	{
		getWarnLabel().setValue(getMessage());
	}

	@ViewEvent(componentID = "okBtn", eventName = Events.ON_CLICK)
	public void perform()
	{
		getWindow().detach();
	}


	protected Label getWarnLabel()
	{
		return warnLabel;
	}

	protected Window getWindow()
	{
		return window;
	}

	protected Button getOkBtn()
	{
		return okBtn;
	}

	protected String getMessage()
	{
		return message;
	}
}
