/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.components.visjs.network.data.Image;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowEntityImageCreatorTest
{

	@Mock
	private Base64ImageEncoder base64ImageEncoder;
	@Mock
	private SvgShapesRenderer svgShapesRenderer;
	@InjectMocks
	private DefaultWorkflowEntityImageCreator defaultWorkflowEntityImageCreator;

	@Test
	public void shouldCorrectImageBeCreated()
	{
		// given
		final ItemModel anyModel = mock(ItemModel.class);
		final String cssClass = "in-progress";

		final String velocityTemplateLocation = "velocityTemplateLocation";
		final String velocityTemplateLocationIE = "velocityTemplateLocationIE";

		final String svgShapeUnselected = "svgShapeUnselected";
		final String svgShapeSelected = "svgShapeSelected";

		given(base64ImageEncoder.encode(any())).willAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
		given(svgShapesRenderer.getSvgShape(velocityTemplateLocation, velocityTemplateLocationIE,
				Map.of("model", anyModel, "class", cssClass))).willReturn(svgShapeUnselected);
		given(svgShapesRenderer.getSvgShape(velocityTemplateLocation, velocityTemplateLocationIE,
				Map.of("model", anyModel, "class", "selected"))).willReturn(svgShapeSelected);

		// when
		final Image image = defaultWorkflowEntityImageCreator.createSvgImage(anyModel, velocityTemplateLocation,
				velocityTemplateLocationIE, cssClass);

		// then
		assertThat(image).isNotNull();
		assertThat(image.getSelected()).isEqualTo(svgShapeSelected);
		assertThat(image.getUnselected()).isEqualTo(svgShapeUnselected);
	}
}
