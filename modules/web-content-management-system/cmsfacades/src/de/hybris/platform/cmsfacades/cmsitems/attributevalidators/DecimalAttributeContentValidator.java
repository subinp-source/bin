/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributevalidators;

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MAX_VIOLATED;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_MIN_VIOLATED;

import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.validation.model.constraints.AttributeConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMaxConstraintModel;
import de.hybris.platform.validation.model.constraints.jsr303.DecimalMinConstraintModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * BigDecimal validator adds validation errors when the value does not respects the attribute's constraints.  
 */
public class DecimalAttributeContentValidator extends AbstractAttributeContentValidator<BigDecimal>
{
	private final Set<String> constraints = Sets.newHashSet(DecimalMinConstraintModel._TYPECODE, DecimalMaxConstraintModel._TYPECODE);
	
	@Override
	public List<ValidationError> validate(final BigDecimal value, final AttributeDescriptorModel attribute)
	{
		final List<ValidationError> errors = new ArrayList<>();
		if (value == null)
		{
			return errors;
		}

		final Map<String, AttributeConstraintModel> validConstraintsMap = getConstraintMap(attribute, constraint -> getConstraints().contains(constraint.getItemtype()));

		final DecimalMinConstraintModel minConstraint = (DecimalMinConstraintModel) validConstraintsMap.get(DecimalMinConstraintModel._TYPECODE);
		final DecimalMaxConstraintModel maxConstraint = (DecimalMaxConstraintModel) validConstraintsMap.get(DecimalMaxConstraintModel._TYPECODE);
		if (minConstraint != null && value.compareTo(minConstraint.getValue()) < 0)
		{
			errors.add(
					newValidationErrorBuilder() //
							.field(attribute.getQualifier()) //
							.errorCode(FIELD_MIN_VIOLATED) //
							.build()
			);	
		}
		
		if (maxConstraint != null && value.compareTo(maxConstraint.getValue()) > 0)
		{
			errors.add(
					newValidationErrorBuilder() //
							.field(attribute.getQualifier()) //
							.errorCode(FIELD_MAX_VIOLATED) //
							.build()
			);
		}
		return errors;
	}

	protected Set<String> getConstraints()
	{
		return constraints;
	}
}
