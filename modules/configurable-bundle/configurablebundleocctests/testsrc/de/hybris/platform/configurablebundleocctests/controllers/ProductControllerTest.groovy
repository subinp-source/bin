/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleocctests.controllers

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.http.HttpStatus.SC_OK

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

@ManualTest
@Unroll
class ProductControllerTest extends AbstractSpockFlowTest{

    def "Check product details contain empty bundle templates: #format"() {

        given: "a prduct with code #productCode"
        def productCode = "81185"

        when: "product details are fetched"
        HttpResponseDecorator productResponse = restClient.get(
                path: getBasePathWithSite() + '/products/' + productCode,
                contentType: format,
                requestContentType: URLENC
        )

        then: "a product with empty bundleTemplates attribute is returned"
        with(productResponse) {
            status == SC_OK
            data.code == productCode
            isEmpty(data.bundleTemplates)
        }

        where:
        format << [XML, JSON]
    }

    def "Check product details contain bundle templates: #format"() {

        given: "a product with code #productCode"
        def productCode = "1225694"

        when: "product details are fetched"
        HttpResponseDecorator productResponse = restClient.get(
                path: getBasePathWithSite() + '/products/' + productCode,
                contentType: format,
                requestContentType: URLENC
        )

        then: "a product with populated bundleTemplates is returned"
        with(productResponse) {
            status == SC_OK
            data.code == productCode
            isNotEmpty(data.bundleTemplates)
            data.bundleTemplates.size() == 1
            data.bundleTemplates[0].id == "PhotoOTGCameraComponent"
            data.bundleTemplates[0].name == "Camera Component"
            data.bundleTemplates[0].rootBundleTemplateName == "Photo On The Go Package"
        }

        where:
        format << [XML, JSON]
    }
}
