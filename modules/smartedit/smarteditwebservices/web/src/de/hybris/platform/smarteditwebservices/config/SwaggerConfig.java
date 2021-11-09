/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.config;

import static de.hybris.platform.smarteditwebservices.constants.SmarteditwebservicesConstants.*;
import static java.util.Collections.singletonList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.smarteditwebservices.constants.SmarteditwebservicesConstants;
import de.hybris.platform.webservicescommons.swagger.services.ApiVendorExtensionService;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Configuration for swagger api documentation
 */
@EnableSwagger2
@Component
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
				.securitySchemes(Arrays.asList(passwordFlow(), clientCredentialFlow()))//
				.securityContexts(singletonList(securityContext()))//
				.host(getHost())//
				.extensions(getApiVendorExtensionService().getAllVendorExtensions(SmarteditwebservicesConstants.EXTENSIONNAME))
				.produces(Sets.newHashSet(APPLICATION_JSON))//
				.tags(new Tag("configurations", "Smartedit Configurations"),
						new Tag("languages", "Smartedit Languages"));
	}

	protected ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()//
				.title(getPropertyValue(DOCUMENTATION_TITLE_PROPERTY))//
				.description(getPropertyValue(DOCUMENTATION_DESC_PROPERTY))//
				.termsOfServiceUrl(getPropertyValue(TERMS_OF_SERVICE_URL_PROPERTY))//
				.license(getPropertyValue(LICENSE_PROPERTY))//
				.licenseUrl(getPropertyValue(LICENSE_URL_PROPERTY))//
				.version(DOCUMENTATION_API_VERSION)//
				.build();
	}

	protected OAuth passwordFlow()
	{
		final ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = //
				new ResourceOwnerPasswordCredentialsGrant(getAuthorizationUrl());
		return new OAuth(PASSWORD_AUTHORIZATION_NAME,
				singletonList(getAuthorizationScope(PASSWORD_AUTHORIZATION_SCOPE)),
				singletonList(resourceOwnerPasswordCredentialsGrant));
	}

	protected OAuth clientCredentialFlow()
	{
		final ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(getAuthorizationUrl());
		return new OAuth(CLIENT_CREDENTIAL_AUTHORIZATION_NAME,
				singletonList(getAuthorizationScope(CLIENT_CREDENTIAL_AUTHORIZATION_SCOPE)),
				singletonList(clientCredentialsGrant));
	}

	protected String getPropertyValue(final String propertyName)
	{
		return getConfigurationService().getConfiguration().getString(propertyName);
	}

	protected AuthorizationScope getAuthorizationScope(final String key)
	{
		return new AuthorizationScope(getPropertyValue(key), StringUtils.EMPTY);
	}

	protected String getHost()
	{
		return getConfigurationService().getConfiguration().getString(HOST, HOST_DEFAULT);
	}

	protected String getAuthorizationUrl()
	{
		return getConfigurationService().getConfiguration().getString(AUTHORIZATION_URL);
	}

	protected SecurityContext securityContext()
	{
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	protected List<SecurityReference> defaultAuth()
	{
		final AuthorizationScope[] authorizationScopes = {};
		return Arrays.asList(new SecurityReference(PASSWORD_AUTHORIZATION_NAME, authorizationScopes),
				new SecurityReference(CLIENT_CREDENTIAL_AUTHORIZATION_NAME, authorizationScopes));
	}

	protected ConfigurationService getConfigurationService()
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
