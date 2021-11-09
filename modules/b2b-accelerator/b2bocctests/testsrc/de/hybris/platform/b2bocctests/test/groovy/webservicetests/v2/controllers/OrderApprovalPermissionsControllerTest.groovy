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
import groovyx.net.http.RESTClient
import org.apache.commons.lang3.StringUtils
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class OrderApprovalPermissionsControllerTest extends AbstractUserTest {
    static final String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
    static final String B2BADMIN_PASSWORD = "1234"

    static final B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
    static final String B2BCUSTOMER_PASSWORD = "1234"

    static final NUMBER_OF_ALL_PERMISSIONS = 3
    static final PAGE_SIZE = 20

    static final PERMISSIONS_PATH = "/orderApprovalPermissions"

    def "B2B Admin gets order approval permissions"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to get order approval permissions available to his organizational unit"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets order approval permissions"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_OK
            data.orderApprovalPermissions.size() >= NUMBER_OF_ALL_PERMISSIONS
            data.sorts.size() > 0
            data.pagination.pageSize == PAGE_SIZE
            data.pagination.currentPage == 0
            data.pagination.totalResults.toInteger() >= NUMBER_OF_ALL_PERMISSIONS
        }
    }

    def "B2B Customer tries to get order approval permissions"() {
        given: "a registered and logged in B2B customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to get order approval permissions"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + PERMISSIONS_PATH,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_UNAUTHORIZED
        }
    }

    def "B2B Admin gets a specific order approval permission: #permissionCode"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to get a specific order approval permission"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH + "/" + permissionCode,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets a specific order approval permission"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_OK

            comparePermissions(data, permissionCode, threshold, unitUid, currencyIso, periodRangeName, active)
        }

        where:
        permissionCode           | threshold | unitUid  | currencyIso | periodRangeName | permissionType                        | active
        "Rustic_1K_USD_ORDER"    | 1000.0    | "Rustic" | "USD"       | null            | 'B2BOrderThresholdPermission'         | true
        "Rustic_3K_USD_MONTH"    | 3000.0    | "Rustic" | "USD"       | "MONTH"         | 'B2BOrderThresholdTimespanPermission' | true
        "Rustic_Budget_Exceeded" | null      | "Rustic" | null        | null            | 'B2BBudgetExceededPermission'         | true
    }

    def "B2B Admin tries to get a non-existing order approval permission"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to get a non-existing order approval permission"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH + "/" + permissionCode,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets not found response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_NOT_FOUND
        }

        where:
        permissionCode << ["Non_Existing_Permission"]
    }

    def "B2B Admin tries to get a specific order approval permission from another organizational unit"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to get a specific order approval permission from another organizational unit"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH + "/" + permissionCode,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets bad request response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_NOT_FOUND
        }

        where:
        permissionCode << ["Pronto_5K_USD_ORDER"]
    }

    def "B2B Customer tries to get a specific order approval permissions"() {
        given: "a registered and logged in customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to get a specific order approval permission"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + PERMISSIONS_PATH + "/" + permissionCode,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_UNAUTHORIZED
        }

        where:
        permissionCode << ["Rustic_1K_USD_ORDER"]
    }

    def "B2B Admin edits a specific order approval permission: #permissionCodeInRequest"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to edit a specific order approval permission"
        HttpResponseDecorator response = restClient.patch(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH + "/" + permissionCodeInRequest,
                body: patchBody,
                contentType: JSON,
                requestContentType: JSON)

        def updatedInfo = getOrderApprovalPermission(restClient, b2bAdminCustomer, permissionCode)

        then: "his changes are successfully saved"
        with(response) { status == SC_OK }

        and: "new values are visible"
        with(updatedInfo) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }

            comparePermissions(data, permissionCode, threshold, unitUid, currencyIso, periodRangeName, active)
        }

        where:
        permissionCodeInRequest  | permissionCode           | threshold | unitUid         | currencyIso | periodRangeName | active | patchBody
        "Rustic_1K_USD_ORDER"    | "Rustic_1K_USD_ORDER_2"  | 1000.0    | "Rustic"        | "USD"       | null            | true   | '{"code": "Rustic_1K_USD_ORDER_2"}'
        "Rustic_3K_USD_MONTH"    | "Rustic_3K_USD_MONTH"    | 2000.0    | "Rustic"        | "EUR"       | "YEAR"          | true   | '{"currency": {"isocode": "EUR"}, "periodRange": "YEAR", "threshold": 2000}'
        "Rustic_Budget_Exceeded" | "Rustic_Budget_Exceeded" | null      | "Rustic_Retail" | null        | null            | true   | '{"orgUnit": {"uid": "Rustic_Retail"}}'
    }

    def "B2B Admin tries to edit a non-existing order approval permission: #permissionCodeInRequest"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to edit a non-existing order approval permission"
        HttpResponseDecorator response = restClient.patch(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH + "/" + permissionCodeInRequest,
                body: patchBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets not found response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_NOT_FOUND
        }

        where:
        permissionCodeInRequest   | patchBody
        "Non_Existing_Permission" | '{"code": "Non_Existing_Permission_2"}'
    }

    def "B2B Customer tries to edit a specific order approval permission: #permissionCodeInRequest"() {
        given: "a registered and logged in customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to edit a specific order approval permission"
        HttpResponseDecorator response = restClient.patch(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + PERMISSIONS_PATH + "/" + permissionCodeInRequest,
                body: patchBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_UNAUTHORIZED
        }

        where:
        permissionCodeInRequest | patchBody
        "Rustic_1K_USD_ORDER"   | '{"code": "Rustic_1K_USD_ORDER_2"}'
        "Rustic_3K_USD_MONTH"   | '{"currency": {"isocode": "EUR"}, "periodRange": "YEAR", "threshold": 2000}'
    }

    def "B2B Admin tries to edit a specific order approval permission with non-valid attributes: #descriptor"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to edit a specific order approval permission with non-valid attributes"
        HttpResponseDecorator response = restClient.patch(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + PERMISSIONS_PATH + "/" + permissionCodeInRequest,
                body: patchBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_BAD_REQUEST
            data.errors.find {
                it.message == message && it.reason == reason && it.subject == subject && it.subjectType == subjectType && it.type == type
            }
        }

        where:
		permissionCodeInRequest | descriptor                 | message                                  | reason    | subject     | subjectType | type              | patchBody
		"Rustic_3K_USD_MONTH"   | "Rustic_MINUS3K_USD_MONTH" | "Please enter a value greater than zero" | "invalid" | "threshold" | "parameter" | "ValidationError" | '{"threshold": -3000}'
		"Rustic_2K_USD_ORDER"   | "Rustic_MINUS2K_USD_ORDER" | "Please enter a value greater than zero" | "invalid" | "threshold" | "parameter" | "ValidationError" | '{"threshold": -2000}'
    }

    def "B2B Admin enables/disables a specific order approval permission: #permissionCodeInRequest"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to enable/disable a specific order approval permission by editing the permission"
        HttpResponseDecorator response = restClient.patch(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH + "/" + permissionCodeInRequest,
                body: patchBody,
                contentType: JSON,
                requestContentType: JSON)

        def updatedInfo = getOrderApprovalPermission(restClient, b2bAdminCustomer, permissionCode)

        then: "activation/deactivation is successfully applied"
        with(response) { status == SC_OK }

        and: "activation/deactivation is visible"
        with(updatedInfo) {
            comparePermissions(data, permissionCode, threshold, unitUid, currencyIso, periodRangeName, active)
        }

        where:
        permissionCodeInRequest | permissionCode       | threshold | unitUid  | currencyIso | periodRangeName | active | patchBody
        "Rustic_4K_USD_DAY"     | "Rustic_4K_USD_DAY"  | 4000.0    | "Rustic" | "USD"       | "DAY"           | false  | '{"active": false}'
        "Rustic_5K_USD_YEAR"    | "Rustic_5K_USD_YEAR" | 5000.0    | "Rustic" | "USD"       | "YEAR"          | true   | '{"active": true}'
    }

    def "B2B Admin creates a new order approval permission: #permissionCode"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to create a new order approval permission"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets the newly created specific order approval permission"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_CREATED

            comparePermissions(data, permissionCode, threshold, unitUid, currencyIso, periodRangeName, active)
        }

        where:
        permissionCode                  | threshold | unitUid         | currencyIso | periodRangeName | permissionType                        | active | postBody
        "Rustic_3K_USD_ORDER"           | 3000.0    | "Rustic"        | "EUR"       | null            | 'B2BOrderThresholdPermission'         | true   | '{"active": true, "code": "Rustic_3K_USD_ORDER", "currency": {"isocode": "EUR"}, "orgUnit": {"uid": "Rustic"}, "threshold": 3000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdPermission", "name": "B2BOrderThresholdPermission"}}'
        "Rustic_Retail_10K_USD_QUARTER" | 10000.0   | "Rustic_Retail" | "USD"       | "QUARTER"       | 'B2BOrderThresholdTimespanPermission' | true   | '{"active": true, "code": "Rustic_Retail_10K_USD_QUARTER", "currency": {"isocode": "USD"}, "orgUnit": {"uid": "Rustic_Retail"}, "periodRange": "QUARTER", "threshold": 10000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdTimespanPermission", "name": "B2BOrderThresholdTimespanPermission"}}'
        "Rustic_Retail_Budget_Exceeded" | null      | "Rustic_Retail" | null        | null            | 'B2BBudgetExceededPermission'         | true   | '{"active": true, "code": "Rustic_Retail_Budget_Exceeded", "orgUnit": {"uid": "Rustic_Retail"}, "orderApprovalPermissionType" : {"code": "B2BBudgetExceededPermission", "name": "B2BBudgetExceededPermission"}}'
    }

    def "B2B Admin tries to create a new order approval permission with an already existing permission code: #permissionCode"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to create a new order approval permission with an already existing permission code"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets bad request response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_BAD_REQUEST

        }

        where:
        permissionCode        | postBody
        "Rustic_3K_USD_ORDER" | '{"active": true, "code": "Rustic_3K_USD_MONTH", "currency": {"isocode": "USD"}, "orgUnit": {"uid": "Rustic"}, "periodRange": "MONTH", "threshold": 3000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdTimespanPermission", "name": "B2BOrderThresholdTimespanPermission"}}'
    }

    def "B2B Admin tries to create a new order approval permission with non-valid attributes: #permissionCode"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bAdminCustomer = [
                'id'      : B2BADMIN_USERNAME,
                'password': B2BADMIN_PASSWORD
        ]
        authorizeCustomer(restClient, b2bAdminCustomer)

        when: "he requests to create a new order approval permission with non-valid attributes"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bAdminCustomer.id + PERMISSIONS_PATH,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets bad request response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_BAD_REQUEST
            data.errors.find {
                it.message == message && it.reason == reason && it.subject == subject && it.subjectType == subjectType && it.type == type
            }

        }

        where:
        permissionCode                      | message                                             | reason    | subject                       | subjectType | type              | postBody
        "Rustic_300K_ORDER"                 | "This field is required."                | "missing" | "currency"                    | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_300K_ORDER", "orgUnit": {"uid": "Rustic"}, "threshold": 3000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdPermission", "name": "B2BOrderThresholdPermission"}}'
        "Rustic_MINUS3K_USD_ORDER"          | "Please enter a value greater than zero" | "invalid" | "threshold"                   | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_MINUS3K_USD_ORDER", "currency": {"isocode": "USD"}, "orgUnit": {"uid": "Rustic"}, "threshold": -3000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdPermission", "name": "B2BOrderThresholdPermission"}}'
        "Rustic_USD_ORDER"                  | "This field is required."                | "missing" | "threshold"                   | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_USD_ORDER", "currency": {"isocode": "USD"}, "orgUnit": {"uid": "Rustic"}, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdPermission", "name": "B2BOrderThresholdPermission"}}'
        "Rustic_Retail_100K_QUARTER"        | "This field is required."                | "missing" | "currency"                    | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_Retail_100K_QUARTER", "orgUnit": {"uid": "Rustic_Retail"}, "periodRange": "QUARTER", "threshold": 10000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdTimespanPermission", "name": "B2BOrderThresholdTimespanPermission"}}'
        "Rustic_Retail_MINUS1K_USD_QUARTER" | "Please enter a value greater than zero" | "invalid" | "threshold"                   | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_Retail_MINUS1K_USD_QUARTER", "currency": {"isocode": "USD"}, "orgUnit": {"uid": "Rustic_Retail"}, "periodRange": "QUARTER", "threshold": -10000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdTimespanPermission", "name": "B2BOrderThresholdTimespanPermission"}}'
        "Rustic_Retail_USD_QUARTER"         | "This field is required."                | "missing" | "threshold"                   | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_Retail_USD_QUARTER", "orgUnit": {"uid": "Rustic_Retail"}, "periodRange": "QUARTER", "orderApprovalPermissionType" : {"code": "B2BOrderThresholdTimespanPermission", "name": "B2BOrderThresholdTimespanPermission"}}'
        "Rustic_Retail_10K_USD"             | "This field is required."                | "missing" | "periodRange"                 | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_Retail_10K_USD", "currency": {"isocode": "USD"}, "orgUnit": {"uid": "Rustic_Retail"}, "threshold": 10000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdTimespanPermission", "name": "B2BOrderThresholdTimespanPermission"}}'
        "Rustic_300K_ORDER"                 | "This field is required."                | "missing" | "orderApprovalPermissionType" | "parameter" | "ValidationError" | '{"active": true, "code": "Rustic_300K_ORDER", "orgUnit": {"uid": "Rustic"}, "threshold": 3000, "currency": {"isocode": "USD"}}'
    }

    def "B2B Customer tries to create a new order approval permission: #permissionCode"() {
        given: "a registered and logged in customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to create a new order approval permission"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + PERMISSIONS_PATH,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_UNAUTHORIZED
        }

        where:
        permissionCode         | postBody
        "Rustic_30K_USD_ORDER" | '{"active": true, "code": "Rustic_30K_USD_ORDER", "currency": {"isocode": "EUR"}, "orgUnit": {"uid": "Rustic"}, "threshold": 30000, "orderApprovalPermissionType" : {"code": "B2BOrderThresholdPermission", "name": "B2BOrderThresholdPermission"}}'
    }


    protected void comparePermissions(responseData, permissionCode, threshold, unitUid, currencyIso, periodRangeName, active) {
        assert responseData.code == permissionCode

        if (threshold == null) {
            assert responseData.threshold == null
        } else {
            assert responseData.threshold.compareTo(threshold) == 0
        }
        assert responseData.orgUnit.uid == unitUid

        if (currencyIso == null) {
            assert responseData.currency == null
        } else {
            assert StringUtils.equals(responseData.currency.isocode, currencyIso)
        }

        if (periodRangeName == null) {
            assert responseData.periodRange == null
        } else {
            assert StringUtils.equals(responseData.periodRange, periodRangeName)
        }

        assert responseData.active == active
    }

    protected getOrderApprovalPermission(RESTClient client, customer, orderApprovalPermissionCode) {
        HttpResponseDecorator response = client.get(
                path: getBasePathWithSite() + '/users/' + customer.id + PERMISSIONS_PATH + '/' + orderApprovalPermissionCode,
                query: ["fields": FIELD_SET_LEVEL_FULL],
                contentType: JSON,
                requestContentType: JSON)
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_OK
        }
        return response;
    }
}

