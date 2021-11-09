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
package de.hybris.platform.promotions.backoffice.widgets;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.backoffice.widgets.advancedsearch.impl.SearchConditionData;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldListType;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType.Builder;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.hybris.cockpitng.testing.AbstractWidgetUnitTest;
import com.hybris.cockpitng.testing.annotation.DeclaredInput;
import com.hybris.cockpitng.testing.annotation.DeclaredInputs;


@DeclaredInputs(@DeclaredInput(value = InitPromotionsAdvancedSearchController.SOCKET_IN_NODE_SELECTED, socketType = NavigationNode.class))
public class InitPromotionsAdvancedSearchControllerTest extends AbstractWidgetUnitTest<InitPromotionsAdvancedSearchController>
{
	@InjectMocks
	private final InitPromotionsAdvancedSearchController controller = new InitPromotionsAdvancedSearchController();

	@Mock
	private NavigationNode navigationNode;
	@Mock
	private TypeFacade typeFacade;
	@Mock
	private PermissionFacade permissionFacade;
	@Mock
	private AdvancedSearch advancedSearch;

	private final FieldListType fieldListType = new FieldListType();

	public InitPromotionsAdvancedSearchControllerTest()
	{
		final FieldType fieldType = new FieldType();
		fieldType.setDisabled(Boolean.FALSE);
		fieldType.setSelected(Boolean.TRUE);
		fieldType.setName(InitPromotionsAdvancedSearchController.IMMUTABLE_KEY_HASH_ATTR);
		fieldListType.getField().add(fieldType);
	}

	@Override
	protected InitPromotionsAdvancedSearchController getWidgetController()
	{
		return this.controller;
	}

	@Test
	public void testCreateAdvancedSearchInitContext() throws Exception
	{
		// given
		given(navigationNode.getId()).willReturn(InitPromotionsAdvancedSearchController.ABSTRACT_PROMOTION_NAVIGATION_NODE);
		given(getTypeFacade().load(InitPromotionsAdvancedSearchController.ABSTRACT_PROMOTION_TYPE))
				.willReturn(new Builder(InitPromotionsAdvancedSearchController.ABSTRACT_PROMOTION_TYPE).build());
		given(Boolean.valueOf(permissionFacade.canReadType(InitPromotionsAdvancedSearchController.ABSTRACT_PROMOTION_TYPE)))
				.willReturn(Boolean.TRUE);
		given(advancedSearch.getFieldList()).willReturn(fieldListType);
		given(widgetInstanceManager.loadConfiguration(any(ConfigContext.class), eq(AdvancedSearch.class)))
				.willReturn(advancedSearch);

		// when
		executeInputSocketEvent(InitPromotionsAdvancedSearchController.SOCKET_IN_NODE_SELECTED, navigationNode);

		// then
		assertThatInitContextSend();
		assertThatAdvancedSearchDataHasProperCondition();
	}

	private void assertThatInitContextSend()
	{
		verify(widgetInstanceManager, times(1)).sendOutput(eq(InitPromotionsAdvancedSearchController.SOCKET_OUT_CONTEXT),
				any(AdvancedSearchData.class));
	}

	private void assertThatAdvancedSearchDataHasProperCondition()
	{
		final AdvancedSearchInitContext outputInitContext = captureInitContextFromOutputSocket();
		assertThat(outputInitContext).isNotNull();
		final AdvancedSearchData outputAdvancedSearchData = outputInitContext.getAdvancedSearchData();
		assertThat(outputAdvancedSearchData).isNotNull();
		assertThat(outputAdvancedSearchData.getTypeCode())
				.isEqualTo(InitPromotionsAdvancedSearchController.ABSTRACT_PROMOTION_TYPE);
		assertThatConditionIsSet(
				outputAdvancedSearchData.getConditions(InitPromotionsAdvancedSearchController.IMMUTABLE_KEY_HASH_ATTR));
	}

	private AdvancedSearchInitContext captureInitContextFromOutputSocket()
	{
		final ArgumentCaptor<String> socketIdCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<AdvancedSearchInitContext> searchDataArgumentCaptor = ArgumentCaptor
				.forClass(AdvancedSearchInitContext.class);
		verify(widgetInstanceManager).sendOutput(socketIdCaptor.capture(), searchDataArgumentCaptor.capture());
		return searchDataArgumentCaptor.getValue();
	}

	private void assertThatConditionIsSet(final List<SearchConditionData> conditions)
	{
		assertThat(conditions).isNotNull().isNotEmpty();

		boolean isEmptySet = false;
		for (final SearchConditionData condition : conditions)
		{
			if (condition.getOperator().equals(ValueComparisonOperator.IS_EMPTY))
			{
				isEmptySet = true;
				break;
			}
		}
		assertThat(isEmptySet).isTrue();
	}

	@Test
	public void testCreateAdvancedSearchInitForDifferentNode() throws Exception
	{
		// given
		given(navigationNode.getId()).willReturn("anotherNavigationNodeId");

		// when
		executeInputSocketEvent(InitPromotionsAdvancedSearchController.SOCKET_IN_NODE_SELECTED, navigationNode);

		// then
		assertThatInitContextSend();
		assertThatAdvancedSearchDataWasNotCreated();
	}

	private void assertThatAdvancedSearchDataWasNotCreated()
	{
		final AdvancedSearchInitContext outputInitContext = captureInitContextFromOutputSocket();
		assertThat(outputInitContext).isNotNull();
		final AdvancedSearchData outputAdvancedSearchData = outputInitContext.getAdvancedSearchData();
		assertThat(outputAdvancedSearchData).isNull();
	}

	/**
	 * @return the typeFacade
	 */
	public TypeFacade getTypeFacade()
	{
		return typeFacade;
	}

	/**
	 * @param typeFacade
	 *           the typeFacade to set
	 */
	public void setTypeFacade(final TypeFacade typeFacade)
	{
		this.typeFacade = typeFacade;
	}
}
