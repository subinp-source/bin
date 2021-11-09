/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.components.visjs.network.data.ChosenNode;
import com.hybris.cockpitng.components.visjs.network.data.Image;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class DisableSelectionNetworkNodeDecoratorTest
{

	final DisableSelectionNetworkNodeDecorator decorator = new DisableSelectionNetworkNodeDecorator();

	@Test
	public void shouldDisableSelectionOfImageNode()
	{
		// given
		final String unselectedImage = "unselected";
		final String selectedImage = "selected";
		final Node nodeToDecorate = new Node.Builder()
				.withImage(new Image.Builder().withSelected(selectedImage).withUnselected(unselectedImage).build()).build();

		// when
		final Node decoratedNode = decorator.decorate(nodeToDecorate);

		// then
		assertThat(decoratedNode.getImage()).isNotNull();
		assertThat(decoratedNode.getImage().getSelected()).isNull();
		assertThat(decoratedNode.getImage().getUnselected()).isEqualTo(unselectedImage);
	}

	@Test
	public void shouldDisableSelectionOfLabelNode()
	{
		// given
		final Node nodeToDecorate = new Node.Builder().withChosen(new ChosenNode.Builder().build()).build();

		// when
		final Node decoratedNode = decorator.decorate(nodeToDecorate);

		// then
		assertThat(decoratedNode.getChosen()).isNull();
	}

}
