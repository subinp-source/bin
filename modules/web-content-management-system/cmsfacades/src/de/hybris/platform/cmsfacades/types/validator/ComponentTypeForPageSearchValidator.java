/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.validator;

import de.hybris.platform.cmsfacades.common.predicate.PageExistsPredicate;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSComponentTypesForPageSearchData;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

/**
 * Validates fields of {@link CMSComponentTypesForPageSearchData} for a component type search operation
 */
public class ComponentTypeForPageSearchValidator implements Validator
{
    private static final String PAGE_ID = "pageId";

    private PageExistsPredicate pageExistsPredicate;

    @Override
    public boolean supports(final Class<?> clazz)
    {
        return CMSComponentTypesForPageSearchData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object object, final Errors errors)
    {
        final CMSComponentTypesForPageSearchData searchData = (CMSComponentTypesForPageSearchData) object;

        if (Objects.isNull(searchData.getPageId()))
        {
            errors.rejectValue(PAGE_ID, CmsfacadesConstants.FIELD_REQUIRED);
        }
        else if (!getPageExistsPredicate().test(searchData.getPageId()))
        {
            errors.rejectValue(PAGE_ID, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
        }
    }

    public PageExistsPredicate getPageExistsPredicate()
    {
        return pageExistsPredicate;
    }

    @Required
    public void setPageExistsPredicate(final PageExistsPredicate pageExistsPredicate)
    {
        this.pageExistsPredicate = pageExistsPredicate;
    }
}
