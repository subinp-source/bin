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
package de.hybris.platform.ruleenginebackoffice.widgets.listview.renderer;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.strategies.CatalogVersionFinderStrategy;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;


public class RulesModuleCatalogVersionCellRenderer<R extends AbstractRulesModuleModel>
		extends AbstractWidgetComponentRenderer<Listcell, ListColumn, R>
{

	private CatalogVersionFinderStrategy catalogVersionFinderStrategy;

	@Override
	public void render(final Listcell parent, final ListColumn columnConfiguration, final R rulesModule, final DataType dataType,
			final WidgetInstanceManager widgetInstanceManager)
	{
		final HtmlBasedComponent label = new Label(getLabelText(rulesModule));

		UITools.modifySClass(parent, "yw-listview-cell-restricted", true);
		UITools.modifySClass(label, "yw-listview-cell-label", true);

		parent.appendChild(label);

		fireComponentRendered(label, parent, columnConfiguration, rulesModule);
		fireComponentRendered(parent, parent, columnConfiguration, rulesModule);
	}

	/**
	 * Finds all catalog versions for the given rules module and concatenates catalog names to obtain a comma separated string
	 * value
	 */
	protected String getLabelText(final AbstractRulesModuleModel rulesModule)
	{

		final List<CatalogVersionModel> catalogVersions = getCatalogVersionFinderStrategy()
				.findCatalogVersionsByRulesModule(rulesModule);

		if (CollectionUtils.isNotEmpty(catalogVersions))
		{
			return catalogVersions.stream().map(v -> v.getCatalog().getName() + ":" + v.getVersion())
					.collect(Collectors.joining(", "));
		}

		return "";
	}

	protected CatalogVersionFinderStrategy getCatalogVersionFinderStrategy()
	{
		return catalogVersionFinderStrategy;
	}

	@Required
	public void setCatalogVersionFinderStrategy(
			final CatalogVersionFinderStrategy catalogVersionFinderStrategy)
	{
		this.catalogVersionFinderStrategy = catalogVersionFinderStrategy;
	}
}
