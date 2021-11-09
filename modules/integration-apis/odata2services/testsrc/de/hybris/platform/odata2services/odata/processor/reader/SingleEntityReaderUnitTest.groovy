/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.odata2services.odata.processor.reader

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.exception.TypeAttributeDescriptorNotFoundException
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException
import de.hybris.platform.integrationservices.security.TypePermissionService
import de.hybris.platform.odata2services.odata.persistence.ConversionOptions
import de.hybris.platform.odata2services.odata.persistence.InternalProcessingException
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.PersistenceService
import de.hybris.platform.odata2services.odata.processor.ExpandedEntity
import de.hybris.platform.odata2services.odata.processor.NavigationSegmentExplorer
import de.hybris.platform.odata2services.odata.processor.writer.ResponseWriter
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmException
import org.apache.olingo.odata2.api.edm.EdmMultiplicity
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.processor.ODataResponse
import org.apache.olingo.odata2.api.uri.KeyPredicate
import org.apache.olingo.odata2.api.uri.NavigationSegment
import org.apache.olingo.odata2.api.uri.UriInfo
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class SingleEntityReaderUnitTest extends Specification {
    def typePermissionService = Mock(TypePermissionService)
    def persistenceService = Mock(PersistenceService)
    def navigationSegmentExplorer = Mock(NavigationSegmentExplorer)
    def responseWriter = Mock(ResponseWriter)

    def entityReader = new SingleEntityReader()

    def setup() {
        entityReader.setTypePermissionService(typePermissionService)
        entityReader.setNavigationSegmentExplorer(navigationSegmentExplorer)
        entityReader.setPersistenceService(persistenceService)
        entityReader.setResponseWriter(responseWriter)
    }

    @Test
    def "isApplicable is true when key predicate exists and no navigation segments"() {
        def uriInfo = Stub(UriInfo) {
            getKeyPredicates() >> [Stub(KeyPredicate)]
            getNavigationSegments() >> []
        }

        expect:
        entityReader.isApplicable(uriInfo)
    }

    @Test
    @Unroll
    def "isApplicable is #isApplicable when key predicate is #keyPredicates, navigation segment's multiplicity is #multiplicity"() {
        def navigationProperty = Stub(EdmNavigationProperty) {
            getMultiplicity() >> multiplicity
        }
        def navigationSegment = Stub(NavigationSegment) {
            getNavigationProperty() >> navigationProperty
        }
        def uriInfo = Stub(UriInfo) {
            getKeyPredicates() >> keyPredicates
            getNavigationSegments() >> [navigationSegment]
        }

        expect:
        isApplicable == entityReader.isApplicable(uriInfo)

        where:
        keyPredicates        | multiplicity                | isApplicable
        [Stub(KeyPredicate)] | EdmMultiplicity.ZERO_TO_ONE | true
        [Stub(KeyPredicate)] | EdmMultiplicity.ONE         | true
        [Stub(KeyPredicate)] | EdmMultiplicity.MANY        | false
        []                   | EdmMultiplicity.ONE         | false
    }

    @Test
    @Unroll
    def "isApplicable throws IllegalArgumentException when key predicate is #keyPredicates, and navigation segment is #navigationSegments "() {
        def uriInfo = Stub(UriInfo) {
            getKeyPredicates() >> keyPredicates
            getNavigationSegments() >> navigationSegments
        }

        when:
        entityReader.isApplicable(uriInfo)

        then:
        thrown(IllegalArgumentException)

        where:
        keyPredicates        | navigationSegments
        null                 | [Stub(NavigationSegment)]
        [Stub(KeyPredicate)] | null
    }

    @Test
    def "read successfully"() {
        def oDataResponse = Stub(ODataResponse)
        def navPropertyName = 'product'
        def navPropertyType = 'Product'
        def itemLookupRequest = Stub(ItemLookupRequest) {
            getNavigationSegments() >> [navigationSegment(navPropertyName)]
            getContentType() >> "application/json"
            getExpand() >> Collections.emptyList()
            getTypeDescriptor() >> typeDescriptor(navPropertyName, navPropertyType)
        }
        def properties = Stub(Map)
        def oDataEntry = Stub(ODataEntry) {
            getProperties() >> properties
        }
        def entitySet = Stub(EdmEntitySet)
        def expandedEntity = Stub(ExpandedEntity) {
            getODataEntry() >> oDataEntry
            getEdmEntitySet() >> entitySet
        }

        when:
        def actualODataResponse = entityReader.read(itemLookupRequest)

        then:
        oDataResponse == actualODataResponse
        1 * persistenceService.getEntityData(itemLookupRequest, _ as ConversionOptions) >> oDataEntry
        1 * navigationSegmentExplorer.expandForSingleEntity(itemLookupRequest, oDataEntry) >> expandedEntity
        1 * responseWriter.write(itemLookupRequest, entitySet, properties) >> oDataResponse
        1 * typePermissionService.checkReadPermission(_ as TypeDescriptor)
    }

    @Test
    def "read successfully when typePermissionService is null"() {
        given: "typePermissionService is null"
        def itemLookupRequest = Stub(ItemLookupRequest) {
            getNavigationSegments() >> [Stub(NavigationSegment)]
            getContentType() >> "application/json"
            getExpand() >> Collections.emptyList()
        }
        def properties = Stub(Map)
        def oDataEntry = Stub(ODataEntry) {
            getProperties() >> properties
        }
        def entitySet = Stub(EdmEntitySet)
        def expandedEntity = Stub(ExpandedEntity) {
            getODataEntry() >> oDataEntry
            getEdmEntitySet() >> entitySet
        }
        persistenceService.getEntityData(itemLookupRequest, _ as ConversionOptions) >> oDataEntry
        navigationSegmentExplorer.expandForSingleEntity(itemLookupRequest, oDataEntry) >> expandedEntity
        entityReader.setTypePermissionService(null)

        when:
        entityReader.read(itemLookupRequest)

        then:
        noExceptionThrown()
    }

    @Test
    def "read throws an InternalProcessingException when getType() throws EdmException"() {
        given:
        def itemLookupRequest = Stub(ItemLookupRequest) {
            getNavigationSegments() >> [Stub(NavigationSegment) {
                getNavigationProperty() >> Stub(EdmNavigationProperty) {
                    getName() >> { throw new EdmException(EdmException.COMMON) }
                }
            }]
            getTypeDescriptor() >> Stub(TypeDescriptor)
        }

        when:
        entityReader.read(itemLookupRequest)

        then:
        thrown(InternalProcessingException)
    }

    @Test
    def "read re-throws permissionValidator TypeAccessPermissionException"() {
        given:
        def navPropertyName = 'product'
        def navPropertyType = 'Product'
        def itemLookupRequest = Stub(ItemLookupRequest) {
            getNavigationSegments() >> [navigationSegment(navPropertyName)]
            getContentType() >> "application/json"
            getExpand() >> Collections.emptyList()
            getTypeDescriptor() >> typeDescriptor(navPropertyName, navPropertyType)
        }
        typePermissionService.checkReadPermission(_ as TypeDescriptor) >> {throw new TypeAccessPermissionException(navPropertyType, 'read')}

        when:
        entityReader.read(itemLookupRequest)

        then:
        thrown(TypeAccessPermissionException)
    }

    @Test
    def 'read throws TypeAttributeDescriptorNotFoundException when TypeAttributeDescriptor is not found for navigation segment'() {
        given:
        def navPropertyName = 'product'
        def itemLookupRequest = Stub(ItemLookupRequest) {
            getNavigationSegments() >> [navigationSegment(navPropertyName)]
            getTypeDescriptor() >> Stub(TypeDescriptor) {
                getAttribute(navPropertyName) >> Optional.empty()
            }
        }

        when:
        entityReader.read(itemLookupRequest)

        then:
        thrown(TypeAttributeDescriptorNotFoundException)
    }

    private NavigationSegment navigationSegment(navPropertyName) {
        Stub(NavigationSegment) {
            getNavigationProperty() >> Stub(EdmNavigationProperty) {
                getName() >> navPropertyName
            }
        }
    }

    private TypeDescriptor typeDescriptor(def navPropertyName, def navPropertyType) {
        def typeAttrDescriptor = Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> navPropertyType
            }
        }
        Stub(TypeDescriptor) {
            getAttribute(navPropertyName) >> Optional.of(typeAttrDescriptor)
        }
    }
}
