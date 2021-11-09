/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;


@RunWith(MockitoJUnitRunner.class)
public class BulkEditTemplateModelCreatorTest
{

	@Mock
	private ModelService modelService;
	@Mock
	private PermissionFacade permissionFacade;
	@Mock
	private SessionService sessionService;
	@InjectMocks
	private BulkEditTemplateModelCreator bulkEditTemplateModelCreator;

	@Test
	public void shouldEmptyOptionalBeReturnedWhenUserHasNoSufficientPermissions()
	{
		// given
		final String typeCode = "Product";
		given(permissionFacade.canReadType(typeCode)).willReturn(false);
		given(permissionFacade.canChangeType(typeCode)).willReturn(false);

		// when
		final Optional<ItemModel> itemOptional = bulkEditTemplateModelCreator.create(typeCode);

		// then
		then(sessionService).should(never()).executeInLocalViewWithParams(anyMap(), any());
		assertThat(itemOptional).isNotPresent();
	}

	@Test
	public void shouldModelBeReturnedWhenUserHasSufficientPermissions()
	{
		// given
		final String typeCode = "Product";
		given(permissionFacade.canReadType(typeCode)).willReturn(true);
		given(permissionFacade.canChangeType(typeCode)).willReturn(true);

		final ItemModel itemModel = mock(ItemModel.class);
		given(modelService.create(typeCode)).willReturn(itemModel);
		given(sessionService.executeInLocalViewWithParams(anyMap(), any())).willReturn(itemModel);

		// when
		final Optional<ItemModel> itemModelOptional = bulkEditTemplateModelCreator.create(typeCode);

		// then
		then(sessionService).should().executeInLocalViewWithParams(anyMap(), any());
		assertThat(itemModelOptional).isPresent();
		assertThat(itemModelOptional).hasValue(itemModel);
	}

}
