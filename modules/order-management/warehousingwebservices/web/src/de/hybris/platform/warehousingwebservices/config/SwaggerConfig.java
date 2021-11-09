/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.warehousingwebservices.config;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@EnableSwagger2
@Component
public class SwaggerConfig
{

	private static final String LICENSE_URL = "";
	private static final String TERMS_OF_SERVICE_URL = "";
	private static final String LICENSE = "Use of this file is subject to the terms of your agreement with SAP SE or its affiliates respecting the use of the SAP product for which this file relates.";
	private static final String DESC = "These services manage consignments, stock levels, and points of service.";
	private static final String TITLE = "Warehousing Webservices";
	private static final String VERSION = "1.0";

	@Bean
	public Docket apiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo()
	{
		return new ApiInfoBuilder().title(TITLE).description(DESC).termsOfServiceUrl(TERMS_OF_SERVICE_URL).license(LICENSE)
				.licenseUrl(LICENSE_URL).version(VERSION).build();
	}
}
