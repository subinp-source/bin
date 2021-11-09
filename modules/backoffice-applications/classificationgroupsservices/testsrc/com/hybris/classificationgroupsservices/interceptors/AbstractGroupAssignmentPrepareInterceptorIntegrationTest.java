/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.classificationgroupsservices.interceptors;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;


public abstract class AbstractGroupAssignmentPrepareInterceptorIntegrationTest extends ServicelayerTest
{
	protected static final String OBJECT_CATEGORY = "Object";
	protected static final String ITEM_CATEGORY = "Item";
	protected static final String DIMENSIONS_ATTRIBUTE = "Dimensions";
	protected static final String WEIGHT_ATTRIBUTE = "Weight";
	protected static final String PHONE_CATEGORY = "Phone";
	protected static final String SAMSUNG_CATEGORY = "Samsung";
	protected static final String DEVICE_CATEGORY = "Device";
	protected static final String PRICE_ATTRIBUTE = "Price";
	protected static final String TOUCH_ID_ATTRIBUTE = "TouchId";
	protected static final String RAM_ATTRIBUTE = "RAM";

	private  static final Pattern PATTERN = Pattern.compile(
			"(?<catalogId>[^/]+)/(?<systemVersion>[^/]+)/(?<classificationClassCode>[^.]+)\\.(?<classificationAttributeCode>.+)");

	private static final String FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT = String.format(
			"SELECT {caa:%s} FROM " + "{%s as caa JOIN %s as cc ON {cc:%s} = {caa:%s} JOIN %s as ca ON {caa:%s} = {ca:%s} "
					+ "JOIN %s as csv ON {caa:%s} = {csv:%s} JOIN %s as c ON {csv:%s} = {c:%s}} "
					+ "WHERE {cc:%s} = ?%s AND LOWER({ca:%s}) = ?%s AND {csv:%s} = ?%s AND {c:%s} = ?%s",
			ClassAttributeAssignmentModel.PK, ClassAttributeAssignmentModel._TYPECODE, ClassificationClassModel._TYPECODE,
			ClassificationClassModel.PK, ClassAttributeAssignmentModel.CLASSIFICATIONCLASS, ClassificationAttributeModel._TYPECODE,
			ClassAttributeAssignmentModel.CLASSIFICATIONATTRIBUTE, ClassificationAttributeModel.PK,
			ClassificationSystemVersionModel._TYPECODE, ClassAttributeAssignmentModel.SYSTEMVERSION,
			ClassificationSystemVersionModel.PK, CatalogModel._TYPECODE, ClassificationSystemVersionModel.CATALOG, CatalogModel.PK,
			ClassificationClassModel.CODE, ClassificationClassModel._TYPECODE + ClassificationClassModel.CODE,
			ClassificationAttributeModel.CODE, ClassificationAttributeModel._TYPECODE + ClassificationAttributeModel.CODE,
			ClassificationSystemVersionModel.VERSION, ClassificationSystemVersionModel.VERSION, CatalogModel.ID, CatalogModel.ID);

	@Resource
	protected ModelService modelService;
	@Resource
	protected FlexibleSearchService flexibleSearchService;
	@Resource
	protected CategoryService categoryService;
	@Resource
	protected CatalogVersionService catalogVersionService;

	protected void assertClassFeatureGroupAssignments(final Pair<String, List<String>> pair,
			final List<ClassFeatureGroupAssignmentModel> groupAssignments)
	{
		final List<String> classificationAttributeCodes = groupAssignments.stream()
				.filter(groupAssignment -> groupAssignment.getClassificationClass().getCode().equals(pair.getLeft()))
				.map(groupAssignment -> groupAssignment.getClassAttributeAssignment().getClassificationAttribute().getCode())
				.collect(Collectors.toList());
		assertThat(pair.getRight()).containsExactlyInAnyOrder(classificationAttributeCodes.toArray(new String[0]));
	}

	protected void createCategory(final CatalogVersionModel catalogVersion, final String categoryCode,
			final List<CategoryModel> categories)
	{
		final ClassificationClassModel classificationClass = modelService.create(ClassificationClassModel.class);
		classificationClass.setCatalogVersion(catalogVersion);
		classificationClass.setCode(categoryCode);
		classificationClass.setSupercategories(categories);
		modelService.save(classificationClass);
	}

	protected List<ClassFeatureGroupAssignmentModel> finaAllClassFeatureGroupAssignments()
	{
		final SearchResult<ClassFeatureGroupAssignmentModel> searchResult = flexibleSearchService
				.search("select {pk} from {ClassFeatureGroupAssignment}");
		return searchResult.getResult();
	}

	protected ClassAttributeAssignmentModel findClassAttributeAssignment(final String classificationAttributeQualifier)
	{
		final Matcher matcher = PATTERN.matcher(classificationAttributeQualifier);

		if (matcher.find())
		{
			final String catalogId = matcher.group("catalogId");
			final String systemVersion = matcher.group("systemVersion");
			final String classificationClassCode = matcher.group("classificationClassCode");
			final String classificationAttributeCode = matcher.group("classificationAttributeCode");
			if (StringUtils.isNotBlank(catalogId) && StringUtils.isNotBlank(systemVersion)
					&& StringUtils.isNotBlank(classificationClassCode) && StringUtils.isNotBlank(classificationAttributeCode))
			{

				return findClassAttributeAssignment(catalogId, systemVersion, classificationClassCode, classificationAttributeCode);
			}
		}
		return null;
	}

	protected ClassAttributeAssignmentModel findClassAttributeAssignment(final String catalogId, final String systemVersionId,
																	   final String classificationClassCode, final String attributeCode)
	{
		return getClassificationAttributeAssignment(catalogId, systemVersionId,
				classificationClassCode, attributeCode);
	}

	protected ClassAttributeAssignmentModel getClassificationAttributeAssignment(final String catalogId, final String systemVersion,
																			   final String classificationClass, final String attribute)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT);
		query.addQueryParameter(CatalogModel.ID, catalogId);
		query.addQueryParameter(ClassificationSystemVersionModel.VERSION, systemVersion);
		query.addQueryParameter(ClassificationClassModel._TYPECODE + ClassificationClassModel.CODE, classificationClass);
		query.addQueryParameter(ClassificationAttributeModel._TYPECODE + ClassificationAttributeModel.CODE,
				attribute.toLowerCase(Locale.US));
		return flexibleSearchService.searchUnique(query);
	}
}
