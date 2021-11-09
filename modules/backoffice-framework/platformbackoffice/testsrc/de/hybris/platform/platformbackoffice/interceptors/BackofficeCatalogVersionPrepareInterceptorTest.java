/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package de.hybris.platform.platformbackoffice.interceptors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BackofficeCatalogVersionPrepareInterceptorTest
{
	@Spy
	@InjectMocks
	private BackofficeCatalogVersionPrepareInterceptor interceptor;

	@Mock
	private UserService userService;

	@Test
	public void shouldNotEditWriteReadPrincipalsForModifiedCatalogVersion()
	{
		//given
		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		final InterceptorContext ctx = mock(InterceptorContext.class);
		given(ctx.isNew(catalogVersion)).willReturn(Boolean.FALSE);

		//when
		interceptor.assignCurrentUserReadWritePermissions(catalogVersion, ctx);

		//then
		then(catalogVersion).should(never()).setReadPrincipals(any());
		then(catalogVersion).should(never()).setWritePrincipals(any());
	}

	@Test
	public void shouldEditWriteReadPrincipalsForNewCatalogVersion()
	{
		//given
		final UserModel currentUser = mock(UserModel.class);
		given(userService.getCurrentUser()).willReturn(currentUser);
		final CatalogVersionModel catalogVersion = mock(CatalogVersionModel.class);
		final InterceptorContext ctx = mock(InterceptorContext.class);
		given(ctx.isNew(catalogVersion)).willReturn(Boolean.TRUE);

		//when
		interceptor.assignCurrentUserReadWritePermissions(catalogVersion, ctx);

		//then
		final ArgumentCaptor<List> readPrincipals = ArgumentCaptor.forClass(List.class);
		then(catalogVersion).should().setReadPrincipals((List<PrincipalModel>) readPrincipals.capture());
		assertThat(readPrincipals.getValue()).contains(currentUser);

		final ArgumentCaptor<List> writePrincipals = ArgumentCaptor.forClass(List.class);
		then(catalogVersion).should().setWritePrincipals((List<PrincipalModel>) writePrincipals.capture());
		assertThat(writePrincipals.getValue()).contains(currentUser);
	}
}
