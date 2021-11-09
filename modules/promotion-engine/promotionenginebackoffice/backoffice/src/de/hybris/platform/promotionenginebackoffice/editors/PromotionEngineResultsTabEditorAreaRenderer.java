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
package de.hybris.platform.promotionenginebackoffice.editors;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import com.google.common.collect.ImmutableMap;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaTabRenderer;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.ReportPromotionService;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResults;
import de.hybris.platform.promotions.backoffice.actions.CalculateWithPromotionsAction;


/**
 * Renderer responsible for visualization promotion engine results tab in the {@link AbstractOrderModel} backoffice
 * presentation
 */
public class PromotionEngineResultsTabEditorAreaRenderer extends AbstractEditorAreaTabRenderer<AbstractOrderModel>
{
	private static final String ZUL_FILE = "promotionsTab.zul";
	private ReportPromotionService reportPromotionService;

	@Override
	public void render(final Component parent, final AbstractTab configuration, final AbstractOrderModel abstractOrder,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		final Component promotionsTab = renderTab(parent, configuration, abstractOrder, dataType, widgetInstanceManager);

		widgetInstanceManager.getModel().addObserver(CalculateWithPromotionsAction.ABSTRACT_ORDER, new ModelObserver()
		{
			@Override
			public void modelChanged()
			{

				widgetInstanceManager.getModel().removeObserver(this);

				final AbstractOrderModel currentValueFromAction = widgetInstanceManager.getModel()
						.getValue(CalculateWithPromotionsAction.ABSTRACT_ORDER, AbstractOrderModel.class);
				parent.getChildren().remove(promotionsTab);
				render(parent, configuration, currentValueFromAction, dataType, widgetInstanceManager);
			}
		});
	}

	@SuppressWarnings("unused")
	protected Component renderTab(final Component parent, final AbstractTab configuration, final AbstractOrderModel abstractOrder,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		final PromotionEngineResults promotionEngineResults = getReportPromotionService().report(abstractOrder);
		final Map args = ImmutableMap.of("promotionEngineResults", promotionEngineResults);
		return getCurrentExecution().createComponents(getTabTemplate(), parent, args);
	}

	protected Execution getCurrentExecution()
	{
		return Executions.getCurrent();
	}

	protected String getTabTemplate()
	{
		return ZUL_FILE;
	}


	protected ReportPromotionService getReportPromotionService()
	{
		return reportPromotionService;
	}

	@Required
	public void setReportPromotionService(final ReportPromotionService reportPromotionService)
	{
		this.reportPromotionService = reportPromotionService;
	}
}
