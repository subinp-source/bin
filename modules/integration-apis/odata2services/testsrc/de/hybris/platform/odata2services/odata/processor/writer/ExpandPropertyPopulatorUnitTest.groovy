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

package de.hybris.platform.odata2services.odata.processor.writer

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.security.TypePermissionService
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult
import org.apache.olingo.odata2.api.edm.EdmEntitySet
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties
import org.apache.olingo.odata2.api.uri.NavigationPropertySegment
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class ExpandPropertyPopulatorUnitTest extends Specification {
    def typePermissionService = Mock(TypePermissionService)
    def expandPopulator = new ExpandPropertyPopulator(typePermissionService: typePermissionService)
    def requestTypeDescriptor = Stub(TypeDescriptor)
    def request = Stub(ItemLookupRequest) {
        getTypeDescriptor() >> requestTypeDescriptor
    }

    @Test
    @Unroll
    def "is applicable when expand property is #expand in request"() {
        given:
        request.expand >> expand

        expect:
        expandPopulator.isApplicable(request) == expected

        where:
        expand                              | expected
        null                                | false
        []                                  | false
        [Stub(NavigationPropertySegment)]   | true
        [[Stub(NavigationPropertySegment)]] | true
    }

    @Test
    def "expand unit property when typePermissionService is null"() {
        given:
        def unitNavProperty = navigationProperty("unit", "Units")
        request.expand >> [[unitNavProperty]]

        when:
        new ExpandPropertyPopulator().populate(Stub(EntityProviderWriteProperties), request, Stub(ItemLookupResult)).build()

        then:
        noExceptionThrown()
    }

    @Test
    def "expand with special localizedAttributes property does not validate"() {
        given:
        def unitNavProperty = navigationProperty("localizedAttributes", "LocalizedAttributes")
        request.expand >> [[unitNavProperty]]

        when:
        def properties = expandPopulator.populate(Stub(EntityProviderWriteProperties), request, Stub(ItemLookupResult)).build()

        then:
        properties.expandSelectTree.links.containsKey("localizedAttributes")
        properties.expandSelectTree.links.get("localizedAttributes").isAll()
        !properties.callbacks.isEmpty()
        properties.callbacks.containsKey("localizedAttributes")
        0 * typePermissionService.checkReadPermission(_ as TypeDescriptor)
    }

    @Test
    def "expand unit property"() {
        given:
        def unitNavProperty = navigationProperty("unit", "Units")
        requestTypeDescriptor.getAttribute("unit") >> Optional.of(Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> "Unit"
            }
        })
        request.expand >> [[unitNavProperty]]

        when:
        def properties = expandPopulator.populate(Stub(EntityProviderWriteProperties), request, Stub(ItemLookupResult)).build()

        then:
        properties.expandSelectTree.links.containsKey("unit")
        properties.expandSelectTree.links.get("unit").isAll()
        !properties.callbacks.isEmpty()
        properties.callbacks.containsKey("unit")
        1 * typePermissionService.checkReadPermission(_ as TypeDescriptor)
    }

    @Test
    def "expand nested property"() {
        given:
        def catalogNavProperty = navigationProperty("catalog", "Catalogs")
        def catalogVersionNavProperty = navigationProperty("catalogVersion", "CatalogVersions")
        requestTypeDescriptor.getAttribute("catalogVersion") >> Optional.of(Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> "CatalogVersion"
                getAttribute("catalog") >> Optional.of(Stub(TypeAttributeDescriptor) {
                    getAttributeType() >> Stub(TypeDescriptor) {
                        getTypeCode() >> "Catalog"
                    }
                })
            }
        })
        request.expand >> [[catalogVersionNavProperty, catalogNavProperty]]

        when:
        def properties = expandPopulator.populate(Stub(EntityProviderWriteProperties), request, Stub(ItemLookupResult)).build()

        then:
        properties.expandSelectTree.links.containsKey("catalogVersion")
        properties.expandSelectTree.links.get("catalogVersion").links.containsKey("catalog")

        properties.expandSelectTree.links.get("catalogVersion").isAll()
        properties.expandSelectTree.links.get("catalogVersion").links.get("catalog").isAll()

        properties.callbacks.containsKey("catalogVersion")
        properties.callbacks.size() == 1

        and: "read permissions are checked for the catalog and catalogVersion attribute item types"
        2 * typePermissionService.checkReadPermission(_ as TypeDescriptor)
    }

    @Test
    def "expand unit and catalogVersion properties"() {
        given:
        def unitNavProperty = navigationProperty("unit", "Units")
        def catalogVersionNavProperty = navigationProperty("catalogVersion", "CatalogVersions")
        requestTypeDescriptor.getAttribute("unit") >> Optional.of(Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> "Unit"
            }
        })
        requestTypeDescriptor.getAttribute("catalogVersion") >> Optional.of(Stub(TypeAttributeDescriptor) {
            getAttributeType() >> Stub(TypeDescriptor) {
                getTypeCode() >> "CatalogVersion"
            }
        })
        request.expand >> [[unitNavProperty], [catalogVersionNavProperty]]

        when:
        def properties = expandPopulator.populate(Stub(EntityProviderWriteProperties), request, Stub(ItemLookupResult)).build()

        then:
        properties.expandSelectTree.links.containsKey("unit")
        properties.expandSelectTree.links.containsKey("catalogVersion")

        properties.expandSelectTree.links.get("unit").isAll()
        properties.expandSelectTree.links.get("catalogVersion").isAll()

        properties.callbacks.containsKey("unit")
        properties.callbacks.containsKey("catalogVersion")

        and: "read permissions are checked for the unit and catalogVersion attribute item types"
        2 * typePermissionService.checkReadPermission(_ as TypeDescriptor)
    }

    def navigationProperty(final String name, final String entitySet) {
        Stub(NavigationPropertySegment) {
            getNavigationProperty() >>
                    Stub(EdmNavigationProperty) { getName() >> name }
            getTargetEntitySet() >>
                    Stub(EdmEntitySet) { getName() >> entitySet }
        }
    }
}
