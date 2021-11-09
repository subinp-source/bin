/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;


@RunWith(MockitoJUnitRunner.class)
public class ExcelMediaCatalogVersionValidatorTest
{

	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private UserService userService;
	@InjectMocks
	private ExcelMediaCatalogVersionValidator excelMediaCatalogVersionValidator;


	@Before
	public void setUp()
	{
		final List<CatalogVersionModel> catalogVersions = new ArrayList<>();
		catalogVersions.add(mockCatalogVersion("Staged", "Default"));
		catalogVersions.add(mockCatalogVersion("Online", "Default"));
		catalogVersions.add(mockCatalogVersion("Middle", "Cars"));
		catalogVersions.add(mockCatalogVersion("Staged", "Cars"));
		final UserModel userModel = mock(UserModel.class);
		given(userService.getCurrentUser()).willReturn(userModel);
		given(userService.isAdmin(userModel)).willReturn(false);
		given(catalogVersionService.getAllWritableCatalogVersions(userModel)).willReturn(catalogVersions);
	}

	private CatalogVersionModel mockCatalogVersion(final String version, final String catalogId)
	{
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		final CatalogModel catalogModel = mock(CatalogModel.class);
		given(catalogVersionModel.getCatalog()).willReturn(catalogModel);
		given(catalogVersionModel.getVersion()).willReturn(version);
		given(catalogModel.getId()).willReturn(catalogId);
		return catalogVersionModel;
	}

	@Test
	public void shouldNotReturnErrorWhenCatalogVersionExists()
	{
		// given
		final Map<String, Object> ctx = new HashMap<>();
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(CatalogVersionModel.CATALOG, "Default");
		parameters.put(CatalogVersionModel.VERSION, "Online");

		// when
		final Collection<ValidationMessage> validationMessages = excelMediaCatalogVersionValidator.validateSingleValue(ctx,
				parameters);

		// then
		assertThat(validationMessages).isEmpty();
	}

	@Test
	public void shouldReturnErrorWhenCatalogVersionDoesNotExist()
	{
		// given
		final Map<String, Object> ctx = new HashMap<>();
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(CatalogVersionModel.CATALOG, "Default");
		parameters.put(CatalogVersionModel.VERSION, "Stg");

		// when
		final List<ValidationMessage> validationMessages = (List) excelMediaCatalogVersionValidator.validateSingleValue(ctx,
				parameters);

		// then
		assertThat(validationMessages).hasSize(1);
		assertThat(validationMessages.get(0).getMessageKey()).isEqualTo("excel.import.validation.catalogversion.doesntexists");
	}

	@Test
	public void shouldReturnErrorWhenCatalogDoesNotExist()
	{
		// given
		final Map<String, Object> ctx = new HashMap<>();
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(CatalogVersionModel.CATALOG, "Clothing");
		parameters.put(CatalogVersionModel.VERSION, "Online");

		// when
		final List<ValidationMessage> validationMessages = (List) excelMediaCatalogVersionValidator.validateSingleValue(ctx,
				parameters);

		// then
		assertThat(validationMessages).hasSize(1);
		assertThat(validationMessages.get(0).getMessageKey()).isEqualTo("excel.import.validation.catalog.doesntexists");
	}

	@Test
	public void shouldReturnErrorWhenVersionDoesNotMatchCatalog()
	{
		// given
		final Map<String, Object> ctx = new HashMap<>();
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(CatalogVersionModel.CATALOG, "Cars");
		parameters.put(CatalogVersionModel.VERSION, "Online");

		// when
		final List<ValidationMessage> validationMessages = (List) excelMediaCatalogVersionValidator.validateSingleValue(ctx,
				parameters);

		// then
		assertThat(validationMessages).hasSize(1);
		assertThat(validationMessages.get(0).getMessageKey()).isEqualTo("excel.import.validation.catalogversion.doesntmatch");
	}

}
