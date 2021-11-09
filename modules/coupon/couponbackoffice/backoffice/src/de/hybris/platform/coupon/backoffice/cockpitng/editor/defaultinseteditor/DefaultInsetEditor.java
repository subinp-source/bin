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
package de.hybris.platform.coupon.backoffice.cockpitng.editor.defaultinseteditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Div;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;


/**
 * Editor that helps in customizing existing editors by injecting custom 'inset' logic
 */
public class DefaultInsetEditor extends AbstractCockpitEditorRenderer<Object>
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInsetEditor.class);

	public static final String EDITOR_ID_PARAM = "editor";
	public static final String INSET_BEAN_PARAM = "insetBean";
	public static final String INSERT_MODE_PARAM = "insertMode";
	public static final String INSERT_BEFORE_MODE = "before";
	public static final String INSERT_AFTER_MODE = "after";
	protected static final String IS_NESTED_OBJECT_CREATION_DISABLED_SETTING = "isNestedObjectCreationDisabled";

	public static final String INSET_EDITOR_CONTAINER_SCLASS = "ye-inset-editor";
	public static final String GENERAL_INSET_SCLASS = "inset";


	@Override
	public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
	{
		if (parent != null && context != null && listener != null)
		{
			final Div insetEditorContainer = new Div();

			insetEditorContainer.setSclass(INSET_EDITOR_CONTAINER_SCLASS);
			insetEditorContainer.setParent(parent);

			String insertMode = INSERT_AFTER_MODE;
			final Object insertModeObject = context.getParameter(INSERT_MODE_PARAM);
			if (insertModeObject instanceof String)
			{
				insertMode = (String) insertModeObject;
			}

			if (INSERT_BEFORE_MODE.equals(insertMode))
			{
				renderInset(insetEditorContainer, context, listener);
			}

			renderEditor(insetEditorContainer, context, listener);

			if (!INSERT_BEFORE_MODE.equals(insertMode))
			{
				renderInset(insetEditorContainer, context, listener);
			}
		}
	}

	protected void renderEditor(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
	{
		String editorId = "";
		final Object editorIdObject = context.getParameter(EDITOR_ID_PARAM);
		if (editorIdObject instanceof String)
		{
			editorId = (String) editorIdObject;
		}

		final Object initialValue = context.getInitialValue();
		final Editor subEditor = new Editor();
		subEditor.setReadableLocales(context.getReadableLocales());
		subEditor.setWritableLocales(context.getWritableLocales());
		subEditor.setType(context.getValueType());
		subEditor.setDefaultEditor(editorId);
		subEditor.setReadOnly(!context.isEditable());
		subEditor.setOrdered(context.isOrdered());
		subEditor.setValue(initialValue);
		subEditor.setOptional(context.isOptional());
		subEditor.setWidgetInstanceManager((WidgetInstanceManager) context.getParameter("wim"));
		subEditor.setNestedObjectCreationDisabled(isNestedObjectCreationDisabled(context));
		subEditor.addEventListener(Editor.ON_VALUE_CHANGED, event -> listener.onValueChanged(subEditor.getValue()));
		subEditor.addEventListener(Editor.ON_EDITOR_EVENT, event ->
		{
			final Object eventCode = event.getData();
			if (eventCode instanceof String)
			{
				listener.onEditorEvent((String) eventCode);
			}
		});
		subEditor.addParameters(context.getParameters());
		subEditor.afterCompose();
		parent.appendChild(subEditor);
	}

	protected void renderInset(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
	{
		final Object insetBeanName = context.getParameter(INSET_BEAN_PARAM);
		if (insetBeanName instanceof String)
		{
			try
			{
				final CockpitEditorRenderer<Object> editorInset = BackofficeSpringUtil.getBean((String) insetBeanName,
						CockpitEditorRenderer.class);
				if (editorInset != null)
				{
					editorInset.render(parent, context, new EditorListener<Object>()
					{
						@Override
						public void onValueChanged(final Object value)
						{
							listener.onValueChanged(value);
							findAncestorEditor(parent).reload();
						}

						@Override
						public void onEditorEvent(final String eventCode)
						{
							listener.onEditorEvent(eventCode);
						}

						@Override
						public void sendSocketOutput(final String outputId, final Object data)
						{
							listener.sendSocketOutput(outputId, data);
						}
					});
				}
				else
				{
					LOG.error("Inset bean '{}' could not be found.", insetBeanName);
				}
			}
			catch (final WrongValueException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
		else
		{
			LOG.error("Parameter '{}' is not specified.", INSET_BEAN_PARAM);
		}
	}

	protected boolean isNestedObjectCreationDisabled(final EditorContext<Object> context)
	{
		boolean isDisabled = false;
		final Object paramObject = context.getParameter(IS_NESTED_OBJECT_CREATION_DISABLED_SETTING);
		if (paramObject instanceof String)
		{
			isDisabled = Boolean.parseBoolean((String) paramObject);
		}
		return isDisabled;
	}

	@Override
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
