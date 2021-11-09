/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.odata2webservices.odata.ODataFacade
import de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.search.SearchResult
import org.apache.olingo.odata2.api.commons.HttpStatusCodes
import org.junit.Test

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.JsonBuilder.json
import static de.hybris.platform.odata2webservices.odata.ODataFacadeTestUtils.createContext
import static org.assertj.core.api.Assertions.assertThat
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@IntegrationTest
class ODataEnumTypeIntegrationTest extends ServicelayerSpockSpecification {
    @Resource(name = "oDataWebMonitoringFacade")
    private ODataFacade facade

    @Resource
    private FlexibleSearchService flexibleSearchService

    def testSystem = "ERP_CLASSIFICATION_3000"
    def testSystemVersion = "ERP_IMPORT"
    def testClassificationSystemVersion = "$testSystem:$testSystemVersion"


    def setupSpec() {
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE IntegrationObject; code[unique = true]",
                "; ClassAttributeAssignment",
                "INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)",
                "; ClassAttributeAssignment ; ClassAttributeAssignment        ; ClassAttributeAssignment",
                "; ClassAttributeAssignment ; ClassificationClass             ; ClassificationClass",
                "; ClassAttributeAssignment ; CatalogVersion                  ; CatalogVersion",
                "; ClassAttributeAssignment ; Catalog                         ; Catalog",
                "; ClassAttributeAssignment ; ClassificationSystem            ; ClassificationSystem",
                "; ClassAttributeAssignment ; ClassificationSystemVersion     ; ClassificationSystemVersion",
                "; ClassAttributeAssignment ; ClassificationAttributeTypeEnum ; ClassificationAttributeTypeEnum",
                "; ClassAttributeAssignment ; ClassificationAttributeUnit     ; ClassificationAttributeUnit",
                "; ClassAttributeAssignment ; ClassificationAttribute         ; ClassificationAttribute",

                '$item = integrationObjectItem(integrationObject(code), code)',
                '$descriptor = attributeDescriptor(enclosingType(code), qualifier)',
                '$refType=returnIntegrationObjectItem(integrationObject(code), code)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $item[unique = true]; attributeName[unique = true]; $descriptor; $refType; unique[default = false]',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; classificationClass     ; ClassAttributeAssignment:classificationClass     ; ClassAttributeAssignment:ClassificationClass             ; true',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; classificationAttribute ; ClassAttributeAssignment:classificationAttribute ; ClassAttributeAssignment:ClassificationAttribute         ; true',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; attributeType           ; ClassAttributeAssignment:attributeType           ; ClassAttributeAssignment:ClassificationAttributeTypeEnum ; true',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; multiValued             ; ClassAttributeAssignment:multiValued             ;                                                          ;',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; unit                    ; ClassAttributeAssignment:unit                    ; ClassAttributeAssignment:ClassificationAttributeUnit     ; true',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; formatDefinition        ; ClassAttributeAssignment:formatDefinition        ;                                                          ;',
                '; ClassAttributeAssignment:ClassAttributeAssignment        ; range                   ; ClassAttributeAssignment:range                   ;                                                          ;',
                '; ClassAttributeAssignment:ClassificationClass             ; code                    ; ClassificationClass:code                         ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationClass             ; catalogVersion          ; ClassificationClass:catalogVersion               ; ClassAttributeAssignment:ClassificationSystemVersion     ; true',
                '; ClassAttributeAssignment:CatalogVersion                  ; catalog                 ; CatalogVersion:catalog                           ; ClassAttributeAssignment:Catalog                         ; true',
                '; ClassAttributeAssignment:CatalogVersion                  ; version                 ; CatalogVersion:version                           ;                                                          ; true',
                '; ClassAttributeAssignment:CatalogVersion                  ; active                  ; CatalogVersion:active                            ;                                                          ;',
                '; ClassAttributeAssignment:Catalog                         ; id                      ; Catalog:id                                       ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationAttribute         ; code                    ; ClassificationAttribute:code                     ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationAttribute         ; systemVersion           ; ClassificationAttribute:systemVersion            ; ClassAttributeAssignment:ClassificationSystemVersion     ; true',
                '; ClassAttributeAssignment:ClassificationSystemVersion     ; version                 ; ClassificationSystemVersion:version              ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationSystemVersion     ; catalog                 ; ClassificationSystemVersion:catalog              ; ClassAttributeAssignment:Catalog                         ; true',
                '; ClassAttributeAssignment:ClassificationSystem            ; id                      ; ClassificationSystem:id                          ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationAttributeTypeEnum ; code                    ; ClassificationAttributeTypeEnum:code             ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationAttributeUnit     ; code                    ; ClassificationAttributeUnit:code                 ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationAttributeUnit     ; unitType                ; ClassificationAttributeUnit:unitType             ;                                                          ; true',
                '; ClassAttributeAssignment:ClassificationAttributeUnit     ; systemVersion           ; ClassificationAttributeUnit:systemVersion        ; ClassAttributeAssignment:ClassificationSystemVersion     ; true'
        )
    }

    def cleanupSpec() {
        IntegrationTestUtil.removeAll IntegrationObjectModel
        IntegrationTestUtil.removeAll ClassAttributeAssignmentModel
        IntegrationTestUtil.removeAll ClassificationSystemModel
        IntegrationTestUtil.removeAll ClassificationSystemVersionModel
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationAttributeValueModel
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
    }

    @Test
    def "can successfully create a new ClassAttributeTypeEnum"() {
        given:
        def classificationClassCode = "WEC_CDRAGON_CAR"
        def classificationAttributeCode = "WEC_DC_COLOR"
        def unitTypeVal = "SAP-POWER"
        def unitCode = "WTT"
        IntegrationTestUtil.importImpEx(
                "INSERT_UPDATE ClassificationSystem; id[unique = true]; name[lang = en];",
                "                                  ; $testSystem      ; $testSystem",
                "INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]; active;",
                "                                         ; $testSystem               ; $testSystemVersion    ; true",
                "INSERT_UPDATE ClassificationClass; code[unique = true]     ; name[lang = en]; catalogVersion(catalog(id), version)",
                "                                 ; $classificationClassCode; Dragon Car     ; $testClassificationSystemVersion",
                "INSERT_UPDATE ClassificationAttribute; code[unique = true]         ; name[lang = en]; systemVersion(catalog(id), version)",
                "                                     ; $classificationAttributeCode; Dark Blue      ; $testClassificationSystemVersion",
                "INSERT_UPDATE ClassificationAttributeValue; code[unique = true]         ; name[lang = en]; systemVersion(catalog(id), version)",
                "                                          ; $classificationAttributeCode; Dark Blue      ; $testClassificationSystemVersion",
                "INSERT_UPDATE ClassificationAttributeUnit; code[unique = true]; unitType[unique = true]; systemVersion(catalog(id), version); symbol",
                "                                         ; $unitCode          ; $unitTypeVal           ; $testClassificationSystemVersion   ; aSymbol",
        )
        final String content = json()
                .withField("range", false)
                .withField("multiValued", false)
                .withField("classificationClass", json()
                        .withField("code", classificationClassCode)
                        .withField("catalogVersion", testSystemVersionJson()))
                .withField("classificationAttribute", json()
                        .withField("code", classificationAttributeCode)
                        .withField("systemVersion", testSystemVersionJson()))
                .withField("attributeType", json()
                        .withField("code", "string"))
                .withField("unit", json()
                        .withField("code", unitCode)
                        .withField("unitType", unitTypeVal)
                        .withField("systemVersion", testSystemVersionJson()))
                .build()

        when:
        def request = ODataFacadeTestUtils.oDataPostRequest("ClassAttributeAssignment", "ClassAttributeAssignments", content, APPLICATION_JSON_VALUE)
        def postResponse = facade.handleRequest(createContext(request))
        postResponse.getStatus() == HttpStatusCodes.CREATED

        then:
        def classAssignment = IntegrationTestUtil.findAny(ClassAttributeAssignmentModel, { it.attributeType == ClassificationAttributeTypeEnum.STRING }).orElse(null)
        with(classAssignment) {
            classificationClass.code == classificationClassCode
            attributeType == ClassificationAttributeTypeEnum.STRING
            with(classificationAttribute) {
                code == classificationAttributeCode
            }
            with(unit) {
                code == unitCode
                unitType == unitTypeVal
            }
        }

        assertHasMoreThanOneStringTypClassificationAttributeTypeEnum()
    }

    def assertHasMoreThanOneStringTypClassificationAttributeTypeEnum() {
        final String searchQuery = "SELECT {pk} FROM {ClassificationAttributeTypeEnum*} WHERE {code} = 'string'"
        final SearchResult<Object> search = flexibleSearchService.search(searchQuery)
        assertThat(search.getCount()).isGreaterThan(1)
    }

    def testSystemVersionJson() {
        json()
                .withField("version", testSystemVersion)
                .withField("catalog", json()
                        .withId(testSystem))
    }
}
