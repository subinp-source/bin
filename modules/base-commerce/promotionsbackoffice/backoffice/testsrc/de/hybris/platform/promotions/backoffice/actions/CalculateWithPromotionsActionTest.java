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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionGroupStrategy;
import de.hybris.platform.promotions.PromotionResultService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.testing.AbstractActionUnitTest;
import com.hybris.cockpitng.util.notifications.NotificationService;


@UnitTest
public class CalculateWithPromotionsActionTest extends AbstractActionUnitTest<CalculateWithPromotionsAction>
{
	private static final String ERROR_MESSAGE = "recalculationFailed";
	private final CalculateWithPromotionsAction action = new CalculateWithPromotionsAction();

	@Mock
	private AbstractOrderModel abstractOrderModel;
	@Mock
	private CurrencyModel currency;
	@Captor
	private ArgumentCaptor<SessionExecutionBody> sessionExecutionBodyArgumentCaptor;

	@Mock
	private PromotionsService promotionsService;
	@Mock
	private CalculationService calculationService;
	@Mock
	private SessionService sessionService;
	@Mock
	private UserService userService;
	@Mock
	private CommonI18NService commonI18NService;
	@Mock
	private PromotionResultService promotionResultService;
	@Mock
	private UserModel user;
	@Mock
	private PromotionGroupStrategy promotionGroupStrategy;
	@Mock
	private PromotionOrderResults promotionOrderResults;
	@Mock
	private ModelService modelService;
	@Mock
	private NotificationService notificationService;
	@Mock
	private WidgetModel widgetModel;
	@Mock
	private ActionContext actionContext;
	@Mock
	private PromotionResultModel promotionResult;
	@Override
	public CalculateWithPromotionsAction getActionInstance()
	{
		return action;
	}

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		when(abstractOrderModel.getEntries()).thenReturn(new ArrayList());
		when(abstractOrderModel.getCurrency()).thenReturn(currency);
		when(actionContext.getParameter(com.hybris.cockpitng.actions.ActionContext.PARENT_WIDGET_MODEL)).thenReturn(widgetModel);
		when(actionContext.getData()).thenReturn(abstractOrderModel);

		action.setPromotionsService(promotionsService);
		action.setCalculationService(calculationService);
		action.setSessionService(sessionService);
		action.setUserService(userService);
		action.setCommonI18NService(commonI18NService);
		action.setPromotionGroupStrategy(promotionGroupStrategy);
		action.setPromotionResultService(promotionResultService);
		action.setModelService(modelService);
		action.setNotificationService(notificationService);
		when(promotionsService.updatePromotions(anyCollection(), eq(abstractOrderModel)))
				.thenReturn(promotionOrderResults);
		when(userService.getCurrentUser()).thenReturn(user);
		when(sessionService.executeInLocalView(sessionExecutionBodyArgumentCaptor.capture())).thenAnswer(new Answer<Object>()
		{
			@Override
			public Object answer(final InvocationOnMock invocationOnMock) throws Throwable
			{
				final SessionExecutionBody sessionExecutionBody = sessionExecutionBodyArgumentCaptor.getValue();
				return sessionExecutionBody.execute();
			}
		});
		final List<PromotionResultModel> results = Collections.singletonList(promotionResult);
		when(promotionResultService.getFiredOrderPromotions(any(), any())).thenReturn(results);
	}

	@Test
	public void testSuccessfullRecalculation()
	{
		Assert.assertTrue(action.canPerform(actionContext));
		final ActionResult result = action.perform(actionContext);
		Assert.assertEquals(ActionResult.SUCCESS, result.getResultCode());
		Assert.assertEquals(abstractOrderModel, result.getData());
	}

	@Test
	public void testErrorResult()
	{
		try
		{
			final CalculationException exception = new CalculationException(ERROR_MESSAGE);
			doThrow(exception).when(calculationService).calculateTotals(abstractOrderModel, false);

			Assert.assertTrue(action.canPerform(actionContext));
			final ActionResult result = action.perform(actionContext);
			Assert.assertEquals(ActionResult.ERROR, result.getResultCode());
			Assert.assertEquals(ERROR_MESSAGE, result.getResultMessage());
			Assert.assertEquals(exception, result.getData());
		}
		catch (final CalculationException e)
		{
			Assert.fail();
		}
	}
}