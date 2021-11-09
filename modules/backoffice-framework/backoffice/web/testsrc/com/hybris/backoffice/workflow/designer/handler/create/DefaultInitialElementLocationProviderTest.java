/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.workflow.designer.dto.ElementLocation;
import com.hybris.cockpitng.components.visjs.network.data.Point;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@RunWith(MockitoJUnitRunner.class)
public class DefaultInitialElementLocationProviderTest
{

	@InjectMocks
	private DefaultInitialElementLocationProvider initialElementLocationProvider;

	@Test
	public void shouldProvideZeroLocation()
	{
		// given
		final WidgetInstanceManager wim = CockpitTestUtil.mockWidgetInstanceManager();

		// when
		final ElementLocation elementLocation = initialElementLocationProvider.provideLocation(wim);

		// then
		assertThat(elementLocation).isEqualTo(ElementLocation.zeroLocation());
		assertThat(elementLocation.getX()).isEqualTo(ElementLocation.ZERO_POSITION_X);
		assertThat(elementLocation.getY()).isEqualTo(ElementLocation.ZERO_POSITION_Y);
	}

	@Test
	public void shouldProvideCanvasCenterLocation()
	{
		// given
		final int x = 10;
		final int y = 15;
		final WidgetInstanceManager wim = CockpitTestUtil.mockWidgetInstanceManager();
		final Point canvasCenter = new Point.Builder().withX(x).withY(y).build();
		wim.getModel().setValue(NetworkChartController.MODEL_CANVAS_CENTER, canvasCenter);

		// when
		final ElementLocation elementLocation = initialElementLocationProvider.provideLocation(wim);

		// then
		assertThat(elementLocation.getX()).isEqualTo(x);
		assertThat(elementLocation.getY()).isEqualTo(y);
	}

	@Test
	public void shouldProvideZeroLocationIfXYAreNull()
	{
		// given
		final WidgetInstanceManager wim = CockpitTestUtil.mockWidgetInstanceManager();
		final Point canvasCenter = new Point.Builder().withX(null).withY(null).build();
		wim.getModel().setValue(NetworkChartController.MODEL_CANVAS_CENTER, canvasCenter);

		// when
		final ElementLocation elementLocation = initialElementLocationProvider.provideLocation(wim);

		// then
		assertThat(elementLocation.getX()).isEqualTo(ElementLocation.ZERO_POSITION_X);
		assertThat(elementLocation.getY()).isEqualTo(ElementLocation.ZERO_POSITION_Y);
	}

}
