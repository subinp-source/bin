/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;


@IntegrationTest
public class ClassificationClassSupercategoriesUnassigningPrepareInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldRemoveGroupAssignmentsIfUserReplaceSupercategory()
	{
		//given
		final CategoryModel samsungCategory = categoryService.getCategoryForCode(SAMSUNG_CATEGORY);
		final CategoryModel deviceCategory = categoryService.getCategoryForCode(DEVICE_CATEGORY);

		//when
		samsungCategory.setSupercategories(List.of(deviceCategory));
		modelService.save(samsungCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(5);
	}

	@Test
	public void shouldRemoveGroupAssignmentsIfUserUnassignsSupercategory()
	{
		//given
		final CategoryModel samsungCategory = categoryService.getCategoryForCode(SAMSUNG_CATEGORY);

		//when
		samsungCategory.setSupercategories(List.of());
		modelService.save(samsungCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(TOUCH_ID_ATTRIBUTE)), groupAssignments);
		assertThat(groupAssignments).hasSize(4);
	}

	@Test
	public void shouldRemoveGroupAssignmentsInSubcategoriesDuringEditingSupercategories()
	{
		//given
		final CategoryModel phoneCategory = categoryService.getCategoryForCode(PHONE_CATEGORY);

		//when
		phoneCategory.setSupercategories(List.of());
		modelService.save(phoneCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(RAM_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(TOUCH_ID_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(4);
	}

	@Test
	public void shouldNotCreateDuplicateGroupAssignments()
	{
		//given
		final CategoryModel deviceCategory = categoryService.getCategoryForCode(DEVICE_CATEGORY);
		final CategoryModel phoneCategory = categoryService.getCategoryForCode(PHONE_CATEGORY);
		final CategoryModel samsungCategory = categoryService.getCategoryForCode(SAMSUNG_CATEGORY);

		//when
		samsungCategory.setSupercategories(List.of(deviceCategory, phoneCategory));
		modelService.save(phoneCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(
				new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE, RAM_ATTRIBUTE)), groupAssignments);
		assertThat(groupAssignments).hasSize(6);
	}

	@Test
	public void shouldNotRemoveGroupAssignmentsDuringRemovingSupercategoryWithDuplicateAttributeAssignment()
	{
		//given
		final CategoryModel deviceCategory = categoryService.getCategoryForCode(DEVICE_CATEGORY);
		final CategoryModel phoneCategory = categoryService.getCategoryForCode(PHONE_CATEGORY);
		final CategoryModel samsungCategory = categoryService.getCategoryForCode(SAMSUNG_CATEGORY);
		samsungCategory.setSupercategories(List.of(deviceCategory, phoneCategory));
		modelService.save(samsungCategory);

		//when
		samsungCategory.setSupercategories(List.of(deviceCategory));
		modelService.save(samsungCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(5);
	}

	@Test
	public void shouldNotRemoveClassFeatureGroupAssignmentInSubCategoryWhileDuplicateSupercategories() throws ImpExException
	{
		//given
		importCsv("/impex/test/shouldNotRemoveClassFeatureGroupAssignmentInSubCategoryWhileDuplicateSupercategories.impex",
				"UTF-8");

		final ClassificationClassModel samsungCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(SAMSUNG_CATEGORY);
		final ClassificationClassModel phoneCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(PHONE_CATEGORY);
		final ClassificationClassModel objectCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(OBJECT_CATEGORY);
		final ClassificationClassModel deviceCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(DEVICE_CATEGORY);
		final ClassFeatureGroupAssignmentModel samsungDimensionsFeatureGroupAssignment = findClassFeatureGroupAssignment(
				objectCategory.getDeclaredClassificationAttributeAssignments().get(0), samsungCategory);
		createClassFeatureGroup(samsungCategory, List.of(samsungDimensionsFeatureGroupAssignment), "samsungGroup");

		//when
		final ClassificationClassModel samsungCategoryUpdated = (ClassificationClassModel) categoryService
				.getCategoryForCode(SAMSUNG_CATEGORY);
		samsungCategoryUpdated.setSupercategories(List.of(phoneCategory, objectCategory));
		modelService.save(samsungCategoryUpdated);
		deviceCategory.setSupercategories(List.of());
		modelService.save(deviceCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(SAMSUNG_CATEGORY,
				List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE, RAM_ATTRIBUTE, DIMENSIONS_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(OBJECT_CATEGORY, List.of(DIMENSIONS_ATTRIBUTE)), groupAssignments);
		assertThat(groupAssignments).hasSize(8);
		assertThat(findClassGroupFeature("samsungGroup").getClassFeatureGroupAssignments()).hasSize(1);
	}

	@Test
	public void shouldNotRemoveClassFeatureGroupAssignmentFromGroupDuringMergingSupercategories() throws ImpExException
	{
		//given
		importCsv("/impex/test/shouldNotRemoveClassFeatureGroupAssignmentFromGroupDuringMergingSupercategories.impex", "UTF-8");

		final ClassificationClassModel samsungCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(SAMSUNG_CATEGORY);
		final ClassificationClassModel phoneCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(PHONE_CATEGORY);
		final ClassificationClassModel objectCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(OBJECT_CATEGORY);
		final ClassificationClassModel deviceCategory = (ClassificationClassModel) categoryService
				.getCategoryForCode(DEVICE_CATEGORY);
		final ClassificationClassModel itemCategory = (ClassificationClassModel) categoryService.getCategoryForCode(ITEM_CATEGORY);

		final ClassFeatureGroupAssignmentModel samsungPriceFeatureGroupAssignment = findClassFeatureGroupAssignment(
				findClassAttributeAssignment(PRICE_ATTRIBUTE, deviceCategory), samsungCategory);
		final ClassFeatureGroupAssignmentModel samsungDimensionsFeatureGroupAssignment = findClassFeatureGroupAssignment(
				findClassAttributeAssignment(DIMENSIONS_ATTRIBUTE, objectCategory), samsungCategory);
		final ClassFeatureGroupAssignmentModel samsungWeightFeatureGroupAssignment = findClassFeatureGroupAssignment(
				findClassAttributeAssignment(WEIGHT_ATTRIBUTE, itemCategory), samsungCategory);
		final ClassFeatureGroupAssignmentModel samsungRamFeatureGroupAssignment = findClassFeatureGroupAssignment(
				findClassAttributeAssignment(RAM_ATTRIBUTE, phoneCategory), samsungCategory);
		final ClassFeatureGroupAssignmentModel samsungTouchIdFeatureGroupAssignment = findClassFeatureGroupAssignment(
				findClassAttributeAssignment(TOUCH_ID_ATTRIBUTE, samsungCategory), samsungCategory);
		createClassFeatureGroup(samsungCategory,
				List.of(samsungPriceFeatureGroupAssignment, samsungDimensionsFeatureGroupAssignment,
						samsungWeightFeatureGroupAssignment, samsungRamFeatureGroupAssignment, samsungTouchIdFeatureGroupAssignment),
				"samsungGroup");

		//when	
		deviceCategory.setSupercategories(List.of(objectCategory, itemCategory));
		modelService.save(deviceCategory);
		samsungCategory.setSupercategories(List.of(phoneCategory));
		modelService.save(samsungCategory);

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(
				new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE, WEIGHT_ATTRIBUTE, DIMENSIONS_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(
				new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE, WEIGHT_ATTRIBUTE, DIMENSIONS_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(
				new ImmutablePair<>(SAMSUNG_CATEGORY,
						List.of(PRICE_ATTRIBUTE, TOUCH_ID_ATTRIBUTE, RAM_ATTRIBUTE, DIMENSIONS_ATTRIBUTE, WEIGHT_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(OBJECT_CATEGORY, List.of(DIMENSIONS_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(ITEM_CATEGORY, List.of(DIMENSIONS_ATTRIBUTE, WEIGHT_ATTRIBUTE)),
				groupAssignments);
		assertThat(groupAssignments).hasSize(15);
		assertThat(findClassGroupFeature("samsungGroup").getClassFeatureGroupAssignments()).hasSize(5);
	}

	private void createClassFeatureGroup(final ClassificationClassModel samsungCategory,
			final List<ClassFeatureGroupAssignmentModel> featureGorupAssignments, final String groupCode)
	{
		final ClassFeatureGroupModel samsungGroup = modelService.create(ClassFeatureGroupModel.class);
		samsungGroup.setClassificationClass(samsungCategory);
		samsungGroup.setCode(groupCode);
		samsungGroup.setClassFeatureGroupAssignments(featureGorupAssignments);
		modelService.save(samsungGroup);
	}

	protected ClassFeatureGroupModel findClassGroupFeature(final String groupCode)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("select {pk} from {ClassFeatureGroup} where {code} = ?code");
		fQuery.addQueryParameter("code", groupCode);
		return flexibleSearchService.searchUnique(fQuery);
	}

	protected ClassFeatureGroupAssignmentModel findClassFeatureGroupAssignment(
			final ClassAttributeAssignmentModel classAttributeAssignment, final ClassificationClassModel category)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"select {pk} from {ClassFeatureGroupAssignment} where {classAttributeAssignment} = ?classAttributeAssignment and {classificationClass} = ?classificationClass");
		fQuery.addQueryParameter("classificationClass", category.getPk());
		fQuery.addQueryParameter("classAttributeAssignment", classAttributeAssignment.getPk());
		return flexibleSearchService.searchUnique(fQuery);
	}

	protected ClassAttributeAssignmentModel findClassAttributeAssignment(final String attributeCode,
			final ClassificationClassModel category)
	{
		final FlexibleSearchQuery attributeQuery = new FlexibleSearchQuery(
				"select {pk} from {ClassificationAttribute} where {code} = ?attributeCode");
		attributeQuery.addQueryParameter("attributeCode", attributeCode);
		final ClassificationAttributeModel classificationAttribute = flexibleSearchService.searchUnique(attributeQuery);

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"select {pk} from {ClassAttributeAssignment} where {classificationClass} = ?classificationClass and {classificationAttribute} = ?classificationAttribute");
		fQuery.addQueryParameter("classificationClass", category.getPk());
		fQuery.addQueryParameter("classificationAttribute", classificationAttribute.getPk());
		return flexibleSearchService.searchUnique(fQuery);
	}
}
