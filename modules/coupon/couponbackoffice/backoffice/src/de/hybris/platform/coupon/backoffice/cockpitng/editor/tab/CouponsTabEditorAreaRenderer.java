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
package de.hybris.platform.coupon.backoffice.cockpitng.editor.tab;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import com.google.common.collect.ImmutableMap;

import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaTabRenderer;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.coupon.backoffice.data.AppliedCouponData;
import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Coupons tab in the cart/order editor view
 */
public class CouponsTabEditorAreaRenderer extends AbstractEditorAreaTabRenderer<AbstractOrderModel>
{
	private static final String ZUL_FILE = "cng/couponsTab.zul";
	private Converter<String, AppliedCouponData> appliedCouponConverter;

	@Override
	public void render(final Component parent, final AbstractTab configuration, final AbstractOrderModel abstractOrder,
			final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
	{
		final Collection<String> appliedCouponCodes = abstractOrder.getAppliedCouponCodes();
		final List<AppliedCouponData> appliedCoupons = getAppliedCouponConverter().convertAll(appliedCouponCodes);
		final Map<String, List<AppliedCouponData>> params = ImmutableMap.of("appliedCoupons", appliedCoupons);
		getCurrentExecution().createComponents(getTabTemplate(), parent, params);
	}

	protected String getTabTemplate()
	{
		return ZUL_FILE;
	}

	protected Execution getCurrentExecution()
	{
		return Executions.getCurrent();
	}


	protected Converter<String, AppliedCouponData> getAppliedCouponConverter()
	{
		return appliedCouponConverter;
	}

	@Required
	public void setAppliedCouponConverter(final Converter<String, AppliedCouponData> appliedCouponConverter)
	{
		this.appliedCouponConverter = appliedCouponConverter;
	}
}
