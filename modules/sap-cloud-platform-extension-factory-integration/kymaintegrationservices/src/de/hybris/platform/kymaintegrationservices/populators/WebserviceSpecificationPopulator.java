/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.populators;

import static de.hybris.platform.kymaintegrationservices.utils.KymaApiExportHelper.getDestinationId;

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedOAuthCredentialModel;
import de.hybris.platform.apiregistryservices.utils.EventExportUtils;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.kymaintegrationservices.dto.ApiSpecificationData;
import de.hybris.platform.kymaintegrationservices.dto.BasicAuthData;
import de.hybris.platform.kymaintegrationservices.dto.CredentialsData;
import de.hybris.platform.kymaintegrationservices.dto.OAuthData;
import de.hybris.platform.kymaintegrationservices.dto.ServiceRegistrationData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.Config;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Kyma specific implementation of Populator that populates Webservice specification DTO.
 * {@link ServiceRegistrationData}
 */
public class WebserviceSpecificationPopulator implements Populator<ExposedDestinationModel, ServiceRegistrationData>
{
	private static final String DEFAULT_PROVIDER = "SAP Hybris";
	private static final String PROVIDER_PROP = "kymaintegrationservices.kyma-specification-provider";
	public static final String METADATA_PATH = "$metadata";
	public static final String ODATA_TYPE = "OData";

	private ObjectMapper jacksonObjectMapper;

	@Override
	public void populate(final ExposedDestinationModel source, final ServiceRegistrationData target)
	{
		target.setDescription(source.getEndpoint().getDescription());
		target.setName(source.getEndpoint().getName());
		target.setIdentifier(getDestinationId(source));
		target.setProvider(Config.getString(PROVIDER_PROP, DEFAULT_PROVIDER));
		target.setApi(extractApiSpecification(source));
	}

	/**
	 * @param source
	 *           ApiConfiguration
	 * @return ApiSpecification (url, api documentation, credentials)
	 */
	protected ApiSpecificationData extractApiSpecification(final ExposedDestinationModel source)
	{
		final ApiSpecificationData apiSpecification = new ApiSpecificationData();
		final String url = EventExportUtils.replacePropertyPlaceholders(source.getUrl());
		if(EventExportUtils.isUrlValid(url))
		{
			apiSpecification.setTargetUrl(url);
		}
		else
		{
			throw new ConversionException(
					String.format("Invalid url for the Exposed Destination with id : %s", source.getId()));
		}
		apiSpecification.setCredentials(extractCredentials(source.getCredential()));

		final String specUrl = source.getEndpoint().getSpecUrl();
		if (StringUtils.isNotBlank(specUrl))
		{
			final String endPointUrl = EventExportUtils.replacePropertyPlaceholders(specUrl);
			if (!EventExportUtils.isUrlValid(endPointUrl))
			{
				throw new ConversionException(
						String.format("Invalid specUrl for the Exposed Destination with id : %s", source.getId()));
			}

			if (endPointUrl.endsWith(METADATA_PATH))
			{
				apiSpecification.setType(ODATA_TYPE);
				if(apiSpecification.getCredentials() != null)
				{
					apiSpecification.setSpecificationCredentials(apiSpecification.getCredentials());
				}
			}

			apiSpecification.setSpecificationUrl(endPointUrl);
			
		}
		else if (StringUtils.isNotEmpty(source.getEndpoint().getSpecData()))
		{
			try
			{
				apiSpecification.setSpec(getJacksonObjectMapper().readTree(source.getEndpoint().getSpecData()));
			}
			catch (final IOException ex)
			{
				throw new ConversionException(
						String.format("Invalid specData for the Exposed Destination with id : %s", source.getId()), ex);
			}
		}
		else
		{
			throw new ConversionException(
					String.format("Neither specData nor specificationUrl for the Exposed Destination with id : %s", source.getId()));
		}

		return apiSpecification;
	}


	/**
	 * @param credential
	 *           AbstractCredentialModel
	 * @return CredentialsData credentials
	 */
	protected CredentialsData extractCredentials(final AbstractCredentialModel credential)
	{
		final CredentialsData credentialsData = new CredentialsData();
		if (credential instanceof ExposedOAuthCredentialModel)
		{
			credentialsData.setOauth(extractOAuth((ExposedOAuthCredentialModel) credential));
		}
		else if (credential instanceof BasicCredentialModel)
		{
			credentialsData.setBasic(extractBasic((BasicCredentialModel) credential));
		}
		else
		{
			return null;
		}
		return credentialsData;
	}

	/**
	 * @param credential
	 *           BasicCredentialModel
	 * @return BasicAuthData credentials
	 */
	protected BasicAuthData extractBasic(final BasicCredentialModel credential)
	{
		if (StringUtils.isEmpty(credential.getUsername()))
		{
			throw new ConversionException("BasicCredentialModel must have username");
		}
		final BasicAuthData auth = new BasicAuthData();
		auth.setUsername(credential.getUsername());
		auth.setPassword(credential.getPassword());
		return auth;
	}

	/**
	 * @param credential
	 *           ExposedOAuthCredentialModel
	 * @return OAuth credentials
	 */
	protected OAuthData extractOAuth(final ExposedOAuthCredentialModel credential)
	{
		if (credential.getOAuthClientDetails() == null)
		{
			throw new ConversionException("ExposedOAuthCredential must have OAuthClientDetails");
		}
		final OAuthData oauth = new OAuthData();
		oauth.setClientId(credential.getOAuthClientDetails().getClientId());
		oauth.setClientSecret(credential.getPassword());
		oauth.setUrl(EventExportUtils.replacePropertyPlaceholders(credential.getOAuthClientDetails().getOAuthUrl()));
		return oauth;
	}

	protected ObjectMapper getJacksonObjectMapper()
	{
		return jacksonObjectMapper;
	}

	@Required
	public void setJacksonObjectMapper(final ObjectMapper jacksonObjectMapper)
	{
		this.jacksonObjectMapper = jacksonObjectMapper;
	}

}
