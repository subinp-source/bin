/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.handler.persistence

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.IntegrationAttributeException
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException
import de.hybris.platform.integrationservices.exception.LocaleNotSupportedException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidationException
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.integrationservices.util.JsonBuilder
import de.hybris.platform.odata2services.odata.asserts.ODataResponseAssertion
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.InvalidEntryDataException
import de.hybris.platform.odata2services.odata.persistence.PersistenceRuntimeApplicationException
import de.hybris.platform.odata2services.odata.persistence.PersistenceService
import de.hybris.platform.odata2services.odata.persistence.StorageRequest
import de.hybris.platform.odata2services.odata.persistence.StorageRequestFactory
import de.hybris.platform.odata2services.odata.persistence.exception.InvalidPropertyValueException
import de.hybris.platform.odata2services.odata.persistence.exception.ItemNotFoundException
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
import org.apache.olingo.odata2.api.uri.PathInfo
import org.apache.olingo.odata2.api.uri.UriInfo
import org.apache.olingo.odata2.core.edm.EdmString
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PersistenceHandlerUnitTest extends Specification {

    private static final String PROPERTY_NAME = 'code'
    private static final String PROPERTY_VALUE = 'persistence handler test'

    def persistenceService = Stub PersistenceService
    def storageRequestFactory = Stub StorageRequestFactory
    def handler = new PersistenceHandler(persistenceService: persistenceService, storageRequestFactory: storageRequestFactory)

    @Test
    def "request payload persisted"() {
        given: "set up the parameters"
        def param = persistenceParameter()

        and:
        persistenceService.createEntityData(_ as StorageRequest) >> Stub(ODataEntry) {
            getProperties() >> [(PROPERTY_NAME): PROPERTY_VALUE]
        }

        and:
        storageRequestFactory.create(param, _ as ODataEntry) >> Stub(StorageRequest) {
            getAcceptLocale() >> Locale.ENGLISH
        }

        when:
        def response = handler.handle param

        then:
        response.getHeader(HttpHeaders.CONTENT_LANGUAGE) == Locale.ENGLISH.getLanguage()
        ODataResponseAssertion.assertionOf(response)
                .jsonBody()
                .hasPathWithValue("d.${PROPERTY_NAME}", PROPERTY_VALUE)
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
        new ItemSearchRequestValidationException(Stub(ItemSearchRequest))                       | ItemSearchRequestValidationException
        new RuntimeException('IGNORE - testing exception')                                      | PersistenceErrorRuntimeException
        new RuntimeException('IGNORE - testing exception')                                      | PersistenceErrorRuntimeException
    }

    @Test
    @Unroll
    def "#exceptionName is re-thrown when fails to persist payload"() {
        given:
        def param = persistenceParameter()

        and:
        def exception = exceptionReceived
        persistenceService.createEntityData(_ as StorageRequest) >> { throw exception }

        when:
        handler.handle param

        then:
        def e = thrown exceptionClazz
        e == exception

        where:
        exceptionName                            | exceptionClazz                         | exceptionReceived
        'IntegrationAttributeException'          | IntegrationAttributeException          | Stub(IntegrationAttributeException)
        'PersistenceRuntimeApplicationException' | PersistenceRuntimeApplicationException | Stub(PersistenceRuntimeApplicationException)
        'ItemNotFoundException'                  | ItemNotFoundException                  | Stub(ItemNotFoundException)
        'ItemSearchRequestValidationException'   | ItemSearchRequestValidationException   | Stub(ItemSearchRequestValidationException)
        'TypeAccessPermissionException'          | TypeAccessPermissionException          | Stub(TypeAccessPermissionException)
    }

    @Test
    def "new LocaleNotSupportedException with item's IntegrationKey is thrown when payload persist fails"() {
        given:
        def integrationKey = "integration|key|string"
        def param = persistenceParameter()

        and:
        def originalException = new LocaleNotSupportedException(Locale.GERMAN)
        persistenceService.createEntityData(_ as StorageRequest) >> { throw  originalException}

        and:
        storageRequestFactory.create(param, _ as ODataEntry) >> Stub(StorageRequest) {
            getAcceptLocale() >> Locale.GERMAN
            getIntegrationKey() >> integrationKey
        }

        when:
        handler.handle param

        then:
        def newException = thrown LocaleNotSupportedException

        originalException != newException
        newException.language == originalException.language
        newException.integrationKey == integrationKey
    }

    private PersistenceParam persistenceParameter() {
        DefaultPersistenceParam.persistenceParam()
                .withContent(payload())
                .withContext(context())
                .withRequestContentType('application/json')
                .withResponseContentType('application/json')
                .withUriInfo(uriInfo())
                .build()
    }

    private static InputStream payload() {
        def json = JsonBuilder.json().withField(PROPERTY_NAME, PROPERTY_VALUE).build()
        new ByteArrayInputStream(json.getBytes())
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
            getPropertyNames() >> [PROPERTY_NAME]
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
        Stub(UriInfo) {
            getStartEntitySet() >> entitySet()
        }
    }
}
