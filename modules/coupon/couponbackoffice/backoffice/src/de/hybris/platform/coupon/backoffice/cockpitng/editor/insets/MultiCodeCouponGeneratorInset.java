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

import java.text.MessageFormat;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;

import com.google.common.base.Strings;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;

import de.hybris.platform.coupon.backoffice.cockpitng.editor.defaultinseteditor.DefaultInsetEditor;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.services.CouponCodeGenerationService;
import de.hybris.platform.servicelayer.model.ModelService;


public class MultiCodeCouponGeneratorInset implements CockpitEditorRenderer<Object>
{
	protected static final String ERROR_WRONG_PARENT_TYPE_MSG = "multiCodeCouponGenerator.wrongParentType";
	protected static final String EDITOR_PLACEHOLDER = "hmc.text.mutlicoupon.setquantity.label";
	protected static final String GENERATE_BUTTON_LABEL = "hmc.btn.generate.coupon.codes";
	protected static final String UNEXPECTED_ERROR_MSG = "multiCodeCouponGenerator.unexpectedError";
	protected static final String ERROR_GENERATE_FOR_NOT_SAVED_MSG = "multiCodeCouponGenerator.generateForNotSavedError";

	public static final String INSET_SCLASS = "ye-multicode-coupon-generator";
	public static final String BUTTON_SCLASS = "ye-coupon-inset-button";
	public static final String TEXTBOX_SCLASS = "ye-coupon-inset-textbox";

	protected static final String PARENT_OBJECT_PARAM = "parentObject";
	protected static final String CURRENT_OBJECT_PARAM = "currentObject";

	private ModelService modelService;
	private CouponCodeGenerationService couponCodeGenerationService;
	private NotificationService notificationService;

	@Override
	public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
	{
		if (parent == null || context == null || listener == null)
		{
			return;
		}

		final Div insetContainer = new Div();
		insetContainer.setSclass(DefaultInsetEditor.GENERAL_INSET_SCLASS + " " + INSET_SCLASS);
		insetContainer.setParent(parent);

		final WidgetInstanceManager wim = (WidgetInstanceManager) context.getParameter("wim");

		final Object parentObject = context.getParameter(PARENT_OBJECT_PARAM);
		if (!(parentObject instanceof MultiCodeCouponModel))
		{
			throw new IllegalStateException(Labels.getLabel(ERROR_WRONG_PARENT_TYPE_MSG));
		}

		final MultiCodeCouponModel multiCodeCoupon = (MultiCodeCouponModel) parentObject;

		final Intbox editorView = new Intbox();
		editorView.setCols(34);
		editorView.setPlaceholder(Labels.getLabel(EDITOR_PLACEHOLDER));
		editorView.setSclass(TEXTBOX_SCLASS);
		editorView.setParent(insetContainer);
		editorView.setConstraint("no negative, no zero");
		editorView.setDisabled(isDisabled(multiCodeCoupon));

		final Button button = createButtonForGenerateCouponCodes(insetContainer, wim, multiCodeCoupon, editorView);

		editorView.addEventListener(Events.ON_CHANGING, new EventListener<InputEvent>()
		{
			@Override
			public void onEvent(final InputEvent event) throws Exception //NOPMD
			{
				final String value = event.getValue();
				button.setDisabled(Strings.isNullOrEmpty(value));
			}
		});
	}


	protected Button createButtonForGenerateCouponCodes(final Div insetContainer, final WidgetInstanceManager wim,
			final MultiCodeCouponModel multiCodeCoupon, final Intbox editorView)
	{
		final Button button = new Button(Labels.getLabel(GENERATE_BUTTON_LABEL));
		button.setSclass(BUTTON_SCLASS);
		button.setParent(insetContainer);
		button.setDisabled(true);
		button.addEventListener(Events.ON_CLICK, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event)
			{
				if (getModelService().isModified(multiCodeCoupon))
				{
					getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(wim), NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE,
							NotificationEvent.Level.FAILURE, Collections.singletonMap(multiCodeCoupon, Labels.getLabel(ERROR_GENERATE_FOR_NOT_SAVED_MSG)));
					return;
				}
				final int quantity = editorView.getValue().intValue();
				final int multiCodeCouponTotal = multiCodeCoupon.getCouponCodeNumber().intValue() + quantity;
				getCouponCodeGenerationService().generateCouponCodes(multiCodeCoupon, quantity);
				final int multiCodeCouponRemaining = multiCodeCouponTotal - multiCodeCoupon.getCouponCodeNumber().intValue();
				if (multiCodeCouponRemaining > 0)
				{
					final String message = MessageFormat.format(Labels.getLabel("couponcode.maximum.generation.information"),
							new Object[]
					{ Integer.valueOf(multiCodeCoupon.getCouponCodeNumber().intValue()), Integer.valueOf(multiCodeCouponRemaining) });
					Messagebox.show(message, Labels.getLabel("couponcode.maximum.generation.information.title"), Messagebox.OK,
							Messagebox.INFORMATION);
				}
				wim.getModel().setValue(CURRENT_OBJECT_PARAM, multiCodeCoupon);
			}
		});
		return button;
	}

	protected boolean isDisabled(final MultiCodeCouponModel multiCodeCoupon)
	{
		return getModelService().isNew(multiCodeCoupon);
	}

	protected CouponCodeGenerationService getCouponCodeGenerationService()
	{
		return couponCodeGenerationService;
	}

	@Required
	public void setCouponCodeGenerationService(final CouponCodeGenerationService couponCodeGenerationService)
	{
		this.couponCodeGenerationService = couponCodeGenerationService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	@Required
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}
}