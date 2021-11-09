/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedSort;
import de.hybris.platform.adaptivesearch.data.AsPromotedSort;
import de.hybris.platform.adaptivesearch.data.AsSort;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.enums.AsSortOrder;

import java.util.List;
import java.util.function.Supplier;


public abstract class AbstractAsSortsMergeStrategyTest extends AbstractAsMergeStrategyTest
{
	protected static final String SORT1_CODE = "code1";
	protected static final String SORT2_CODE = "code2";
	protected static final String SORT3_CODE = "code3";
	protected static final String SORT4_CODE = "code4";
	protected static final String SORT5_CODE = "code5";
	protected static final String SORT6_CODE = "code6";

	protected static final String INDEX_PROPERTY_1 = "property1";
	protected static final String INDEX_PROPERTY_2 = "property2";
	protected static final String INDEX_PROPERTY_3 = "property3";
	protected static final String INDEX_PROPERTY_4 = "property4";
	protected static final String INDEX_PROPERTY_5 = "property5";
	protected static final String INDEX_PROPERTY_6 = "property6";
	protected static final String INDEX_PROPERTY_7 = "property7";
	protected static final String INDEX_PROPERTY_8 = "property8";
	protected static final String INDEX_PROPERTY_9 = "property9";
	protected static final String INDEX_PROPERTY_10 = "property10";
	protected static final String INDEX_PROPERTY_11 = "property11";
	protected static final String INDEX_PROPERTY_12 = "property12";
	protected static final String INDEX_PROPERTY_13 = "property13";

	public static final class SortBuilder<T extends AbstractAsSortConfiguration>
	{
		private final T sortConfig;

		private SortBuilder(final Supplier<T> tSupplier)
		{
			this.sortConfig = tSupplier.get();
		}

		public static SortBuilder<AsSort> anAsSort()
		{
			return new SortBuilder(AsSort::new);
		}

		public static SortBuilder<AsExcludedSort> anAsExcludedSort()
		{
			return new SortBuilder(AsExcludedSort::new);
		}

		public static SortBuilder<AsPromotedSort> anAsPromotedSort()
		{
			return new SortBuilder(AsPromotedSort::new);
		}

		public SortBuilder<T> withCode(final String code)
		{
			sortConfig.setCode(code);
			return this;
		}

		public SortBuilder<T> withUid(final String uid)
		{
			sortConfig.setUid(uid);
			return this;
		}

		public SortBuilder<T> withExpressions(final List<AsSortExpression> expresions)
		{
			sortConfig.setExpressions(expresions);
			return this;
		}

		public T build()
		{
			return sortConfig;
		}
	}

	public static final class AsSortExpressionBuilder
	{
		private final AsSortExpression asSortExpression;

		private AsSortExpressionBuilder()
		{
			asSortExpression = new AsSortExpression();
		}

		public static AsSortExpressionBuilder anAsSortExpression()
		{
			return new AsSortExpressionBuilder();
		}

		public AsSortExpressionBuilder withUid(final String uid)
		{
			asSortExpression.setUid(uid);
			return this;
		}

		public AsSortExpressionBuilder withIndexProperty(final String indexProperty)
		{
			asSortExpression.setExpression(indexProperty);
			return this;
		}

		public AsSortExpressionBuilder withOrder(final AsSortOrder order)
		{
			asSortExpression.setOrder(order);
			return this;
		}

		public AsSortExpression build()
		{
			return asSortExpression;
		}
	}
}
