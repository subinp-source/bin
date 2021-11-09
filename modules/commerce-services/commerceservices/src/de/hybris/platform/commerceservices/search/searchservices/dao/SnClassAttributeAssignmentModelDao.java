/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.dao;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.Optional;


/**
 * The {@link ClassAttributeAssignmentModel} DAO.
 */
public interface SnClassAttributeAssignmentModelDao extends GenericDao<ClassAttributeAssignmentModel>
{
	/**
	 * Finds the classification class attribute assignment for the given class and attribute.
	 *
	 * @param classificationClass     - the classification class
	 * @param classificationAttribute - the classification attribute
	 * @return the classification class attribute assignment
	 */
	Optional<ClassAttributeAssignmentModel> findClassAttributeAssignmentByClassAndAttribute(
			ClassificationClassModel classificationClass, ClassificationAttributeModel classificationAttribute);
}
