/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;


@RunWith(MockitoJUnitRunner.class)
public class DefaultClassFeatureGroupAssignmentServiceTest
{

	private static final String UNIQUE_FEATURE_GROUP_ASSIGNMENT_TEST_QUERY = "select {cfga.pk} from {ClassFeatureGroupAssignment as cfga} where {cfga.classAttributeAssignment} in ({{select {c.pk} from {ClassAttributeAssignment as c} where {c.pk} = ?attributePk}}) and {cfga.classificationClass} in ({{ select {cc.pk} from {ClassificationClass as cc} where {cc.pk} = ?categoryPk}})";
	private static final String FEATURE_GROUP_ASSIGNMENTS_TEST_QUERY = "select {cfga.pk} from {ClassFeatureGroupAssignment as cfga} where {cfga.classAttributeAssignment} in ({{select {c.pk} from {ClassAttributeAssignment as c} where {c.pk} = ?pk}})";
	private static final String FEATURE_GROUP_ASSIGNMENTS_FROM_ATTRIBUTE_TEST_QUERY = "select {caa.pk} from {ClassAttributeAssignment as caa} where {caa.classificationAttribute}  = ?pk";
	@Mock
	ModelService modelService;
	@Mock
	FlexibleSearchService flexibleSearchService;
	@Mock
	InterceptorContext interceptorContext;
	@Mock
	ClassificationClassModel classificationClass;
	@InjectMocks
	@Spy
	DefaultClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;

	@Test
	public void shouldCreateNewFeatureGroupAssignment()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		given(classificationClass.getCode()).willReturn("category");
		given(modelService.create(ClassFeatureGroupAssignmentModel.class)).willReturn(new ClassFeatureGroupAssignmentModel());

		//when
		final ClassFeatureGroupAssignmentModel result = classFeatureGroupAssignmentService
				.createClassFeatureGroupAssignment(classAttributeAssignment, classificationClass);

		//then
		assertThat(result.getClassAttributeAssignment()).isEqualTo(classAttributeAssignment);
		assertThat(result.getClassificationClass()).isEqualTo(classificationClass);
	}

	@Test
	public void shouldSearchForUniqueClassFeatureGroupAssignment()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final PK attributeAssignmentPK = PK.parse("12345");
		final PK classificationClassPK = PK.parse("56789");
		given(classAttributeAssignment.getPk()).willReturn(attributeAssignmentPK);
		given(classificationClass.getPk()).willReturn(classificationClassPK);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(UNIQUE_FEATURE_GROUP_ASSIGNMENT_TEST_QUERY);
		query.addQueryParameter("attributePk", attributeAssignmentPK);
		query.addQueryParameter("categoryPk", classificationClassPK);
		given(flexibleSearchService.searchUnique(any())).willReturn(mock(ClassFeatureGroupAssignmentModel.class));

		//when
		classFeatureGroupAssignmentService.findFeatureGroupAssignment(classAttributeAssignment, classificationClass);

		//then
		then(flexibleSearchService).should().searchUnique(query);
	}

	@Test
	public void shouldSearchAllFeatureGroupAssignmentForClassAttributeAssignment()
	{
		//given
		final PK attributeAssignmentPK = PK.parse("12345");
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		given(classAttributeAssignment.getPk()).willReturn(attributeAssignmentPK);
		given(flexibleSearchService.search(any(), any())).willReturn(mock(SearchResult.class));

		//when
		classFeatureGroupAssignmentService.findAllFeatureGroupAssignments(classAttributeAssignment);

		//then
		then(flexibleSearchService).should().search(eq(FEATURE_GROUP_ASSIGNMENTS_TEST_QUERY),
				eq(Map.of("pk", attributeAssignmentPK)));
	}

	@Test
	public void shouldCreateFeatureGroupAssignmentsForNewClassificationClass()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = mock(ClassFeatureGroupAssignmentModel.class);
		given(modelService.isNew(classificationClass)).willReturn(true);
		given(classificationClass.getAllSubcategories()).willReturn(List.of());
		doReturn(classFeatureGroupAssignment).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(classAttributeAssignment, classificationClass);

		//when
		classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(interceptorContext, classificationClass,
				classAttributeAssignment);

		//then
		then(interceptorContext).should().registerElementFor(classFeatureGroupAssignment, PersistenceOperation.SAVE);
	}

	@Test
	public void shouldNotCreateFeatureGroupAssignemntsIfItIsAlreadyAssignedToClassFeatureAssignment()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = mock(ClassFeatureGroupAssignmentModel.class);

		given(modelService.isNew(classificationClass)).willReturn(false);
		given(modelService.isNew(classAttributeAssignment)).willReturn(false);
		doReturn(Optional.of(classFeatureGroupAssignment)).when(classFeatureGroupAssignmentService)
				.findFeatureGroupAssignment(classAttributeAssignment, classificationClass);

		//when
		classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(interceptorContext, classificationClass,
				classAttributeAssignment);

		//then
		then(interceptorContext).should(never()).registerElementFor(any(), any());
	}

	@Test
	public void shouldCreateFeatureGroupsAssignmentsForAllSubCategories()
	{
		//given
		final ClassAttributeAssignmentModel classAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassificationClassModel subCategory = mock(ClassificationClassModel.class);
		final ClassFeatureGroupAssignmentModel subCategoryClassFeatureGroupAssignment = mock(
				ClassFeatureGroupAssignmentModel.class);

		given(modelService.isNew(classificationClass)).willReturn(false);
		given(modelService.isNew(classAttributeAssignment)).willReturn(false);
		doReturn(Optional.empty()).when(classFeatureGroupAssignmentService).findFeatureGroupAssignment(classAttributeAssignment,
				classificationClass);
		given(classificationClass.getAllSubcategories()).willReturn(List.of(subCategory));
		doReturn(classFeatureGroupAssignment).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(classAttributeAssignment, classificationClass);
		given(modelService.isNew(subCategory)).willReturn(false);
		doReturn(Optional.empty()).when(classFeatureGroupAssignmentService).findFeatureGroupAssignment(classAttributeAssignment,
				subCategory);
		doReturn(subCategoryClassFeatureGroupAssignment).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(classAttributeAssignment, subCategory);

		//when
		classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(interceptorContext, classificationClass,
				classAttributeAssignment);

		//then
		then(interceptorContext).should().registerElementFor(classFeatureGroupAssignment, PersistenceOperation.SAVE);
		then(interceptorContext).should().registerElementFor(subCategoryClassFeatureGroupAssignment, PersistenceOperation.SAVE);
	}

	@Test
	public void shouldRemoveAllFeatureGroupAssignments()
	{
		//given
		final ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassAttributeAssignmentModel notRemovedAttribute = mock(ClassAttributeAssignmentModel.class);
		final ClassAttributeAssignmentModel removedAttribute = mock(ClassAttributeAssignmentModel.class);
		final ItemModelContext itemModelContext = mock(ItemModelContext.class);

		given(classificationClass.getDeclaredClassificationAttributeAssignments()).willReturn(List.of(notRemovedAttribute));
		given(classificationClass.getItemModelContext()).willReturn(itemModelContext);
		given(itemModelContext.getOriginalValue("declaredClassificationAttributeAssignments"))
				.willReturn(List.of(notRemovedAttribute, removedAttribute));
		doReturn(List.of(classFeatureGroupAssignment)).when(classFeatureGroupAssignmentService)
				.findAllFeatureGroupAssignments(removedAttribute);

		//when
		classFeatureGroupAssignmentService.removeFeatureGroupAssignments(interceptorContext, classificationClass);

		//then
		then(interceptorContext).should().registerElementFor(classFeatureGroupAssignment, PersistenceOperation.DELETE);
	}

	@Test
	public void shouldFindUnassignedSupercategories()
	{
		//given
		final ClassificationClassModel superCategory1 = mock(ClassificationClassModel.class);
		final ClassificationClassModel unassignedCategory1 = mock(ClassificationClassModel.class);
		final ClassificationClassModel unassignedCategory2 = mock(ClassificationClassModel.class);
		final ItemModelContext itemModelContext = mock(ItemModelContext.class);
		given(classificationClass.getSupercategories()).willReturn(List.of(superCategory1));
		given(classificationClass.getItemModelContext()).willReturn(itemModelContext);
		given(itemModelContext.getOriginalValue("supercategories"))
				.willReturn(List.of(unassignedCategory1, unassignedCategory2, superCategory1));

		//when
		final List<ClassificationClassModel> result = classFeatureGroupAssignmentService
				.findUnassignedSupercategories(classificationClass);

		//then
		assertThat(result).containsExactlyInAnyOrder(unassignedCategory1, unassignedCategory2);
	}

	@Test
	public void shouldRemoveNotInheritedFeatureGroupAssignments()
	{
		//given
		final ClassAttributeAssignmentModel attributeAssignmentForRemoval = mock(ClassAttributeAssignmentModel.class);
		final ClassAttributeAssignmentModel attributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel subCategory = mock(ClassificationClassModel.class);
		final ClassFeatureGroupAssignmentModel featureGroupAssignmentModel = mock(ClassFeatureGroupAssignmentModel.class);

		given(classificationClass.getAllSubcategories()).willReturn(List.of(subCategory));
		given(subCategory.getAllClassificationAttributeAssignments()).willReturn(List.of(attributeAssignment));
		doReturn(Optional.of(featureGroupAssignmentModel)).when(classFeatureGroupAssignmentService)
				.findFeatureGroupAssignment(attributeAssignmentForRemoval, subCategory);
		//when
		classFeatureGroupAssignmentService.removeFeatureGroupAssignmentsInSubCategories(interceptorContext, classificationClass,
				List.of(attributeAssignment, attributeAssignmentForRemoval));

		//then
		then(interceptorContext).should().registerElementFor(featureGroupAssignmentModel, PersistenceOperation.DELETE);
	}

	@Test
	public void shouldCreateFeatureGroupAssignmentsForSubcategories()
	{
		//given
		final ClassFeatureGroupAssignmentModel featureGroupAssignment1 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel featureGroupAssignment2 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassAttributeAssignmentModel attributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel subCategory1 = mock(ClassificationClassModel.class);
		final ClassificationClassModel subCategory2 = mock(ClassificationClassModel.class);

		doReturn(featureGroupAssignment1).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(attributeAssignment, subCategory1);
		doReturn(featureGroupAssignment2).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(attributeAssignment, subCategory2);
		given(attributeAssignment.getClassificationClass()).willReturn(classificationClass);
		given(classificationClass.getAllSubcategories()).willReturn(List.of(subCategory1, subCategory2));

		//when
		classFeatureGroupAssignmentService.createGroupAssignmentsForSubcategories(interceptorContext, attributeAssignment);

		//then
		then(interceptorContext).should().registerElementFor(featureGroupAssignment1, PersistenceOperation.SAVE);
		then(interceptorContext).should().registerElementFor(featureGroupAssignment2, PersistenceOperation.SAVE);
	}

	@Test
	public void shouldNotCreateFeatureGroupAssignmentsIfModelIsRegisteredInContext()
	{
		//given
		final ClassFeatureGroupAssignmentModel featureGroupAssignment1 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel featureGroupAssignment2 = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassFeatureGroupAssignmentModel registeredGroupAssignment = mock(ClassFeatureGroupAssignmentModel.class);
		final ClassAttributeAssignmentModel attributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel subCategory1 = mock(ClassificationClassModel.class);
		final ClassificationClassModel subCategory2 = mock(ClassificationClassModel.class);

		doReturn(featureGroupAssignment1).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(attributeAssignment, subCategory1);
		doReturn(featureGroupAssignment2).when(classFeatureGroupAssignmentService)
				.createClassFeatureGroupAssignment(attributeAssignment, subCategory2);
		given(registeredGroupAssignment.getClassificationClass()).willReturn(subCategory1);
		given(registeredGroupAssignment.getClassAttributeAssignment()).willReturn(attributeAssignment);
		given(featureGroupAssignment2.getClassificationClass()).willReturn(subCategory1);
		given(featureGroupAssignment2.getClassAttributeAssignment()).willReturn(attributeAssignment);
		given(interceptorContext.getElementsRegisteredFor(PersistenceOperation.SAVE)).willReturn(Set.of(registeredGroupAssignment));
		given(attributeAssignment.getClassificationClass()).willReturn(classificationClass);
		given(classificationClass.getAllSubcategories()).willReturn(List.of(subCategory1, subCategory2));

		//when
		classFeatureGroupAssignmentService.createGroupAssignmentsForSubcategories(interceptorContext, attributeAssignment);

		//then
		then(interceptorContext).should().registerElementFor(featureGroupAssignment1, PersistenceOperation.SAVE);
	}

	@Test
	public void shouldRemoveClassFeatureGroupAssignmentsRelatedWithClassificationAttribute()
	{
		//given
		final PK classificationAttributePK = PK.parse("12345");
		final ClassificationAttributeModel classificationAttribute = mock(ClassificationAttributeModel.class);
		final SearchResult searchResult = mock(SearchResult.class);
		final ClassAttributeAssignmentModel correctClassAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassAttributeAssignmentModel incorrectClassAttributeAssignment = mock(ClassAttributeAssignmentModel.class);
		final ClassificationClassModel correctClassificationClass = mock(ClassificationClassModel.class);
		final ClassificationClassModel incorrectClassificationClass = mock(ClassificationClassModel.class);

		given(classificationAttribute.getPk()).willReturn(classificationAttributePK);
		given(flexibleSearchService.search(any(), any())).willReturn(searchResult);
		given(searchResult.getResult()).willReturn(List.of(correctClassAttributeAssignment, incorrectClassAttributeAssignment));
		given(correctClassAttributeAssignment.getClassificationClass()).willReturn(correctClassificationClass);
		given(incorrectClassAttributeAssignment.getClassificationClass()).willReturn(incorrectClassificationClass);
		doReturn(true).when(classFeatureGroupAssignmentService).isInstanceOfClassificationClass(correctClassificationClass);
		doReturn(false).when(classFeatureGroupAssignmentService).isInstanceOfClassificationClass(incorrectClassificationClass);
		doNothing().when(classFeatureGroupAssignmentService).removeAllFeatureGroupAssignments(any(InterceptorContext.class),
				anyList());

		//when
		classFeatureGroupAssignmentService.removeAllFeatureGroupAssignments(interceptorContext, classificationAttribute);

		//then
		then(flexibleSearchService).should().search(eq(FEATURE_GROUP_ASSIGNMENTS_FROM_ATTRIBUTE_TEST_QUERY),
				eq(Map.of("pk", classificationAttributePK)));
		then(classFeatureGroupAssignmentService).should().removeAllFeatureGroupAssignments(interceptorContext,
				List.of(correctClassAttributeAssignment));
	}
}
