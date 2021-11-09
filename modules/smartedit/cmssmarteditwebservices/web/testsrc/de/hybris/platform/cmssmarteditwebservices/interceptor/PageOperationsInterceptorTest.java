/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.interceptor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;


@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class PageOperationsInterceptorTest
{

	private static final String SOURCE_VERSION_ID = "sourceVersion";
	private static final String TARGET_VERSION_ID = "sourceVersion";
	private static final String CATALOG_ID = "electronics";
	private static final String HTTP_REQUEST_POST_METHOD = "POST";

	@InjectMocks
	private PageOperationsInterceptor interceptor;

	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private CatalogSynchronizationService catalogSynchronizationService;
	@Spy
	private MockHttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private CatalogVersionModel sourceCatalogVersion;
	@Mock
	private CatalogVersionModel targetCatalogVersion;
	@Mock
	private UserModel principal;
	@Mock
	private UserService userService;
	@Mock
	private Object handler;

	@Mock
	private SyncItemJobModel syncItemJobModel;

	@Before
	public void setup()
	{
		final Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put(CmssmarteditwebservicesConstants.URI_CATALOG_ID, CATALOG_ID);

		request.setContent(("{\"" + CmssmarteditwebservicesConstants.URI_SOURCE_CATALOG_VERSION + "\":\"" + SOURCE_VERSION_ID
				+ "\",\"" + CmssmarteditwebservicesConstants.URI_TARGET_CATALOG_VERSION + "\":\"" + TARGET_VERSION_ID + "\"}")
						.getBytes());

		when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(pathVariables);
		when(userService.getCurrentUser()).thenReturn(principal);

		when(catalogVersionService.getCatalogVersion(CATALOG_ID, SOURCE_VERSION_ID)).thenReturn(sourceCatalogVersion);
		when(sourceCatalogVersion.getSynchronizations()).thenReturn(Arrays.asList(syncItemJobModel));
		when(syncItemJobModel.getTargetVersion()).thenReturn(targetCatalogVersion);
		when(targetCatalogVersion.getVersion()).thenReturn(TARGET_VERSION_ID);
		when(syncItemJobModel.getActive()).thenReturn(true);
	}


	@Test
	public void shouldFail_NoSyncPermission() throws Exception
	{
		when(catalogSynchronizationService.canSynchronize(syncItemJobModel, principal)).thenReturn(false);

		final boolean value = interceptor.preHandle(request, response, handler);

		assertThat(value, is(false));
		verify(catalogSynchronizationService).canSynchronize(syncItemJobModel, principal);
	}

	@Test
	public void shouldPass_HasSyncPermissionToCatalogVersion() throws Exception
	{
		when(catalogSynchronizationService.canSynchronize(syncItemJobModel, principal)).thenReturn(true);

		final boolean value = interceptor.preHandle(request, response, handler);

		assertThat(value, is(true));
		verify(catalogSynchronizationService).canSynchronize(syncItemJobModel, principal);
	}

}
