/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import java.util.Optional;

/**
 * Provides operations to interact with {@link ClassificationAttributeValueModel}s
 */
public interface ClassificationAttributeValueService
{
	/**
	 * Finds the {@link ClassificationAttributeValueModel} by the given system version and code
	 * @param  systemVersion System version the attribute value is associated with
	 * @param valueCode The code of the attribute value to find
	 * @return Optional contains the result if the attribute value exists, otherwise {@link Optional#empty()}
	 */
	Optional<ClassificationAttributeValueModel> find(ClassificationSystemVersionModel systemVersion, String valueCode);

	/**
	 * Creates the {@link ClassificationAttributeValueModel} with the given system version and code
	 * @param systemVersion System version the attribute value is associated with
	 * @param valueCode The code of the attribute to create
	 * @return The created attribute value
	 */
	ClassificationAttributeValueModel create(ClassificationSystemVersionModel systemVersion, String valueCode);
}
