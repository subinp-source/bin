/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata;

import de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils;

import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;

public class ODataAssociation
{
	private final Association association;

	public ODataAssociation(final Association association)
	{
		this.association = association;
	}

	public boolean isAssociationBetweenTypes(final String type1, final String type2)
	{
		return association.getEnd1().getType().equals(qualified(type1))
				&& association.getEnd2().getType().equals(qualified(type2));
	}

	public boolean hasName(final String name)
	{
		return association.getName().equals(name);
	}

	public boolean hasSource(final String type1)
	{
		return association.getEnd1().getRole().equals(type1);
	}

	public boolean hasTarget(final String type2)
	{
		return association.getEnd2().getRole().equals(type2);
	}

	private FullQualifiedName qualified(final String name)
	{
		return new FullQualifiedName(SchemaUtils.NAMESPACE, name);
	}

	public boolean hasSourceMultiplicity(final EdmMultiplicity multiplicity)
	{
		return association.getEnd1().getMultiplicity().equals(multiplicity);
	}

	public boolean hasTargetMultiplicity(final EdmMultiplicity multiplicity)
	{
		return association.getEnd2().getMultiplicity().equals(multiplicity);
	}
}
