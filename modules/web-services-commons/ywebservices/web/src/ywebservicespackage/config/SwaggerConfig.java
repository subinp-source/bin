/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ywebservicespackage.config;

import static ywebservicespackage.constants.YWebServicesConstants.API_VERSION;
import static ywebservicespackage.constants.YWebServicesConstants.AUTHORIZATION_SCOPE_PROPERTY;
import static ywebservicespackage.constants.YWebServicesConstants.AUTHORIZATION_URL;
import static ywebservicespackage.constants.YWebServicesConstants.CLIENT_CREDENTIAL_AUTHORIZATION_NAME;
import static ywebservicespackage.constants.YWebServicesConstants.DOCUMENTATION_DESC_PROPERTY;
import static ywebservicespackage.constants.YWebServicesConstants.DOCUMENTATION_TITLE_PROPERTY;
import static ywebservicespackage.constants.YWebServicesConstants.HOST;
import static ywebservicespackage.constants.YWebServicesConstants.HOST_DEFAULT;
import static ywebservicespackage.constants.YWebServicesConstants.LICENSE_PROPERTY;
import static ywebservicespackage.constants.YWebServicesConstants.LICENSE_URL_PROPERTY;
import static ywebservicespackage.constants.YWebServicesConstants.PASSWORD_AUTHORIZATION_NAME;
import static ywebservicespackage.constants.YWebServicesConstants.TERMS_OF_SERVICE_URL_PROPERTY;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.swagger.strategies.ApiVendorExtensionStrategy;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
import ywebservicespackage.constants.YWebServicesConstants;


@EnableSwagger2
@Component
public class SwaggerConfig
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "apiVendorExtensionStrategy")
	private ApiVendorExtensionStrategy apiVendorExtensionStrategy;

	@Bean
	public Docket apiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(apiInfo())//
				.select()//
				.paths(PathSelectors.any())//
				.build()//
				.securitySchemes(Arrays.asList(clientCredentialFlow(), passwordFlow()))//
				.securityContexts(Arrays.asList(oauthSecurityContext()))//
				.host(getHost())//
				.extensions(apiVendorExtensionStrategy.getVendorExtensions(YWebServicesConstants.EXTENSIONNAME))//
				.tags(new Tag("Sample", "Sample Methods"));
	}

	protected ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()//
				.title(getPropertyValue(DOCUMENTATION_TITLE_PROPERTY))//
				.description(getPropertyValue(DOCUMENTATION_DESC_PROPERTY))//
				.termsOfServiceUrl(getPropertyValue(TERMS_OF_SERVICE_URL_PROPERTY))//
				.license(getPropertyValue(LICENSE_PROPERTY))//
				.licenseUrl(getPropertyValue(LICENSE_URL_PROPERTY))//
				.version(API_VERSION)//
				.build();
	}

	protected OAuth passwordFlow()
	{
		final AuthorizationScope authorizationScope = new AuthorizationScope(getPropertyValue(AUTHORIZATION_SCOPE_PROPERTY),
				StringUtils.EMPTY);
		final ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(
				AUTHORIZATION_URL);
		return new OAuth(PASSWORD_AUTHORIZATION_NAME, Arrays.asList(authorizationScope),
				Arrays.asList(resourceOwnerPasswordCredentialsGrant));
	}

	protected OAuth clientCredentialFlow()
	{
		final AuthorizationScope authorizationScope = new AuthorizationScope(getPropertyValue(AUTHORIZATION_SCOPE_PROPERTY),
				StringUtils.EMPTY);
		final ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(AUTHORIZATION_URL);
		return new OAuth(CLIENT_CREDENTIAL_AUTHORIZATION_NAME, Arrays.asList(authorizationScope),
				Arrays.asList(clientCredentialsGrant));
	}

	protected String getPropertyValue(final String propertyName)
	{
		return configurationService.getConfiguration().getString(propertyName);
	}

	protected SecurityContext oauthSecurityContext()
	{
		return SecurityContext.builder().securityReferences(oauthSecurityReferences()).forPaths(PathSelectors.any()).build();
	}

	protected List<SecurityReference> oauthSecurityReferences()
	{
		final AuthorizationScope[] authorizationScopes = {};
		return Arrays.asList(new SecurityReference(PASSWORD_AUTHORIZATION_NAME, authorizationScopes), new SecurityReference(
				CLIENT_CREDENTIAL_AUTHORIZATION_NAME, authorizationScopes));
	}

	private String getHost()
	{
		return configurationService.getConfiguration().getString(HOST, HOST_DEFAULT);
	}
}
