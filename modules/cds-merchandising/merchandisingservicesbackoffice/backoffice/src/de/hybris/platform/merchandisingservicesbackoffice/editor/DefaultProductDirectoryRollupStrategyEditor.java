/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.merchandisingservicesbackoffice.editor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;

import de.hybris.platform.merchandisingservicesbackoffice.editor.facade.MerchandisingRollupStrategySearchFacade;

/**
 * Custom editor to display a dynamically generated list of 'MerchandisingMetricRollupStrategy' beans in
 * a dropdown when creating a 'MerchProductDirectoryConfig'
 */
public class DefaultProductDirectoryRollupStrategyEditor implements CockpitEditorRenderer<String>
{
	@Resource
	private MerchandisingRollupStrategySearchFacade merchandisingRollupStrategySearchFacade;

	@Override
	public void render(final Component parentComponent,
		final EditorContext<String> editorContext,
		final EditorListener<String> editorListener)
	{
		final Combobox editorView = new Combobox();
		editorView.setValue(editorContext.getInitialValue());

		final List<String> options = merchandisingRollupStrategySearchFacade.getRollupStrategies();


		final ListModel<String> listModel = new ListModelList<>(options);
		editorView.setModel(listModel);
		editorView.setReadonly(true);
		editorView.addEventListener(Events.ON_CHANGE, event -> handleEvent(editorView, event, editorListener));
		editorView.addEventListener(Events.ON_OK, event -> handleEvent(editorView, event, editorListener));
		editorView.setParent(parentComponent);
	}

	private void handleEvent(final Textbox editorView, final Event event, final EditorListener<String> listener)
	{
		final String result = (String) editorView.getRawValue();
		listener.onValueChanged(StringUtils.isEmpty(result) ? "" : result);
		if (Events.ON_OK.equals(event.getName()))
		{
			listener.onEditorEvent(EditorListener.ENTER_PRESSED);
		}
	}
}
