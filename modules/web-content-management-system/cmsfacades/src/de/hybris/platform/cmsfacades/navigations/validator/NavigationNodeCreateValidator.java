/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.validator;

import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link NavigationNodeData} upon creation
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class NavigationNodeCreateValidator implements Validator
{
	private static final String UID = "uid";
	private static final String PARENT_UID = "parentUid";
	private static final String NAME = "name";

	private Predicate<String> validateUidPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return NavigationNodeData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		final NavigationNodeData target = (NavigationNodeData) obj;
		ValidationUtils.rejectIfEmpty(errors, PARENT_UID, CmsfacadesConstants.FIELD_REQUIRED);
		ValidationUtils.rejectIfEmpty(errors, NAME, CmsfacadesConstants.FIELD_REQUIRED);
		if (!getValidateUidPredicate().test(target.getUid()))
		{
			errors.rejectValue(UID, CmsfacadesConstants.INVALID_ROOT_NODE_UID);
		}
	}

	protected Predicate<String> getValidateUidPredicate()
	{
		return validateUidPredicate;
	}

	@Required
	public void setValidateUidPredicate(final Predicate<String> validateUidPredicate)
	{
		this.validateUidPredicate = validateUidPredicate;
	}
}
