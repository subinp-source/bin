/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;

import org.junit.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@UnitTest
public class DefaultBasicCredentialsRestTemplateProviderUnitTest
{
	private DefaultBasicCredentialsRestTemplateProvider defaultBasicCredentialsRestTemplateProvider = new DefaultBasicCredentialsRestTemplateProvider();

	@Test(expected = CredentialException.class)
	public void testValidateCredentialForWrongCredentialType() throws CredentialException
	{
		defaultBasicCredentialsRestTemplateProvider.validateCredential(new ConsumedOAuthCredentialModel());
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialNoUserName() throws CredentialException
	{
		defaultBasicCredentialsRestTemplateProvider.validateCredential(getCredential(null, "test"));
	}

	@Test
	public void testValidateCredentialNoPassword() throws CredentialException
	{
		defaultBasicCredentialsRestTemplateProvider.validateCredential(getCredential("test", null));
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialEmptyUserName() throws CredentialException
	{
		defaultBasicCredentialsRestTemplateProvider.validateCredential(getCredential("", "test"));
	}

	@Test
	public void testValidateCredentialEmptyPassword() throws CredentialException
	{
		defaultBasicCredentialsRestTemplateProvider.validateCredential(getCredential("test", ""));
	}

	@Test
	public void testGetRestTemplate() throws CredentialException
	{
		final RestTemplate restTemplate = defaultBasicCredentialsRestTemplateProvider
				.getRestTemplate(getCredential("test", "test"));

		assertNotNull(restTemplate);

		assertTrue(restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory);

		final HttpComponentsClientHttpRequestFactory requestFactory = (HttpComponentsClientHttpRequestFactory) restTemplate
				.getRequestFactory();
		assertNotNull(requestFactory.getHttpClient());
	}

	protected BasicCredentialModel getCredential(final String userName, final String passWord)
	{
		final BasicCredentialModel credential = new BasicCredentialModel();
		credential.setUsername(userName);
		credential.setPassword(passWord);
		return credential;
	}
}
