/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemContextProvider;
import de.hybris.platform.cmsfacades.cmsitems.CMSItemConverter;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;


/**
 * Default implementation of the validator for {@link AbstractRestrictionModel}
 */
public class DefaultUpdateAbstractRestrictionValidator implements Validator<AbstractRestrictionModel>
{
	private ValidationErrorsProvider validationErrorsProvider;
	private Validator<CMSItemModel> workflowItemValidator;
	private CMSItemConverter cmsItemConverter;
	private CMSItemContextProvider<Map<String, Object>> originalConvertedItemProvider;

	/**
	 * {@inheritDoc}
	 *
	 * The restriction is saved with cms item that contains it in the same transaction.
	 * Sometimes the restriction can be in a workflow (means it cannot be edited) and the cms item itself is not.
	 * And we want to be able to save the cms item without validating the restriction if it was not changed.
	 */
	@Override
	public void validate(final AbstractRestrictionModel validatee)
	{
		final Map<String, Object> originalItem = getOriginalConvertedItemProvider().getCurrentItem();

		final Map<String, Object> currentItem = getCmsItemConverter().convert(validatee);

		final MapDifference<String, Object> difference = Maps.difference(originalItem, currentItem);

		if (!difference.areEqual())
		{
			getWorkflowItemValidator().validate(validatee);
		}
	}

	protected ValidationErrorsProvider getValidationErrorsProvider()
	{
		return validationErrorsProvider;
	}

	@Required
	public void setValidationErrorsProvider(final ValidationErrorsProvider validationErrorsProvider)
	{
		this.validationErrorsProvider = validationErrorsProvider;
	}

	protected Validator<CMSItemModel> getWorkflowItemValidator()
	{
		return workflowItemValidator;
	}

	@Required
	public void setWorkflowItemValidator(
			final Validator<CMSItemModel> workflowItemValidator)
	{
		this.workflowItemValidator = workflowItemValidator;
	}

	protected CMSItemConverter getCmsItemConverter()
	{
		return cmsItemConverter;
	}

	@Required
	public void setCmsItemConverter(final CMSItemConverter cmsItemConverter)
	{
		this.cmsItemConverter = cmsItemConverter;
	}

	protected CMSItemContextProvider<Map<String, Object>> getOriginalConvertedItemProvider()
	{
		return originalConvertedItemProvider;
	}

	@Required
	public void setOriginalConvertedItemProvider(
			final CMSItemContextProvider<Map<String, Object>> originalConvertedItemProvider)
	{
		this.originalConvertedItemProvider = originalConvertedItemProvider;
	}
}
