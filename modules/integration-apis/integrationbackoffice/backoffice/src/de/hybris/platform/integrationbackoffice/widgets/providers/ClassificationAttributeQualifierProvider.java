/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.providers;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;


/**
 * Provides full qualifier for classification attribute.
 */
public interface ClassificationAttributeQualifierProvider
{
	/**
	 * Provides full qualifier for given attribute assignment
	 *
	 * @param assignment
	 * @return string which represents full qualifier for given attribute assignment
	 */
	String provide(ClassAttributeAssignmentModel assignment);
}
