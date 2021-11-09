/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.schema.association;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.provider.Association;

public class OneToOneAssociationGenerator extends AbstractAssociationGenerator
{
	private static final EdmMultiplicity TARGET_MULTIPLICITY = EdmMultiplicity.ZERO_TO_ONE;

	@Override
	public boolean isApplicable(final IntegrationObjectItemAttributeModel attribute)
	{
		return isApplicable(asDescriptor(attribute));
	}

	@Override
	public boolean isApplicable(final TypeAttributeDescriptor attribute)
	{
		return falseIfNull(attribute) && !attribute.isPrimitive() && hasOneElement(attribute);
	}

	private boolean hasOneElement(final TypeAttributeDescriptor attribute)
	{
		return !attribute.isCollection() && (!attribute.isMap() || attribute.isLocalized());
	}

	@Override
	public Association generate(final IntegrationObjectItemAttributeModel attribute)
	{
		return generate(asDescriptor(attribute));
	}

	@Override
	protected EdmMultiplicity getTargetCardinality(final TypeAttributeDescriptor descriptor)
	{
		return TARGET_MULTIPLICITY;
	}
}
