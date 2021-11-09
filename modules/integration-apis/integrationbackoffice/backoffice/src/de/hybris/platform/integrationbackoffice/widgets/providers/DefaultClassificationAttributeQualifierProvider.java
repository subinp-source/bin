/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.providers;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.integrationbackoffice.utility.QualifierNameUtils;


/**
 * Default implementation of {@link ClassificationAttributeQualifierProvider}. The provided qualifier consists of
 * {@link de.hybris.platform.catalog.model.classification.ClassificationSystemModel#ID},
 * {@link de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel#VERSION},
 * {@link de.hybris.platform.catalog.model.classification.ClassificationClassModel#CODE} and
 * {@link de.hybris.platform.catalog.model.classification.ClassificationAttributeModel#CODE} without spaces.
 */
public class DefaultClassificationAttributeQualifierProvider implements ClassificationAttributeQualifierProvider
{
	@Override
	public String provide(final ClassAttributeAssignmentModel assignment)
	{
		final String catalogId = assignment.getSystemVersion().getCatalog().getId();
		final String version = assignment.getSystemVersion().getVersion();
		final String categoryCode = assignment.getClassificationClass().getCode();
		final String attributeCode = assignment.getClassificationAttribute().getCode();

		final String qualifier = String.format("%s_%s_%s_%s", catalogId, version, categoryCode, attributeCode);
		return QualifierNameUtils.removeNonAlphaNumericCharacters(qualifier);
	}
}
