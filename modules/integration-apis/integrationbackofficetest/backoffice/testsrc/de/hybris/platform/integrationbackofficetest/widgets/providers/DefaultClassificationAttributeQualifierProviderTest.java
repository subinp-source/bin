package de.hybris.platform.integrationbackofficetest.widgets.providers;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.integrationbackoffice.widgets.providers.ClassificationAttributeQualifierProvider;
import de.hybris.platform.integrationbackoffice.widgets.providers.DefaultClassificationAttributeQualifierProvider;

import org.junit.Test;


public class DefaultClassificationAttributeQualifierProviderTest
{
	ClassificationAttributeQualifierProvider provider = new DefaultClassificationAttributeQualifierProvider();

	@Test
	public void shouldConstructFullQualifierForClassificationAssignment()
	{
		// given
		final ClassAttributeAssignmentModel assignment = prepareAssignment();
		final ClassificationAttributeModel attribute = mock(ClassificationAttributeModel.class);
		given(assignment.getClassificationAttribute()).willReturn(attribute);

		given(assignment.getSystemVersion().getCatalog().getId()).willReturn("Cars");
		given(assignment.getSystemVersion().getVersion()).willReturn("v1");
		given(assignment.getClassificationClass().getCode()).willReturn("sportsCars");
		given(attribute.getCode()).willReturn("color");

		// when
		final String fullQualifier = provider.provide(assignment);

		// then
		assertThat(fullQualifier).isEqualTo("Cars_v1_sportsCars_color");
	}

	@Test
	public void shouldConstructFullQualifierForClassificationAssignmentRemovingInvalidCharacters()
	{
		// given
		final ClassAttributeAssignmentModel assignment = prepareAssignment();
		final ClassificationAttributeModel attribute = mock(ClassificationAttributeModel.class);
		given(assignment.getClassificationAttribute()).willReturn(attribute);

		given(assignment.getSystemVersion().getCatalog().getId()).willReturn("My Catalog");
		given(assignment.getSystemVersion().getVersion()).willReturn("My$Version");
		given(assignment.getClassificationClass().getCode()).willReturn("My Category");
		given(attribute.getCode()).willReturn("My_Attribute");

		// when
		final String fullQualifier = provider.provide(assignment);

		// then
		assertThat(fullQualifier).isEqualTo("MyCatalog_MyVersion_MyCategory_My_Attribute");
	}

	protected ClassAttributeAssignmentModel prepareAssignment()
	{
		final ClassificationSystemModel classificationSystem = mock(ClassificationSystemModel.class);
		final ClassificationSystemVersionModel classificationSystemVersion = mock(ClassificationSystemVersionModel.class);
		final ClassificationClassModel classificationClass = mock(ClassificationClassModel.class);
		final ClassAttributeAssignmentModel assignment = mock(ClassAttributeAssignmentModel.class);

		given(assignment.getSystemVersion()).willReturn(classificationSystemVersion);
		given(classificationSystemVersion.getCatalog()).willReturn(classificationSystem);
		given(assignment.getClassificationClass()).willReturn(classificationClass);
		return assignment;
	}
}
