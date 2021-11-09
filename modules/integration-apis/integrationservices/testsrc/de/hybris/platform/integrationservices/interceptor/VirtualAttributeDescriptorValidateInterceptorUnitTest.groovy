/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.core.model.type.TypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel
import de.hybris.platform.integrationservices.util.AtomicTypeModelRetriever
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class VirtualAttributeDescriptorValidateInterceptorUnitTest extends Specification {
    private static final String DESC_CODE = 'virtualAttrDescCode'
    private static final String TYPE_CODE = 'someInvalidTypeCode'
    private static final List SUPPORTED_PRIMITIVES = ["java.lang.Integer", "java.lang.Boolean", "java.lang.Byte",
            "java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.String", "java.lang.Character",
            "java.util.Date", "java.math.BigInteger", "java.math.BigDecimal"]
    private static final String LOGIC_LOCATION = 'model://someLogicLocation'

    def atomicTypeModelRetriever = Stub(AtomicTypeModelRetriever) {
        atomicTypeModelRetriever.get("java.lang.String") >> atomicType("java.lang.String")
    }

    def interceptor = new VirtualAttributeDescriptorValidateInterceptor(Stub(AtomicTypeModelRetriever))

    @Unroll
    @Test
    def "exception is thrown for a #condition type"() {
        given:
        def attr = attribute type

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "Invalid [$TYPE_CODE] type for virtual attribute descriptor with code [$DESC_CODE]. " +
                "Virtual attribute descriptor must have supported primitive type from the following list: $SUPPORTED_PRIMITIVES"

        where:
        condition             | type
        'composed type'       | composedType()
        'enum type'           | enumType()
        'map type'            | mapType()
        'invalid atomic type' | atomicType()
    }

    @Test
    @Unroll
    def "exception is thrown if logicLocation '#logicLocation' is incorrectly formatted"() {
        given:
        def attr = attribute(atomicType("java.util.Date"), logicLocation)

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "URI '[$logicLocation]' provided does not meet the pattern model://logicLocation"

        where:
        logicLocation << [null, '', 'file://someFile', 'model', 'model://', 'model:/', 'model/', 'model:// ']
    }

    @Test
    def "no exception is thrown if logicLocation is correctly formatted"() {
        given:
        def attr = attribute(atomicType("java.lang.Integer"), "model://correctFormat")

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        noExceptionThrown()
    }


    @Test
    def "no exception is thrown for supported primitive type #type"() {
        given:
        def attr = attribute atomicType(type)

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        type << ["java.lang.Integer", "java.lang.Boolean", "java.lang.Byte",
                 "java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.String", "java.lang.Character",
                 "java.util.Date", "java.math.BigInteger", "java.math.BigDecimal"]
    }

    @Test
    def "when type is not provided, type defaults to java.lang.String"() {
        given:
        def attr = attributeDefaultType null

        when:
        interceptor.onValidate attr, Stub(InterceptorContext)

        then:
        noExceptionThrown()
        attr.getType().getCode() == "java.lang.String"
    }

    def composedType() {
        Stub(ComposedTypeModel) {
            getCode() >> TYPE_CODE
        }
    }

    def enumType() {
        Stub(EnumerationMetaTypeModel) {
            getCode() >> TYPE_CODE
        }
    }

    def atomicType(String code = TYPE_CODE) {
        Stub(AtomicTypeModel) {
            getCode() >> code
        }
    }

    def mapType() {
        Stub(MapTypeModel) {
            getCode() >> TYPE_CODE
        }
    }

    def attribute(TypeModel type, String logicLocation = LOGIC_LOCATION) {
        Stub(IntegrationObjectVirtualAttributeDescriptorModel) {
            getCode() >> DESC_CODE
            getType() >> type
            getLogicLocation() >> logicLocation
        }
    }

    def attributeDefaultType(TypeModel type, String logicLocation = LOGIC_LOCATION) {
        Stub(IntegrationObjectVirtualAttributeDescriptorModel) {
            getCode() >> DESC_CODE
            getType() >>> [type, atomicType("java.lang.String")]
            getLogicLocation() >> logicLocation
        }
    }
}
