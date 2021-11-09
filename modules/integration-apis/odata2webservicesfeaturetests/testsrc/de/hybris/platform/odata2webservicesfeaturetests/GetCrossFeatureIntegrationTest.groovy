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

package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.inboundservices.enums.AuthenticationType
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel
import de.hybris.platform.inboundservices.util.InboundMonitoringRule
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.integrationservices.util.JsonObject
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants
import de.hybris.platform.odata2webservicesfeaturetests.ws.BasicAuthRequestBuilder
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer
import org.junit.Rule
import org.junit.Test

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static de.hybris.platform.odata2webservicesfeaturetests.useraccess.UserAccessTestUtils.givenUserExistsWithUidAndGroups
import static de.hybris.platform.odata2webservicesfeaturetests.ws.InboundChannelConfigurationBuilder.inboundChannelConfigurationBuilder

@NeedsEmbeddedServer(webExtensions = Odata2webservicesConstants.EXTENSIONNAME)
@IntegrationTest
class GetCrossFeatureIntegrationTest extends ServicelayerSpockSpecification {

    private static final String CROSS_FEATURE_IO = "CrossFeature"
    private static final String USER_UID = "tester"
    private static final String PASSWORD = "retset"

    @Rule
    InboundMonitoringRule monitoring = InboundMonitoringRule.disabled()

    def setupSpec() {
        importCsv("/impex/essentialdata-odata2services.impex", "UTF-8") // for the integrationadmingroup
        IntegrationTestUtil.importImpEx(
                '$catalog = Default',
                '$version = Staged',
                '$catalogVersion = $catalog:$version',
                'INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)',
                "; $CROSS_FEATURE_IO; INBOUND",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "; $CROSS_FEATURE_IO  ; Catalog         ; Catalog",
                "; $CROSS_FEATURE_IO  ; CatalogVersion  ; CatalogVersion",
                "; $CROSS_FEATURE_IO  ; Product         ; Product",
                "; $CROSS_FEATURE_IO  ; Category        ; Category",
                'INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]',
                "; $CROSS_FEATURE_IO:Catalog        ; id              ; Catalog:id              ;",
                "; $CROSS_FEATURE_IO:CatalogVersion ; catalog         ; CatalogVersion:catalog  ; $CROSS_FEATURE_IO:Catalog",
                "; $CROSS_FEATURE_IO:CatalogVersion ; version         ; CatalogVersion:version  ;",
                "; $CROSS_FEATURE_IO:Product        ; code            ; Product:code            ;",
                "; $CROSS_FEATURE_IO:Product        ; catalogVersion  ; Product:catalogVersion  ; $CROSS_FEATURE_IO:CatalogVersion",
                "; $CROSS_FEATURE_IO:Product        ; supercategories ; Product:supercategories ; $CROSS_FEATURE_IO:Category",
                "; $CROSS_FEATURE_IO:Category       ; code            ; Category:code           ;",
                "; $CROSS_FEATURE_IO:Category       ; products        ; Category:products       ; $CROSS_FEATURE_IO:Product",
                'INSERT_UPDATE Catalog; id[unique = true]; name[lang = en]; defaultCatalog;',
                '; $catalog ; $catalog ; true',
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                '; $catalog ; $version ; true',
                'INSERT_UPDATE Category; code[unique = true];',
                '; category1',
                '; category2',
                '; category3',
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version); supercategories(code)',
                '; pr1-1   ; $catalogVersion; category1',
                '; pr2-1   ; $catalogVersion; category1',
                '; pr3-2   ; $catalogVersion; category2',
                '; pr4-2   ; $catalogVersion; category2',
                '; pr5-2_3 ; $catalogVersion; category2, category3',
                '; pr6     ; $catalogVersion;')
        givenUserExistsWithUidAndGroups(USER_UID, PASSWORD, "integrationadmingroup")
        inboundChannelConfigurationBuilder()
                .withCode(CROSS_FEATURE_IO)
                .withAuthType(AuthenticationType.BASIC)
                .build()
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove InboundChannelConfigurationModel, { it?.integrationObject?.code == CROSS_FEATURE_IO }
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.removeAll CategoryModel
        IntegrationTestUtil.removeSafely EmployeeModel, { it.uid == USER_UID }
    }

    @Test
    def "request with \$expand, \$top, \$skip, and \$inlinecount"() {
        when:
        def response = basicAuthRequest()
                .path('Products')
                .queryParam('$expand', 'supercategories')
                .queryParam('$top', 10)
                .queryParam('$skip', 1)
                .queryParam('$inlinecount', 'allpages')
                .build()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get()

        then:
        response.status == 200
        def json = extractBody response
        json.getCollection("\$.d.results").size() == 5
        json.getString('d.__count') == '6'
        json.getCollectionOfObjects('d.results[?(@.code == "pr2-1")].supercategories.results[*].code') == ['category1']
        json.getCollectionOfObjects('d.results[?(@.code == "pr3-2")].supercategories.results[*].code') == ['category2']
        json.getCollectionOfObjects('d.results[?(@.code == "pr4-2")].supercategories.results[*].code') == ['category2']
        json.getCollectionOfObjects('d.results[?(@.code == "pr5-2_3")].supercategories.results[*].code') == ['category2', 'category3']
        json.getCollectionOfObjects('d.results[?(@.code == "pr6")].supercategories.results[*]') == []
        json.getCollectionOfObjects("d.results[*].supercategories.__deferred").isEmpty()
    }

    BasicAuthRequestBuilder basicAuthRequest() {
        new BasicAuthRequestBuilder()
                .extensionName(Odata2webservicesConstants.EXTENSIONNAME)
                .credentials(USER_UID, PASSWORD) // defined inside setup()
                .path(CROSS_FEATURE_IO)
    }

    JsonObject extractBody(final Response response) {
        JsonObject.createFrom((InputStream) response.getEntity())
    }
}
