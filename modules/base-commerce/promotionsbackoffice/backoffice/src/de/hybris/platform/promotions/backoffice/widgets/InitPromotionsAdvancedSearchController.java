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

import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


public class InitPromotionsAdvancedSearchController extends AbstractInitAdvanceSearchController
{
	public static final String IMMUTABLE_KEY_HASH_ATTR = "immutableKeyHash";
	public static final String ABSTRACT_PROMOTION_TYPE = "AbstractPromotion";
	public static final String ABSTRACT_PROMOTION_NAVIGATION_NODE = "hmc_type_tree_abstractpromotion";

	@Override
	public void addSearchDataConditions(final AdvancedSearchData searchData)
	{
		if (searchData != null)
		{
			final FieldType fieldType = new FieldType();
			fieldType.setDisabled(Boolean.FALSE);
			fieldType.setSelected(Boolean.TRUE);
			fieldType.setName(IMMUTABLE_KEY_HASH_ATTR);
			searchData.addCondition(fieldType, ValueComparisonOperator.IS_EMPTY, null);
		}
	}

	@Override
	public String getNavigationNodeId()
	{
		return ABSTRACT_PROMOTION_NAVIGATION_NODE;
	}

	@Override
	public String getTypeCode()
	{
		return ABSTRACT_PROMOTION_TYPE;
	}
}
