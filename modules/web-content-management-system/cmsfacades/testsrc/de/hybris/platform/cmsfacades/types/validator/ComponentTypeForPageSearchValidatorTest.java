/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.validator;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.common.predicate.PageExistsPredicate;
import de.hybris.platform.cmsfacades.data.CMSComponentTypesForPageSearchData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ComponentTypeForPageSearchValidatorTest
{
    private final static String PAGE_ID_FIELD_NAME = "pageId";
    private final static String PAGE_ID = "SomePageID";

    @Mock
    private PageExistsPredicate pageExistsPredicate;

    @Mock
    private CMSComponentTypesForPageSearchData searchData;

    @Mock
    private Errors errors;

    @InjectMocks
    private ComponentTypeForPageSearchValidator componentTypeForPageSearchValidator;

    @Before
    public void setUp()
    {
        when(searchData.getPageId()).thenReturn(PAGE_ID);
        when(pageExistsPredicate.test(PAGE_ID)).thenReturn(true);
    }

    @Test
    public void shouldReturnAnErrorWhenPageIdIsNull()
    {
        // GIVEN
        when(searchData.getPageId()).thenReturn(null);

        // WHEN
        componentTypeForPageSearchValidator.validate(searchData, errors);

        // THEN
        assertHasError(FIELD_REQUIRED);
    }

    @Test
    public void shouldReturnAnErrorWhenPageDoesNotExist()
    {
        // GIVEN
        when(pageExistsPredicate.test(PAGE_ID)).thenReturn(false);

        // WHEN
        componentTypeForPageSearchValidator.validate(searchData, errors);

        // THEN
        assertHasError(FIELD_DOES_NOT_EXIST);
    }

    @Test
    public void shouldNotReturnErrorWhenPayloadIsFine()
    {
        // WHEN
        componentTypeForPageSearchValidator.validate(searchData, errors);

        // THEN
        assertHasNoErrors();
    }

    protected void assertHasNoErrors()
    {
        verify(errors, never()).rejectValue(any(), any());
    }

    protected void assertHasError(final String expectedError)
    {
        verify(errors).rejectValue(PAGE_ID_FIELD_NAME, expectedError);
    }
}
