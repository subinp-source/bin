/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocctests.test.groovy.webservicetests.v2.controllers

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.util.Config
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.users.AbstractUserTest
import groovyx.net.http.HttpResponseDecorator
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class B2BOrdersControllerReplenishmentPostTest extends AbstractUserTest {

    static final String B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
    static final String B2BCUSTOMER_PASSWORD = "1234"

    static final String OCC_OVERLAPPING_PATHS_FLAG = "occ.rewrite.overlapping.paths.enabled"
    static final ENABLED_CONTROLLER_PATH = Config.getBoolean(OCC_OVERLAPPING_PATHS_FLAG, false) ? COMPATIBLE_CONTROLLER_PATH : CONTROLLER_PATH
    static final String CONTROLLER_PATH = "/users/"
    static final String COMPATIBLE_CONTROLLER_PATH = "/orgUsers/"

    static final ORDER_REPLENISHMENT_PATH = "/replenishmentOrders"

    def setup() {
        authorizeTrustedClient(restClient)
    }

    def "B2B Customer creates a new replenishment order"() {
        given: "a registered and logged in customer"
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to create a new order"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + ENABLED_CONTROLLER_PATH + b2bCustomer.id + ORDER_REPLENISHMENT_PATH,
                body: postBody,
                query: [
                        'cartId'      : cartId,
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
            status == SC_CREATED
        }

        where:
        cartId | postBody
        "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1", "numberOfWeeks": "2","numberOfDays": "3", "recurrencePeriod": "WEEKLY", "daysOfWeek": ["MONDAY", "SATURDAY"] }'

    }

    def "B2B Customer tries to create a new order with non-valid attributes: #descriptor"() {
        given: "a registered and logged in customer with B2B Admin role"
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to create a new order with non-valid attributes"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + ENABLED_CONTROLLER_PATH + b2bCustomer.id + ORDER_REPLENISHMENT_PATH,
                body: postBody,
                query: [
                        'cartId'      : cartId,
                        'termsChecked': true,
                        'fields'      : FIELD_SET_LEVEL_FULL
                ],
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
        descriptor                | cartId | postBody
        "NumberOfWeeksMissing"    | "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1","numberOfDays": "3", "recurrencePeriod": "WEEKLY", "daysOfWeek": ["MONDAY", "SATURDAY"] }'
        "DaysOfWeekWrongFormat"   | "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1", "numberOfWeeks": "2","numberOfDays": "3", "recurrencePeriod": "WEEKLY", "daysOfWeek": ["MO", "SATURDAY"] }'
        "DaysOfWeekMissing"       | "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1", "numberOfWeeks": "2","numberOfDays": "3", "recurrencePeriod": "WEEKLY" }'
        "NumberOfDaysMissing"     | "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1", "numberOfWeeks": "2", "recurrencePeriod": "DAILY", "daysOfWeek": ["MONDAY", "SATURDAY"] }'
        "NthDayOfMonthMissing"    | "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "numberOfWeeks": "2","numberOfDays": "3", "recurrencePeriod": "MONTHLY", "daysOfWeek": ["MONDAY", "SATURDAY"] }'
        "StartDateMissing"        | "abc"  | '{"nthDayOfMonth": "1", "numberOfWeeks": "2","numberOfDays": "3", "recurrencePeriod": "WEEKLY", "daysOfWeek": ["MONDAY", "SATURDAY"] }'
        "RecurrencePeriodMissing" | "abc"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1", "numberOfWeeks": "2","numberOfDays": "3", "daysOfWeek": ["MONDAY", "SATURDAY"] }'

    }

    def "B2B Customer creates a new replenishment order with payment type CARD"() {
        given: "a registered and logged in customer"
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to create a new replenishment order"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + ENABLED_CONTROLLER_PATH + b2bCustomer.id + ORDER_REPLENISHMENT_PATH,
                body: postBody,
                query: [
                        'cartId'      : cartId,
                        'termsChecked': true,
                        'fields'      : FIELD_SET_LEVEL_FULL
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets the newly created replenishment order"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_CREATED
        }

        where:
        cartId | postBody
        "qwe"  | '{"replenishmentStartDate": "2020-12-31T09:00:00+0000", "nthDayOfMonth": "1", "numberOfWeeks": "2", "numberOfDays": "3", "recurrencePeriod": "WEEKLY", "daysOfWeek": ["MONDAY", "SATURDAY"] }'

    }
}
