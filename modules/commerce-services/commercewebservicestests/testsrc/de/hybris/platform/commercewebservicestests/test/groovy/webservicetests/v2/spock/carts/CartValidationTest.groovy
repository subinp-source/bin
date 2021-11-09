/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicestests.test.groovy.webservicetests.v2.spock.carts

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.basecommerce.enums.InStockStatus
import de.hybris.platform.commercewebservicestests.setup.TestSetupUtils
import groovyx.net.http.HttpResponseDecorator
import spock.lang.IgnoreIf
import spock.lang.Unroll

import static groovyx.net.http.ContentType.*
import static org.apache.http.HttpStatus.SC_OK

@Unroll
@ManualTest
class CartValidationTest extends AbstractCartTest {

	def "Anonymous user validates his empty cart successful: #format"() {
		given: "anonymous cart"
		authorizeClient(restClient)
		def anonymous = ['id': 'anonymous']
		def cart = createCart(restClient, anonymous, format)

		when: "is validated"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + anonymous.id + '/carts/' + cart.guid + '/validate',
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
		)

		then: "successful"
		with(response) {
			println(data)
			status == SC_OK
			isEmpty(data.cartModifications)
		}

		where:
		format << [XML, JSON]
	}

	def "Anonymous user validates his cart with entries successful: #format"() {
		given: "anonymous cart"
		authorizeClient(restClient)
		def anonymous = ['id': 'anonymous']
		def cart = createCart(restClient, anonymous, format)

		and: "with product added"
		addProductToCartOnline(restClient, anonymous, cart.guid, PRODUCT_REMOTE_CONTROL_TRIPOD, 1, format)

		when: "is validated"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + anonymous.id + '/carts/' + cart.guid + '/validate',
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
		)

		then: "successful"
		with(response) {
			println(data)
			status == SC_OK
			isEmpty(data.cartModifications)
		}

		where:
		format << [XML, JSON]
	}

	def "Anonymous user validates his cart, which fails due to out of stock: #format"() {
		given: "anonymous cart"
		authorizeClient(restClient)
		def anonymous = ['id': 'anonymous']
		def cart = createCart(restClient, anonymous, format)

		and: "with product added"
		addProductToCartOnline(restClient, anonymous, cart.guid, PRODUCT_REMOTE_CONTROL_TRIPOD, 1, format)

		and: "which is out of stock"
		setStockStatus(restClient, InStockStatus.FORCEOUTOFSTOCK, PRODUCT_REMOTE_CONTROL_TRIPOD)

		when: "is validated"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + anonymous.id + '/carts/' + cart.guid + '/validate',
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
		)

		then: "with no stock error"
		with(response) {
			println(data)
			status == SC_OK
			isNotEmpty(data.cartModifications)
			data.cartModifications.size() == 1
			data.cartModifications[0].statusCode == 'noStock'
			data.cartModifications[0].entry.product.code == String.valueOf(PRODUCT_REMOTE_CONTROL_TRIPOD)
		}

		cleanup: "restore stock status"
		setStockStatus(restClient, InStockStatus.NOTSPECIFIED, PRODUCT_REMOTE_CONTROL_TRIPOD)

		where:
		format << [XML, JSON]
	}

	@IgnoreIf({ TestSetupUtils.isNewPromotionEngineTurnedOn() })
	def "Anonymous user validates his cart, which fails due to rejected voucher: #format"() {
		given: "anonymous cart"
		authorizeClient(restClient)
		def anonymous = ['id': 'anonymous']
		def cart = createCart(restClient, anonymous, format)

		addProductToCartOnline(restClient, anonymous, cart.guid, PRODUCT_REMOTE_CONTROL_TRIPOD, 1, format)

		and: "with un-redeemable voucher code added"
		HttpResponseDecorator voucherResponse = restClient.post(
				path: getBasePathWithSite() + '/users/' + anonymous.id + '/carts/' + cart.guid + '/vouchers',
				query: ['voucherId': PROMOTION_VOUCHER_CODE],
				contentType: format,
				requestContentType: URLENC
		)
		with(voucherResponse) {
			println(data)
			status == SC_OK
		}

		when: "is validated"
		HttpResponseDecorator response = restClient.post(
				path: getBasePathWithSite() + '/users/' + anonymous.id + '/carts/' + cart.guid + '/validate',
				query: ["fields": FIELD_SET_LEVEL_FULL],
				contentType: format,
				requestContentType: URLENC
		)

		then: "voucher rejected error"
		with(response) {
			println(data)
			status == SC_OK
			isNotEmpty(data.cartModifications)
			data.cartModifications.size() == 1
			data.cartModifications[0].statusMessage == PROMOTION_VOUCHER_CODE
			data.cartModifications[0].statusCode == 'couponNotValid'
		}

		where:
		format << [XML, JSON]
	}
}
