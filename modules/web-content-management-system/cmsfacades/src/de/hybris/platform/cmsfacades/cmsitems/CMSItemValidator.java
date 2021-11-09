/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems;

import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.core.model.ItemModel;

/**
 * Interface that deals with Type Validation. 
 * The main purpose of this service is to validate an {@link ItemModel} after it has been successfully converted, 
 * allowing custom validation on Item Models when there is a need to validate perform business validation and 
 * cross attribute validation.  
 */
public interface CMSItemValidator<T extends ItemModel> extends Validator<T>
{
	// Intentionally left empty 
}
