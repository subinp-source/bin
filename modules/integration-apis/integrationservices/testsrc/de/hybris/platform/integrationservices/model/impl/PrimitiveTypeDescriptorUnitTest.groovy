/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class PrimitiveTypeDescriptorUnitTest extends Specification {
    @Test
    def "creates primitive type descriptor"() {
        expect:
        PrimitiveTypeDescriptor.create("TestObject", atomicType())
    }

    @Test
    @Unroll
    def "create() throws exception when object code is #objCode and item model is #model"() {
        when:
        PrimitiveTypeDescriptor.create(objCode, model)

        then:
        thrown(IllegalArgumentException)

        where:
        objCode      | model
        'TestObject' | null
        null         | atomicType()
    }

    @Test
    def "reads back integration object code specified during creation"() {
        given:
        def descriptor = typeDescriptor("TestObject", "java.lang.Integer")

        expect:
        descriptor.integrationObjectCode == "TestObject"
    }

    @Test
    def "typeCode comes from the value in type system"() {
        given:
        def descriptor = typeDescriptor("java.lang.Integer")

        expect:
        descriptor.typeCode == "java.lang.Integer"
    }

    @Test
    def "itemCode is same as typeCode"() {
        given:
        def descriptor = typeDescriptor("java.lang.String")

        expect:
        descriptor.itemCode == descriptor.typeCode
    }

    @Test
    def "is always primitive"() {
        expect:
        typeDescriptor().primitive
    }

    @Test
    def "is never enumeration"() {
        expect:
        ! typeDescriptor().enumeration
    }

    @Test
    def "primitive descriptor is not abstract"() {
        expect:
        !typeDescriptor().isAbstract()
    }

    @Test
    def "does not have attributes"() {
        expect:
        typeDescriptor().attributes.empty
    }

    @Test
    def "getAttributes() does not leak mutability"() {
        given:
        def descriptor = typeDescriptor()

        when:
        descriptor.attributes.add Stub(TypeAttributeDescriptor)

        then:
        descriptor.attributes.empty
    }

    @Test
    def "getAttribute() is always empty"() {
        expect:
        ! typeDescriptor().getAttribute("someAttribute").present
    }

    @Test
    def "KeyDescriptor can be retrieved"() {
        expect:
        typeDescriptor().keyDescriptor != null
    }

    @Test
    @Unroll
    def "isInstance() is true when the object is instance of the type descriptor #condition"() {
        given:
        def descriptor = typeDescriptor("java.lang.Number")

        expect:
        descriptor.isInstance(obj)

        where:
        condition  | obj
        'type'     | Stub(Number)
        'sub-type' | new Integer(1)
    }

    @Test
    @Unroll
    def "isInstance() is false when the object is #condition"() {
        given:
        def descriptor = typeDescriptor("java.lang.Integer")

        expect:
        ! descriptor.isInstance(obj)

        where:
        condition                                | obj
        'null'                                   | null
        'not an instance of the type descriptor' | typeDescriptor("java.lang.Long")
    }

    @Test
    def "primitive descriptor is not the root"() {
        expect:
        !typeDescriptor().isRoot()
    }

    @Test
    def 'never has path to root item'() {
        given:
        def descriptor = typeDescriptor()

        expect:
        !descriptor.hasPathToRoot()
        descriptor.pathsToRoot.empty
    }

    @Test
    @Unroll
    def "not equals to other type descriptor if other type descriptor #condition"() {
        given:
        def sample = typeDescriptor("Integer")

        expect:
        sample != other

        where:
        condition                                    | other
        'is null'                                    | null
        'is not instance of PrimitiveTypeDescriptor' | Stub(TypeDescriptor)
        'is for a different primitive type'          | typeDescriptor("Float")
    }

    @Test
    def "equals to another primitive type descriptor for the same primitive type"() {
        given:
        def sample = typeDescriptor("Integer")

        expect:
        sample == typeDescriptor("Integer")
    }

    @Test
    @Unroll
    def "hashCode is different if other descriptor #condition"() {
        given:
        def sample = typeDescriptor("Date")

        expect:
        sample.hashCode() != other.hashCode()

        where:
        condition                                    | other
        'is for a different type'                    | typeDescriptor('String')
        'is not instance of PrimitiveTypeDescriptor' | Stub(TypeDescriptor)
    }

    @Test
    def "hashCode() is same when other primitive type descriptor is for the same type"() {
        given:
        def sample = typeDescriptor("Date")
        expect:
        sample.hashCode() == typeDescriptor("Date").hashCode()
    }

    private PrimitiveTypeDescriptor typeDescriptor() {
        return typeDescriptor("", atomicType())
    }

    private PrimitiveTypeDescriptor typeDescriptor(final String type) {
        return typeDescriptor("", type)
    }

    private PrimitiveTypeDescriptor typeDescriptor(final String objCode, final String type) {
        return typeDescriptor(objCode, atomicType(type))
    }

    private static PrimitiveTypeDescriptor typeDescriptor(final String objCode, final AtomicTypeModel model) {
        return new PrimitiveTypeDescriptor(objCode, model)
    }

    private AtomicTypeModel atomicType(String name = null) {
        Stub(AtomicTypeModel) {
            getCode() >> name
            getJavaClass() >> { name != null ? Class.forName(name) : null }
        }
    }
}