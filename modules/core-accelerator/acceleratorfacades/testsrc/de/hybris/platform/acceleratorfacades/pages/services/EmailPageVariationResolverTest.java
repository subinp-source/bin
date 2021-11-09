/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.pages.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cmsfacades.data.OptionData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class EmailPageVariationResolverTest
{
	@Mock
	private CMSAdminPageService adminPageService;

	@InjectMocks
	private EmailPageVariationResolver resolver;

	private EmailPageModel email1;
	private EmailPageModel email2;

	@Before
	public void setUp()
	{
		email1 = new EmailPageModel();
		email1.setUid("EMAIL_UID_1");
		email1.setDefaultPage(Boolean.TRUE);

		email2 = new EmailPageModel();
		email2.setUid("EMAIL_UID_2");
		email2.setDefaultPage(Boolean.TRUE);
	}

	@Test
	public void shouldFindAllDefaultPages()
	{
		when(adminPageService.getAllPagesByType(EmailPageModel.TYPECODE)).thenReturn(Arrays.asList(email1, email2));

		final List<EmailPageModel> results = resolver.findPagesByType(EmailPageModel.TYPECODE, true);

		assertThat(results, hasSize(2));
		assertThat(results, containsInAnyOrder(email1, email2));
	}

	@Test
	public void shouldReturnEmptyListForVariationType()
	{
		final List<EmailPageModel> results = resolver.findPagesByType(EmailPageModel.TYPECODE, true);

		assertThat(results, empty());
	}

	@Test
	public void shouldOnlyReturnDefaultPageTypeForDisplayCondition()
	{
		List<OptionData> options = resolver.findDisplayConditions(EmailPageModel.TYPECODE);

		assertThat(options, hasSize(1));
		assertThat(options, hasItem(hasProperty("id", equalTo("PRIMARY"))));
	}

	@Test
	public void findDefaultPagesShouldReturnEmpty()
	{
		final List<EmailPageModel> results = resolver.findDefaultPages(new EmailPageModel());

		assertThat(results, empty());
	}

	@Test
	public void findVariationPagesPagesShouldReturnEmpty()
	{
		final List<EmailPageModel> results = resolver.findVariationPages(new EmailPageModel());

		assertThat(results, empty());
	}

	@Test
	public void shouldReturnDefaultPageBoolean()
	{
		final boolean isDefault = resolver.isDefaultPage(email1);
		assertThat(isDefault, is(true));
	}
}
