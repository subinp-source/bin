/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.populator

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.integrationservices.model.AttributeValueAccessor
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor
import de.hybris.platform.servicelayer.i18n.CommonI18NService
import org.junit.Test
import spock.lang.Specification

@UnitTest
class LocalizedAttribute2MapPopulatorUnitTest extends Specification {
    private static final String LOCALIZED_ATTRIBUTES = "localizedAttributes"
    private static final String LANGUAGE = "language"

    def commonI18NService = Stub(CommonI18NService)
    def populator = new LocalizedAttribute2MapPopulator(commonI18NService: commonI18NService)

    @Test
    def "populator is applicable for localized attribute"() {
        given:
        def localizedAttribute = Stub(TypeAttributeDescriptor) { isLocalized() >> true }

        expect:
        populator.isApplicable(localizedAttribute)
    }

    @Test
    def "populator is not applicable for non-localized attribute"() {
        given:
        def nonLocalizedAttribute = Stub(TypeAttributeDescriptor) { isLocalized() >> false }

        expect:
        !populator.isApplicable(nonLocalizedAttribute)
    }

    @Test
    def "populate to map when attribute 'description' has an empty string translation"() {
        given: "an attribute 'description' with English value set to empty string"
        def description = attribute('description', [(Locale.ENGLISH): ''])

        and: 'English is the only supported language'
        supportedLanguages(Locale.ENGLISH)

        and: 'empty target map'
        def target = [:]

        when: 'the target map is populated with the attribute'
        populator.populateToMap(description, context(), target)

        then: 'the target map contains the attribute and value'
        def attributes = target[LOCALIZED_ATTRIBUTES]
        attributes.size() == 1
        attributes.contains(['description': '', (LANGUAGE): 'en'])
    }

    @Test
    def "populate to map when attribute 'name' has a non-empty string translation and multiple supported languages"() {
        given: "an attribute 'name' with English value set"
        def value = 'earphones'
        def attributeName = 'name'
        def name = attribute(attributeName, [(Locale.ENGLISH): value])

        and: 'English and German are supported languages'
        supportedLanguages(Locale.ENGLISH, Locale.GERMAN)

        and: 'empty target map'
        def target = [:]

        when: 'the target map is populated with the attribute'
        populator.populateToMap(name, context(), target)

        then: 'the target map contains the attribute and value'
        def attributes = target[LOCALIZED_ATTRIBUTES]
        attributes.size() == 1
        attributes.contains([(attributeName): value, (LANGUAGE): 'en'])
    }

    @Test
    def "populate to map when attribute 'name' has a Spanish translation with region"() {
        given: "an attribute 'name' with Spanish value set"
        def value = 'auriculares'
        def attributeName = 'name'
        def spanish = new Locale.Builder().setLanguage("es").setRegion("CO").build()
        def name = attribute(attributeName, [(spanish): value])

        and: 'Spanish and German are supported languages'
        supportedLanguages(spanish, Locale.GERMAN)

        and: 'empty target map'
        def target = [:]

        when: 'the target map is populated with the attribute'
        populator.populateToMap(name, context(), target)

        then: 'the target map contains the attribute and value'
        def attributes = target[LOCALIZED_ATTRIBUTES]
        attributes.size() == 1
        attributes.contains([(attributeName): value, (LANGUAGE): spanish.toString()])
    }

    @Test
    def "populate to map when attribute 'name' has English and German translations"() {
        given: "an attribute 'name' with English and German values"
        def attributeName = 'name'
        def englishValue = 'earphones'
        def germanValue = 'Kopfhörer'
        def name = attribute(attributeName, [(Locale.ENGLISH): englishValue, (Locale.GERMAN): germanValue])

        and: 'English and German are supported languages'
        supportedLanguages(Locale.ENGLISH, Locale.GERMAN)

        and: 'empty target map'
        def target = [:]

        when: 'the target map is populated with the attribute'
        populator.populateToMap(name, context(), target)

        then: 'the target map contains the attribute and values for all locales'
        List attributes = target[LOCALIZED_ATTRIBUTES] as List
        attributes.size() == 2
        attributes.containsAll(
                [(attributeName): englishValue, (LANGUAGE): Locale.ENGLISH.toString()],
                [(attributeName): germanValue, (LANGUAGE): Locale.GERMAN.toString()])
    }

    @Test
    def "populate to map when attribute 'name' already contains localizedAttributes and another locale value is added"() {
        given: "an attribute 'name' with English value"
        def attributeName = 'name'
        def englishValue = 'earphones'
        def name = attribute(attributeName, [(Locale.ENGLISH): englishValue])

        and: 'English and German are supported languages'
        supportedLanguages(Locale.ENGLISH, Locale.GERMAN)

        and: 'target already contains value for German'
        def germanValue = 'Kopfhörer'
        def target = [(LOCALIZED_ATTRIBUTES): [[(LANGUAGE): Locale.GERMAN.toString(), (attributeName): germanValue]]]

        when: 'the target map is populated with the attribute'
        populator.populateToMap(name, context(), target)

        then: 'the target map contains the attribute and values for all locales'
        List attributes = target[LOCALIZED_ATTRIBUTES]
        attributes.size() == 2
        attributes.containsAll(
                [(attributeName): englishValue, (LANGUAGE): Locale.ENGLISH.toString()],
                [(attributeName): germanValue, (LANGUAGE): Locale.GERMAN.toString()])
    }

    private TypeAttributeDescriptor attribute(String name, Map<Locale, String> values) {
        Stub(TypeAttributeDescriptor) {
            getAttributeName() >> name
            accessor() >> Stub(AttributeValueAccessor) {
                getValues(_ as ItemModel, _) >> values
            }
        }
    }

    private ItemToMapConversionContext context() {
        Stub(ItemToMapConversionContext) {
            getItemModel() >> Stub(ItemModel)
        }
    }

    private void supportedLanguages(Locale... locales) {
        def languages = Arrays.asList(locales)
                .collect {
                    def language = Stub(LanguageModel)
                    commonI18NService.getLocaleForLanguage(language) >> it
                    language
                }
        commonI18NService.getAllLanguages() >> languages
    }
}
