/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.odata.asserts.ODataResponseAssertion
import de.hybris.platform.odata2services.odata.integrationkey.IntegrationKeyToODataEntryGenerator
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.InvalidEntryDataException
import de.hybris.platform.odata2services.odata.persistence.PersistenceService
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.StorageRequestFactory
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidPropertyValueException
import de.hybris.platform.odata2services.odata.processor.ODataPayloadProcessingException
import de.hybris.platform.odata2services.odata.processor.PersistenceErrorRuntimeException
import org.apache.olingo.odata2.api.commons.HttpHeaders
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmEntityType
import org.apache.olingo.odata2.api.edm.EdmProperty
import org.apache.olingo.odata2.api.edm.provider.Facets
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.processor.ODataContext
import org.apache.olingo.odata2.api.uri.KeyPredicate
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.api.uri.UriInfo
import org.apache.olingo.odata2.core.edm.EdmString
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PatchPersistenceHandlerUnitTest extends Specification {

    private static final String KEY_PROPERTY_NAME = 'code'
    private static final String KEY_PROPERTY_VALUE = 'patch persistence handler key'
    private static final String PROPERTY_NAME = 'name'
    private static final String PROPERTY_VALUE = 'patch persistence handler name'

    @Shared
    def integrationKeyEntry = Stub(ODataEntry) {
        getProperties() >> [(KEY_PROPERTY_NAME): KEY_PROPERTY_VALUE]
    }

    def storageRequestFactory = Stub StorageRequestFactory
    def persistenceService = Stub PersistenceService
    def integrationKeyToODataEntryGenerator = Stub(IntegrationKeyToODataEntryGenerator) {
        generate(_ as EdmEntitySet, _ as List<KeyPredicate>) >> integrationKeyEntry
    }
    def handler = new PatchPersistenceHandler(
            storageRequestFactory: storageRequestFactory,
            persistenceService: persistenceService,
            integrationKeyToODataEntryGenerator: integrationKeyToODataEntryGenerator)

    @Test
    def "request payload persisted"() {
        given: "set up the parameters"
        def param = persistenceParameter()

        and:
        persistenceService.createEntityData(_ as StorageRequest) >> Stub(ODataEntry) {
            getProperties() >> [(KEY_PROPERTY_NAME): KEY_PROPERTY_VALUE, (PROPERTY_NAME): PROPERTY_VALUE]
        }

        and:
        storageRequestFactory.create(param, _ as ODataEntry) >> Stub(StorageRequest) {
            getAcceptLocale() >> Locale.ENGLISH
        }

        when:
        def response = handler.handle param

        then:
        response.getHeader(HttpHeaders.CONTENT_LANGUAGE) == Locale.ENGLISH.getLanguage()
        response.status == HttpStatusCodes.OK
        ODataResponseAssertion.assertionOf(response)
                .jsonBody()
                .hasPathWithValue("d.${KEY_PROPERTY_NAME}", KEY_PROPERTY_VALUE)
                .hasPathWithValue("d.${PROPERTY_NAME}", PROPERTY_VALUE)
    }

    @Test
    def 'payload key attributes are overridden with values from the integration key'() {
        given:
        def param = persistenceParameter()

        and:
        ODataEntry requestEntity = null
        handler.storageRequestFactory = Stub(StorageRequestFactory) {
            create(param, _ as ODataEntry) >> { args ->
                requestEntity = args[1] as ODataEntry
                Stub(StorageRequest) {
                    getAcceptLocale() >> Locale.ENGLISH
                }
            }
        }

        when:
        handler.handle param

        then:
        requestEntity.properties == [(KEY_PROPERTY_NAME): KEY_PROPERTY_VALUE, (PROPERTY_NAME): PROPERTY_VALUE]
    }

    @Test
    def "fail to read the payload"() {
        given: "payload is null"
        def param = DefaultPersistenceParam.persistenceParam()
                .withUriInfo(uriInfo())
                .build()

        when:
        handler.handle param

        then:
        def e = thrown ODataPayloadProcessingException
        e.httpStatus == HttpStatusCodes.BAD_REQUEST
        e.code == 'odata_error'

    }

    @Test
    @Unroll
    def "fail to persist payload with #exception"() {
        given:
        def param = persistenceParameter()

        and:
        persistenceService.createEntityData(_ as StorageRequest) >> { throw exception }

        when:
        handler.handle param

        then:
        thrown clazz

        where:
        exception                                                                               | clazz
        new InvalidAttributeValueException('Test Invalid Value', Stub(TypeAttributeDescriptor)) | InvalidPropertyValueException
        new InternalProcessingException('IGNORE - testing exception')                           | InvalidEntryDataException
        new ODataPayloadProcessingException(Stub(Throwable))                                    | ODataPayloadProcessingException
        new RuntimeException('IGNORE - testing exception')                                      | PersistenceErrorRuntimeException
    }

    @Test
    def 'IntegrationAttributeException is not handled but is propagated to the caller'() {
        given:
        def param = persistenceParameter()

        and:
        def exception = Stub IntegrationAttributeException
        persistenceService.createEntityData(_ as StorageRequest) >> { throw exception }

        when:
        handler.handle param

        then:
        def e = thrown IntegrationAttributeException
        e.is exception
    }

    @Test
    def 'validates payload and the parameters when validator is injected'() {
        given: 'PatchPersistenceHandler has a validator injected'
        handler.requestValidator = Mock RequestValidator

        and: 'a persistence parameter'
        def param = persistenceParameter()

        and: 'storage request is created'
        storageRequestFactory.create(param, _ as ODataEntry) >> Stub(StorageRequest) {
            getAcceptLocale() >> Locale.ENGLISH
        }

        when:
        def response = handler.handle param

        then: 'the validator was invoked'
        1 * handler.requestValidator.validate(param, integrationKeyEntry, ! null)
        and: 'a non-null response received'
        response
    }

    @Test
    def 'item is not persisted if the request validation fails'() {
        given: 'PatchPersistenceHandler has dependencies injected'
        handler.requestValidator = Stub RequestValidator
        handler.persistenceService = Mock PersistenceService
        handler.storageRequestFactory = Mock StorageRequestFactory

        and: 'the validation fails for the request parameters'
        def param = persistenceParameter()
        handler.requestValidator.validate(param, integrationKeyEntry, _ as ODataEntry) >> { throw new RuntimeException() }

        when:
        handler.handle param

        then:
        0 * handler.storageRequestFactory._
        0 * handler.persistenceService._
        thrown RuntimeException
    }

    private static InputStream payload() {
        def json = JsonBuilder.json().withField(PROPERTY_NAME, PROPERTY_VALUE).build()
        new ByteArrayInputStream(json.getBytes())
    }

    private DefaultPersistenceParam persistenceParameter() {
        DefaultPersistenceParam.persistenceParam()
                .withContent(payload())
                .withContext(context())
                .withUriInfo(uriInfo())
                .withRequestContentType('application/json')
                .withResponseContentType('application/json')
                .build()
    }

    private ODataContext context() {
        Stub(ODataContext) {
            getHeader(HttpHeaders.CONTENT_LANGUAGE) >> 'en'
            getPathInfo() >> Stub(PathInfo) {
                getServiceRoot() >> URI.create('https://url/to/service/root')
            }
        }
    }

    private EdmEntityType entityType() {
        Stub(EdmEntityType) {
            getName() >> 'Product'
            getPropertyNames() >> [KEY_PROPERTY_NAME, PROPERTY_NAME]
            getProperty(KEY_PROPERTY_NAME) >> Stub(EdmProperty) {
                getName() >> KEY_PROPERTY_NAME
                getType() >> EdmString.getInstance()
                getFacets() >> new Facets().setMaxLength(KEY_PROPERTY_VALUE.length())
            }
            getProperty(PROPERTY_NAME) >> Stub(EdmProperty) {
                getName() >> PROPERTY_NAME
                getType() >> EdmString.getInstance()
                getFacets() >> new Facets().setMaxLength(PROPERTY_VALUE.length())
            }
        }
    }

    private EdmEntitySet entitySet() {
        Stub(EdmEntitySet) {
            getEntityType() >> entityType()
            getName() >> 'Products'
        }
    }

    private UriInfo uriInfo() {
        def doesNotMatterKeyPredicates = []
        Stub(UriInfo) {
            getStartEntitySet() >> entitySet()
            getKeyPredicates() >> doesNotMatterKeyPredicates
        }
    }
}
