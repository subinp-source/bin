/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp.wizard.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;


@RunWith(MockitoJUnitRunner.class)
public class ExcelImportValidationResultRendererTest
{
	private static final int MAX_RENDER_LIST_SIZE = 20;
	private static final String NOTIFICATION_SOURCE_NAME = "excelImportValidationResultRendererHandler";
	private static final String PROPERTY_NAME = "backoffice.excel.import.validation.result.renderer.max.results.to.render";

	@Mock
	private NotificationService notificationService;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration configuration;

	@Spy
	@InjectMocks
	private ExcelImportValidationResultRenderer excelImportValidationResultRenderer;



	@Before
	public void setUp()
	{
		doNothing().when(notificationService).notifyUser(NOTIFICATION_SOURCE_NAME, NotificationEventTypes.EVENT_TYPE_GENERAL,
				NotificationEvent.Level.WARNING);
		doReturn(MAX_RENDER_LIST_SIZE).when(configuration).getInt(PROPERTY_NAME, MAX_RENDER_LIST_SIZE);
		doReturn(configuration).when(configurationService).getConfiguration();
	}

	@Test
	public void shouldReduceResultsListAndNotifyUser()
	{
		//given
		final List<ExcelValidationResult> resultListToShorten = mock(List.class);
		final List<ExcelValidationResult> resultListAfterShortening = mock(List.class);
		when(resultListToShorten.size()).thenReturn(MAX_RENDER_LIST_SIZE + 1);
		when(resultListAfterShortening.size()).thenReturn(MAX_RENDER_LIST_SIZE);
		doReturn(resultListAfterShortening).when(excelImportValidationResultRenderer)
				.limitValidationResultsToMaxThreshold(resultListToShorten, MAX_RENDER_LIST_SIZE);

		//when
		final List<ExcelValidationResult> resultListFromFunction = excelImportValidationResultRenderer
				.reduceResultsList(resultListToShorten);

		//then
		assertThat(resultListFromFunction.size()).isEqualTo(MAX_RENDER_LIST_SIZE);
		assertThat(resultListFromFunction).isEqualTo(resultListAfterShortening);

		verify(excelImportValidationResultRenderer).limitValidationResultsToMaxThreshold(resultListToShorten, MAX_RENDER_LIST_SIZE);
		verify(notificationService).notifyUser(NOTIFICATION_SOURCE_NAME, NotificationEventTypes.EVENT_TYPE_GENERAL,
				NotificationEvent.Level.WARNING);
		verify(configurationService).getConfiguration();
		verify(configuration).getInt(PROPERTY_NAME, MAX_RENDER_LIST_SIZE);
	}

	@Test
	public void shouldNotReduceResultsListIfMaxSizeIsNotExceed()
	{
		//given
		final List<ExcelValidationResult> resultListPassed = mock(List.class);
		when(resultListPassed.size()).thenReturn(MAX_RENDER_LIST_SIZE);

		//when
		final List<ExcelValidationResult> resultListFromFunction = excelImportValidationResultRenderer
				.reduceResultsList(resultListPassed);

		//then
		assertThat(resultListFromFunction.size()).isEqualTo(MAX_RENDER_LIST_SIZE);
		assertThat(resultListFromFunction).isEqualTo(resultListPassed);

		verify(configurationService).getConfiguration();
		verify(configuration).getInt(PROPERTY_NAME, MAX_RENDER_LIST_SIZE);
	}

	@Test
	public void limitValidationResultsToMaxThresholdTest()
	{
		//given
		final List<ExcelValidationResult> resultListToBigToRender = new ArrayList<>();
		for (int i = 0; i < MAX_RENDER_LIST_SIZE + 1; i++)
		{
			resultListToBigToRender.add(mock(ExcelValidationResult.class));
		}

		//when
		final List<ExcelValidationResult> shortenResultList = excelImportValidationResultRenderer
				.limitValidationResultsToMaxThreshold(resultListToBigToRender, MAX_RENDER_LIST_SIZE);

		//then
		assertThat(shortenResultList.size()).isEqualTo(MAX_RENDER_LIST_SIZE);
		assertThat(shortenResultList).isNotEqualTo(resultListToBigToRender);
	}

}
