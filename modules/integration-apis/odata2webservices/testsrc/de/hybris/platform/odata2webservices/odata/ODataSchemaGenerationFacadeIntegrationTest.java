/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2webservices.odata;

import static de.hybris.platform.odata2services.odata.ODataResponse.createFrom;
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.oDataGetMetadataRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectItemsContext;
import de.hybris.platform.integrationservices.util.IntegrationObjectsContext;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.integrationservices.util.XmlObject;
import de.hybris.platform.odata2services.odata.IncorrectQueryParametersException;
import de.hybris.platform.odata2services.odata.ODataContextGenerator;
import de.hybris.platform.odata2services.odata.ODataResponse;
import de.hybris.platform.odata2services.odata.ODataSchema;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

@IntegrationTest
public class ODataSchemaGenerationFacadeIntegrationTest extends ServicelayerTest
{
	private static final String SCHEMA_NAME = "HybrisCommerceOData";
	private static final String DEFAULT_INTEGRATION_OBJECT = "MyProduct";
	@Rule
	public IntegrationObjectItemsContext definitionsContext = IntegrationObjectItemsContext.create();
	@Rule
	public IntegrationObjectsContext integrationObjectsContext = IntegrationObjectsContext.create();
	@Resource(name = "defaultODataFacade")
	private ODataFacade facade;
	@Resource
	private ODataContextGenerator oDataContextGenerator;

	@After
	public void cleanup()
	{
		IntegrationTestUtil.removeAll(InboundChannelConfigurationModel.class);
		IntegrationTestUtil.removeAll(IntegrationObjectModel.class);
	}

	@Test
	public void testRequestedItemDoesNotExist() throws ODataException
	{
		final ODataSchema schema = whenGettingSchemaForItems("Product");

		assertThat(schema.isEmpty()).isTrue();
	}

	@Test
	public void testOnlyRequestedItemReturnedEvenIfItIsReferencedFromSomeOtherItem() throws ODataException, ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true];",
				"; MyProduct",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
				"; MyProduct; SomeCategory; Category",
				"; MyProduct; SomeProduct ; Product",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code)",
				"; MyProduct:SomeCategory             ; code                      ; Category:code                     ;",
				"; MyProduct:SomeProduct              ; code                      ; Product:code                      ;",
				"; MyProduct:SomeProduct              ; supercategories           ; Product:supercategories           ; MyProduct:SomeCategory");

		final ODataSchema schema = whenGettingSchemaForItems("SomeCategory");

		assertThat(schema.getEntityTypeNames()).containsExactly("SomeCategory");
	}

	@Test
	public void testRequestedItemReturnedWithItsReferencedItems() throws ODataException, ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true];",
				"; MyProduct",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
				"; MyProduct; SomeUnit    ; Unit",
				"; MyProduct; SomeProduct ; Product",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code)",
				"; MyProduct:SomeUnit                 ; code                      ; Unit:code                         ;",
				"; MyProduct:SomeProduct              ; code                      ; Product:code                      ;",
				"; MyProduct:SomeProduct              ; unit                      ; Product:unit                      ; MyProduct:SomeUnit");

		final ODataSchema schema = whenGettingSchemaForItems("SomeProduct");

		assertThat(schema.getEntityTypeNames()).containsExactlyInAnyOrder("SomeProduct", "SomeUnit");
	}

	@Test
	public void testItemsAreReturnedForIntegrationObjectSpecifiedInServiceRootWhenOthersExist()
			throws ODataException, ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true];",
				"; InboundProduct",
				"; OutboundProduct",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
				"; InboundProduct ; Product ; Product",
				"; OutboundProduct; Product ; Product",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
				"; InboundProduct:Product               ; code                      ; Product:code",
				"; OutboundProduct:Product              ; code                      ; Product:code");

		final ODataSchema schema = whenGettingSchemaForIntegrationObject("InboundProduct");

		assertThat(schema.getEntityTypeNames()).containsExactly("Product");
	}

	@Test
	public void testMultipleItemsRequested()
	{
		assertThatThrownBy(() -> whenGettingSchemaForItems("Category", "Product")).isInstanceOf(
				IncorrectQueryParametersException.class);
	}

	@Test
	public void abstractReferencedItemErrorResponse() throws ImpExException, ODataException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)",
				"; IO1                ; INBOUND",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)     ; root[default = false]",
				"; IO1                                   ; PrincipalGroup     ; PrincipalGroup ;      ;",
				"; IO1                                   ; Employee           ; Employee       ; true ;",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]",
				"; IO1:PrincipalGroup                                                 ; uid                         ; PrincipalGroup:uid                                 ;                                                           ; true                   ;",
				"; IO1:Employee                                                       ; name                        ; Employee:name                                      ;                                                           ;                        ;",
				"; IO1:Employee                                                       ; uid                         ; Employee:uid                                       ;                                                           ; true                   ;",
				"; IO1:Employee                                                       ; groups                      ; Employee:groups                                    ; IO1:PrincipalGroup                                        ;                        ; true"
		);
		final ODataRequest oDataRequest = oDataGetMetadataRequest("IO1");
		final ODataContext context = oDataContext(oDataRequest);

		final org.apache.olingo.odata2.api.processor.ODataResponse response = facade.handleGetSchema(context);

		final XmlObject xml = XmlObject.createFrom(response.getEntityAsStream());
		assertThat(xml.get("/error/code")).isEqualTo("invalid_property_definition");
		assertThat(xml.get("/error/message")).isEqualTo("Invalid attribute found [Employee.groups]. " +
				"Cannot define attribute of an abstract item type as autocreate or partof for the item that it belongs to.");
	}

	@Test
	public void testAllItemsReturnedWhenAllMetadataIsRequested() throws ODataException, ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true];",
				"; MyProduct",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
				"; MyProduct; SomeCategory; Category",
				"; MyProduct; SomeProduct ; Product",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier)",
				"; MyProduct:SomeCategory             ; code                      ; Category:code",
				"; MyProduct:SomeProduct              ; code                      ; Product:code");

		final ODataSchema schema = whenGettingSchema();

		assertThat(schema.getEntityTypeNames()).containsExactlyInAnyOrder("SomeProduct", "SomeCategory");
	}


	private ODataSchema whenGettingSchema() throws ODataException
	{
		return whenGettingSchemaForItems(StringUtils.EMPTY);
	}

	private ODataSchema whenGettingSchemaForItems(final String... items) throws ODataException
	{
		return whenGettingSchemaForIntegrationObjectAndItems(DEFAULT_INTEGRATION_OBJECT, items);
	}

	private ODataSchema whenGettingSchemaForIntegrationObject(final String integrationObject) throws ODataException
	{
		return whenGettingSchemaForIntegrationObjectAndItems(integrationObject, StringUtils.EMPTY);
	}

	private ODataSchema whenGettingSchemaForIntegrationObjectAndItems(final String integrationObject, final String... items)
			throws ODataException
	{
		final ODataRequest oDataRequest = oDataGetMetadataRequest(integrationObject, items);
		final ODataContext context = oDataContext(oDataRequest);
		final ODataResponse response = createFrom(facade.handleGetSchema(context));
		return response.getSchema(SCHEMA_NAME);
	}

	private ODataContext oDataContext(final ODataRequest oDataRequest)
	{
		return oDataContextGenerator.generate(oDataRequest);
	}
}