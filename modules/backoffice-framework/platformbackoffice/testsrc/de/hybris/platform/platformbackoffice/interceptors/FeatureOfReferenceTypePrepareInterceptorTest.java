/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.platformbackoffice.interceptors;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ItemModelContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FeatureOfReferenceTypePrepareInterceptorTest
{

	private final FeatureOfReferenceTypePrepareInterceptor interceptor = new FeatureOfReferenceTypePrepareInterceptor();

	@Test
	public void shouldReferenceTypeBeClearedWhenAttributeTypeChangedFromReferenceIntoNonReference() throws InterceptorException
	{
		// given
		final ClassAttributeAssignmentModel assignment = mockAssignment(ClassificationAttributeTypeEnum.REFERENCE,
				ClassificationAttributeTypeEnum.NUMBER, false, mock(ComposedTypeModel.class));

		// when
		interceptor.onPrepare(assignment, null);

		// then
		then(assignment).should().setReferenceType(isNull(ComposedTypeModel.class));
	}

	@Test
	public void shouldReferenceTypeBeNotClearedWhenAttributeTypeWasNotReferenceBefore() throws InterceptorException
	{
		// given
		final ClassAttributeAssignmentModel assignment = mockAssignment(ClassificationAttributeTypeEnum.DATE,
				ClassificationAttributeTypeEnum.NUMBER, false, mock(ComposedTypeModel.class));

		// when
		interceptor.onPrepare(assignment, null);

		// then
		then(assignment).should(never()).setReferenceType(any());
	}

	private ClassAttributeAssignmentModel mockAssignment(final ClassificationAttributeTypeEnum previousType,
			final ClassificationAttributeTypeEnum newType, final boolean isNew, final ComposedTypeModel referenceType)
	{
		final ClassAttributeAssignmentModel assignment = mock(ClassAttributeAssignmentModel.class);
		final ItemModelContext itemModelContext = mock(ItemModelContext.class);
		given(itemModelContext.isNew()).willReturn(isNew);
		given(itemModelContext.getOriginalValue(ClassAttributeAssignment.ATTRIBUTETYPE)).willReturn(previousType);
		given(assignment.getItemModelContext()).willReturn(itemModelContext);
		given(assignment.getAttributeType()).willReturn(newType);
		given(assignment.getReferenceType()).willReturn(referenceType);
		return assignment;
	}

}
