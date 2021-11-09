package de.hybris.platform.odata2services.converter

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.odata2services.odata.persistence.exception.MissingPropertyException
import org.apache.olingo.odata2.api.ep.entry.ODataEntry
import org.apache.olingo.odata2.api.processor.ODataContext
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

import static de.hybris.platform.odata2services.converter.ConversionParameters.ConversionParametersBuilder.conversionParametersBuilder

@UnitTest
class ODataEntryAttributeValueConverterUnitTest extends Specification {
    static final def ATTR_NAME = 'TestAttribute'
    def oDataConverter = Stub(ODataEntryToIntegrationItemConverter)
    def payLoadConverter = Stub(PayloadAttributeValueConverter)
    def converter = new ODataEntryAttributeValueConverter(oDataConverter, payLoadConverter)

    @Test
    @Unroll
    def "isApplicable returns #applicable if #attributeValue is an instance of ODataEntry"() {
        given:
        def parameters = conversionParametersBuilder()
                .withAttributeValue(attributeValue)
                .build()


        expect:
        converter.isApplicable(parameters) == applicable

        where:
        attributeValue   | applicable
        Stub(ODataEntry) | true
        'String'         | false
    }

    @Test
    def "converts value when ODataEntry represents a primitive type"() {
        given:
        def integrationItem = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute("primitiveAttribute") >> Optional.of(primitiveAttribute())
            }
        }

        and:
        def parameters = conversionParametersBuilder()
                .withAttributeName("primitiveAttribute")
                .withIntegrationItem(integrationItem)
                .withAttributeValue(oDataEntry([value: 'string']))
                .build()

        expect:
        converter.convert(parameters) == 'string'
    }

    @Test
    def "error is thrown if the attribute does not exist in the context item type"() {
        given:
        def item = Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttributes() >> []
                getAttribute(_) >> Optional.empty()
            }
        }

        and:
        def parameters = conversionParametersBuilder()
                .withAttributeName('attribute')
                .withIntegrationItem(item)
                .build()

        when:
        converter.convert parameters

        then:
        thrown MissingPropertyException
    }

    @Test
    def "converts value when ODataEntry is a nested item"() {
        given:
        ODataEntry attrValue = oDataEntry([value: 'string'])
        def attributeType = Stub(TypeDescriptor)

        and:
        def params = conversionParametersBuilder()
                .withAttributeName(ATTR_NAME)
                .withContext(context())
                .withAttributeValue(attrValue)
                .withIntegrationItem(integrationItem(attributeType, ATTR_NAME))
                .build()

        and:
        def convertedItem = Stub(IntegrationItem)
        oDataConverter.convert(params.context, attributeType, attrValue, params.integrationItem) >> convertedItem

        when:
        def item = converter.convert params

        then:
        item.is convertedItem
    }

    ODataEntry oDataEntry(Map<String, Object> properties) {
        Stub(ODataEntry) {
            getProperties() >> properties
            getProperty(_ as String) >> { properties.get(it) }
        }
    }

    def integrationItem(TypeDescriptor attributeType, String attributeName) {
        Stub(IntegrationItem) {
            getItemType() >> Stub(TypeDescriptor) {
                getAttribute(attributeName) >> Optional.of(Stub(TypeAttributeDescriptor) {
                    getAttributeType() >> attributeType
                })
            }
        }
    }

    def primitiveAttribute(){
        Stub(TypeAttributeDescriptor){
            isPrimitive() >> true
        }
    }

    ODataContext context() {
        Stub(ODataContext)
    }
}
