/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.utils;

import de.hybris.bootstrap.annotations.UnitTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;

import de.hybris.platform.apiregistryservices.exceptions.CredentialException;
import de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;


@UnitTest
public class DefaultOAuthCredentialsRestTemplateProviderUnitTest
{

	private DefaultOAuthCredentialsRestTemplateProvider oAuthCredentialsRestTemplateProvider = new DefaultOAuthCredentialsRestTemplateProvider();

	@Test(expected = CredentialException.class)
	public void testValidateCredentialForWrongCredentialType() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(new ConsumedCertificateCredentialModel());
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialNoClientId() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(getCredential(null, "clientSecret", "url"));
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialEmptyClientId() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(getCredential("", "clientSecret", "url"));
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialNoClientSecret() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(getCredential("clientId", null, "url"));
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialEmptyClientSecret() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(getCredential("clientId", "", "url"));
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialNoUrl() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(getCredential("clientId", "clientSecret", null));
	}

	@Test(expected = CredentialException.class)
	public void testValidateCredentialEmptyUrl() throws CredentialException
	{
		oAuthCredentialsRestTemplateProvider.validateCredential(getCredential("clientId", "clientSecret", ""));
	}

	@Test
	public void testGetRestTemplate() throws CredentialException
	{
		final RestTemplate restTemplate = oAuthCredentialsRestTemplateProvider
				.getRestTemplate(getCredential("clientId", "clientSecret", "url"));

		assertTrue(restTemplate instanceof OAuth2RestTemplate);

		assertNotNull(restTemplate.getRequestFactory());

		final OAuth2RestTemplate oAuth2RestTemplate = (OAuth2RestTemplate) restTemplate;
		assertTrue(oAuth2RestTemplate.getResource() instanceof ClientCredentialsResourceDetails);
		final ClientCredentialsResourceDetails resource = (ClientCredentialsResourceDetails) oAuth2RestTemplate.getResource();
		assertEquals("url", resource.getAccessTokenUri());
		assertEquals("clientId", resource.getClientId());
		assertEquals("clientSecret", resource.getClientSecret());
	}

	protected ConsumedOAuthCredentialModel getCredential(final String clientId, final String clientSecret, final String url)
	{
		final ConsumedOAuthCredentialModel credential = new ConsumedOAuthCredentialModel();
		credential.setOAuthUrl(url);
		credential.setClientSecret(clientSecret);
		credential.setClientId(clientId);
		return credential;
	}
}
