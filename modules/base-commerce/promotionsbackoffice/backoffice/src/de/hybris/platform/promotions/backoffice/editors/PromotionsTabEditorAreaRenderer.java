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
package de.hybris.platform.promotions.backoffice.editors;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.backoffice.actions.CalculateWithPromotionsAction;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.result.PromotionOrderResults;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaTabRenderer;


public class PromotionsTabEditorAreaRenderer extends AbstractEditorAreaTabRenderer<AbstractOrderModel>
{
	private static final String ZUL_FILE = "cng/promotionsTab.zul";
	private PromotionsService promotionsService;

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

				final AbstractOrderModel currentValueFromAction = widgetInstanceManager.getModel().getValue(
						CalculateWithPromotionsAction.ABSTRACT_ORDER, AbstractOrderModel.class);
				parent.getChildren().remove(promotionsTab);
				render(parent, configuration, currentValueFromAction, dataType, widgetInstanceManager);
			}
		});
	}

	@SuppressWarnings("unused")
	protected Component renderTab(final Component parent, final AbstractTab configuration, final AbstractOrderModel abstractOrder,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		final PromotionOrderResults promotionOrderResults = abstractOrder != null ? getPromotionsService().getPromotionResults(abstractOrder) : new PromotionOrderResults(JaloSession.getCurrentSession().getSessionContext(), null, Collections.<PromotionResult>emptyList(), 0.0D);
		final Map args = new HashMap();
		args.put("promotionResults", promotionOrderResults);
		return Executions.getCurrent().createComponents(ZUL_FILE, parent, args);
	}

	public PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}
}
