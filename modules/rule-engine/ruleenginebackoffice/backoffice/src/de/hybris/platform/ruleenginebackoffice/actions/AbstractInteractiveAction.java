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

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.WidgetUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import javax.annotation.Resource;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Collections.emptyMap;


/**
 * AbstractInteractiveAction is the abstract class for the common functionality interactive actions.
 */
public abstract class AbstractInteractiveAction<I, O> extends AbstractComponentWidgetAdapterAware implements
		InteractiveAction<I, O>
{
	protected static final String DEFAULT_ALERT_DIALOG_WIDTH = "600px";
	protected static final String DEFAULT_ALERT_DIALOG_TEMPLATE_URL = "/cng/ruleengine/multiLineMessageBox.zul";
	protected static final String DEFAULT_ALERT_DIALOG_OK = "rule.action.alert.ok";

	@Resource
	private WidgetUtils widgetUtils;

	protected abstract String getDialogTemplate(final ActionContext<I> context);

	protected abstract String getDialogTitle(final ActionContext<I> context);

	/**
	 * Override the method in subclasses to supplement dialog window with attributes
	 *
	 * @param context
	 *           action context
	 * @param window
	 *           window to supplement attributes
	 */
	protected void addDialogWindowAttribute(final ActionContext<I> context, final Window window)
	{
		// empty
	}

	@Override
	public boolean canPerform(final ActionContext<I> context)
	{
		return context.getData() != null;
	}

	@Override
	public ActionResult<O> perform(final ActionContext<I> context)
	{
		final Window dialogWindow = new Window(context.getLabel(getDialogTitle(context)), "normal", true);
		Executions.createComponents(getDialogTemplate(context), dialogWindow, getArguments(context));

		addDialogWindowAttribute(context, dialogWindow);
		dialogWindow.setAttribute("context", context);
		dialogWindow.setAttribute("window", dialogWindow);
		dialogWindow.setAttribute("interactiveAction", this);

		dialogWindow.setWidth(getDialogWidth());
		dialogWindow.setHeight(getDialogHeight());
		dialogWindow.setTop(getDialogTop());
		dialogWindow.setParent(getRoot());
		dialogWindow.setVisible(true);
		dialogWindow.doHighlighted();

		return new ActionResult(ActionResult.SUCCESS);
	}

	@Override
	public void showAlertDialog(final ActionContext<I> context, final String title, final String message)
	{
		final Map<String, String> params = newHashMap();
		params.put("width", getAlertDialogWidth());
		final String origTemplate = Messagebox.getTemplate();
		try
		{
			Messagebox.setTemplate(getAlertDialogTemplate());
			Messagebox.show(context.getLabel(message), context.getLabel(title), new Messagebox.Button[]
			{ Messagebox.Button.OK }, new String[]
			{ context.getLabel(DEFAULT_ALERT_DIALOG_OK) }, Messagebox.EXCLAMATION, Messagebox.Button.CANCEL, this::doOnAlertOkClick,
					params);
		}
		finally
		{
			Messagebox.setTemplate(origTemplate);
		}
	}

	protected String getAlertDialogTemplate()
	{
		return DEFAULT_ALERT_DIALOG_TEMPLATE_URL;
	}

	protected String getAlertDialogWidth()
	{
		return DEFAULT_ALERT_DIALOG_WIDTH;
	}

	@Override
	public void sendOutputDataToSocket(final String socketId, final Object data)
	{
		sendOutput(socketId, data);
	}

	/**
	 * Override the method in descent classes to process onOk event of alert dialog if needed
	 *
	 * @param clickEvent
	 *           message box click event
	 */
	protected void doOnAlertOkClick(final Messagebox.ClickEvent clickEvent)
	{
		// empty
	}

	protected String getDialogWidth()
	{
		return "500px";
	}

	protected String getDialogHeight()
	{
		return "300px";
	}

	protected String getDialogTop()
	{
		return "100px";
	}

	protected Component getRoot()
	{
		return widgetUtils.getRoot();
	}

	protected Map<String, Object> getArguments(final ActionContext<I> context)
	{
		return emptyMap();
	}
}
