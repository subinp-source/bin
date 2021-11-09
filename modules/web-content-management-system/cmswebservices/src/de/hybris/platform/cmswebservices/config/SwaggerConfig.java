/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.config;

import de.hybris.platform.webservicescommons.swagger.services.ApiVendorExtensionService;
import com.google.common.collect.Sets;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@EnableSwagger2
@Component
public class SwaggerConfig
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "apiVendorExtensionService")
	private ApiVendorExtensionService apiVendorExtensionService;

	public static final String UNAUTHORIZED_MESSAGE = "Must be authenticated as an Admin or CMS Manager to access this resource";

	@Bean
	public Docket apiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2)//
				.apiInfo(apiInfo())//
				.select()//
				.paths(PathSelectors.any())//
				.build()//
				.securitySchemes(Collections.singletonList(passwordSecurityScheme()))//
				.securityContexts(Collections.singletonList(oauthSecurityContext()))//
				.produces(Sets.newHashSet(APPLICATION_JSON))
				.globalResponseMessage(RequestMethod.GET, globalGETResponseMessages())
				.globalResponseMessage(RequestMethod.POST, globalPOSTResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, globalPUTResponseMessages())
				.globalResponseMessage(RequestMethod.DELETE, globalDETELEResponseMessages())
				.host(getHost())//
				.extensions(getApiVendorExtensionService().getAllVendorExtensions(EXTENSIONNAME))
				.tags(new Tag("catalog versions", "Controller that provides an API to retrieve a catalog version information."),
						new Tag("catalog version synchronization", "Controller that handles synchronization of catalogs"),
						new Tag("catalog version media", "Controller that provides media."),
						new Tag("item synchronization", "Controller to retrieve a synchronization status to perform synchronization"),
						new Tag("cmsitems", "Generic controller to deal with CMS items (Components, Pages, Restrictions)."),
						new Tag("languages", "Controller to deal with languages."),
						new Tag("media", "Controller that handles searching for media."),
						new Tag("navigation", "Controller to deal with navigation objects"),
						new Tag("page restrictions", "Controller that provides an API to retrieve all pages and their restrictions."),
						new Tag("page slot components", "Controller that provides an API to update components either between slots, or within a single slot."),
						new Tag("page slot containers", "Controller that provides an API to retrieve information about containers in content slots in a page."),
						new Tag("page slot restrictions", "Controller that provides type restrictions for CMS content slots."),
						new Tag("page slots", "Controller that provides an API to retrieve all pages where a given content slot is present."),
						new Tag("page templates", "Controller to deal with page template objects"),
						new Tag("page type restrictions", "Controller that provides an API to retrieve all pages types and their restrictions types."),
						new Tag("page types", "Controller to get page types."),
						new Tag("pages", "Controller to deal with an abstract page model objects"),
						new Tag("product categories", "Controller to retrieve and search products within a Product Catalog Version."),
						new Tag("restriction types", "Controller to get restriction types."),
						new Tag("sites", "Controller to support the sites end point."),
						new Tag("types", "Controller to deal with component types."),
						new Tag("user groups", "Controller to retrieve and search for user groups."),
						new Tag("versions", "Controller to deal with versions"),
						new Tag("workflow actions", "Controller to manage workflow actions."),
						new Tag("workflow operations", "Controller that provides an API to perform different operations on workflows."),
						new Tag("workflow templates", "Controller that provides an API to retrieve workflow templates for a given catalog version."),
						new Tag("workflows", "Controller to manage workflows for CMS Items."))
				.useDefaultResponseMessages(false);
	}

	protected ApiInfo apiInfo()
	{
		return new ApiInfoBuilder()//
				.title(getPropertyValue(DOCUMENTATION_TITLE_PROPERTY))
				.description(getPropertyValue(DOCUMENTATION_DESC_PROPERTY))
				.termsOfServiceUrl(getPropertyValue(TERMS_OF_SERVICE_URL_PROPERTY))
				.license(getPropertyValue(LICENSE_PROPERTY))//
				.licenseUrl(getPropertyValue(LICENSE_URL_PROPERTY))//
				.version(DOCUMENTATION_API_VERSION)//
				.build();
	}

	protected OAuth passwordSecurityScheme()
	{
		final AuthorizationScope authorizationScope = new AuthorizationScope(getPropertyValue(AUTHORIZATION_SCOPE_PROPERTY),
				StringUtils.EMPTY);
		final ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(
				AUTHORIZATION_URL);
		return new OAuth(PASSWORD_AUTHORIZATION_NAME, Collections.singletonList(authorizationScope),
				Collections.singletonList(resourceOwnerPasswordCredentialsGrant));
	}

	protected ResponseMessage unauthorizedResponseMessage()
	{
		return new ResponseMessageBuilder()
				.code(HttpStatus.UNAUTHORIZED.value())
				.message(UNAUTHORIZED_MESSAGE)
				.build();
	}

	protected ResponseMessage genericMessage(HttpStatus httpStatus)
	{
		return new ResponseMessageBuilder()
				.code(httpStatus.value())
				.message(httpStatus.getReasonPhrase())
				.build();
	}

	protected List<ResponseMessage> globalGETResponseMessages()
	{
		return Lists.newArrayList(
				unauthorizedResponseMessage(),
				genericMessage(HttpStatus.OK)
		);
	}

	protected List<ResponseMessage> globalPUTResponseMessages()
	{
		return Lists.newArrayList(
				unauthorizedResponseMessage(),
				genericMessage(HttpStatus.OK));
	}

	protected List<ResponseMessage> globalPOSTResponseMessages()
	{
		return Lists.newArrayList(
				unauthorizedResponseMessage()
		);
	}

	protected List<ResponseMessage> globalDETELEResponseMessages()
	{
		return Lists.newArrayList(unauthorizedResponseMessage(),
				genericMessage(HttpStatus.NO_CONTENT));
	}

	protected SecurityContext oauthSecurityContext()
	{
		return SecurityContext.builder().securityReferences(oauthSecurityReferences()).forPaths(PathSelectors.any()).build();
	}

	protected List<SecurityReference> oauthSecurityReferences()
	{
		final AuthorizationScope[] authorizationScopes = {};
		return Collections.singletonList(new SecurityReference(PASSWORD_AUTHORIZATION_NAME, authorizationScopes));
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

	protected ApiVendorExtensionService getApiVendorExtensionService()
	{
		return apiVendorExtensionService;
	}
}
