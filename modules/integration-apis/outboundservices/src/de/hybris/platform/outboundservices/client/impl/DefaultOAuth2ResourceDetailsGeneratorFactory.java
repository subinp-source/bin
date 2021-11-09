/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.client.impl;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;

import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of {@link OAuth2ResourceDetailsGeneratorFactory}
 */
public class DefaultOAuth2ResourceDetailsGeneratorFactory implements OAuth2ResourceDetailsGeneratorFactory
{
	private static final String MESSAGE = "There is not an applicable generator for this credential model [%s]";

	private ExposedOAuth2ResourceDetailsGenerator exposedOAuth2ResourceDetailsGenerator = new ExposedOAuth2ResourceDetailsGenerator();
	private ConsumedOAuth2ResourceDetailsGenerator consumedOAuth2ResourceDetailsGenerator = new ConsumedOAuth2ResourceDetailsGenerator();

	private List<OAuth2ResourceDetailsGenerator> oAuth2ResourceDetailsGenerators =
			Arrays.asList(exposedOAuth2ResourceDetailsGenerator, consumedOAuth2ResourceDetailsGenerator);

	@Override
	public boolean isApplicable(final AbstractCredentialModel credentialModel)
	{
		return oAuth2ResourceDetailsGenerators.stream().anyMatch(generator -> generator.isApplicable(credentialModel));
	}

	@Override
	public OAuth2ResourceDetailsGenerator getGenerator(final AbstractCredentialModel credentialModel)
	{
		return oAuth2ResourceDetailsGenerators.stream()
		                                      .filter(generator -> generator.isApplicable(credentialModel))
		                                      .findFirst()
		                                      .orElseThrow(() -> new IllegalStateException(String.format(MESSAGE, credentialModel.getId())));
	}
}