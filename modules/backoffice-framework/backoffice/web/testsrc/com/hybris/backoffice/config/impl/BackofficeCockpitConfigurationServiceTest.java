/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.config.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.cockpitng.core.util.impl.DefaultPropertyResolverRegistry;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.events.AbstractBackofficeEventListener;
import com.hybris.backoffice.events.ExternalEventCallback;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.cache.ConfigurationCache;
import com.hybris.cockpitng.core.config.impl.cache.DefaultConfigurationCache;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.persistence.ConfigurationImportSupport;
import com.hybris.cockpitng.core.persistence.packaging.CockpitClassLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.modules.CockpitModuleConnector;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeCockpitConfigurationServiceTest
{

	@Spy
	private BackofficeCockpitConfigurationService backofficeCockpitConfigurationService;

	@InjectMocks
	@Spy
	private DefaultBackofficeConfigurationMediaHelper backofficeConfigurationMediaHelper;

	@Spy
	private DefaultMediaCockpitConfigurationPersistenceStrategy mediaCockpitConfigurationPersistenceStrategy;

	@Spy
	private DefaultConfigurationCache configurationCache;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private MediaService mediaService;

	@Mock
	private UserService userService;

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	private MockSessionService sessionService;

	@Mock
	private TimeService timeService;

	@Mock
	private SearchRestrictionService searchRestrictionService;

	@Mock
	private ModelService modelService;

	@Mock
	private EmployeeModel admin;

	@Mock
	private CockpitApplicationContext applicationContext;

	@Mock
	private CockpitClassLoader cockpitClassLoader;

	@Mock
	private WidgetLibUtils widgetLibUtils;

	@Mock
	private CockpitModuleConnector cockpitModuleConnector;

	@Mock
	private ConfigurationImportSupport importSupport;

	@Before
	public void setUp() throws Exception
	{
		backofficeCockpitConfigurationService.setSessionService(sessionService);
		backofficeCockpitConfigurationService.setBackofficeConfigurationMediaHelper(backofficeConfigurationMediaHelper);
		backofficeCockpitConfigurationService.setPersistenceStrategy(mediaCockpitConfigurationPersistenceStrategy);
		backofficeCockpitConfigurationService.setConfigurationCache(configurationCache);
		backofficeCockpitConfigurationService.setApplicationContext(applicationContext);
		backofficeCockpitConfigurationService.setWidgetLibUtils(widgetLibUtils);
		backofficeCockpitConfigurationService.setCockpitModuleConnector(cockpitModuleConnector);
		backofficeCockpitConfigurationService.setImportSupport(importSupport);
		backofficeCockpitConfigurationService.setCockpitProperties(new DefaultPropertyResolverRegistry());

		mediaCockpitConfigurationPersistenceStrategy.setBackofficeConfigurationMediaHelper(backofficeConfigurationMediaHelper);
		mediaCockpitConfigurationPersistenceStrategy.setUserService(userService);
		mediaCockpitConfigurationPersistenceStrategy.setSessionService(sessionService);
		mediaCockpitConfigurationPersistenceStrategy.setMediaService(mediaService);
		mediaCockpitConfigurationPersistenceStrategy.setModelService(modelService);
		when(userService.getAdminUser()).thenReturn(admin);
		when(mediaService.getFolder(any())).thenReturn(mock(MediaFolderModel.class));

		doReturn(new ByteArrayOutputStream()).when(mediaCockpitConfigurationPersistenceStrategy).getConfigurationOutputStream();
		doReturn(new Date()).when(timeService).getCurrentTime();
		backofficeCockpitConfigurationService.setTimeService(timeService);

		when(applicationContext.getClassLoader()).thenReturn(cockpitClassLoader);
		when(cockpitModuleConnector.getCockpitModuleUrls()).thenReturn(Collections.emptyList());
		when(importSupport.resolveImports(any(), any())).then(invoke -> invoke.getArgumentAt(0, Config.class));
	}

	protected BackofficeCockpitConfigurationService createConfigurationService()
	{
		return new BackofficeCockpitConfigurationService();
	}

	@Mock
	private AbstractBackofficeEventListener resetTrigger;

	@Test
	public void shouldRegisterCallback()
	{
		// given

		//then
		backofficeCockpitConfigurationService.setResetTrigger(resetTrigger);

		//verify
		verify(resetTrigger).registerCallback(any());
	}

	@Test
	public void shouldResetWhenCallbackTriggered()
	{
		// given
		final ArgumentCaptor<ExternalEventCallback> callback = ArgumentCaptor.forClass(ExternalEventCallback.class);
		backofficeCockpitConfigurationService.setResetTrigger(resetTrigger);
		verify(resetTrigger).registerCallback(callback.capture());

		//then
		callback.getValue().onEvent(mock(AbstractEvent.class));

		//verify
		verify(backofficeCockpitConfigurationService).reset();
	}

	@Test
	public void getCockpitNGConfigTest() throws CockpitConfigurationException
	{
		final MediaModel media = mock(MediaModel.class);
		media.setCode(anyString());
		when(mediaService.getMedia(any(), anyString())).thenReturn(media);
		assertNotNull(backofficeCockpitConfigurationService.getCockpitNGConfig());
		assertEquals(media, backofficeCockpitConfigurationService.getCockpitNGConfig());
	}

	@Test
	public void storeRootConfig()
	{
		final ConfigurationCache cache = mock(ConfigurationCache.class);
		final com.hybris.cockpitng.core.config.impl.jaxb.Config config = mock(
				com.hybris.cockpitng.core.config.impl.jaxb.Config.class);

		backofficeCockpitConfigurationService.setConfigurationCache(cache);

		final long timeInMills = System.currentTimeMillis();
		when(timeService.getCurrentTime()).thenReturn(new Date(timeInMills));
		when(modelService.create(MediaModel.class)).thenReturn(mock(MediaModel.class));

		doReturn(mock(ByteArrayOutputStream.class)).when(mediaCockpitConfigurationPersistenceStrategy)
				.getConfigurationOutputStream();

		try
		{
			backofficeCockpitConfigurationService.storeRootConfig(config);
		}
		catch (final CockpitConfigurationException e)
		{
			fail("Could not store Configuration");
		}

		verify(cache).cacheRootConfiguration(eq(config), anyLong());
	}

	@Test
	public void shouldVerifySecureFolderAssignmentOnGetCockpitNGConfig() throws CockpitConfigurationException
	{
		//given
		final MediaModel media = mock(MediaModel.class);
		when(mediaService.getMedia(any(), anyString())).thenReturn(media);

		//when
		backofficeCockpitConfigurationService.getCockpitNGConfig();

		//then
		verify(backofficeConfigurationMediaHelper).assureSecureFolderAssignment(media);
	}

	@Test
	public void shouldRunCallbackOnCacheInvalidation() throws CockpitConfigurationException
	{
		final Runnable callback = mock(Runnable.class);
		backofficeCockpitConfigurationService.onCacheInvalidation(callback);

		backofficeCockpitConfigurationService.storeRootConfig(mock(Config.class));

		verify(callback).run();
	}
}
