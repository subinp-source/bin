/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.data;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;

import java.util.Collection;


/**
 * Represents attributes which have been selected on wizard. Moreover the class contains information whether 'use full
 * qualifiers' checkbox is selected or not.
 */
public class SelectedClassificationAttributesData
{
	private final Collection<ClassAttributeAssignmentModel> assignments;
	private final boolean useFullQualifier;

	public SelectedClassificationAttributesData(final Collection<ClassAttributeAssignmentModel> assignments,
	                                            final boolean useFullQualifier)
	{
		this.assignments = assignments;
		this.useFullQualifier = useFullQualifier;
	}

	public Collection<ClassAttributeAssignmentModel> getAssignments()
	{
		return assignments;
	}

	public boolean isUseFullQualifier()
	{
		return useFullQualifier;
	}

}
