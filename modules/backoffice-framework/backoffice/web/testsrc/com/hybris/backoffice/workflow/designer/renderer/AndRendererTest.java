/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static com.hybris.backoffice.workflow.designer.renderer.AndRenderer.AND_CONNECTION_PROPERTY;
import static com.hybris.backoffice.workflow.designer.renderer.AndRenderer.AND_CONNECTION_TEMPLATE_PROPERTY;
import static com.hybris.backoffice.workflow.designer.renderer.AndRenderer.LABEL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ItemModelContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecisionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowLink;
import com.hybris.cockpitng.components.visjs.network.data.ChosenNode;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class AndRendererTest
{
	private static final String GENERATED_ID = "generatedId";

	@Mock
	KeyGenerator mockedKeyGenerator;

	@InjectMocks
	AndRenderer andRenderer;

	@Test
	public void shouldHandleLinkModelWithAndConnectionForTemplate() throws JaloSecurityException
	{
		// given
		final LinkModel link = mock(LinkModel.class);
		final Link linkSource = prepareLink(link);
		given(linkSource.getAttribute(AND_CONNECTION_TEMPLATE_PROPERTY)).willReturn(true);
		final WorkflowLink workflowLink = WorkflowLink.ofSavedModel(link);


		// when
		final boolean result = andRenderer.canHandle(workflowLink);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldHandleLinkModelWithAndConnectionForInstance() throws JaloSecurityException
	{
		// given
		final LinkModel link = mock(LinkModel.class);
		final Link linkSource = prepareLink(link);
		given(linkSource.getAttribute(AND_CONNECTION_PROPERTY)).willReturn(true);
		final WorkflowLink workflowLink = WorkflowLink.ofSavedModel(link);

		// when
		final boolean result = andRenderer.canHandle(workflowLink);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldNotHandleNewLinkModelsWithoutAndConnection()
	{
		// given
		final LinkModel link = mock(LinkModel.class);
		final WorkflowLink workflowLink = WorkflowLink.ofSavedModel(link);

		// when
		final boolean result = andRenderer.canHandle(workflowLink);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldHandleNewLinkModelWithAndConnection()
	{
		// given
		final LinkModel link = mock(LinkModel.class);
		final WorkflowLink workflowLink = WorkflowLink.ofUnsavedModel(link, true);

		// when
		final boolean result = andRenderer.canHandle(workflowLink);

		// then
		assertThat(result).isTrue();
	}

	@Test
	public void shouldHandleLinkModelWithoutAndConnection()
	{
		// given
		final LinkModel link = mock(LinkModel.class);
		final WorkflowLink workflowLink = WorkflowLink.ofUnsavedModel(link, false);

		// when
		final boolean result = andRenderer.canHandle(workflowLink);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldNotHandleObjectsOtherThanLinkModel()
	{
		// given
		final WorkflowDecisionTemplate workflowDecisionTemplate = mock(WorkflowDecisionTemplate.class);

		// when
		final boolean result = andRenderer.canHandle(workflowDecisionTemplate);

		// then
		assertThat(result).isFalse();
	}

	@Test
	public void shouldRenderAndNode() throws JaloSecurityException
	{
		// given
		final LinkModel link = mock(LinkModel.class);
		final Link linkSource = prepareLink(link);
		given(linkSource.getAttribute(AND_CONNECTION_PROPERTY)).willReturn(true);

		final WorkflowLink workflowLink = WorkflowLink.ofSavedModel(link);
		given(mockedKeyGenerator.generate()).willReturn(GENERATED_ID);

		// when
		final Node node = andRenderer.render(workflowLink);

		// then
		assertThat(node).isEqualToIgnoringGivenFields(new Node.Builder().withId(GENERATED_ID).withLabel(LABEL).withX(0).withY(0)
				.withGroup(WorkflowDesignerGroup.AND.getValue()).build(), "chosen");
		assertThat(node.getChosen().getNode()).isEqualTo(new ChosenNode.Builder().withNode(String.valueOf(true)).build().getNode());
	}

	@Test
	public void shouldHandleXYCoordinates() throws JaloSecurityException
	{
		// given
		final int visualisationX = 100;
		final int visualisationY = 200;

		final LinkModel link = mock(LinkModel.class);
		final Link linkSource = prepareLink(link);
		given(linkSource.getAttribute("visualisationX")).willReturn(visualisationX);
		given(linkSource.getAttribute("visualisationY")).willReturn(visualisationY);

		final WorkflowLink workflowLink = WorkflowLink.ofSavedModel(link);

		prepareLink(mock(LinkModel.class));

		// when
		final Node node = andRenderer.render(workflowLink);

		// then
		assertThat(node.getX()).isEqualTo(visualisationX);
		assertThat(node.getY()).isEqualTo(visualisationY);
	}

	private Link prepareLink(final LinkModel linkModel)
	{
		final ItemModelContext context = mock(ItemModelContext.class);
		given(linkModel.getItemModelContext()).willReturn(context);

		final Link linkSource = mock(Link.class);
		given(context.getSource()).willReturn(linkSource);
		return linkSource;
	}

}
