/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.odata2services.odata.monitoring

import de.hybris.bootstrap.annotations.UnitTest
import org.apache.commons.io.IOUtils
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.api.uri.PathSegment
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

import static de.hybris.platform.odata2services.odata.monitoring.RequestBatchEntityBuilder.*

@UnitTest
class RequestBatchEntityBuilderUnitTest extends Specification {

	@Test
	@Unroll
	def "Build RequestBatchEntity with #condition"() {
		when:
		def entity = requestBatchEntity()
						.withBatchContent(actual['content'] as InputStream)
						.withIntegrationKey(actual['key'] as String)
						.withIntegrationObjectType(actual['type'] as String)
						.withMessageId(actual['msgId'] as String)
						.withNumberOfChangeSets(actual['numChangeSets'] as Integer)
						.build()

		then:
		with(entity) {
			def isContentEqual = { IOUtils.contentEquals content, expected['content'] as InputStream }
			isContentEqual()
			integrationObjectType == expected['type']
			integrationKey == expected['key']
			messageId == expected['msgId']
			numberOfChangeSets == expected['numChangeSets']
		}

		where:
		condition 				| actual 																		| expected
		"null parameters"		| [content:null, type:null, key:null, msgId:null, numChangeSets:1]				| [content:null, type:"", key:"", msgId:"", numChangeSets:1]
		"non-null parameters"	| [content:body('body'), type:'type', key:'key', msgId:'id', numChangeSets:2]	| [content:body('body'), type:'type', key:'key', msgId:'id', numChangeSets:2]
	}

	@Test
	@Unroll
	def "Build RequestBatchEntity with ODataContext when #condition"() {
		given:
		def context = Stub(ODataContext) {
			getRequestHeader('integrationMessageId') >> msgId
			getParameter('service') >> type
			getPathInfo() >> pathInfo
		}

		when:
		def entity = requestBatchEntity().withContext(context).build()

		then:
		with(entity) {
			integrationKey == expected['key']
			integrationObjectType == expected['type']
			messageId == expected['msgId']
			content == null
			numberOfChangeSets == 1
		}

		where:
		condition 								| msgId 	| type 		| pathInfo											| expected
		'message id not in header' 				| null 		| 'type' 	| pathInfoWithKey("'my|key'")						| [msgId:'', type:'type', key:'my|key' ]
		'service type not in header' 			| 'msgId' 	| null 		| pathInfoWithKey("'my|key'")						| [msgId:'msgId', type:'', key:'my|key' ]
		'pathInfo is null' 						| 'msgId' 	| 'type' 	| null												| [msgId:'msgId', type:'type', key:'' ]
		'oData segment list is null' 			| 'msgId' 	| 'type' 	| pathInfoODataSegmentsListIs(null)					| [msgId:'msgId', type:'type', key:'' ]
		'oData segment list is empty' 			| 'msgId' 	| 'type' 	| pathInfoODataSegmentsListIs([])					| [msgId:'msgId', type:'type', key:'' ]
		'path segment has no key' 				| 'msgId' 	| 'type' 	| pathInfoODataSegmentsListIs([pathSegmentNoKey()])	| [msgId:'msgId', type:'type', key:'' ]
		'key with single quotes is URL encoded' | 'msgId' 	| 'type' 	| pathInfoWithKey('%27my%7Ckey%27')					| [msgId:'msgId', type:'type', key:'my|key' ]
		'key with double quotes is URL encoded' | 'msgId' 	| 'type' 	| pathInfoWithKey('%22my%7Ckey%22')					| [msgId:'msgId', type:'type', key:'my|key' ]
		'key with symbols is URL encoded' 		| 'msgId' 	| 'type' 	| pathInfoWithKey('%2Bmy%7Ckey%2D')					| [msgId:'msgId', type:'type', key:'+my|key-' ]
	}

	@Test
	def "Build RequestBatchEntity with ODataContext when getting pathInfo throws exception"() {
		given:
		def context = Stub(ODataContext) {
			getPathInfo() >> { throw new ODataException() }
		}

		when:
		def entity = requestBatchEntity().withContext(context).build()

		then:
		entity.integrationKey == ''
	}

	def body(String content) {
		IOUtils.toInputStream content, StandardCharsets.UTF_8
	}

	def pathInfoWithKey(String key) {
		pathInfoODataSegmentsListIs([pathSegment(key)])
	}

	def pathInfoODataSegmentsListIs(List oDataSegments) {
		Stub(PathInfo) {
			getODataSegments() >> oDataSegments
		}
	}

	def pathSegment(String key) {
		Stub(PathSegment) {
			getPath() >> "Products($key)"
		}
	}

	def pathSegmentNoKey() {
		Stub(PathSegment) {
			getPath() >> "Products"
		}
	}
}
