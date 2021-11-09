/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static com.hybris.backoffice.workflow.designer.renderer.SvgShapesRenderer.LOG_TAG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.ItemModel;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;

import com.hybris.backoffice.workflow.designer.renderer.SvgShapesRenderer;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SvgShapesRendererTest
{
	private static final String SHAPE_RESOURCE = "shapeResource";
	private static final String RESOLVED_SHAPE_AS_STRING = "resolvedShapeAsString";
	@Mock
	VelocityEngine mockedVelocityEngine;

	@Spy
	@InjectMocks
	SvgShapesRenderer svgShapesRenderer;

	@Test
	public void shouldResolveSvgShapesWithVelocityEngine()
	{
		// given
		final InputStreamReader inputStreamReader = mock(InputStreamReader.class);
		final ArgumentCaptor<VelocityContext> velocityContextArgumentCaptor = ArgumentCaptor.forClass(VelocityContext.class);

		final ItemModel itemModel = mock(ItemModel.class);
		willReturn(Optional.of(inputStreamReader)).given(svgShapesRenderer).resolveResource(eq(SHAPE_RESOURCE));
		willAnswer(this::mockVelocityShapeResolution).given(mockedVelocityEngine).evaluate(velocityContextArgumentCaptor.capture(),
				any(StringWriter.class), eq(LOG_TAG), eq(inputStreamReader));

		// when
		final String resolvedSvgShapeAsString = svgShapesRenderer.getSvgShape(SHAPE_RESOURCE, Map.of("model", itemModel));

		// then
		assertThat(resolvedSvgShapeAsString).isEqualTo(RESOLVED_SHAPE_AS_STRING);
		assertThat(velocityContextArgumentCaptor.getValue().get("model")).isEqualTo(itemModel);
	}

	private boolean mockVelocityShapeResolution(final InvocationOnMock mockInvocation)
	{
		final StringWriter sw = mockInvocation.getArgumentAt(1, StringWriter.class);
		sw.write(RESOLVED_SHAPE_AS_STRING);
		return true;
	}
}
