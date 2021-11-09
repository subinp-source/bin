/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.intf;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;


/**
 * Provide logic to access classification attribute description
 */
public interface ClassificationAttributeDescriptionAccess
{
	/**
	 * Get the description for the given Classification Attribute
	 *
	 * @param classificationAttribute
	 * @return description
	 */
	String getDescription(final ClassificationAttributeModel classificationAttribute);
}
