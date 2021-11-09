/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.config;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.webservicescommons.swagger.services.ApiVendorExtensionService;
import static de.hybris.smartedit.constants.SmarteditConstants.DOCUMENTATION_API_VERSION;
import static de.hybris.smartedit.constants.SmarteditConstants.DOCUMENTATION_DESC_PROPERTY;
import static de.hybris.smartedit.constants.SmarteditConstants.DOCUMENTATION_TITLE_PROPERTY;
import static de.hybris.smartedit.constants.SmarteditConstants.HOST;
import static de.hybris.smartedit.constants.SmarteditConstants.HOST_DEFAULT;
import static de.hybris.smartedit.constants.SmarteditConstants.LICENSE_PROPERTY;
import static de.hybris.smartedit.constants.SmarteditConstants.LICENSE_URL_PROPERTY;
import static de.hybris.smartedit.constants.SmarteditConstants.TERMS_OF_SERVICE_URL_PROPERTY;
import static de.hybris.smartedit.constants.SmarteditConstants.PASSWORD_AUTHORIZATION_NAME;
import static de.hybris.smartedit.constants.SmarteditConstants.PASSWORD_AUTHORIZATION_SCOPE;
import static de.hybris.smartedit.constants.SmarteditConstants.CLIENT_CREDENTIAL_AUTHORIZATION_NAME;
import static de.hybris.smartedit.constants.SmarteditConstants.CLIENT_CREDENTIAL_AUTHORIZATION_SCOPE;
import static de.hybris.smartedit.constants.SmarteditConstants.AUTHORIZATION_URL;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import de.hybris.smartedit.constants.SmarteditConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Configuration for swagger api documentation
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "apiVendorExtensionService")
	private ApiVendorExtensionService apiVendorExtensionService;

	@Bean
	public Docket apiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(apiInfo())//
				.select()//
				.paths(PathSelectors.any())//
				.build()//
				.produces(ImmutableSet.of("application/json", "application/xml"))
				.securitySchemes(Arrays.asList(clientCredentialFlow(), passwordFlow()))
				.securityContexts(Arrays.asList(securityContext()))//
				.host(getHost())//
				.extensions(getApiVendorExtensionService().getAllVendorExtensions(SmarteditConstants.EXTENSIONNAME))
				.tags(new Tag("authentications", "Controller used to generate OAuth2 token based on SSO authentication"),
						new Tag("configurations", "Controller used to send the requested CRUD operations " +
								"to the secured webservice responsible of executing the operation"));
	}

	protected ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()//
				.title(getPropertyValue(DOCUMENTATION_TITLE_PROPERTY)) //
				.description(getPropertyValue(DOCUMENTATION_DESC_PROPERTY))
				.termsOfServiceUrl(getPropertyValue(TERMS_OF_SERVICE_URL_PROPERTY)) //
				.license(getPropertyValue(LICENSE_PROPERTY))//
				.licenseUrl(getPropertyValue(LICENSE_URL_PROPERTY))//
				.version(DOCUMENTATION_API_VERSION)//
				.build();
	}

	protected OAuth passwordFlow()
	{
		final ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(
				getAuthorizationUrl());
		return new OAuth(PASSWORD_AUTHORIZATION_NAME, getAuthorizationScopes(PASSWORD_AUTHORIZATION_SCOPE),
				Arrays.asList(resourceOwnerPasswordCredentialsGrant));
	}

	protected OAuth clientCredentialFlow()
	{
		final ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(getAuthorizationUrl());
		return new OAuth(CLIENT_CREDENTIAL_AUTHORIZATION_NAME, getAuthorizationScopes(CLIENT_CREDENTIAL_AUTHORIZATION_SCOPE),
				Arrays.asList(clientCredentialsGrant));
	}

	private List<AuthorizationScope> getAuthorizationScopes(final String propertyName)
	{
		final List<AuthorizationScope> authorizationScopes = new ArrayList<>();

		final String strScopes = configurationService.getConfiguration().getString(propertyName);
		if (StringUtils.isNotEmpty(strScopes))
		{
			final String[] scopes = strScopes.split(",");
			for (final String scope : scopes)
			{
				authorizationScopes.add(new AuthorizationScope(scope, StringUtils.EMPTY));
			}

		}
		return authorizationScopes;
	}

	private List<SecurityReference> defaultAuth()
	{
		final AuthorizationScope[] authorizationScopes = {};
		return Arrays.asList(new SecurityReference(PASSWORD_AUTHORIZATION_NAME, authorizationScopes),
				new SecurityReference(CLIENT_CREDENTIAL_AUTHORIZATION_NAME, authorizationScopes));
	}

	private SecurityContext securityContext()
	{
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	protected String getAuthorizationUrl()
	{
		return configurationService.getConfiguration().getString(AUTHORIZATION_URL);
	}

	protected String getPropertyValue(final String propertyName)
	{
		return getConfigurationService().getConfiguration().getString(propertyName);
	}

	protected String getHost()
	{
		return configurationService.getConfiguration().getString(HOST, HOST_DEFAULT);
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected ApiVendorExtensionService getApiVendorExtensionService()
	{
		return apiVendorExtensionService;
	}
}