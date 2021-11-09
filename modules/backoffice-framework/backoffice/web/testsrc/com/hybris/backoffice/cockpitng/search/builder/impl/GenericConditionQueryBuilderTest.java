/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.search.builder.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSubQueryCondition;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.enums.RelationEndCardinalityEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.type.impl.DefaultTypeService;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


@RunWith(MockitoJUnitRunner.class)
public class GenericConditionQueryBuilderTest
{

	public static final String DATE_TYPE_ATTRIBUTE = "dateAttribute";
	public static final String TYPE_TO_TYPE_RELATION = "TypeToTypeRelation";
	private static final String TYPE_CODE = "Product";
	private static final String QUALIFIER = "qualifier";
	private static final String RELATION_TYPE_CODE = "relationTypeCode";
	private static final String SOURCE_TYPE_CARDINALITY = "sourceTypeCardinality";

	private final Set<Character> queryBuilderSeparators = Sets.newHashSet(ArrayUtils.toObject(new char[]
	{ ' ', ',', ';', '\t', '\n', '\r' }));

	@Mock
	private DefaultTypeService typeService;

	@InjectMocks
	@Spy
	private GenericConditionQueryBuilder queryBuilder;

	@Mock
	private RelationDescriptorModel relationDescriptor;

	@Mock
	private RelationMetaTypeModel relationMetaType;

	@Mock
	private GenericCondition one2ManyCondition;

	@Mock
	private GenericCondition many2OneCondition;

	@Mock
	private GenericCondition many2ManyCondition;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		queryBuilder.setSeparators(queryBuilderSeparators);
		when(relationDescriptor.getIsSource()).thenReturn(true);
		when(relationDescriptor.getRelationType()).thenReturn(relationMetaType);
		when(relationMetaType.getCode()).thenReturn(TYPE_TO_TYPE_RELATION);
	}

	@Test
	public void shouldSearchForDateWithEquals()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date midnightOfToday = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
		final Date midnightOfTomorrow = DateUtils.addDays(midnightOfToday, 1);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.EQUALS);


		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.AND);

		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(0)).getValue()).isEqualTo(midnightOfToday);

		assertThat(conditionList.getConditionList().get(1).getOperator()).isEqualTo(Operator.LESS);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(1)).getValue()).isEqualTo(midnightOfTomorrow);
	}

	@Test
	public void shouldSearchForGraterDateWithGrater()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);

		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date midnightOfToday = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
		final Date midnightOfTomorrow = DateUtils.addDays(midnightOfToday, 1);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.GREATER);

		assertThat(condition instanceof GenericCondition).isTrue();

		assertThat(condition.getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition) condition).getValue()).isEqualTo(midnightOfTomorrow);
	}

	@Test
	public void shouldSearchForExactDateWithRoundingUpToSecondWithEqualsFollowAllOperatorConfig()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);

		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date dateTruncatedToSecond = DateUtils.truncate(now, Calendar.SECOND);
		final Date dateRoundedUpToSecond = DateUtils.addSeconds(dateTruncatedToSecond, 1);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.EQUALS);

		// then
		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.AND);

		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(0)).getValue()).isEqualTo(dateTruncatedToSecond);

		assertThat(conditionList.getConditionList().get(1).getOperator()).isEqualTo(Operator.LESS);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(1)).getValue()).isEqualTo(dateRoundedUpToSecond);
	}

	@Test
	public void shouldSearchForExactDateWithRoundingUpToSecondWithEquals()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE, "true");
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "false");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date dateTruncatedToSecond = DateUtils.truncate(now, Calendar.SECOND);
		final Date dateRoundedUpToSecond = DateUtils.addSeconds(dateTruncatedToSecond, 1);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.EQUALS);

		// then
		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;

		assertThat(conditionList.getConditionList()).hasSize(2);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.AND);

		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(0)).getValue()).isEqualTo(dateTruncatedToSecond);

		assertThat(conditionList.getConditionList().get(1).getOperator()).isEqualTo(Operator.LESS);
		assertThat(((GenericValueCondition) conditionList.getConditionList().get(1)).getValue()).isEqualTo(dateRoundedUpToSecond);
	}


	@Test
	public void shouldSearchForGraterDateWithRoundingUpToSecondWithGreater()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date dateTruncatedToSecond = DateUtils.truncate(now, Calendar.SECOND);
		final Date dateRoundedUpToSecond = DateUtils.addSeconds(dateTruncatedToSecond, 1);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.GREATER);

		// then
		assertThat(condition instanceof GenericCondition).isTrue();

		assertThat(condition.getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition) condition).getValue()).isEqualTo(dateRoundedUpToSecond);
	}

	@Test
	public void shouldSearchForGraterOrEqualDateWithRoundingUpToSecondWithGreaterOrEqual()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date dateTruncatedToSecond = DateUtils.truncate(now, Calendar.SECOND);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.GREATER_OR_EQUAL);

		// then
		assertThat(condition instanceof GenericCondition).isTrue();

		assertThat(condition.getOperator()).isEqualTo(Operator.GREATER_OR_EQUAL);
		assertThat(((GenericValueCondition) condition).getValue()).isEqualTo(dateTruncatedToSecond);
	}

	@Test
	public void shouldSearchForLessDateWithRoundingUpToSecondWithLess()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date dateTruncatedToSecond = DateUtils.truncate(now, Calendar.SECOND);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.LESS);

		// then
		assertThat(condition instanceof GenericCondition).isTrue();

		assertThat(condition.getOperator()).isEqualTo(Operator.LESS);
		assertThat(((GenericValueCondition) condition).getValue()).isEqualTo(dateTruncatedToSecond);
	}


	@Test
	public void shouldSearchForLessOrEqualDateWithRoundingUpToSecondWithLessOrEqual()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "true");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));

		final Date now = new Date();
		final Date dateTruncatedToSecond = DateUtils.truncate(now, Calendar.SECOND);
		final Date dateRoundedUpToSecond = DateUtils.addSeconds(dateTruncatedToSecond, 1);

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.LESS_OR_EQUAL);

		// then
		assertThat(condition instanceof GenericCondition).isTrue();

		assertThat(condition.getOperator()).isEqualTo(Operator.LESS);
		assertThat(((GenericValueCondition) condition).getValue()).isEqualTo(dateRoundedUpToSecond);
	}

	@Test
	public void shouldReturnNotInSubQueryWithEmptyWhereClauseInsteadOfNotExists()
	{
		//given
		final RelationDescriptorModel relationDescriptor = mock(RelationDescriptorModel.class);
		final RelationMetaTypeModel relType = mock(RelationMetaTypeModel.class);
		when(relType.getCode()).thenReturn("RelTypeTestCode");
		when(relationDescriptor.getRelationType()).thenReturn(relType);

		//when
		final GenericCondition genericCondition = queryBuilder.createMany2ManyRelationCondition(relationDescriptor, TYPE_CODE,
				Operator.NOT_EXISTS, new Object());

		//then
		assertThat(genericCondition).isInstanceOf(GenericSubQueryCondition.class);
		final GenericSubQueryCondition subQuery = (GenericSubQueryCondition) genericCondition;
		assertThat(subQuery.getOperator()).isEqualTo(Operator.NOT_IN);
		assertThat(subQuery.getSubQuery().getCondition()).isNull();

		final GenericSearchField searchField = subQuery.getField();
		assertThat(searchField.getQualifier()).isEqualTo(ItemModel.PK);
		assertThat(searchField.getTypeIdentifier()).isEqualTo(TYPE_CODE);
	}


	@Test
	public void shouldSearchForExactDateInViewTypes()
	{
		// given
		final SearchAttributeDescriptor attributeDescriptor = mock(SearchAttributeDescriptor.class);
		when(attributeDescriptor.getAttributeName()).thenReturn(DATE_TYPE_ATTRIBUTE);
		final HashMap<String, String> editorParameters = new HashMap<>();
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_EQUALS_COMPARES_EXACT_DATE, "false");
		editorParameters.put(GenericConditionQueryBuilder.EDITOR_PARAM_COMPARES_EXACT_DATE, "false");
		when(attributeDescriptor.getEditorParameters()).thenReturn(editorParameters);

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		when(searchQueryData.getSearchType()).thenReturn(TYPE_CODE);
		when(typeService.getAttributeDescriptor(TYPE_CODE, DATE_TYPE_ATTRIBUTE)).thenReturn(mock(AttributeDescriptorModel.class));
		when(typeService.getComposedTypeForCode(TYPE_CODE)).thenReturn(mock(ViewTypeModel.class));

		final Date now = new Date();

		// when
		final GenericCondition condition = queryBuilder.createSingleTokenCondition(searchQueryData, attributeDescriptor, now,
				ValueComparisonOperator.EQUALS);

		// then
		assertThat(condition instanceof GenericValueCondition).isTrue();
		final GenericValueCondition valueCondition = (GenericValueCondition) condition;

		assertThat(valueCondition.getOperator()).isEqualTo(Operator.EQUAL);
		assertThat(valueCondition.getValue()).isEqualTo(now);
	}

	public void buildMany2ManyQueryNullDataInOperator()
	{

		//when
		final GenericQuery query = queryBuilder.buildMany2ManyQuery(relationDescriptor, null, Operator.IN);

		//then
		assertThat(query.getCondition()).isNull();
		assertThat(query.getInitialTypeCode()).isEqualTo(TYPE_TO_TYPE_RELATION);

	}

	@Test
	public void buildMany2ManyQueryNullDataContainsOperator()
	{
		//when
		final GenericQuery query = queryBuilder.buildMany2ManyQuery(relationDescriptor, null, Operator.CONTAINS);

		//then
		assertThat(query.getCondition()).isNull();
		assertThat(query.getInitialTypeCode()).isEqualTo(TYPE_TO_TYPE_RELATION);
	}

	@Test
	public void buildMany2ManyQueryNonNullEqualsExpected()
	{
		//given
		final Object value = new Object();

		//when
		final GenericQuery query = queryBuilder.buildMany2ManyQuery(relationDescriptor, value, Operator.EQUAL);

		//then
		assertThat(query.getCondition()).matches(condition -> Operator.EQUAL.equals(condition.getOperator()));
		assertThat(query.getCondition()).isInstanceOf(GenericValueCondition.class);
		assertThat(((GenericValueCondition) query.getCondition()).getValue()).isEqualTo(value);
		assertThat(query.getInitialTypeCode()).isEqualTo(TYPE_TO_TYPE_RELATION);
	}


	@Test
	public void buildMany2ManyQueryNonNullInExpected()
	{
		//given
		final Object value = new Object();

		//when
		final GenericQuery query = queryBuilder.buildMany2ManyQuery(relationDescriptor, value, Operator.IN);

		//then
		assertThat(query.getCondition()).matches(condition -> Operator.IN.equals(condition.getOperator()));
		assertThat(query.getCondition()).isInstanceOf(GenericValueCondition.class);
		assertThat(((GenericValueCondition) query.getCondition()).getValue()).isEqualTo(Collections.singletonList(value));
		assertThat(query.getInitialTypeCode()).isEqualTo(TYPE_TO_TYPE_RELATION);
	}

	@Test
	public void buildMany2ManyQueryNonNullNotInExpected()
	{
		//given
		final Object value = Collections.singleton(new Object());

		//when
		final GenericQuery query = queryBuilder.buildMany2ManyQuery(relationDescriptor, value, Operator.NOT_IN);

		//then
		assertThat(query.getCondition()).matches(condition -> Operator.IN.equals(condition.getOperator()));
		assertThat(query.getCondition()).isInstanceOf(GenericValueCondition.class);
		assertThat(((GenericValueCondition) query.getCondition()).getValue()).isEqualTo(value);
		assertThat(query.getInitialTypeCode()).isEqualTo(TYPE_TO_TYPE_RELATION);
	}


	@Test(expected = IllegalArgumentException.class)
	public void buildMany2ManyQueryEmptyCollectionNotInOperator()
	{
		//given
		final Object value = Collections.emptyList();

		//when
		queryBuilder.buildMany2ManyQuery(relationDescriptor, value, Operator.NOT_IN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildMany2ManyQueryEmptyCollectionInOperator()
	{
		//given
		final Object value = Collections.emptyList();

		//when
		queryBuilder.buildMany2ManyQuery(relationDescriptor, value, Operator.IN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildMany2ManyQueryEmptyCollectionEqualsOperator()
	{
		//given
		final Object value = Collections.emptyList();

		//when
		queryBuilder.buildMany2ManyQuery(relationDescriptor, value, Operator.EQUAL);
	}

	@Test
	public void shouldCreateMany2OneConditionForValueWhenOneToManyRelationFoundWithRelationAttributeDescriptorEndOfTheManyType()
	{
		//given
		final Object value = mock(Object.class);
		final ValueComparisonOperator comparisonOperator = ValueComparisonOperator.EQUALS;

		when(relationDescriptor.getIsSource()).thenReturn(true);
		when(relationMetaType.getProperty(any()))
				.thenAnswer(inv -> inv.getArguments()[0].equals(SOURCE_TYPE_CARDINALITY) ? RelationEndCardinalityEnum.MANY : null);

		when(relationMetaType.getAbstract()).thenReturn(true);

		prepareForCheckingRelationConditions();

		//when
		final GenericCondition genericCondition = queryBuilder.createRelationCondition(relationDescriptor, TYPE_CODE, QUALIFIER,
				comparisonOperator, value);

		//then
		assertThat(genericCondition).isSameAs(many2OneCondition);
	}

	@Test
	public void shouldCreateOne2ManyConditionForValueWhenOneToManyRelationFoundWithRelationAttributeDescriptorEndOfTheOneType()
	{
		//given
		final Object value = mock(Object.class);
		final ValueComparisonOperator comparisonOperator = ValueComparisonOperator.EQUALS;

		when(relationDescriptor.getIsSource()).thenReturn(false);
		when(relationMetaType.getProperty(any()))
				.thenAnswer(inv -> inv.getArguments()[0].equals(SOURCE_TYPE_CARDINALITY) ? null : RelationEndCardinalityEnum.ONE);

		when(relationMetaType.getAbstract()).thenReturn(true);

		prepareForCheckingRelationConditions();

		//when
		final GenericCondition genericCondition = queryBuilder.createRelationCondition(relationDescriptor, TYPE_CODE, QUALIFIER,
				comparisonOperator, value);

		//then
		assertThat(genericCondition).isSameAs(one2ManyCondition);
	}

	@Test
	public void shouldCreateMany2ManyConditionForValueWhenManyToManyRelationFound()
	{
		//given
		final Object value = mock(Object.class);
		final ValueComparisonOperator comparisonOperator = ValueComparisonOperator.IN;

		when(relationMetaType.getAbstract()).thenReturn(false);
		when(relationMetaType.getCode()).thenReturn(RELATION_TYPE_CODE);

		prepareForCheckingRelationConditions();

		//when
		final GenericCondition genericCondition = queryBuilder.createRelationCondition(relationDescriptor, TYPE_CODE, QUALIFIER,
				comparisonOperator, value);

		//then
		assertThat(genericCondition).isSameAs(many2ManyCondition);
	}

	private void prepareForCheckingRelationConditions()
	{
		doReturn(one2ManyCondition).when(queryBuilder).createOne2ManyRelationCondition(any(), any(), any(), any());
		doReturn(many2OneCondition).when(queryBuilder).createMany2OneRelationCondition(any(), any(), any(), any(), any());
		doReturn(many2ManyCondition).when(queryBuilder).createMany2ManyRelationCondition(any(), any(), any(), any());
	}


}
