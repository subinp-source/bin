/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.template.populator;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;

import org.junit.Test;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;


public class ClassificationSystemVersionPopulatorTest
{
	ClassificationSystemVersionPopulator populator = new ClassificationSystemVersionPopulator();

	@Test
	public void shouldGetCategorySystemId()
	{
		// given
		final ClassificationSystemVersionModel systemVersion = mock(ClassificationSystemVersionModel.class);
		given(systemVersion.getVersion()).willReturn("version");

		final ClassAttributeAssignmentModel assignment = mock(ClassAttributeAssignmentModel.class);
		given(assignment.getSystemVersion()).willReturn(systemVersion);

		final ExcelClassificationAttribute attribute = mock(ExcelClassificationAttribute.class);
		given(attribute.getAttributeAssignment()).willReturn(assignment);

		// when
		final String result = populator.apply(DefaultExcelAttributeContext.ofExcelAttribute(attribute));

		// then
		assertThat(result).isEqualTo("version");
	}
}
