/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.search;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.search.data.SearchAttributeDescriptor;
import com.hybris.cockpitng.search.data.SearchQueryData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;


@RunWith(MockitoJUnitRunner.class)
public class DefaultPlatformFieldSearchFacadeStrategyTest
{
	private static final String PRODUCT_CODE = "Product";
	private static final String PRODUCT_SUB1_CODE = "Product_Subtype1";
	private static final String PRODUCT_SUB2_CODE = "Product_Subtype2";
	private static final String ATTRIBUTE_CODE = "code";
	private static final String ATTRIBUTE_EAN = "ean";
	private static final String ATTRIBUTE_NAME = "name";

	@InjectMocks
	@Spy
	private DefaultPlatformFieldSearchFacadeStrategy fieldSearchFacadeStrategy;

	@Mock
	private SearchQueryCondition condition;
	@Mock
	private SearchQueryConditionList conditionList;

	@Mock
	private PermissionFacade permissionFacade;

	@Mock
	private TypeService typeService;

	@Test
	public void isValidQueryData()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.singletonList(new Object()));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isTrue();
	}

	@Test
	public void isValidQueryWhenDataIsSingletonCollectionWithOneNull()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.singletonList(null));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsCollectionWithOneNull()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Lists.newArrayList(new Object(), null, new Object()));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsNull()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(null);

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsEmptyCollection()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.emptyList());

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsCollectionWhichContainsEmptyCollection()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.singleton(Collections.emptyList()));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsCollectionWhichContainsNull()
	{
		//given
		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.singleton(null));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(condition);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsCollectionWithNotEmptyObject()
	{
		//given
		when(conditionList.getOperator()).thenReturn(ValueComparisonOperator.AND);
		when(conditionList.getValue()).thenReturn(null);

		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.singleton(new Object()));
		when(conditionList.getConditions()).thenReturn(Lists.newArrayList(condition));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(conditionList);

		//then
		assertThat(validQueryCondition).isTrue();
	}

	@Test
	public void isValidQueryWhenDataIsNestedCollectionWithNull()
	{
		//given
		when(conditionList.getOperator()).thenReturn(ValueComparisonOperator.AND);
		when(conditionList.getValue()).thenReturn(null);

		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(null);
		when(conditionList.getConditions()).thenReturn(Lists.newArrayList(condition));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(conditionList);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void isValidQueryWhenDataIsNestedCollectionWithEmptyCollection()
	{
		//given
		when(conditionList.getOperator()).thenReturn(ValueComparisonOperator.AND);
		when(conditionList.getValue()).thenReturn(null);

		when(condition.getOperator()).thenReturn(ValueComparisonOperator.CONTAINS);
		when(condition.getValue()).thenReturn(Collections.emptyList());
		when(conditionList.getConditions()).thenReturn(Lists.newArrayList(condition));

		//when
		final boolean validQueryCondition = fieldSearchFacadeStrategy.isValidQueryCondition(conditionList);

		//then
		assertThat(validQueryCondition).isFalse();
	}

	@Test
	public void shouldNotExcludeSubTypesIfSubtypesNotIncluded()
	{
		final  List<SearchQueryCondition> conditions = new ArrayList<>();
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		//given
		when(searchQueryData.isIncludeSubtypes()).thenReturn(false);

		//when
		fieldSearchFacadeStrategy.excludeNoReadPermissionSubTypes(conditions, searchQueryData);

		//then
		assertThat(conditions).isEmpty();
	}

	@Test
	public void shouldNotExcludeSubTypesIfNoSubtypesExist()
	{
		final  List<SearchQueryCondition> conditions = new ArrayList<>();
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		//given
		when(searchQueryData.isIncludeSubtypes()).thenReturn(true);
		when(searchQueryData.getSearchType()).thenReturn(PRODUCT_CODE);
		when(typeService.getComposedTypeForCode(PRODUCT_CODE)).thenReturn(typeModel);
		when(typeModel.getAllSubTypes()).thenReturn(Collections.emptyList());

		//when
		fieldSearchFacadeStrategy.excludeNoReadPermissionSubTypes(conditions, searchQueryData);

		//then
		assertThat(conditions).isEmpty();
	}


	@Test
	public void shouldNotExcludeSubTypesIfHaveReadPermissionForAllSubtypes()
	{
		final  List<SearchQueryCondition> conditions = new ArrayList<>();

		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		final ComposedTypeModel subtypeModel1 = mock(ComposedTypeModel.class);
		final ComposedTypeModel subtypeModel2 = mock(ComposedTypeModel.class);

		//given
		when(searchQueryData.isIncludeSubtypes()).thenReturn(true);
		when(searchQueryData.getSearchType()).thenReturn(PRODUCT_CODE);
		when(typeService.getComposedTypeForCode(PRODUCT_CODE)).thenReturn(typeModel);
		when(typeModel.getAllSubTypes()).thenReturn(Arrays.asList(subtypeModel1, subtypeModel2));
		when(subtypeModel1.getCode()).thenReturn(PRODUCT_SUB1_CODE);
		when(subtypeModel2.getCode()).thenReturn(PRODUCT_SUB2_CODE);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(permissionFacade.canReadType(PRODUCT_SUB2_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(false);

		//when
		fieldSearchFacadeStrategy.excludeNoReadPermissionSubTypes(conditions, searchQueryData);

		//then
		assertThat(conditions).isEmpty();
	}

	@Test
	public void shouldExcludeNoReadPermissionSubType()
	{
		final  List<SearchQueryCondition> conditions = new ArrayList<>();
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		final ComposedTypeModel subtypeModel1 = mock(ComposedTypeModel.class);
		final ComposedTypeModel subtypeModel2 = mock(ComposedTypeModel.class);

		//given
		when(searchQueryData.isIncludeSubtypes()).thenReturn(true);
		when(searchQueryData.getSearchType()).thenReturn(PRODUCT_CODE);
		when(typeService.getComposedTypeForCode(PRODUCT_CODE)).thenReturn(typeModel);
		when(typeModel.getAllSubTypes()).thenReturn(Arrays.asList(subtypeModel1, subtypeModel2));
		when(subtypeModel1.getCode()).thenReturn(PRODUCT_SUB1_CODE);
		when(subtypeModel2.getCode()).thenReturn(PRODUCT_SUB2_CODE);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(permissionFacade.canReadType(PRODUCT_SUB2_CODE)).thenReturn(false);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(false);

		//when
		fieldSearchFacadeStrategy.excludeNoReadPermissionSubTypes(conditions, searchQueryData);

		//then
		assertThat(conditions).isNotEmpty();
		assertThat(conditions.size()).isEqualTo(1);
		final SearchQueryCondition condition = conditions.get(0);
		assertThat(condition.getDescriptor().getAttributeName()).isEqualTo(ItemModel.ITEMTYPE);
		assertThat(condition.getOperator()).isEqualTo(ValueComparisonOperator.NOT_IN);
		assertThat(condition.isFilteringCondition()).isTrue();

		final List values = (List<?>) condition.getValue();
		assertThat(values.size()).isEqualTo(1);
		assertThat(values.get(0)).isEqualTo(subtypeModel2);
	}

	@Test
	public void shouldExcludeAllNoReadPermissionSubTypes()
	{
		final  List<SearchQueryCondition> conditions = new ArrayList<>(Arrays.asList(mock(SearchQueryCondition.class)));
		final SearchQueryData searchQueryData = mock(SearchQueryData.class);
		final ComposedTypeModel typeModel = mock(ComposedTypeModel.class);
		final ComposedTypeModel subtypeModel1 = mock(ComposedTypeModel.class);
		final ComposedTypeModel subtypeModel2 = mock(ComposedTypeModel.class);

		//given
		when(searchQueryData.isIncludeSubtypes()).thenReturn(true);
		when(searchQueryData.getSearchType()).thenReturn(PRODUCT_CODE);
		when(typeService.getComposedTypeForCode(PRODUCT_CODE)).thenReturn(typeModel);
		when(typeModel.getAllSubTypes()).thenReturn(Arrays.asList(subtypeModel1, subtypeModel2));
		when(subtypeModel1.getCode()).thenReturn(PRODUCT_SUB1_CODE);
		when(subtypeModel2.getCode()).thenReturn(PRODUCT_SUB2_CODE);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(false);
		when(permissionFacade.canReadType(PRODUCT_SUB2_CODE)).thenReturn(false);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(false);

		//when
		fieldSearchFacadeStrategy.excludeNoReadPermissionSubTypes(conditions, searchQueryData);

		//then
		assertThat(conditions).isNotEmpty();
		assertThat(conditions.size()).isEqualTo(2);
		final SearchQueryCondition condition = conditions.get(1);
		assertThat(condition.getDescriptor().getAttributeName()).isEqualTo(ItemModel.ITEMTYPE);
		assertThat(condition.getOperator()).isEqualTo(ValueComparisonOperator.NOT_IN);
		assertThat(condition.isFilteringCondition()).isTrue();

		final List values = (List<?>) condition.getValue();
		assertThat(values.size()).isEqualTo(2);
		assertThat(values.get(0)).isEqualTo(subtypeModel1);
		assertThat(values.get(1)).isEqualTo(subtypeModel2);
	}

	@Test
	public void shouldExcludeSubtypeIfNoReadPermission()
	{
		//given
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(false);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, Collections.emptyList());

		//then
		assertThat(isExcluded).isTrue();
	}

	@Test
	public void shouldNotExcludeSubtypeIfAttributePermissionCheckNotEnable()
	{
		//given
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(false);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, Collections.emptyList());

		//then
		assertThat(isExcluded).isFalse();
	}

	@Test
	public void shouldNotExcludeSubtypeIfAttributeCheckEnableAndHaveNoReadPermissionAttributeExist()
	{
		//given
		final List<SearchQueryCondition> conditions = Arrays.asList(createCondition(ATTRIBUTE_CODE));
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(false);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(false);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isFalse();
	}

	@Test
	public void shouldNotExcludeSubtypeNoConditionExist()
	{
		//given
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, Collections.emptyList());

		//then
		assertThat(isExcluded).isFalse();
	}

	@Test
	public void shouldNotExcludeSubtypeIfNotHaveNoReadPermissionAttributeExist()
	{
		//given
		final List<SearchQueryCondition> conditions = Arrays.asList(createCondition(ATTRIBUTE_CODE), createCondition(ATTRIBUTE_NAME));
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_NAME)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isFalse();
	}

	@Test
	public void shouldExcludeSubtypeIfNoReadPermissionAttributeExist()
	{
		//given
		final List<SearchQueryCondition> conditions = Arrays.asList(createCondition(ATTRIBUTE_CODE), createCondition(ATTRIBUTE_NAME));
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(false);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_NAME)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isTrue();
	}

	@Test
	public void shouldExcludeSubtypeIfNoReadPermissionAttributeExistInFirstLevelHierarchyCondition()
	{
		//given
		final SearchQueryConditionList conditionList = createConditionList(Arrays.asList(createCondition(ATTRIBUTE_NAME), createCondition(ATTRIBUTE_EAN)));
		final List<SearchQueryCondition> conditions = Arrays.asList(createCondition(ATTRIBUTE_CODE), conditionList);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(false);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_NAME)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_EAN)).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isTrue();
	}

	@Test
	public void shouldExcludeSubtypeIfNoReadPermissionAttributeExistInSecondLevelHierarchyCondition()
	{
		//given
		final SearchQueryConditionList conditionList = createConditionList(Arrays.asList(createCondition(ATTRIBUTE_NAME), createCondition(ATTRIBUTE_EAN)));
		final List<SearchQueryCondition> conditions = Arrays.asList(createCondition(ATTRIBUTE_CODE), conditionList);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_NAME)).thenReturn(false);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_EAN)).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isTrue();
	}

	@Test
	public void shouldExcludeSubtypeIfNoReadPermissionAttributeExistInThirdLevelHierarchyCondition()
	{
		//given
		final SearchQueryConditionList thirdLevelConditionList = createConditionList(Arrays.asList(createCondition(ATTRIBUTE_NAME), createCondition(ATTRIBUTE_EAN)));
		final SearchQueryConditionList secondLevelConditionList = createConditionList(Arrays.asList(createCondition(ATTRIBUTE_CODE), thirdLevelConditionList));
		final List<SearchQueryCondition> conditions = Arrays.asList(secondLevelConditionList);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_NAME)).thenReturn(false);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_EAN)).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isTrue();
	}

	@Test
	public void shouldNotExcludeSubtypeIfNoReadPermissionAttributeNotExistInHierarchyCondition()
	{
		//given
		final SearchQueryConditionList thirdLevelConditionList = createConditionList(Arrays.asList(createCondition(ATTRIBUTE_NAME), createCondition(ATTRIBUTE_EAN)));
		final SearchQueryConditionList secondLevelConditionList = createConditionList(Arrays.asList(createCondition(ATTRIBUTE_CODE), thirdLevelConditionList));
		final List<SearchQueryCondition> conditions = Arrays.asList(secondLevelConditionList);
		when(permissionFacade.canReadType(PRODUCT_SUB1_CODE)).thenReturn(true);
		when(fieldSearchFacadeStrategy.isAttributePermissionCheckEnabled()).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_CODE)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_NAME)).thenReturn(true);
		when(permissionFacade.canReadProperty(PRODUCT_SUB1_CODE, ATTRIBUTE_EAN)).thenReturn(true);

		//when
		final boolean isExcluded = fieldSearchFacadeStrategy.isNeedToExcludeNoReadPermissionSubType(PRODUCT_SUB1_CODE, conditions);

		//then
		assertThat(isExcluded).isFalse();
	}

	private SearchQueryCondition createCondition(final String attribute)
	{
		final SearchQueryCondition condition = new SearchQueryCondition();
		condition.setDescriptor(new SearchAttributeDescriptor(attribute));
		return condition;
	}

	private SearchQueryConditionList createConditionList(final List<SearchQueryCondition> conditions)
	{
		return new SearchQueryConditionList(ValueComparisonOperator.CONTAINS, conditions);
	}
}