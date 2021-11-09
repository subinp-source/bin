/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.dao.impl;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.commerceservices.search.searchservices.dao.SnClassAttributeAssignmentModelDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of {@link SnClassAttributeAssignmentModelDao}.
 */
public class DefaultSnClassAttributeAssignmentModelDao extends DefaultGenericDao<ClassAttributeAssignmentModel>
		implements SnClassAttributeAssignmentModelDao
{
	public DefaultSnClassAttributeAssignmentModelDao()
	{
		super(ClassAttributeAssignmentModel._TYPECODE);
	}

	@Override
	public Optional<ClassAttributeAssignmentModel> findClassAttributeAssignmentByClassAndAttribute(
			final ClassificationClassModel classificationClass, final ClassificationAttributeModel classificationAttribute)
	{
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(ClassAttributeAssignmentModel.CLASSIFICATIONCLASS, classificationClass);
		queryParams.put(ClassAttributeAssignmentModel.CLASSIFICATIONATTRIBUTE, classificationAttribute);

		final List<ClassAttributeAssignmentModel> classAttributeAssignments = find(queryParams);

		return CollectionUtils.isEmpty(classAttributeAssignments) ?
				Optional.empty() :
				Optional.of(classAttributeAssignments.get(0));
	}
}
