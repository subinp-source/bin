/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.util.Config
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.SC_CREATED
import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED

@ManualTest
@Unroll
class B2BOrdersControllerTest extends AbstractSpockFlowTest {
    static final String B2BADMIN_USERNAME = "mark.rivers.approval@rustic-retail-hw.com"
    static final String B2BADMIN_PASSWORD = "1234"

    static final String B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
    static final String B2BCUSTOMER_PASSWORD = "1234"

    static final String OCC_OVERLAPPING_PATHS_FLAG = "occ.rewrite.overlapping.paths.enabled"
    static final ENABLED_CONTROLLER_PATH = Config.getBoolean(OCC_OVERLAPPING_PATHS_FLAG, false) ? COMPATIBLE_CONTROLLER_PATH : CONTROLLER_PATH
    static final String CONTROLLER_PATH = "/users"
    static final String COMPATIBLE_CONTROLLER_PATH = "/orgUsers"
    static final ORDER_PATH = "/orders"

    def setup() {
        authorizeTrustedClient(restClient)
    }

    def "Should not be able to create cart from order from a B2C store"() {
        given: "a registered and logged in customer with B2B Admin role"
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "A request is made to create a new cart from a given order"
        String path = getBasePath() + "/wsTestB2C" + ENABLED_CONTROLLER_PATH + '/' + B2BADMIN_USERNAME + '/cartFromOrder'
        HttpResponseDecorator response = restClient.post(
                path: path,
                query: [
                        "orderCode": "testOrder1",
                        "fields"   : "FULL"
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "request fails because a restricted B2B API endpoint is used from a B2C base-site"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_UNAUTHORIZED
        }
    }

    def "Should create a new cart from a given order"() {
        given: "a registered and logged in customer with B2B Admin role"
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "A request is made to create a new cart from an existing order"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + ENABLED_CONTROLLER_PATH + '/' + B2BADMIN_USERNAME + '/cartFromOrder',
                query: [
                        "orderCode": "testOrder1",
                        "fields"   : "FULL"
                ],
                contentType: JSON,
                requestContentType: URLENC
        )
        then: "An empty list of modifications is retrieved"
        with(response) {
            status == SC_CREATED
            data != ""
        }

        where:
        descriptor << [ENABLED_CONTROLLER_PATH]
    }

    def "B2B Customer creates a new order with payment type CARD"() {
        given: "a registered and logged in customer"
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to create a new order"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + ENABLED_CONTROLLER_PATH + '/' + b2bCustomer.id + ORDER_PATH,
                query: [
                        'cartId'      : "xyz",
                        'termsChecked': true,
                        'fields'      : FIELD_SET_LEVEL_FULL
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets the newly created order"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_OK
        }
    }

    def "Returned orders contain purchase order number and cost center"() {
        given: "a registered and logged in customer"
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to create new order"
        HttpResponseDecorator postResult = restClient.post(
                path: getBasePathWithSite() + ENABLED_CONTROLLER_PATH + '/' + b2bCustomer.id + ORDER_PATH,
                query: [
                        'cartId'      : "ccpo",
                        'termsChecked': true,
                        'fields'      : FIELD_SET_LEVEL_FULL
                ])
        then: "expect an order"
        with(postResult) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)){
                println(data)
            }
            status == SC_OK
        }
        def orderCode = postResult.data.code

        when: "customer retrieves his orders"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + "/orders",
                contentType: JSON,
                requestContentType: URLENC)

        then: "all orders contain purchase order number and cost center"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_OK
            def order = data.orders.find {it.code == orderCode}
            order != null
            order.purchaseOrderNumber == "purchaseCCPO"
            order.costCenter.code == "Rustic_US"
        }
    }
}
