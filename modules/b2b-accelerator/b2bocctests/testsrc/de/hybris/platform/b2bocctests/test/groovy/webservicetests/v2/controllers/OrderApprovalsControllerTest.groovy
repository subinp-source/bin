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
import spock.lang.Unroll

import static groovyx.net.http.ContentType.JSON
import static org.apache.http.HttpStatus.*

@ManualTest
@Unroll
class OrderApprovalsControllerTest extends AbstractUserTest {
    static final String B2BADMIN_USERNAME = "linda.wolf@rustic-hw.com"
    static final String B2BADMIN_PASSWORD = "1234"

    static final String B2BAPPROVER_USERNAME = "hanna.schmidt.approval@rustic-hw.com"
    static final String B2BAPPROVER_PASSWORD = "1234"

    static final String B2BAPPROVER_2_USERNAME = "hanna.schmidt.2.approval@rustic-hw.com"
    static final String B2BAPPROVER_2_PASSWORD = "1234"

    static final B2BCUSTOMER_USERNAME = "mark.rivers@rustic-hw.com"
    static final String B2BCUSTOMER_PASSWORD = "1234"

    static final PAGE_SIZE = 20

    static final APPROVALS_PATH = "/orderapprovals"

    def "#userRole gets order approvals"() {
        given: "a registered and logged in customer with B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to get order approvals"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets order approvals"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_OK
            data.orderApprovals.size() >= 0
            data.sorts.size() > 0
            data.pagination.pageSize == PAGE_SIZE
            data.pagination.currentPage == 0
            data.pagination.totalResults.toInteger() >= 0
        }
        where:
        userRole       | id                   | password
        "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD
    }

    def "B2B Customer tries to get order approvals"() {
        given: "a registered and logged in B2B customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to get order approvals"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + APPROVALS_PATH,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets unauthorized error"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) println(data)
            status == SC_UNAUTHORIZED
        }
    }

    def "#userRole gets a specific order approval: #orderApprovalCode"() {
        given: "a registered and logged in customer with a valid role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to get a specific order approval"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH + "/" + orderApprovalCode,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets a specific order approval"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_OK

            data.code == orderApprovalCode
            data.approvalDecisionRequired == approvalDecisionRequired
            data.customerOrderApprovalRecords.find {
                it.approver.uid == customerOrderApprovalRecordApproverUid && it.permissionTypes.find {
                    it.code == permissionTypeCode
                }
            }
            data.order.code == orderCode
            data.order.purchaseOrderNumber == orderPurchaseOrderNumber
            data.order.costCenter.code == orderCostCenterCode
            data.order.permissionResults.find {
                it.approverName = approverName && it.permissionType.code == permissionTypeCode
            }
            if (replenishmentOrder) {
                data.trigger != null
            } else {
                data.trigger == null
            }
        }

        where:
        userRole       | id                     | password               | orderApprovalCode                  | approvalDecisionRequired | customerOrderApprovalRecordApproverUid | permissionTypeCode            | orderCode    | orderPurchaseOrderNumber | orderOrgCustomerUid                         | orderCostCenterCode  | approverName    | replenishmentOrder
        "B2B Approver" | B2BAPPROVER_USERNAME   | B2BAPPROVER_PASSWORD   | "approval_process_workflow_code"   | true                     | "admin"                                | "B2BBudgetExceededPermission" | "testOrder1" | "P.O. number"            | "mark.rivers.approval@rustic-retail-hw.com" | "Rustic_Cost_Center" | "Hanna Schmidt" | true
        "B2B Approver" | B2BAPPROVER_2_USERNAME | B2BAPPROVER_2_PASSWORD | "approval_process_workflow_code_2" | true                     | "admin"                                | "B2BBudgetExceededPermission" | "testOrder2" | "P.O. number"            | "mark.rivers.approval@rustic-retail-hw.com" | "Rustic_Cost_Center" | "Hanna Schmidt" | false
    }

    def "#userRole tries to get a non-existing order approval"() {
        given: "a registered and logged in customer with a valid role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to get a non-existing order approval"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH + "/" + orderApprovalCode,
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
        userRole       | id                   | password             | orderApprovalCode
        "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "Non_Existing_Approval"
    }

    def "#userRole tries to get an order approval that is not assigned to him/her"() {
        given: "a registered and logged in customer with a valid role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to get a non-existing order approval"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH + "/" + orderApprovalCode,
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
        userRole       | id                   | password             | orderApprovalCode
        "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "approval_process_workflow_code_2"
        "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "non_valid_approval_code"
    }

    def "B2B Customer tries to get a specific order approvals"() {
        given: "a registered and logged in customer without B2B Admin role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to get a specific order approval"
        HttpResponseDecorator response = restClient.get(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + APPROVALS_PATH + "/" + orderApprovalCode,
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
        orderApprovalCode << ["approval_process_workflow_code"]
    }

    def "B2B Customer tries to make an order approval decision: #orderApprovalCode"() {
        given: "a registered and logged in customer without a valid role"
        authorizeTrustedClient(restClient)
        def b2bCustomer = [
                'id'      : B2BCUSTOMER_USERNAME,
                'password': B2BCUSTOMER_PASSWORD
        ]
        authorizeCustomer(restClient, b2bCustomer)

        when: "he requests to make an order approval decision"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bCustomer.id + APPROVALS_PATH + "/" + orderApprovalCode + "/decision",
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
        orderApprovalCode                | postBody
        "approval_process_workflow_code" | '{ "decision": "APPROVE", "comment": "Approved"}'
    }

    def "#userRole makes an order approval decision with non-valid attributes: #descriptor"() {
        given: "a registered and logged in customer with a valid role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to make an order approval decision with non-valid attributes"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH + "/" + orderApprovalCode + "/decision",
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets bad request response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == responseStatus
            data.errors.find { it.message == message }
        }

        where:
        descriptor                                                           | userRole       | id                   | password             | orderApprovalCode                  | message                                 | responseStatus | postBody
        "Empty Comment While Rejected"                                       | "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "approval_process_workflow_code"   | "Please add comments for your decision" | SC_BAD_REQUEST | '{ "decision": "REJECT", "comment": ""}'
        "Not valid decision"                                                 | "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "approval_process_workflow_code"   | "Illegal argument error."               | SC_BAD_REQUEST | '{ "decision": "NON_VALID_DECISION", "comment": "abc"}'
        "Order approval code form a process that's not assigned to approver" | "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "approval_process_workflow_code_2" | "Requested resource cannot be found."   | SC_NOT_FOUND   | '{ "decision": "REJECT", "comment": "abc"}'
        "Not existing order approval code"                                   | "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "non_valid_approval_code"          | "Requested resource cannot be found."   | SC_NOT_FOUND   | '{ "decision": "REJECT", "comment": "abc"}'
    }


    def "#userRole makes an order approval decision: #orderApprovalCode"() {
        given: "a registered and logged in customer with a valid role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to make an order approval decision"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH + "/" + orderApprovalCode + "/decision",
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        def updatedInfo = getOrderApproval(restClient, b2bUser, orderApprovalCode)

        then: "he gets the order approval decision he made"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_OK
            data.decision == decision
        }
        and: "a decision for the approval is no longer necessary"
        with(updatedInfo) {
            with(updatedInfo) {
                if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                    println(data)
                }
                data.approvalDecisionRequired == false
            }
        }


        where:
        userRole         | id                     | password               | orderApprovalCode                  | decision  | postBody
        "B2B Approver"   | B2BAPPROVER_USERNAME   | B2BAPPROVER_PASSWORD   | "approval_process_workflow_code"   | "APPROVE" | '{ "decision": "APPROVE", "comment": "Approved"}'
        "B2B Approver 2" | B2BAPPROVER_2_USERNAME | B2BAPPROVER_2_PASSWORD | "approval_process_workflow_code_2" | "REJECT"  | '{ "decision": "REJECT", "comment": "Rejected"}'
    }

    def "#userRole makes an order approval decision for an already approved order: #orderApprovalCode"() {
        given: "a registered and logged in customer with a valid role"
        authorizeTrustedClient(restClient)
        def b2bUser = [
                'id'      : id,
                'password': password
        ]
        authorizeCustomer(restClient, b2bUser)

        when: "he requests to make an order approval decision for an already approved order"
        HttpResponseDecorator response = restClient.post(
                path: getBasePathWithSite() + '/users/' + b2bUser.id + APPROVALS_PATH + "/" + orderApprovalCode + "/decision",
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "he gets bad request response"
        with(response) {
            if (isNotEmpty(data) && isNotEmpty(data.errors)) {
                println(data)
            }
            status == SC_BAD_REQUEST
            data.errors.find { it.message == message }
        }

        where:
        userRole       | id                   | password             | orderApprovalCode                | message                                                            | postBody
        "B2B Approver" | B2BAPPROVER_USERNAME | B2BAPPROVER_PASSWORD | "approval_process_workflow_code" | "An error occurred during the execution of the approval workflow." | '{ "decision": "APPROVE", "comment": "Approved"}'
    }

    protected getOrderApproval(RESTClient client, customer, orderApprovalCode) {
        HttpResponseDecorator response = client.get(
                path: getBasePathWithSite() + '/users/' + customer.id + APPROVALS_PATH + '/' + orderApprovalCode,
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

