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

import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.buildAssociationName;
import static de.hybris.platform.odata2services.odata.schema.utils.SchemaUtils.toFullQualifiedName;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.model.impl.DefaultTypeAttributeDescriptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;

import com.google.common.base.Preconditions;

public abstract class AbstractAssociationGenerator implements AssociationGenerator
{
	@Override
	public Association generate(final TypeAttributeDescriptor attrDescriptor)
	{
		final String sourceTypeCode = getSourceRole(attrDescriptor);
		final String targetTypeCode = getTargetRole(attrDescriptor);
		return new Association()
				.setName(getAssociationName(attrDescriptor))
				.setEnd1(new AssociationEnd()
						.setType(toFullQualifiedName(sourceTypeCode))
						.setRole(sourceTypeCode)
						.setMultiplicity(getSourceCardinality(attrDescriptor)))
				.setEnd2(new AssociationEnd()
						.setType(toFullQualifiedName(getTargetType(attrDescriptor)))
						.setRole(targetTypeCode)
						.setMultiplicity(getTargetCardinality(attrDescriptor)));
	}

	@Override
	public String getSourceRole(final IntegrationObjectItemAttributeModel attributeDefinitionModel)
	{
		return getSourceRole(asDescriptor(attributeDefinitionModel));
	}

	@Override
	public String getTargetRole(final IntegrationObjectItemAttributeModel attributeDefinitionModel)
	{
		return getTargetRole(asDescriptor(attributeDefinitionModel));
	}

	@Override
	public String getAssociationName(final IntegrationObjectItemAttributeModel attributeDefinitionModel)
	{
		return getAssociationName(asDescriptor(attributeDefinitionModel));
	}

	/**
	 * Runs safety checks on the specified attribute definition model, to make sure it can be analyzed by the
	 * {@link #isApplicable(IntegrationObjectItemAttributeModel)} method. Specifically it checks the the model is not {@code null}
	 * and contains an {@link de.hybris.platform.core.model.type.AttributeDescriptorModel}.
	 * @param attributeDefinitionModel a model to analyze
	 * @deprecated no alternative method. The DefaultTypeAttributeDescriptor.create validates the appropriate precondition.
	 * A IntegrationObjectItemAttributeModel cannot possibly exist with a null attribute descriptor.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected void checkIsApplicablePrecondition(final IntegrationObjectItemAttributeModel attributeDefinitionModel)
	{
		Preconditions.checkArgument(attributeDefinitionModel != null, "Cannot generate an Association with a null attribute definition.");
		Preconditions.checkArgument(attributeDefinitionModel.getAttributeDescriptor() != null, "Cannot generate an Association with a null attribute  descriptor.");
	}

	/**
	 * Generates association name based on the the provided attribute descriptor.
	 * @param descriptor a descriptor to generate the association name for.
	 * @return name of the descriptor in {@code "FK_<item_type>_<attribute_type>"} format, where {@code item_type} is the type
	 * containing the attribute descriptor and {@code attribute_type} is the type of the attribute values or, in other words,
	 * type referenced by the attribute.
	 */
	@Override
	public String getAssociationName(final TypeAttributeDescriptor descriptor)
	{
		return buildAssociationName(descriptor.getTypeDescriptor().getItemCode(), descriptor.getAttributeName());
	}

	/**
	 * Converts attribute model to an attribute descriptor.
	 * @param attributeDefinitionModel a model to convert.
	 * @return attribute descriptor for the specified attribute model.
	 * @deprecated no alternative method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected TypeAttributeDescriptor asDescriptor(final IntegrationObjectItemAttributeModel attributeDefinitionModel)
	{
		return DefaultTypeAttributeDescriptor.create(attributeDefinitionModel);
	}

	/**
	 * Generates an association source role.
	 * @param descriptor an attribute descriptor to generate the source role for.
	 * @return type code of the {@link TypeDescriptor} containing the attribute descriptor.
	 * @see TypeAttributeDescriptor#getTypeDescriptor()
	 * @see TypeDescriptor#getItemCode()
	 */
	@Override
	public String getSourceRole(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.getTypeDescriptor().getItemCode();
	}

	/**
	 * Generates an association target role.
	 * @param descriptor an attribute descriptor to generate the target role from.
	 * @return type code of the {@link TypeDescriptor} referenced by the attribute descriptor (type of the attribute values) or
	 * the attribute name if the source type is the same as the target type in the association.
	 * @see TypeAttributeDescriptor#getAttributeType()
	 * @see TypeDescriptor#getItemCode()
	 * @see TypeAttributeDescriptor#getAttributeName()
	 */
	@Override
	public String getTargetRole(final TypeAttributeDescriptor descriptor)
	{
		final String targetTypeName = getTargetType(descriptor);
		return targetTypeName.equals(getSourceRole(descriptor)) ?
				StringUtils.capitalize(descriptor.getAttributeName()) :
				targetTypeName;
	}

	/**
	 * Generates an association target type.
	 * @param descriptor an attribute descriptor to generate the target type from.
	 * @return type code of the {@link TypeDescriptor} referenced by the attribute descriptor (type of the attribute values).
	 * @see TypeAttributeDescriptor#getAttributeType()
	 * @see TypeDescriptor#getItemCode()
	 */
	protected String getTargetType(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.getAttributeType().getItemCode();
	}

	/**
	 * Generates an association target type.
	 * @param attributeModel an attribute descriptor to generate the target type from.
	 * @return type code of the {@link TypeDescriptor} referenced by the attribute descriptor (type of the attribute values).
	 * @see TypeAttributeDescriptor#getAttributeType()
	 * @see TypeDescriptor#getItemCode()
	 * @deprecated convert the IntegrationObjectItemAttributeModel to a TypeAttributeDescriptor and
	 * use the {@link #getTargetType(TypeAttributeDescriptor)} method.
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	protected String getTargetType(final IntegrationObjectItemAttributeModel attributeModel)
	{
		return getTargetType(asDescriptor(attributeModel));
	}

	/**
	 * Determines cardinality of the source type presented in the specified attribute descriptor. Normally the source type is
	 * the item type containing the attribute.
	 * @param descriptor descriptor to derive the cardinality from
	 * @return multiplicity
	 */
	protected EdmMultiplicity getSourceCardinality(final TypeAttributeDescriptor descriptor)
	{
		return descriptor.reverse()
				.map(AbstractAssociationGenerator::toMultiplicity)
				.orElse(EdmMultiplicity.ZERO_TO_ONE);
	}

	/**
	 * Determines cardinality of the target type presented in the specified attribute descriptor. Normally the target type is
	 * the item type returned by the attribute.
	 * @param descriptor descriptor to derive the cardinality from
	 * @return multiplicity
	 */
	protected EdmMultiplicity getTargetCardinality(final TypeAttributeDescriptor descriptor)
	{
		return toMultiplicity(descriptor);
	}

	private static EdmMultiplicity toMultiplicity(final TypeAttributeDescriptor d)
	{
		if (d.isCollection() || (d.isMap() && !d.isLocalized()))
		{
			return EdmMultiplicity.MANY;
		}
		return d.isNullable()
				? EdmMultiplicity.ZERO_TO_ONE
				: EdmMultiplicity.ONE;
	}

	protected static boolean falseIfNull(final Object attribute)
	{
		return attribute != null;
	}
}
