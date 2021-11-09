/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.odata2services.odata.InvalidServiceNameException
import de.hybris.platform.odata2services.odata.ODataRequestEntityExtractor
import de.hybris.platform.odata2services.odata.ODataWebException
import org.apache.olingo.odata2.api.ODataService
import org.apache.olingo.odata2.api.ODataServiceFactory
import org.apache.olingo.odata2.api.exception.ODataException
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.processor.ODataRequest
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.core.PathInfoImpl
import org.assertj.core.util.Maps
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.SAP_PASSPORT_HEADER_NAME
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.ODATA_REQUEST
import static de.hybris.platform.odata2services.constants.Odata2servicesConstants.USER
import static org.apache.olingo.odata2.api.commons.HttpHeaders.CONTENT_LANGUAGE

@UnitTest
class DefaultODataContextGeneratorUnitTest extends Specification {

	private static final String PRODUCT = 'Product'
	private static final String SERVICE_ROOT_PREFIX = '/odata2webservices/'
	private static final String SERVICE_NAME = 'InboundProduct'
	private static final String SERVICE = 'service'
	private static final String ENTITY_TYPE = 'entityType'

	def entityExtractor = Stub(ODataRequestEntityExtractor)
	def serviceFactory = Mock(ODataServiceFactory)
	def contextGenerator = new DefaultODataContextGenerator()

	def setup() {
		contextGenerator.setEntityExtractors([entityExtractor])
		contextGenerator.setServiceFactory serviceFactory
	}

	@Test
	@Unroll
	def "generate throws exception when #condition"() {
		given:
		def request = createODataRequest pathInfo

		when:
		contextGenerator.generate request

		then:
		thrown exception

		where:
		condition				| pathInfo					| exception
		'pathInfo is null'		| null						| IllegalArgumentException
		'service root is empty'	| createPathInfoImp('','')	| IllegalArgumentException
		'service name is empty'	| createPathInfoImp('')		| InvalidServiceNameException
	}

	@Test
	def "service name is a parameter in the context"() {
		given:
		def request = createODataRequest createPathInfoImp()

		when:
		def context = contextGenerator.generate request

		then:
		SERVICE_NAME == context.getParameter(SERVICE)
	}

	@Test
	@Unroll
	def "entity type in the context is #expectedValue when isApplicable is #applicable"() {
		given:
		def request = createODataRequest createPathInfoImp()
		entityExtractor.isApplicable(_ as ODataRequest) >> applicable
		entityExtractor.extract(_ as ODataRequest) >> PRODUCT

		when:
		def context = contextGenerator.generate request

		then:
		expectedValue == context.getParameter(ENTITY_TYPE)

		where:
		applicable	| expectedValue
		true		| PRODUCT
		false		| ""
	}

	@Test
	def "oData service is set in the context"() {
		given:
		def request = createODataRequest createPathInfoImp()
		serviceFactory.createService(_ as ODataContext) >> Stub(ODataService)

		when:
		def context = contextGenerator.generate request

		then:
		context.service != null
	}

	@Test
	def "creating oData service throws exception"() {
		given:
		def request = createODataRequest createPathInfoImp()
		serviceFactory.createService(_ as ODataContext) >> { throw new ODataException("Testing throw exception while creating odata service") }

		when:
		contextGenerator.generate request

		then:
		thrown ODataWebException
	}

	@Test
	@Unroll
	def "get request header #headerKey from context"() {
		given:
		def request = createODataRequest createPathInfoImp()
		request.getRequestHeaders().put(headerKey, headerValue)

		when:
		def context = contextGenerator.generate request

		then:
		headerValue == context.getRequestHeaders().get(headerKey)

		where:
		headerKey 					| headerValue
		CONTENT_LANGUAGE 			| [[Locale.ENGLISH.getLanguage()], [Locale.ENGLISH.getLanguage(), Locale.JAPANESE.getLanguage()], []]
		USER						| ['Joe']
		SAP_PASSPORT_HEADER_NAME 	| ['my-passort']
	}

	@Test
	def "request is a parameter in the context"() {
		given:
		def request = createODataRequest createPathInfoImp()

		when:
		def context = contextGenerator.generate request

		then:
		request == context.getParameter(ODATA_REQUEST)
	}

	ODataRequest createODataRequest(PathInfo pathInfoImp)
	{
		ODataRequest.newBuilder()
			.httpMethod("GET")
			.queryParameters(Maps.newHashMap(PRODUCT, null))
			.pathInfo(pathInfoImp)
			.acceptableLanguages(new ArrayList<>())
			.build()
	}

	PathInfo createPathInfoImp(final String serviceRoot, final String serviceName) {
		def pathInfo = new PathInfoImpl()
		pathInfo.setServiceRoot(URI.create(serviceRoot + serviceName))
		pathInfo
	}

	PathInfo createPathInfoImp(final String serviceName) {
		return createPathInfoImp(SERVICE_ROOT_PREFIX, serviceName)
	}

	PathInfo createPathInfoImp() {
		return createPathInfoImp(SERVICE_NAME)
	}
}


