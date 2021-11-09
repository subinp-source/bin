/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.editor.utility;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;

import javax.validation.constraints.NotNull;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.impl.XulElement;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;

/**
 * Used to dynamically render a non model field, URL, in the Inbound Channel Configurations List View
 */
public class DefaultUrlRenderer implements WidgetComponentRenderer<XulElement, ListColumn, InboundChannelConfigurationModel>
{
	private static final String URL = "https://<your-host-name>/odata2webservices/";

	@Override
	public void render(final XulElement xulElement, final ListColumn listColumn,
	                   @NotNull final InboundChannelConfigurationModel inboundChannelConfigurationModel, final DataType dataType,
	                   final WidgetInstanceManager widgetInstanceManager)
	{

		if (inboundChannelConfigurationModel.getIntegrationObject() != null)
		{
			final String ioCode = inboundChannelConfigurationModel.getIntegrationObject().getCode();
			xulElement.appendChild(createElement(ioCode));
		}
	}

	private Component createElement(final String ioCode)
	{
		final Div urlDiv = new Div();
		final Label urlLabel = new Label();
		urlLabel.setValue(URL + ioCode);
		urlDiv.appendChild(urlLabel);
		urlDiv.setSclass("yw-compound-renderer-container");
		return urlDiv;
	}
}