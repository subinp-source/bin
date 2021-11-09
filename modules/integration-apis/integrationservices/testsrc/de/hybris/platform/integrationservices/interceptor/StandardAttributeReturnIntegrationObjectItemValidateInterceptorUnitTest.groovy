/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.core.model.type.RelationDescriptorModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class StandardAttributeReturnIntegrationObjectItemValidateInterceptorUnitTest extends Specification {

    private static final String CODE = "MatchingTypeCode"
    private static final String SUB_TYPE_CODE = "SubTypeCode"
    private static final String SUPER_TYPE_CODE = "SuperTypeCode"
    def interceptor = new StandardAttributeReturnIntegrationObjectItemValidateInterceptor()

    @Test
    @Unroll
    def "no exception thrown when #property does not need a returnIntegrationObjectItem"() {
        given:
        def attribute = attribute descriptor, null

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        property                         | descriptor
        'primitive attribute'            | primitiveDescriptor()
        'primitive collection attribute' | primitiveCollectionDescriptor()
        'map attribute'                  | mapDescriptor()
        'enum attribute'                 | enumDescriptor()
    }

    @Test
    @Unroll
    def "no exception thrown when #typeDescription attribute provides returnIntegrationObjectItem of #msg"() {
        given:
        def attribute = attribute descriptor, defaultItem()

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        typeDescription      | msg                        | descriptor
        'relational'         | 'same type as descriptor'  | relationalDescriptor()
        'relational'         | 'super type of descriptor' | relationalDescriptor(SUB_TYPE_CODE)
        'relational'         | 'sub type of descriptor'   | relationalDescriptor(SUPER_TYPE_CODE)
        'composed'           | 'same type as descriptor'  | composedDescriptor()
        'composed'           | 'super type as descriptor' | composedDescriptor(SUB_TYPE_CODE)
        'composed'           | 'sub type as descriptor'   | composedDescriptor(SUPER_TYPE_CODE)
        'complex collection' | 'same type as descriptor'  | complexCollectionDescriptor()
        'complex collection' | 'super type as descriptor' | complexCollectionDescriptor(SUB_TYPE_CODE)
        'complex collection' | 'sub type as descriptor'   | complexCollectionDescriptor(SUPER_TYPE_CODE)
    }

    @Test
    @Unroll
    def "exception is thrown when #attrType attribute does not provide returnIntegrationObjectItem"() {
        given:
        def attrName = 'ReferencedAttrName'
        def returnIntObjItem = null
        def attribute = attribute descriptor, returnIntObjItem, attrName

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Missing returnIntegrationObjectItem for referenced type attribute [$attrName]."

        where:
        attrType             | descriptor
        'relational'         | relationalDescriptor()
        'composed'           | composedDescriptor()
        'complex collection' | complexCollectionDescriptor()
    }

    @Test
    @Unroll
    def "exception is thrown when #attrType provides the wrong returnIntegrationObjectItem"() {
        given:
        def attrName = 'ReferencedAttrName'
        def returnIntObjItem = defaultItem()
        def attribute = attribute descriptor, returnIntObjItem, attrName

        when:
        interceptor.onValidate attribute, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Invalid returnIntegrationObjectItem for referenced type attribute [$attrName]."

        where:
        attrType             | descriptor
        'relational'         | relationalDescriptor('diff' + CODE)
        'composed'           | composedDescriptor('diff' + CODE)
        'complex collection' | complexCollectionDescriptor('diff' + CODE)
    }

    def attribute(def descriptor, def returnIntObjItem = null, def attrName = 'attrName') {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> descriptor
            getReturnIntegrationObjectItem() >> returnIntObjItem
            getAttributeName() >> attrName
        }
    }

    def defaultItem() {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> CODE
                getAllSubTypes() >> [Stub(ComposedTypeModel) {
                    getCode() >> SUB_TYPE_CODE
                }]
                getAllSuperTypes() >> [Stub(ComposedTypeModel) {
                    getCode() >> SUPER_TYPE_CODE
                }]
            }
        }
    }

    def primitiveDescriptor() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(AtomicTypeModel)
        }
    }

    private def mapDescriptor() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(MapTypeModel)
        }
    }

    private def enumDescriptor() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(EnumerationMetaTypeModel)
        }
    }

    def primitiveCollectionDescriptor() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(CollectionTypeModel) {
                getElementType() >> Stub(AtomicTypeModel)
            }
        }
    }

    def relationalDescriptor(final String code = CODE) {
        Stub(RelationDescriptorModel) {
            getAttributeType() >> Stub(ComposedTypeModel) {
                getCode() >> code
            }
        }
    }

    def composedDescriptor(final String code = CODE) {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(ComposedTypeModel) {
                getCode() >> code
            }
        }
    }

    def complexCollectionDescriptor(final String code = CODE) {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(CollectionTypeModel) {
                getElementType() >> Stub(ComposedTypeModel) {
                    getCode() >> code
                }
            }
        }
    }
}
