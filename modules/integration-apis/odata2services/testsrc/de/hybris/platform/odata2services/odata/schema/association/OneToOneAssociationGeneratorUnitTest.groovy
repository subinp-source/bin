package de.hybris.platform.odata2services.odata.schema.association

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.core.model.type.AttributeDescriptorModel
import de.hybris.platform.core.model.type.CollectionTypeModel
import de.hybris.platform.core.model.type.MapTypeModel
import de.hybris.platform.core.model.type.TypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.ODataAssociation
import org.apache.commons.lang3.StringUtils
import org.apache.olingo.odata2.api.edm.EdmMultiplicity
import org.apache.olingo.odata2.api.edm.provider.Association
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.integrationservices.model.BaseMockItemAttributeModelBuilder.oneToOneRelationAttributeBuilder
import static de.hybris.platform.integrationservices.model.MockRelationAttributeDescriptorModelBuilder.relationAttribute
import static de.hybris.platform.integrationservices.model.MockRelationDescriptorModelBuilder.oneToOneRelation

@UnitTest
class OneToOneAssociationGeneratorUnitTest extends Specification {
    private static final String SOURCE = "MyProduct"
    private static final String TARGET = "MyUnit"
    private static final String NAVIGATION_PROPERTY = "navigationProperty"
    private static final String CATEGORY = "Category"

    def generator = new OneToOneAssociationGenerator()

    @Test
    def "isApplicable for null attribute throws exception"() {
        when:
        generator.isApplicable((IntegrationObjectItemAttributeModel) null)

        then:
        thrown(IllegalArgumentException)
    }

    @Test
    def "is not applicable for null attribute descriptor"() {
        expect:
        !generator.isApplicable((TypeAttributeDescriptor) null)
    }

    @Test
    @Unroll
    def "is applicable is #expectedResult when attribute is #msg"() {
        given:
        def attributeModel = Stub(IntegrationObjectItemAttributeModel) {
            getAttributeDescriptor() >> Stub(AttributeDescriptorModel) {
                getAttributeType() >> attributeType
                getLocalized() >> localized
            }
            getIntegrationObjectItem() >> Stub(IntegrationObjectItemModel) {
                getIntegrationObject() >> Stub(IntegrationObjectModel) {
                    getCode() >> "IOCode"
                }
            }
            getReturnIntegrationObjectItem() >> returnItem
        }

        expect:
        generator.isApplicable(attributeModel as IntegrationObjectItemAttributeModel) == expectedResult

        where:
        msg                                  | localized | returnItem                       | attributeType                | expectedResult
        'primitive collection'               | false     | null                             | primitiveCollection()        | false
        'non-primitive collection'           | false     | Stub(IntegrationObjectItemModel) | Stub(CollectionTypeModel)    | false
        'primitive map'                      | false     | null                             | Stub(MapTypeModel)           | false
        'non-primitive map'                  | false     | Stub(IntegrationObjectItemModel) | Stub(MapTypeModel)           | false
        'primitive single value'             | false     | null                             | Stub(AtomicTypeModel)        | false
        'non-primitive single value'         | false     | Stub(IntegrationObjectItemModel) | Stub(TypeModel)              | true
        'primitive localized collection'     | true      | null                             | primitiveCollection()        | false
        'non-primitive localized collection' | true      | Stub(IntegrationObjectItemModel) | Stub(CollectionTypeModel)    | false
        'primitive localized map'            | true      | null                             | mapWithPrimitiveReturnType() | false
        'non-primitive localized map'        | true      | Stub(IntegrationObjectItemModel) | Stub(MapTypeModel)           | true
    }

    @Test
    @Unroll
    def "is applicable is #expectedResult when #msg"() {
        given:
        def typeAttributeDescriptor = Stub(TypeAttributeDescriptor) {
            isCollection() >> collection
            isMap() >> map
            isLocalized() >> localized
            isPrimitive() >> primitive
        }

        expect:
        generator.isApplicable(typeAttributeDescriptor as TypeAttributeDescriptor) == expectedResult

        where:
        msg                                  | collection | map   | localized | primitive | expectedResult
        'primitive collection'               | true       | false | false     | true      | false
        'non-primitive collection'           | true       | false | false     | false     | false
        'primitive map'                      | false      | true  | false     | true      | false
        'non-primitive map'                  | false      | true  | false     | false     | false
        'primitive single value'             | false      | false | false     | true      | false
        'non-primitive single value'         | false      | false | false     | false     | true
        'primitive localized collection'     | true       | false | true      | true      | false
        'non-primitive localized collection' | true       | false | true      | false     | false
        'primitive localized map'            | false      | true  | true      | true      | false
        'non-primitive localized map'        | false      | true  | true      | false     | true
    }

    @Test
    def "generate for attribute"() {
        given:
        final IntegrationObjectItemAttributeModel mockAttributeDefinitionModel =
                oneToOneRelationAttributeBuilder()
                        .withSource(SOURCE)
                        .withTarget(TARGET)
                        .withName(NAVIGATION_PROPERTY)
                        .withAttributeDescriptor(oneToOneRelation()
                                .withSourceAttribute(relationAttribute().withQualifier(SOURCE))
                                .withTargetAttribute(relationAttribute().withQualifier(TARGET)))
                        .build()

        when:
        final Association generated = generator.generate(mockAttributeDefinitionModel)

        then:
        final ODataAssociation association = new ODataAssociation(generated)
        association.isAssociationBetweenTypes(SOURCE, TARGET)
        association.hasName("FK_${SOURCE}_${NAVIGATION_PROPERTY}")
        association.hasSource(SOURCE)
        association.hasTarget(TARGET)
        association.hasSourceMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
        association.hasTargetMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
    }

    @Test
    def "generate for attribute descriptor"() {
        given:
        final TypeAttributeDescriptor descriptor = oneToOneAttributeDescriptor()
        descriptor.getAttributeName() >> NAVIGATION_PROPERTY

        final TypeDescriptor sourceTypeDescriptor = Stub(TypeDescriptor)
        sourceTypeDescriptor.getItemCode() >> SOURCE

        final TypeDescriptor targetTypeDescriptor = Stub(TypeDescriptor)
        targetTypeDescriptor.getItemCode() >> TARGET
        descriptor.getTypeDescriptor() >> sourceTypeDescriptor
        descriptor.getAttributeType() >> targetTypeDescriptor
        descriptor.reverse() >> Optional.empty()

        when:
        final Association generated = generator.generate(descriptor)

        then:
        final ODataAssociation association = new ODataAssociation(generated)
        association.isAssociationBetweenTypes(SOURCE, TARGET)
        association.hasName("FK_${SOURCE}_${NAVIGATION_PROPERTY}")
        association.hasSource(SOURCE)
        association.hasTarget(TARGET)
        association.hasSourceMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
        association.hasTargetMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
    }

    @Test
    def "generate association for attribute with same source and target type"() {
        given:
        final IntegrationObjectItemAttributeModel mockAttributeDefinitionModel =
                oneToOneRelationAttributeBuilder()
                        .withSource(CATEGORY)
                        .withTarget(CATEGORY)
                        .withName(NAVIGATION_PROPERTY)
                        .withAttributeDescriptor(oneToOneRelation()
                                .withSourceAttribute(relationAttribute().withQualifier(CATEGORY))
                                .withTargetAttribute(relationAttribute().withQualifier(CATEGORY)))
                        .build()

        when:
        final Association generated = generator.generate(mockAttributeDefinitionModel)

        then:
        final ODataAssociation association = new ODataAssociation(generated)
        association.isAssociationBetweenTypes(CATEGORY, CATEGORY)
        association.hasName("FK_${CATEGORY}_${NAVIGATION_PROPERTY}")
        association.hasSource(CATEGORY)
        association.hasTarget(StringUtils.capitalize(NAVIGATION_PROPERTY))
        association.hasSourceMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
        association.hasTargetMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
    }

    @Test
    def "generate association for descriptor with same source and target type"() {
        given:
        final TypeAttributeDescriptor descriptor = oneToOneAttributeDescriptor()
        descriptor.getAttributeName() >> NAVIGATION_PROPERTY

        final TypeDescriptor sourceTypeDescriptor = Stub(TypeDescriptor)
        sourceTypeDescriptor.getItemCode() >> CATEGORY

        final TypeDescriptor targetTypeDescriptor = Stub(TypeDescriptor)
        targetTypeDescriptor.getItemCode() >> CATEGORY
        descriptor.getTypeDescriptor() >> sourceTypeDescriptor
        descriptor.getAttributeType() >> targetTypeDescriptor

        descriptor.reverse() >> Optional.empty()

        when:
        final Association generated = generator.generate(descriptor)

        then:
        final ODataAssociation association = new ODataAssociation(generated)
        association.isAssociationBetweenTypes(CATEGORY, CATEGORY)
        association.hasName("FK_${CATEGORY}_${NAVIGATION_PROPERTY}")
        association.hasSource(CATEGORY)
        association.hasTarget(StringUtils.capitalize(NAVIGATION_PROPERTY))
        association.hasSourceMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
        association.hasTargetMultiplicity(EdmMultiplicity.ZERO_TO_ONE)
    }

    private TypeAttributeDescriptor oneToOneAttributeDescriptor() {
        final TypeAttributeDescriptor descriptor = Stub(TypeAttributeDescriptor)
        descriptor.isCollection() >> false
        descriptor.isPrimitive() >> false
        return descriptor
    }

    def mapWithPrimitiveReturnType() {
        Stub(MapTypeModel) { getReturntype() >> Stub(AtomicTypeModel) }
    }

    def primitiveCollection() {
        Stub(CollectionTypeModel) {
            getElementType() >> Stub(AtomicTypeModel)
        }
    }
}
