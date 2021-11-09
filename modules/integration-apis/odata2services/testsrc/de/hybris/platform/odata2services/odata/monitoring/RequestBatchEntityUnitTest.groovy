/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.odata2services.odata.monitoring

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.commons.io.IOUtils
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

@UnitTest
class RequestBatchEntityUnitTest extends Specification {

	@Test
	@Unroll
	def "Construct RequestBatchEntity with parameters that are #condition"() {
		given:
		def entity = requestBatchEntity actual

		expect:
		with(entity) {
			def isContentEqual = { IOUtils.contentEquals content, expected['body'] as InputStream }
			isContentEqual()
			messageId == expected['msgId']
			integrationObjectType == expected['type']
			integrationKey == expected['key']
			numberOfChangeSets == expected['numChanges']
		}

		where:
		condition 	| actual 																		| expected
		'not null' 	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:2, key:'key'] 	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:2, key:'key']
		'null'		| [msgId:null, body:null, type:null, numChanges:1, key:null]					| [msgId:"", body:null, type:"", numChanges:1, key:""]
	}

	@Test
	def "exception is thrown when number of change sets is less than 1"() {
		when:
		new RequestBatchEntity('Message', body('Body'), 'type', 0, 'key')

		then:
		thrown IllegalArgumentException
	}

	@Test
	@Unroll
	def "RequestBatchEntity equals() == #result when #condition"() {
		given:
		def entity1 = requestBatchEntity actual1
		def entity2 = requestBatchEntity actual2

		expect:
		(entity1 == entity2) == result

		where:
		condition					| actual1 																		| actual2																		| result
		"equal"						| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| true
		"msgId not equal"			| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message2', body:body('Body'), type:'Type', numChanges:1, key:'key']	| false
		"body not equal"			| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body2'), type:'Type', numChanges:1, key:'key']	| false
		"type not equal"			| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type2', numChanges:1, key:'key']	| false
		"num change sets not equal"	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:2, key:'key']	| false
		"key not equal"				| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key2']	| false
	}

	@Test
	@Unroll
	def "RequestBatchEntity hashcode() == #result when #condition"() {
		given:
		def entity1 = requestBatchEntity actual1
		def entity2 = requestBatchEntity actual2

		expect:
		(entity1.hashCode() == entity2.hashCode()) == result

		where:
		condition					| actual1 																		| actual2																		| result
		"equal"						| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| true
		"msgId not equal"			| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message2', body:body('Body'), type:'Type', numChanges:1, key:'key']	| false
		"body not equal"			| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body2'), type:'Type', numChanges:1, key:'key']	| false
		"type not equal"			| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type2', numChanges:1, key:'key']	| false
		"num change sets not equal"	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:2, key:'key']	| false
		"key not equal"				| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key']	| [msgId:'Message', body:body('Body'), type:'Type', numChanges:1, key:'key2']	| false
	}

	def body(String content) {
		IOUtils.toInputStream content, StandardCharsets.UTF_8
	}

	def requestBatchEntity(Map params) {
		new RequestBatchEntity(
				params['msgId'] as String,
				params['body'] as InputStream,
				params['type'] as String,
				params['numChanges'] as Integer,
				params['key'] as String
		)
	}
}
