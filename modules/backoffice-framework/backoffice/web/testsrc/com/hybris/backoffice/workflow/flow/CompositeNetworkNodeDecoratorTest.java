/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.components.visjs.network.data.Node;

import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class CompositeNetworkNodeDecoratorTest
{

	private final CompositeNetworkNodeDecorator decorator = new CompositeNetworkNodeDecorator();

	@Test
	public void shouldNodeBeDecoratedByAllCombinedDecorators()
	{
		// given
		final Node nodeToDecorate = new Node.Builder().withId("primaryNode").build();
		final Node firstNodeDecoration = mock(Node.class);
		final Node secondNodeDecoration = mock(Node.class);

		final NetworkNodeDecorator decorator1 = mock(NetworkNodeDecorator.class);
		final NetworkNodeDecorator decorator2 = mock(NetworkNodeDecorator.class);

		given(decorator1.decorate(eq(nodeToDecorate), any())).willReturn(firstNodeDecoration);
		given(decorator2.decorate(eq(firstNodeDecoration), any())).willReturn(secondNodeDecoration);

		decorator.setNetworkNodeDecorators(List.of(decorator1, decorator2));

		// when
		final Node decoratedNode = decorator.decorate(nodeToDecorate, Map.of());

		// then
		assertThat(decoratedNode).isEqualTo(secondNodeDecoration);
		then(decorator1).should().decorate(argThat(new ArgumentMatcher<>()
		{
			@Override
			public boolean matches(final Object o)
			{
				if (o instanceof Node)
				{
					return o == nodeToDecorate;
				}
				return false;
			}
		}), anyMap());
		then(decorator2).should().decorate(argThat(new ArgumentMatcher<>()
		{
			@Override
			public boolean matches(final Object o)
			{
				if (o instanceof Node)
				{
					return o == firstNodeDecoration;
				}
				return false;
			}
		}), anyMap());
	}

}
