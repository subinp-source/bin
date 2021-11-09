/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.impex.jalo.ImpExException;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;


@IntegrationTest
public class ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptorIntegrationTest
		extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
	private static final String IPHONE_CATEGORY = "Iphone";

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
	}

	@Test
	public void shouldCreateGroupAssignmentsForNewCategory()
	{
		//given
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("classAttributeAssignmentCatalog",
				"Staged");
		final CategoryModel phoneCategory = categoryService.getCategoryForCode(PHONE_CATEGORY);

		//when
		createCategory(catalogVersion, IPHONE_CATEGORY, List.of(phoneCategory));

		//then
		final List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
		assertClassFeatureGroupAssignments(new ImmutablePair<>(DEVICE_CATEGORY, List.of(PRICE_ATTRIBUTE)), groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(PHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(new ImmutablePair<>(IPHONE_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE)),
				groupAssignments);
		assertClassFeatureGroupAssignments(
				new ImmutablePair<>(SAMSUNG_CATEGORY, List.of(PRICE_ATTRIBUTE, RAM_ATTRIBUTE, TOUCH_ID_ATTRIBUTE)), groupAssignments);
		assertThat(groupAssignments).hasSize(8);
	}
}
