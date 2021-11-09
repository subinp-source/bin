/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributevalidators;

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.INVALID_MEDIA_CODE_L10N;

import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.validator.data.ValidationError;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

/**
 * Media attribute content validator adds validation errors when String value is not a valid unique identifier for Media.  
 */
public class MediaAttributeContentValidator extends AbstractAttributeContentValidator<String>
{
	
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	
	@Override
	public List<ValidationError> validate(final String value, final AttributeDescriptorModel attribute)
	{
		final List<ValidationError> errors = new ArrayList<>();
		if (value == null)
		{
			return errors;
		}
		final Optional<MediaModel> media = getUniqueItemIdentifierService().getItemModel(value, MediaModel.class);

		if (!media.isPresent())
		{
			errors.add(
					newValidationErrorBuilder() //
							.field(attribute.getQualifier()) //
							.errorCode(INVALID_MEDIA_CODE_L10N) //
							.build()
			);
		}
		return errors;
	}
	

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}
}
