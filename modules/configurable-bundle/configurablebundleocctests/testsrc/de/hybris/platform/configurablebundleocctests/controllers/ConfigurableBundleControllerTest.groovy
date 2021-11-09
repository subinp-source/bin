/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleocctests.controllers

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.XML
import static org.apache.commons.lang3.ArrayUtils.isEmpty
import static org.apache.http.HttpStatus.SC_BAD_REQUEST
import static org.apache.http.HttpStatus.SC_CREATED
import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.carts.AbstractCartTest
import spock.lang.Unroll


@ManualTest
@Unroll
class ConfigurableBundleControllerTest extends AbstractCartTest{

    protected static final String PRODUCT_EOS_40D_BODY_VALUE = "" + PRODUCT_EOS_40D_BODY
    protected static final String PRODUCT_SONY_NPFH50_BATTERY_VALUE = "805693"
    protected static final String PRODUCT_ICIDU_SD_CARD_2GB_VALUE = "872912a"
    protected static final String PRODUCT_KINGSTON_SD_CARD_32GB_VALUE = "1641905"

    def "Should fail start a bundle with product PRODUCT_EOS_40D_BODY and unsupported contentType"() {
        given: "anonymous user with cart"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)


        when: "a bundle is started with different content-type"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: "<a>WHATEVER</a>",
                contentType: JSON,
                requestContentType: XML)

        then: "An error is returned"
        with(response) {
            status == SC_UNSUPPORTED_MEDIA_TYPE
            data.errors[0].type == "HttpMediaTypeNotSupportedError"
            data.errors[0].message == "Content type 'application/xml' not supported"
        }
    }

    def "Should start a bundle with valid product in cart"() {
        given: "anonymous user with cart"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "a bundle is started with a valid product"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
            path: path,
            body: [
                    "templateId": "PhotoOTGCameraComponent",
                    "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                    "quantity": 1
            ],
            contentType: JSON,
            requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
        }
    }

    def "Validation should fail for missing required attribute #missingAttribute"() {
        given: "anonymous user with cart"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "a bundle is started with missing required attribute"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "A Validation Error is returned"
        with(response) {
            status == SC_BAD_REQUEST

            data.errors.size() == 1
            data.errors[0].type == "ValidationError"
            data.errors[0].message.startsWith(missingAttribute)
        }

        where:
        missingAttribute    | postBody
        "templateId"        | "{\"productCode\": \""+PRODUCT_EOS_40D_BODY_VALUE+"\", \"quantity\": \"1\"}"
        "productCode"       | "{\"templateId\": \"PhotoOTGCameraComponent\", \"quantity\": \"1\"}"
        "quantity"          | "{\"templateId\": \"PhotoOTGCameraComponent\", \"productCode\": \""+PRODUCT_EOS_40D_BODY_VALUE+"\"}"
    }

    def "should fail for invalid attribute #invalidAttribute"() {
        given: "anonymous user with cart"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "a bundle is started with an invalid attribute"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: postBody,
                contentType: JSON,
                requestContentType: JSON)

        then: "An Error is returned"
        with(response) {
            status == SC_BAD_REQUEST

            data.errors.size() == 1
            data.errors[0].type == errorType
            data.errors[0].message.startsWith(errorMessage);
        }

        where:
        invalidAttribute    | errorType                 | errorMessage                      | postBody
        "templateId"        | "ModelNotFoundError"      | "No result for the given query"   | "{\"templateId\": \"XXXPhotoOTGCameraComponent\",\"productCode\": \""+PRODUCT_EOS_40D_BODY_VALUE+"\", \"quantity\": \"1\"}"
        "productCode"       | "UnknownIdentifierError"  | "Product with code "              | "{\"templateId\": \"PhotoOTGCameraComponent\", \"productCode\": \"XXX\", \"quantity\": \"1\"}"
        "quantity"          | "ValidationError"         | "quantity: "                      | "{\"templateId\": \"PhotoOTGCameraComponent\", \"productCode\": \""+PRODUCT_EOS_40D_BODY_VALUE+"\", \"quantity\": \"-1\"}"

    }

    def "Should success to get allowed products for bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)


        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
        }

        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        String path2 = getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid
        def responseCart = restClient.get(
                path: path2,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "An entryGroupNumber is returned"
        with(responseCart) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entryGroups[0].entryGroups[1].entryGroupNumber == 3
        }

        when: "A list of allowed products is requested"
        String path3 = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/entrygroups/3/allowedProductsSearch"
        def responseAllowedProduct = restClient.get(
                path: path3,
                contentType: JSON,
                query: [
                        'sort': 'topRated'
                ],
                requestContentType: JSON)
        then: "A paginated list of Allowed products sorted by topRated is returned"
        with(responseAllowedProduct) {
            status == SC_OK
            data.pagination.currentPage == 0
            data.pagination.pageSize == 20
            data.pagination.sort == "topRated"
            data.pagination.totalResults == 1
            data.products[0].code == PRODUCT_SONY_NPFH50_BATTERY_VALUE
        }
    }

    def "Should success to add product to a bundle from getAllowedProducts"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)


        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
        }

        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        String path2 = getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid
        def responseCart = restClient.get(
                path: path2,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "An entryGroupNumber is returned"
        with(responseCart) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entryGroups[0].entryGroups[1].entryGroupNumber == 3
        }

        when: "A list of allowed products is requested for Camera"
        String path3 = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/entrygroups/3/allowedProductsSearch"
        def responseAllowedProduct = restClient.get(
                path: path3,
                contentType: JSON,
                query: [
                        'sort': 'topRated'
                ],
                requestContentType: JSON)
        then: "A paginated list of Allowed products sorted by topRated is returned"
        with(responseAllowedProduct) {
            status == SC_OK
            data.pagination.currentPage == 0
            data.pagination.pageSize == 20
            data.pagination.sort == "topRated"
            data.pagination.totalResults == 1
            data.products[0].code == PRODUCT_SONY_NPFH50_BATTERY_VALUE
        }

        when: "A product from getAllowedProductsSearch is added to an entry group"
        String path4 = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/entrygroups/3"
        def response4 = restClient.post(
                path: path4,
                body: [
                        "product": [ "code" : PRODUCT_SONY_NPFH50_BATTERY_VALUE ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)
        then: "The product is added successfully"
        with(response4) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_SONY_NPFH50_BATTERY_VALUE
            data.entry.quantity == 1
        }
    }

    def "Should not return any allowed products for a root bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)


        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
        }

        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        String path2 = getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid
        def responseCart = restClient.get(
                path: path2,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "An entryGroupNumber is returned"
        with(responseCart) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
        }

        when: "A list of allowed products for root bundle is requested"
        String path3 = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/entrygroups/1/allowedProductsSearch"
        def responseAllowedProduct = restClient.get(
                path: path3,
                contentType: JSON,
                requestContentType: JSON)
        then: "An empty list is returned"
        with(responseAllowedProduct) {
            status == SC_OK
            data.pagination.currentPage == 0
            data.pagination.pageSize == 20
            data.pagination.sort == "relevance"
            data.pagination.totalResults == 0
        }
    }

    def "Should fail to get allowed products for non-existing entryGroup"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
        }

        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        String path2 = getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid
        def responseCart = restClient.get(
                path: path2,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "An entryGroupNumber is returned"
        with(responseCart) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
        }

        when: "A list of allowed products for a non-existing entryGroup is requested"
        String path3 = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/entrygroups/999/allowedProductsSearch"
        def responseAllowedProduct = restClient.get(
                path: path3,
                contentType: JSON,
                requestContentType: JSON)
        then: "An Error is returned"
        with(responseAllowedProduct) {
            status == SC_BAD_REQUEST
            data.errors[0].type == "IllegalArgumentError"
            data.errors[0].message.startsWith("No group with number '999' in the order with code ")
        }
    }

    def "Should success to get multiple pages of allowed products for a specific bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)


        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE

        }

        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        String path2 = getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid
        def responseCart = restClient.get(
                path: path2,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "An entryGroupNumber is returned"
        with(responseCart) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entryGroups[0].entryGroups[2].entryGroupNumber == 4
        }

        when: "A query is provided for a specific bundle to get multiple pages of allowed products"
        String path3 = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/entrygroups/4/allowedProductsSearch"
        def responseAllowedProduct = restClient.get(
                path: path3,
                contentType: JSON,
                query: [
                        'pageSize': 1,
                        'currentPage':1,
                ],
                requestContentType: JSON)
        then: "Multiple pages of allowed products for entryGroupNumber=4 are returned"
        with(responseAllowedProduct) {
            status == SC_OK
            data.pagination.currentPage == 1
            data.pagination.pageSize == 1
            data.pagination.sort == "relevance"
            data.pagination.totalResults == 2
            data.pagination.totalPages == 2
        }
    }

    def "Should succeed when trying to add a product to a bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add product to a bundle"
        def response2 = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/2',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_EOS_40D_BODY_VALUE ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response2) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 2
        }
    }

    def "Should succeed when trying to add a product to a bundle not specifying quantity"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add product to a bundle"
        def response2 = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/2',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_EOS_40D_BODY_VALUE ]
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response2) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 2
        }
    }

    def "Should fail when trying to add wrong product to a bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add wrong product to a bundle"
        def response2 = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/1',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_POWER_SHOT_A480_2 ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A BadRequest response should be returned with error message"
        with(response2) {
            status == SC_BAD_REQUEST
            data.errors[0].type == "CommerceCartModificationError"
            data.errors[0].message == "Product '1934793' is not in the product list of component (bundle template) 'Photo On The Go Package'"
        }
    }

    def "Should fail when trying to start a non-existing bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started with wrong template"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "NonExistingTemplate",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A BadRequest response should be returned"
        with(response) {
            status == SC_BAD_REQUEST
            data.errors[0].type == "ModelNotFoundError"
            data.errors[0].message == "No result for the given query"
        }
    }

    def "Should fail when trying to add product to a non-existing bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add product to a non-existing bundle"
        def response2 = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/300',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_POWER_SHOT_A480_2 ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A BadRequest response should be returned"
        with(response2) {
            status == SC_BAD_REQUEST
            data.errors[0].type == "CommerceCartModificationError"
            data.errors[0].message.startsWith("No group with number '300' in the order with code ")
        }
    }

    def "Should succeed when trying to delete a root bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to delete a root bundle"
        def response2 = restClient.delete(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/1',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response2) {
            status == SC_OK
        }

        // we check that everything was removed
        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        response = restClient.get(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "The entryGroups field is returned"
        with(response) {
            status == SC_OK
            isEmpty(data.entryGroups)
        }
    }

    def "Should fail when trying to delete a non-root bundle"() {
        given: "An anonymous user"
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started"
        String path = getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles"
        def response = restClient.post(
                path: path,
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to delete a non-root bundle"
        def response2 = restClient.delete(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/3',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                contentType: JSON,
                requestContentType: JSON)

        then: "An error is returned"
        with(response2) {
            status == SC_BAD_REQUEST
            data.errors[0].type == "CommerceCartModificationError"
            data.errors[0].message.startsWith("Cannot remove non-root entry group with number '3' from the cart with code");
        }

        // we check that nothing was removed
        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        response = restClient.get(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "The entryGroups field is returned"
        with(response) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entryGroups[0].entryGroups[0].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[0].entries[0].entryNumber == 0
            data.entryGroups[0].entryGroups[0].entryGroupNumber == 2

            isEmpty(data.entryGroups[0].entryGroups[1].entries)  // Product not added
            data.entryGroups[0].entryGroups[1].entryGroupNumber == 3

            isEmpty(data.entryGroups[0].entryGroups[2].entries)  // Product not added
            data.entryGroups[0].entryGroups[2].entryGroupNumber == 4

            isEmpty(data.entryGroups[0].entryGroups[3].entries)  // root bundle for sd cards
            data.entryGroups[0].entryGroups[3].entryGroupNumber == 5

            isEmpty(data.entryGroups[0].entryGroups[3].entryGroups[0].entries)
            data.entryGroups[0].entryGroups[3].entryGroups[0].entryGroupNumber == 6

            isEmpty(data.entryGroups[0].entryGroups[3].entryGroups[1].entries)
            data.entryGroups[0].entryGroups[3].entryGroups[1].entryGroupNumber == 7
        }
    }

    def "Should succeed when trying to add a product to a sub-bundle and product and entrygroup removals should work"() {
        given:
        def user = ['id': 'anonymous']
        def cart = createAnonymousCart(restClient, JSON)

        when: "A bundle is started with a Camera and a bundle template"
        def response = restClient.post(
                path: getBasePathWithSite() + "/users/" + user.id + "/carts/" + cart.guid + "/bundles",
                body: [
                        "templateId": "PhotoOTGCameraComponent",
                        "productCode": PRODUCT_EOS_40D_BODY_VALUE,
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_CREATED
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add a battery to a bundle"
        response = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/3',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_SONY_NPFH50_BATTERY_VALUE ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_SONY_NPFH50_BATTERY_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add SD Card to a Card bundle"
        response = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/6',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_ICIDU_SD_CARD_2GB_VALUE ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_ICIDU_SD_CARD_2GB_VALUE
            data.entry.quantity == 1
        }

        when: "customer tries to add a second SD Card to a bundle"
        response = restClient.post(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/7',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_KINGSTON_SD_CARD_32GB_VALUE ],
                        "quantity": 1
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_KINGSTON_SD_CARD_32GB_VALUE
            data.entry.quantity == 1
        }

        when: "A Customer gets his cart by sending entryGroups(entries,DEFAULT) as requested fields"
        response = restClient.get(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "The entryGroups field is returned"
        with(response) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entryGroups[0].entryGroups[0].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[0].entries[0].entryNumber == 0
            data.entryGroups[0].entryGroups[0].entryGroupNumber == 2

            data.entryGroups[0].entryGroups[1].entries[0].product.code == PRODUCT_SONY_NPFH50_BATTERY_VALUE
            data.entryGroups[0].entryGroups[1].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[1].entries[0].entryNumber == 1
            data.entryGroups[0].entryGroups[1].entryGroupNumber == 3

            isEmpty(data.entryGroups[0].entryGroups[2].entries)  // Product not added
            data.entryGroups[0].entryGroups[2].entryGroupNumber == 4

            isEmpty(data.entryGroups[0].entryGroups[3].entries)  // root bundle for sd cards
            data.entryGroups[0].entryGroups[3].entryGroupNumber == 5

            data.entryGroups[0].entryGroups[3].entryGroups[0].entries[0].product.code == PRODUCT_ICIDU_SD_CARD_2GB_VALUE
            data.entryGroups[0].entryGroups[3].entryGroups[0].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[3].entryGroups[0].entries[0].entryNumber == 2
            data.entryGroups[0].entryGroups[3].entryGroups[0].entryGroupNumber == 6

            data.entryGroups[0].entryGroups[3].entryGroups[1].entries[0].product.code == PRODUCT_KINGSTON_SD_CARD_32GB_VALUE
            data.entryGroups[0].entryGroups[3].entryGroups[1].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[3].entryGroups[1].entries[0].entryNumber == 3
            data.entryGroups[0].entryGroups[3].entryGroups[1].entryGroupNumber == 7
        }

        // update quantity for camera
        when: "customer tries to update quantity for camera"
        response = restClient.patch(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entries/0',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                body: [
                        "product": [ "code" : PRODUCT_EOS_40D_BODY_VALUE ],
                        "quantity": 3
                ],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_OK
            data.statusCode == 'success'
            data.entry.product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entry.quantity == 3
        }

        // delete ICIDU SD CARD
        when: "customer tries to delete a product from within a bundle"
        response = restClient.delete(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entries/2',
                query: ['fields': FIELD_SET_LEVEL_FULL],
                contentType: JSON,
                requestContentType: JSON)

        then: "A successful response should be returned"
        with(response) {
            status == SC_OK
        }

        // get cart entries again
        when: "A Customer gets his cart again by sending entryGroups(entries,DEFAULT) as requested fields"
        response = restClient.get(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "The entryGroups field is returned and the deleted product is no longer present. Following entryNumbers are rearranged"
        with(response) {
            status == SC_OK
            data.entryGroups.size() == 1
            data.entryGroups[0].entryGroups[0].entries[0].product.code == PRODUCT_EOS_40D_BODY_VALUE
            data.entryGroups[0].entryGroups[0].entries[0].quantity == 3
            data.entryGroups[0].entryGroups[0].entries[0].entryNumber == 0
            data.entryGroups[0].entryGroups[0].entryGroupNumber == 2

            data.entryGroups[0].entryGroups[1].entries[0].product.code == PRODUCT_SONY_NPFH50_BATTERY_VALUE
            data.entryGroups[0].entryGroups[1].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[1].entries[0].entryNumber == 1
            data.entryGroups[0].entryGroups[1].entryGroupNumber == 3

            isEmpty(data.entryGroups[0].entryGroups[2].entries)  // Product not added
            data.entryGroups[0].entryGroups[2].entryGroupNumber == 4

            isEmpty(data.entryGroups[0].entryGroups[3].entries)  // root bundle for sd cards
            data.entryGroups[0].entryGroups[3].entryGroupNumber == 5

            isEmpty(data.entryGroups[0].entryGroups[3].entryGroups[0].entries)  // product was removed
            data.entryGroups[0].entryGroups[3].entryGroups[0].entryGroupNumber == 6

            data.entryGroups[0].entryGroups[3].entryGroups[1].entries[0].product.code == PRODUCT_KINGSTON_SD_CARD_32GB_VALUE
            data.entryGroups[0].entryGroups[3].entryGroups[1].entries[0].quantity == 1
            data.entryGroups[0].entryGroups[3].entryGroups[1].entries[0].entryNumber == 2  // entryNumber was re-assigned
            data.entryGroups[0].entryGroups[3].entryGroups[1].entryGroupNumber == 7
        }

        // delete the root-bundle
        when: "A Customer deletes a root bundle in his cart."
        response = restClient.delete(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid + '/entrygroups/1',
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "The response is successful"
        with(response) {
            status == SC_OK
        }

        // get cart entries again
        when: "A Customer gets his cart again by sending entryGroups(entries,DEFAULT) as requested fields"
        response = restClient.get(
                path: getBasePathWithSite() + '/users/' + user.id + '/carts/' + cart.guid,
                contentType: JSON,
                query: [
                        'fields': 'entryGroups(DEFAULT)'
                ],
                requestContentType: URLENC
        )
        then: "The empty entryGroups field is returned"
        with(response) {
            status == SC_OK
            isEmpty(data.entryGroups)
        }
    }
}
