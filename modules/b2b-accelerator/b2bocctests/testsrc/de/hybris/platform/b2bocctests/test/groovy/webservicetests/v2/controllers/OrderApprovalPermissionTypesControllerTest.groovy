/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED

@ManualTest
@Unroll
class OrderApprovalPermissionTypesControllerTest extends AbstractUserTest {
    static final String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
    static final String B2BADMIN_PASSWORD = "1234"

    static final B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
    static final String B2BCUSTOMER_PASSWORD = "1234"

    static final NUMBER_OF_ALL_PERMISSION_TYPES = 3

    static final PERMISSION_TYPES_PATH = "/orderApprovalPermissionTypes"

    def "B2B Admin gets order approval permission types: #format"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to get order approval permission types"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + PERMISSION_TYPES_PATH,
                contentType: format,
                requestContentType: URLENC)

        then: "he gets order approval permission types"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_OK
            data.orderApprovalPermissionTypes.size() == NUMBER_OF_ALL_PERMISSION_TYPES
        }

        where:
        format << [JSON]
    }

    def "B2B Customer tries to get order approval permission types: #format"() {
        given: "a registered and logged in B2B customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to get order approval permission types"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + PERMISSION_TYPES_PATH,
                contentType: format,
                requestContentType: URLENC)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_UNAUTHORIZED
        }

        where:
        format << [JSON]
    }
}

