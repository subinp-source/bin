/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.interceptor


import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class MapTypeValidateInterceptorUnitTest extends Specification {
    private static final String ATTR_NAME = 'mapAttr'
    def interceptor = new MapTypeValidateInterceptor()

    @Unroll
    @Test
    def "#condition is allowed"() {
        given:
        def attr = attribute descriptor

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        condition                      | descriptor
        'primitive map'                | primitiveMap()
        'localized primitive map'      | localizedPrimitiveMap()
        'not map type'                 | Stub(AttributeDescriptorModel) { getAttributeType() >> Stub(ComposedTypeModel) }
        'attribute descriptor is null' | null
    }

    @Unroll
    @Test
    def "exception is thrown for #condition"() {
        given:
        def attr = attribute descriptor

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Map type attribute [$ATTR_NAME] must have AtomicTypeModel argumentType and returnType, " +
                "or must be localized with AtomicTypeModel returnType"

        where:
        condition                     | descriptor
        'non-primitive argumentType'  | composedArgumentType()
        'non-primitive returnType'    | composedReturnType()
    }

    private def attribute(AttributeDescriptorModel descriptor) {
        Stub(IntegrationObjectItemAttributeModel) {
            getAttributeName() >> ATTR_NAME
            getAttributeDescriptor() >> descriptor
        }
    }

    private def composedReturnType() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(MapTypeModel) {
                getArgumentType() >> Stub(AtomicTypeModel)
                getReturntype() >> Stub(ComposedTypeModel)
            }
        }
    }

    private def composedArgumentType() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(MapTypeModel) {
                getArgumentType() >> Stub(ComposedTypeModel)
                getReturntype() >> Stub(AtomicTypeModel)
            }
        }
    }

    private def primitiveMap() {
        Stub(AttributeDescriptorModel) {
            getAttributeType() >> Stub(MapTypeModel) {
                getArgumentType() >> Stub(AtomicTypeModel)
                getReturntype() >> Stub(AtomicTypeModel)
            }
        }
    }

    private def localizedPrimitiveMap() {
        Stub(AttributeDescriptorModel) {
            getLocalized() >> true
            getAttributeType() >> Stub(MapTypeModel) {
                getReturntype() >> Stub(AtomicTypeModel)
            }
        }
    }
}
