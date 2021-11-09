/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagescontentslotstyperestrictions.validator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.CMSPageContentSlotListData;
import de.hybris.platform.cmsfacades.common.predicate.PageExistsPredicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.CONTENT_SLOT_INVALID_UID;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_DOES_NOT_EXIST;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentSlotTypeRestrictionsGetValidatorTest
{
    private static final String PAGE_ID_FIELD = "pageId";
    private static final String SLOT_IDS_FIELD = "slotIds";
    private static final String PAGE_ID = "SomePageId";
    private static final String VALID_SLOT_ID_1 = "someSlotId1";
    private static final String VALID_SLOT_ID_2 = "someSlotId2";


    @Mock
    private Errors errors;

    @Mock
    private CMSPageContentSlotListData cmsPageContentSlotListData;

    @Mock
    private PageExistsPredicate pageExistsPredicate;

    @InjectMocks
    private ContentSlotTypeRestrictionsGetValidator contentSlotTypeRestrictionsGetValidator;

    @Before
    public void setUp()
    {
        when(cmsPageContentSlotListData.getPageId()).thenReturn(PAGE_ID);
        when(cmsPageContentSlotListData.getSlotIds()).thenReturn(List.of(VALID_SLOT_ID_1, VALID_SLOT_ID_2));
        when(pageExistsPredicate.test(PAGE_ID)).thenReturn(true);
    }

    @Test
    public void shouldReturnAnErrorWhenPageIdIsNull()
    {
        // GIVEN
        when(cmsPageContentSlotListData.getPageId()).thenReturn(null);

        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertFieldHasError(PAGE_ID_FIELD, FIELD_REQUIRED);
    }

    @Test
    public void shouldReturnAnErrorWhenPageDoesNotExist()
    {
        // GIVEN
        when(pageExistsPredicate.test(PAGE_ID)).thenReturn(false);

        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertFieldHasError(PAGE_ID_FIELD, FIELD_DOES_NOT_EXIST);
    }

    @Test
    public void shouldReturnAnErrorWhenSlotIdsIsNull()
    {
        // GIVEN
        when(cmsPageContentSlotListData.getSlotIds()).thenReturn(null);

        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertFieldHasError(SLOT_IDS_FIELD, FIELD_REQUIRED);
    }

    @Test
    public void shouldReturnAnErrorWhenSlotIdsIsEmpty()
    {
        // GIVEN
        when(cmsPageContentSlotListData.getSlotIds()).thenReturn(Collections.emptyList());

        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertFieldHasError(SLOT_IDS_FIELD, FIELD_REQUIRED);
    }

    @Test
    public void shouldReturnAnErrorIfAnySlotIdsIsNull()
    {
        // GIVEN
        when(cmsPageContentSlotListData.getSlotIds()).thenReturn(Arrays.asList(VALID_SLOT_ID_1, null));

        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertFieldHasError(SLOT_IDS_FIELD, CONTENT_SLOT_INVALID_UID);
    }

    @Test
    public void shouldReturnAnErrorIfAnySlotIdsIsEmpty()
    {
        // GIVEN
        when(cmsPageContentSlotListData.getSlotIds()).thenReturn(List.of(VALID_SLOT_ID_1, ""));

        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertFieldHasError(SLOT_IDS_FIELD, CONTENT_SLOT_INVALID_UID);
    }

    @Test
    public void shouldNotReturnAnErrorIfThePayloadIsCorrect()
    {
        // WHEN
        contentSlotTypeRestrictionsGetValidator.validate(cmsPageContentSlotListData, errors);

        // THEN
        assertNoErrors();
    }

    protected void assertNoErrors()
    {
        verify(errors, never()).rejectValue(any(), any());
    }

    protected void assertFieldHasError(final String fieldName, final String expectedError)
    {
        verify(errors).rejectValue(fieldName, expectedError);
    }
}
