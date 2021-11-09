/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.CatalogModel
import de.hybris.platform.catalog.model.CatalogVersionModel
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.category.model.CategoryModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.c2l.LanguageModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.populator.ItemToMapConversionContext
import de.hybris.platform.integrationservices.populator.PrimitiveCollectionElement
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.servicelayer.ServicelayerTransactionalSpockSpecification
import de.hybris.platform.servicelayer.i18n.I18NService
import org.junit.Test
import spock.lang.Unroll

import javax.annotation.Resource

@IntegrationTest
class IntegrationObjectConversionServiceIntegrationTest extends ServicelayerTransactionalSpockSpecification {
    private static final String TEST_NAME = "IntegrationObjectConversionService"
    private static final String INTEGRATION_OBJECT = "${TEST_NAME}_IO"
    private static final String CATALOG_ID = "${TEST_NAME}_Catalog"
    private static final String CATEGORY = "${TEST_NAME}_Category"

    @Resource(name = "integrationObjectConversionService")
    private IntegrationObjectConversionService conversionService
    @Resource
    private I18NService i18NService

    private Locale defaultLocale

    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true];',
                "                               ; $INTEGRATION_OBJECT",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)',
                "                                   ; $INTEGRATION_OBJECT                   ; Catalog            ; Catalog",
                "                                   ; $INTEGRATION_OBJECT                   ; CatalogVersion     ; CatalogVersion",
                "                                   ; $INTEGRATION_OBJECT                   ; Category           ; Category",
                "                                   ; $INTEGRATION_OBJECT                   ; Product            ; Product",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$attributeDescriptor=attributeDescriptor(enclosingType(code), qualifier)',
                '$references=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]              ; attributeName[unique = true]; $attributeDescriptor  ; $references',
                "                                            ; $INTEGRATION_OBJECT:Product       ; tstCode                     ; Product:code          ;",
                "                                            ; $INTEGRATION_OBJECT:Category      ; tstCode                     ; Category:code         ;",
                "                                            ; $INTEGRATION_OBJECT:Category      ; tstName                     ; Category:name         ;",
                "                                            ; $INTEGRATION_OBJECT:Category      ; tstDescription              ; Category:description  ;",
                "                                            ; $INTEGRATION_OBJECT:Category      ; tstProducts                 ; Category:products     ; $INTEGRATION_OBJECT:Product",
                "                                            ; $INTEGRATION_OBJECT:Catalog       ; tstId                       ; Catalog:id            ;",
                "                                            ; $INTEGRATION_OBJECT:Catalog       ; tstUrlPatterns              ; Catalog:urlPatterns   ;",
                "                                            ; $INTEGRATION_OBJECT:CatalogVersion; tstCatalog                  ; CatalogVersion:catalog; $INTEGRATION_OBJECT:Catalog",
                "                                            ; $INTEGRATION_OBJECT:CatalogVersion; tstVersion                  ; CatalogVersion:version;",
                'INSERT_UPDATE IntegrationObject; code[unique = true];',
                '                               ; ClassAttributeAssignment',
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]            ; type(code)',
                '                                   ; ClassAttributeAssignment              ; ClassAttributeAssignment       ; ClassAttributeAssignment',
                '                                   ; ClassAttributeAssignment              ; ClassificationAttributeTypeEnum; ClassificationAttributeTypeEnum',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]                                     ; attributeName[unique = true]; $attributeDescriptor                  ; $references                                              ; unique[default = false]',
                '                                            ; ClassAttributeAssignment:ClassAttributeAssignment        ; attributeType               ; ClassAttributeAssignment:attributeType; ClassAttributeAssignment:ClassificationAttributeTypeEnum ; true',
                '                                            ; ClassAttributeAssignment:ClassificationAttributeTypeEnum ; code                        ; ClassificationAttributeTypeEnum:code  ;                                                          ; true',
                '                                            ; ClassAttributeAssignment:ClassificationAttributeTypeEnum ; codex                       ; ClassificationAttributeTypeEnum:code  ;                                                          ;',
                '# For localized attribute test case',
                'INSERT_UPDATE Language; isocode[unique = true]',
                '                      ; fr',
                'INSERT_UPDATE Catalog; id[unique = true];',
                "                     ; $CATALOG_ID",
                'INSERT_UPDATE CatalogVersion; catalog(id)[unique = true]; version[unique = true]; active;',
                "                            ; $CATALOG_ID               ; Staged                ; true")
    }

    def cleanupSpec() {
        IntegrationTestUtil.remove IntegrationObjectModel, { it.code == INTEGRATION_OBJECT }
        IntegrationTestUtil.removeSafely LanguageModel, { it.isocode == 'fr' }
        IntegrationTestUtil.removeSafely CatalogVersionModel, { it.version == 'Staged' && it.catalog.id == CATALOG_ID }
        IntegrationTestUtil.removeSafely CatalogModel, { it.id == CATALOG_ID }
    }

    def setup() {
        defaultLocale = i18NService.currentLocale
    }

    def cleanup() {
        i18NService.currentLocale = defaultLocale
        IntegrationTestUtil.remove CategoryModel, {it.code == CATEGORY}
    }

    @Test
    def "converts simple item model without nested items"() {
        given:
        def catalog = new CatalogModel(id: CATALOG_ID, urlPatterns: ['url1', 'url2'])

        when:
        def converted = conversionService.convert conversionContext(catalog)

        then:
        def expectedAttributes = [tstId: CATALOG_ID, tstUrlPatterns: [PrimitiveCollectionElement.create('url1'), PrimitiveCollectionElement.create('url2')]]
        converted.intersect(expectedAttributes) == expectedAttributes // contains all expected attributes
    }

    @Test
    def "converted model contains generated integration key"() {
        given:
        def catalog = new CatalogModel(id: CATALOG_ID)

        when:
        def converted = conversionService.convert conversionContext(catalog)

        then:
        converted['integrationKey'] == CATALOG_ID
    }

    @Test
    def "converted model does not contain null attributes"() {
        given:
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version)',
                "                      ; $CATEGORY          ; $CATALOG_ID:Staged")
        when:
        def converted = conversionService.convert conversionContext(findCategoryByCode(CATEGORY))

        then:
        !converted.keySet().containsAll(['tstName', 'tstProducts'])
    }

    @Test
    def "can convert properties of Enum type"() {
        given:
        final ClassAttributeAssignmentModel model = new ClassAttributeAssignmentModel()
        model.setAttributeType(ClassificationAttributeTypeEnum.STRING)

        when:
        def map = conversionService.convert conversionContext(model, "ClassAttributeAssignment")

        then:
        map["attributeType"] == [code: 'string', codex: 'string']
    }

    @Test
    def "converts localized attributes into nested entities"() {
        given: 'default locale in the system is FRENCH'
        i18NService.setCurrentLocale(Locale.FRENCH)
        and: 'there is a category with multiple locales set'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version); name[lang = en]; name[lang = fr]',
                "                      ; $CATEGORY          ; $CATALOG_ID:Staged                  ; english value  ; french value")

        when:
        def converted = conversionService.convert conversionContext(findCategoryByCode(CATEGORY))

        then: 'default language populated in localized properties'
        converted['tstName'] == 'french value'
        converted['localizedAttributes'] == [[language: 'en', tstName: 'english value'], [language: 'fr', tstName: 'french value']]
    }

    @Test
    def "converted model contains empty localized attribute values but no null localized attribute values"() {
        given: 'default locale in the system is ENGLISH'
        i18NService.setCurrentLocale(Locale.ENGLISH)
        and: 'there a category with some localized attributes set'
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version); name[lang = en]',
                "                      ; $CATEGORY          ; $CATALOG_ID:Staged                  ; english name",
                'UPDATE Category; code[unique = true]; catalogVersion(catalog(id), version); description[lang = fr]',
                "               ; $CATEGORY          ; $CATALOG_ID:Staged                  ; french description")
        // Cannot set value to empty string via impex so have to do it programmatically
        def category = findCategoryByCode(CATEGORY)
        category.setName("", Locale.FRENCH)

        when:
        def converted = conversionService.convert conversionContext(category)

        then:
        converted['tstName'] == 'english name'
        !converted.hasProperty('tstDescription')
        converted['localizedAttributes'] == [[language: 'fr', tstName: '', tstDescription: 'french description'], [language: 'en', tstName: 'english name']]
    }

    @Test
    @Unroll
    def "throws exception when the specified IntegrationObject code is '#objCode'"() {
        when:
        conversionService.convert new ItemModel(), objCode

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains('null')
        e.message.contains('empty')

        where:
        objCode << [null, '']
    }

    @Test
    def "throws exception when the IntegrationObject does not contain item definition for the specified item model"() {
        when:
        conversionService.convert new EmployeeModel(), INTEGRATION_OBJECT

        then:
        thrown IntegrationObjectAndItemMismatchException
    }

    @Test
    def "converts attribute value that is subclass of the declared integration object item attribute type"() {
        given:
        def catalog = new ClassificationSystemModel(id: 'classifications') // subtype of CatalogModel
        def catalogVersion = new CatalogVersionModel(version: 'test', catalog: catalog)

        when:
        def converted = conversionService.convert conversionContext(catalogVersion, INTEGRATION_OBJECT)

        then:
        def expectedClassificationAttributes = [tstId: 'classifications']
        converted['tstCatalog'].intersect(expectedClassificationAttributes) == expectedClassificationAttributes
    }

    private static CategoryModel findCategoryByCode(String code) {
        IntegrationTestUtil.findAny(CategoryModel, { code == it.code }).orElse(null)
    }

    private static ItemToMapConversionContext conversionContext(ItemModel item, String ioCode = INTEGRATION_OBJECT) {
        def io = IntegrationObjectTestUtil.findIntegrationObjectDescriptorByCode ioCode
        def type = io.getItemTypeDescriptor(item).orElse(null)
        new ItemToMapConversionContext(item, type)
    }
}
