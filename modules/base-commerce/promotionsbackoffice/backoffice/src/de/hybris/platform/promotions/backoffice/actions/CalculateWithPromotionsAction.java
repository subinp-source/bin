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
package de.hybris.platform.promotions.backoffice.actions;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionGroupStrategy;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.backoffice.constants.PromotionsbackofficeConstants.NotificationSource;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;




public class CalculateWithPromotionsAction implements CockpitAction<AbstractOrderModel, Object>
{
	private static final String CONFIRMATION_MESSAGE = "calculate.with.promotions.confirmation.message";
	public static final String ABSTRACT_ORDER = "abstractOrder";

	private static final Logger LOG = Logger.getLogger(CalculateWithPromotionsAction.class.getName());
	@Resource
	private PromotionsService promotionsService;

	@Resource
	private CalculationService calculationService;

	@Resource
	private PromotionGroupStrategy promotionGroupStrategy;

	@Resource
	private PromotionResultService promotionResultService;

	@Resource
	private SessionService sessionService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private UserService userService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private ModelService modelService;

	@Resource
	private NotificationService notificationService;

	@Override
	public boolean canPerform(final ActionContext<AbstractOrderModel> context)
	{
		return context.getData() != null;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<AbstractOrderModel> context)
	{
		return context.getLabel(CONFIRMATION_MESSAGE);
	}

	@Override
	public boolean needsConfirmation(final ActionContext<AbstractOrderModel> context)
	{
		return context.getData() != null;
	}

	@Override
	public ActionResult<Object> perform(final ActionContext<AbstractOrderModel> context)
	{
		final AbstractOrderModel order = context.getData();

		@SuppressWarnings("squid:S1188") // ignore exceeded anonymous inner class line of code limit
		final ActionResult result = getSessionService().executeInLocalView(new SessionExecutionBody()
		{
			@SuppressWarnings("deprecation")
			@Override
			public Object execute()
			{
				configureSession(order);
				try
				{
					updatePromotions(order);
					getModelService().refresh(order);
					getNotificationService().notifyUser(NotificationSource.CALCULATION_MESSAGE_SOURCE,
							NotificationEventTypes.EVENT_TYPE_GENERAL, Level.SUCCESS);
					return new ActionResult(ActionResult.SUCCESS, order);
				}
				catch (final CalculationException e)
				{
					LOG.error("Order recalculation error ", e);
					getNotificationService().notifyUser(NotificationSource.CALCULATION_MESSAGE_SOURCE,
							NotificationEventTypes.EVENT_TYPE_GENERAL, Level.FAILURE, e);
					final ActionResult result = new ActionResult(ActionResult.ERROR, e);
					result.setResultMessage(e.getMessage());
					return result;
				}
			}
		});

		final WidgetModel widgetModel = (WidgetModel) context
				.getParameter(com.hybris.cockpitng.actions.ActionContext.PARENT_WIDGET_MODEL);
		widgetModel.setValue(ABSTRACT_ORDER, order);

		return result;
	}

	protected void updatePromotions(final AbstractOrderModel order) throws CalculationException
	{
		final PromotionOrderResults promotionOrderResults = promotionsService.updatePromotions(createPromotionGroups(order), order);
		calculateIfNeeded(order, promotionOrderResults);
	}

	protected Collection<PromotionGroupModel> createPromotionGroups(final AbstractOrderModel order)
	{
		final PromotionGroupModel promotionGroup = promotionGroupStrategy.getDefaultPromotionGroup(order);
		if (promotionGroup != null)
		{
			return Collections.singleton(promotionGroup);
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected void calculateIfNeeded(final AbstractOrderModel order, final PromotionOrderResults promotionOrderResults)
			throws CalculationException
	{
		boolean needsCalculate = false;
		boolean resultApplied = false;

		AbstractPromotionModel promotionFromResult = null;
		final List allResults = promotionOrderResults.getAllResults();

		if (CollectionUtils.isNotEmpty(allResults) && allResults.get(0) instanceof PromotionResultModel)
		{
			promotionFromResult = ((PromotionResultModel) allResults.get(0)).getPromotion();
		}

		// auto apply promotions that need to be auto applied

		final List<PromotionResultModel> unappliedOrderPromotions = getPromotionResultService()
				.getFiredOrderPromotions(promotionOrderResults, promotionFromResult);
		final List<PromotionResultModel> unappliedProductPromotions = getPromotionResultService()
				.getFiredProductPromotions(promotionOrderResults, promotionFromResult);
		final List<PromotionResultModel> unappliedPromotions = ListUtils.union(unappliedOrderPromotions,
				unappliedProductPromotions);

		for (final PromotionResultModel result : unappliedPromotions)
		{
			needsCalculate = true;
			if (getPromotionResultService().isApplied(result))
			{
				resultApplied |= getPromotionResultService().apply(result);
			}
		}

		if (resultApplied)
		{
			getCalculationService().calculateTotals(order, true);
		}
		else if (needsCalculate)
		{
			getCalculationService().calculateTotals(order, false);
		}
	}

	/**
	 * Impersonates local session with values from the order.
	 *
	 */
	protected void configureSession(final AbstractOrderModel order)
	{
		final UserModel user = determineSessionUser(order);
		if (user != null)
		{
			getUserService().setCurrentUser(user);
		}

		final Collection<CatalogVersionModel> catalogVersions = determineSessionCatalogVersions(order);
		if (catalogVersions != null && !catalogVersions.isEmpty())
		{
			getCatalogVersionService().setSessionCatalogVersions(catalogVersions);
		}

		final CurrencyModel currency = determineSessionCurrency(order);
		if (currency != null)
		{
			getCommonI18NService().setCurrentCurrency(currency);
		}
	}

	protected CurrencyModel determineSessionCurrency(final AbstractOrderModel order)
	{
		CurrencyModel currency = order.getCurrency();
		if (currency == null)
		{
			currency = getCommonI18NService().getCurrentCurrency();
		}
		return currency;
	}

	protected Collection<CatalogVersionModel> determineSessionCatalogVersions(final AbstractOrderModel order)
	{
		final Set<CatalogVersionModel> catalogVersionsForOrder = new HashSet<>();
		order.getEntries().stream().forEach(entry -> catalogVersionsForOrder.add(entry.getProduct().getCatalogVersion()));
		return catalogVersionsForOrder;
	}

	protected UserModel determineSessionUser(final AbstractOrderModel order)
	{
		UserModel user = order.getUser();
		if (user == null)
		{
			user = getUserService().getCurrentUser();
		}
		return user;
	}

	protected PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	protected CalculationService getCalculationService()
	{
		return calculationService;
	}

	public void setCalculationService(final CalculationService calculationService)
	{
		this.calculationService = calculationService;
	}

	protected PromotionResultService getPromotionResultService()
	{
		return promotionResultService;
	}

	public void setPromotionResultService(final PromotionResultService promotionResultService)
	{
		this.promotionResultService = promotionResultService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService i18nService)
	{
		this.commonI18NService = i18nService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected NotificationService getNotificationService()
	{
		return notificationService;
	}

	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	protected PromotionGroupStrategy getPromotionGroupStrategy()
	{
		return promotionGroupStrategy;
	}

	public void setPromotionGroupStrategy(final PromotionGroupStrategy promotionGroupStrategy)
	{
		this.promotionGroupStrategy = promotionGroupStrategy;
	}

}
