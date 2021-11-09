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
package de.hybris.platform.coupon.backoffice.cockpitng.editor.insets;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;

import de.hybris.platform.coupon.backoffice.cockpitng.editor.defaultinseteditor.DefaultInsetEditor;


/**
 * Abstract class for insets that add single action button
 */
public abstract class AbstractSingleButtonInset<T> implements CockpitEditorRenderer<T>
{
	public static final String BUTTON_LABEL_PARAM = "buttonLabel";

	public static final String INSET_SCLASS = "single-button";
	public static final String BUTTON_SCLASS = "inset-button";

	protected abstract EventListener<Event> getInsetListener(final Component parent, final EditorContext<T> context,
			final EditorListener<T> listener);

	@Override
	public void render(final Component parent, final EditorContext<T> context, final EditorListener<T> listener)
	{
		if (parent != null && context != null && listener != null)
		{
			final Div insetContainer = new Div();
			insetContainer.setSclass(DefaultInsetEditor.GENERAL_INSET_SCLASS + " " + getSclass());
			insetContainer.setParent(parent);

			String insetLabel = "";
			final Object insetLabelObject = context.getParameter(BUTTON_LABEL_PARAM);
			if (insetLabelObject instanceof String)
			{
				insetLabel = context.getLabel((String) insetLabelObject);
			}
			final Button button = new Button();
			button.setLabel(insetLabel);
			button.setSclass(BUTTON_SCLASS);
			button.setParent(insetContainer);
			button.addEventListener(Events.ON_CLICK, getInsetListener(parent, context, listener));
			button.setDisabled(!isEnabled());
		}
	}

	protected boolean isEnabled()
	{
		return true;
	}

	protected String getSclass()
	{
		return INSET_SCLASS;
	}

	protected Editor findAncestorEditor(final Component component)
	{
		Component current = component;
		while (current != null && !(current instanceof Editor) && !(current instanceof Widget))
		{
			current = current.getParent();
		}
		if (current instanceof Editor)
		{
			return (Editor) current;
		}
		return null;
	}
}
