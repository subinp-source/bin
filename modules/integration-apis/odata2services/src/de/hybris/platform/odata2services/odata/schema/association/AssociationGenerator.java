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
import de.hybris.platform.odata2services.odata.schema.SchemaElementGenerator;

import org.apache.olingo.odata2.api.edm.provider.Association;

public interface AssociationGenerator extends SchemaElementGenerator<Association, IntegrationObjectItemAttributeModel>
{
	/**
	 * Generates an association for the referenced TypeAttributeDescriptor
	 *
	 * @param descriptor the integration object attribute descriptor
	 * @return the association
	 */
	Association generate(TypeAttributeDescriptor descriptor);

	/**
	 * Generates an association for the referenced IntegrationObjectItemAttributeModel
	 *
	 * @param attribute the integration object attribute
	 * @return the association
	 * @deprecated convert the IntegrationObjectItemAttributeModel to a TypeAttributeDescriptor and
	 * use the {@link #generate(TypeAttributeDescriptor)} method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	Association generate(IntegrationObjectItemAttributeModel attribute);

	/**
	 * Derives the source role from the given attribute definition model
	 *
	 * @param attributeDefinitionModel the integration object attribute definition model
	 * @return the source role
	 * @deprecated convert the IntegrationObjectItemAttributeModel to a TypeAttributeDescriptor and
	 * use the {@link #getSourceRole(TypeAttributeDescriptor)} method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	String getSourceRole(IntegrationObjectItemAttributeModel attributeDefinitionModel);


	/**
	 * Derives the source role from the given attribute descriptor
	 *
	 * @param attributeDescriptor the integration object attribute descriptor
	 * @return the source role
	 * @deprecated Use {@link AssociationGenerator#generate(TypeAttributeDescriptor)} and then retrieve the
	 * role from the generated association's end2
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	String getSourceRole(TypeAttributeDescriptor attributeDescriptor);

	/**
	 * Derives the target role from the given attribute definition model
	 *
	 * @param attributeDefinitionModel the integration object attribute definition model
	 * @return the target role
	 * @deprecated convert the IntegrationObjectItemAttributeModel to a TypeAttributeDescriptor and
	 * use the {@link #getTargetRole(TypeAttributeDescriptor)} method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	String getTargetRole(IntegrationObjectItemAttributeModel attributeDefinitionModel);

	/**
	 * Derives the target role from the given attribute descriptor
	 *
	 * @param descriptor the integration object attribute descriptor
	 * @return the target role
	 * @deprecated Use {@link AssociationGenerator#generate(TypeAttributeDescriptor)} and then retrieve the
	 * role from the generated association's end2
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	String getTargetRole(final TypeAttributeDescriptor descriptor);

	/**
	 * Derives the association name from the given attribute definition model
	 *
	 * @param attributeDefinitionModel the integration object attribute definition model
	 * @return the association name
	 * @deprecated convert the IntegrationObjectItemAttributeModel to a TypeAttributeDescriptor and
	 * use the {@link #getAssociationName(TypeAttributeDescriptor)} method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	String getAssociationName(IntegrationObjectItemAttributeModel attributeDefinitionModel);

	/**
	 * Derives the association name from the given attribute descriptor
	 *
	 * @param attributeDescriptor the integration object attribute descriptor
	 * @return the association name
	 * @deprecated Use {@link AssociationGenerator#generate(TypeAttributeDescriptor)} and then retrieve the
	 * name from the generated association
	 */
	@Deprecated(since = "1905.10-CEP", forRemoval = true)
	String getAssociationName(TypeAttributeDescriptor attributeDescriptor);

	/**
	 * Determines if this association generator is applicable for the given attribute
	 *
	 * @param attributeDefinitionModel the attribute we are verifying
	 * @return true if applicable, otherwise false
	 * @deprecated convert the IntegrationObjectItemAttributeModel to a TypeAttributeDescriptor and
	 * use the {@link #isApplicable(TypeAttributeDescriptor)} method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	boolean isApplicable(IntegrationObjectItemAttributeModel attributeDefinitionModel);

	/**
	 * Determines if this association generator is applicable for the given attribute descriptor
	 *
	 * @param attributeDescriptor the attribute descriptor we are verifying
	 * @return true if applicable, otherwise false
	 */
	boolean isApplicable(TypeAttributeDescriptor attributeDescriptor);
}
