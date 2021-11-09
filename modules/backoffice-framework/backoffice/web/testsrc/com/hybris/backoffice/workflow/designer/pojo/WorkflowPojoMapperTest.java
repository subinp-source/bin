/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowPojoMapperTest
{

	@Test
	public void shouldWorkflowModelBeMappedToPojo()
	{
		// given
		final WorkflowModel workflowModel = mock(WorkflowModel.class);

		// when
		final Optional<Workflow> output = WorkflowPojoMapper.mapItemToWorkflow(workflowModel);

		// then
		assertThat(output).isPresent();
		assertThat(output.get()).isInstanceOf(WorkflowInstance.class);
	}

	@Test
	public void shouldRandomModelNotBeMappedToWorkflowPojo()
	{
		// given
		final ProductModel productModel = mock(ProductModel.class);

		// when
		final Optional<Workflow> output = WorkflowPojoMapper.mapItemToWorkflow(productModel);

		// then
		assertThat(output).isEmpty();
	}

	@Test
	public void shouldWorkflowEntityModelBeMappedToPojo()
	{
		// given
		final WorkflowActionTemplateModel workflowActionTemplateModel = mock(WorkflowActionTemplateModel.class);

		// when
		final Optional<WorkflowEntity> output = WorkflowPojoMapper.mapItemToWorkflowEntity(workflowActionTemplateModel);

		// then
		assertThat(output).isPresent();
		assertThat(output.get()).isInstanceOf(WorkflowActionTemplate.class);
	}

	@Test
	public void shouldRandomModelNotBeMappedToWorkflowEntityPojo()
	{
		// given
		final ProductModel productModel = mock(ProductModel.class);

		// when
		final Optional<WorkflowEntity> output = WorkflowPojoMapper.mapItemToWorkflowEntity(productModel);

		// then
		assertThat(output).isEmpty();
	}

}
