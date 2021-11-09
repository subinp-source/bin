/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.core.model.user.EmployeeModel
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.integrationservices.util.ClassificationBuilder
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade
import de.hybris.platform.outboundservices.util.TestOutboundFacade
import de.hybris.platform.outboundsync.activator.OutboundItemConsumer
import de.hybris.platform.outboundsync.activator.impl.DefaultOutboundSyncService
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.util.OutboundSyncTestUtil
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import de.hybris.platform.servicelayer.cronjob.CronJobService
import org.junit.Rule
import org.junit.Test
import spock.lang.Issue

import javax.annotation.Resource

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.condition
import static de.hybris.platform.outboundservices.ConsumedDestinationBuilder.consumedDestinationBuilder

@IntegrationTest
@Issue('https://jira.hybris.com/browse/STOUT-2920')
class OutboundSyncClassificationAttributesE2EIntegrationTest extends ServicelayerSpockSpecification {
    private static final def SYSTEM = 'Electronics'
    private static final def VERSION = 'Test'
    private static final def SYSTEM_VERSION = "$SYSTEM:$VERSION"
    private static final def IO = 'OutboundProduct'
    private static final def CLASS = 'QA'
    private static final def PRODUCT_CODE = 'pr1'
    private static final def UID = 'robbert.tester'

    @Resource
    private CronJobService cronJobService
    @Resource(name = 'outboundSyncService')
    private DefaultOutboundSyncService outboundSyncService
    @Resource(name = 'outboundServiceFacade')
    private OutboundServiceFacade outboundServiceFacade
    @Resource(name = 'outboundItemConsumer')
    private OutboundItemConsumer outboundItemConsumer

    TestItemChangeDetector changeDetector = new TestItemChangeDetector()
    @Rule
    TestOutboundFacade testOutboundFacade = new TestOutboundFacade().respondWithCreated()
    @Rule
    TestOutboundItemConsumer testOutboundItemConsumer = new TestOutboundItemConsumer()

    ConsumedDestinationModel destination
    def tester = createEmployee()
    CronJobModel cronJob

    def setupSpec() {
        ClassificationBuilder.classification()
                .withSystem(SYSTEM)
                .withVersion(VERSION)
                .withClassificationClass(CLASS)
                .withAttribute(ClassificationBuilder.attribute().withName('tester').references('Employee'))
                .setup()
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE IntegrationObject; code[unique = true]',
                "                               ; $IO",
                'INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code) ; root[default = false]',
                "                                   ; $IO                                   ; Product            ; Product    ; true",
                "                                   ; $IO                                   ; Catalog            ; Catalog",
                "                                   ; $IO                                   ; CatalogVersion     ; CatalogVersion",
                "                                   ; $IO                                   ; Employee           ; Employee",
                '$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
                '$attributeName = attributeName[unique = true]',
                '$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
                'INSERT_UPDATE IntegrationObjectItemAttribute; $integrationItem   ; $attributeName ; $attributeDescriptor   ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                            ; $IO:Catalog        ; id             ; Catalog:id             ;",
                "                                            ; $IO:CatalogVersion ; catalog        ; CatalogVersion:catalog ; $IO:Catalog",
                "                                            ; $IO:CatalogVersion ; version        ; CatalogVersion:version ;",
                "                                            ; $IO:Product        ; code           ; Product:code           ;",
                "                                            ; $IO:Product        ; catalogVersion ; Product:catalogVersion ; $IO:CatalogVersion",
                "                                            ; $IO:Product        ; catalogVersion ; Product:catalogVersion ; $IO:CatalogVersion",
                "                                            ; $IO:Product        ; catalogVersion ; Product:catalogVersion ; $IO:CatalogVersion",
                "                                            ; $IO:Employee       ; uid            ; Employee:uid           ;",
                "                                            ; $IO:Employee       ; name           ; Employee:name          ;",
                '$item=integrationObjectItem(integrationObject(code), code)',
                '$systemVersionHeader=systemVersion(catalog(id), version)',
                '$classificationClassHeader=classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader=classificationAttribute($systemVersionHeader, code)',
                '$classificationAssignment=classAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'INSERT_UPDATE IntegrationObjectItemClassificationAttribute; $item[unique = true]; attributeName[unique = true]; $classificationAssignment                    ; returnIntegrationObjectItem(integrationObject(code), code)',
                "                                                          ; $IO:Product         ; tester                      ; $SYSTEM_VERSION:$CLASS:$SYSTEM_VERSION:tester; $IO:Employee")
    }

    def setup() {
        importCsv '/impex/essentialdata-outboundsync.impex', 'UTF-8'
        destination = consumedDestinationBuilder().withId('outbound-classification-e2e').build()
        cronJob = OutboundSyncTestUtil.outboundCronJob()
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Product; code[unique = true]; catalogVersion(catalog(id), version)[unique = true]',
                "                     ; $PRODUCT_CODE      ; $SYSTEM_VERSION")
        outboundSyncService.outboundServiceFacade = testOutboundFacade
        outboundSyncService.outboundItemConsumer = testOutboundItemConsumer
    }

    def cleanup() {
        outboundSyncService.outboundServiceFacade = outboundServiceFacade
        outboundSyncService.outboundItemConsumer = outboundItemConsumer
        changeDetector.after() // consume all items before removing stream container
        IntegrationTestUtil.removeAll OutboundChannelConfigurationModel
        IntegrationTestUtil.removeAll ProductModel
        IntegrationTestUtil.remove tester
        IntegrationTestUtil.remove cronJob
        ConsumedDestinationBuilder.cleanup()
    }

    def cleanupSpec() {
        IntegrationObjectTestUtil.cleanup()
        ClassificationBuilder.cleanup()
    }

    @Test
    def 'update not sent when root item cannot be derived from the changed item model'() {
        given: 'product is associated with the classification class'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader=catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]; products($catalogVersionHeader, code)',
                "                                 ; $CLASS             ; $SYSTEM_VERSION                     ; $SYSTEM_VERSION:$PRODUCT_CODE")
        and: 'product has classification attribute assigned'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$assignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                '$valueHeader=value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                'INSERT_UPDATE ProductFeature; product($catalogVersionHeader, code)[unique = true]; $assignmentHeader[unique = true]             ; qualifier                     ; $valueHeader               ; valuePosition[unique = true]',
                "                            ; $SYSTEM_VERSION:$PRODUCT_CODE                      ; $SYSTEM_VERSION:$CLASS:$SYSTEM_VERSION:tester; $SYSTEM/$VERSION/${CLASS}.tester; \"reference, ${tester.pk}\"; 0")
        and: 'Outbound sync channel listens for Employee and Product changes after they created'
        changeDetector.createChannel('outboundProducts', IO, destination)
        changeDetector.createChangeStream 'outboundProducts', 'Product'
        changeDetector.createChangeStream 'outboundProducts', 'Employee'
        and: 'the Employee is changed'
        IntegrationTestUtil.importImpEx(
                'UPDATE Employee ; uid[unique = true]; name',
                "                ; $UID              ; Robbert Tester")

        when:
        cronJobService.performCronJob(cronJob, true)

        then: 'update not sent'
        condition().eventually {
            assert testOutboundFacade.invocations() == 0
            assert testOutboundItemConsumer.invocations() == 0
        }
    }

    @Test
    def 'sends update when product is associated with new classification attribute'() {
        given: 'outbound channel listens for Product created'
        changeDetector.createChannel('outboundProducts', IO, destination)
        changeDetector.createChangeStream 'outboundProducts', 'Product'
        and: 'product is associated with the classification class'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader=catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]; products($catalogVersionHeader, code)',
                "                                 ; $CLASS             ; $SYSTEM_VERSION                     ; $SYSTEM_VERSION:$PRODUCT_CODE")

        when:
        cronJobService.performCronJob(cronJob, true)

        then: "update is sent"
        condition().eventually {
            assert testOutboundFacade.invocations() == 1
            assert testOutboundFacade.itemsFromInvocationsTo(destination, IO)
                    .collect({ it.itemtype }) == ['Product']
            assert testOutboundItemConsumer.invocations() == 1
        }
    }

    @Test
    def 'sends update when product is disassociated from the classification class'() {
        given: 'product is associated with the classification class'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader=catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]; products($catalogVersionHeader, code)',
                "                                 ; $CLASS             ; $SYSTEM_VERSION                     ; $SYSTEM_VERSION:$PRODUCT_CODE")
        and: 'product has classification attribute assigned'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$assignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                '$valueHeader=value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                'INSERT_UPDATE ProductFeature; product($catalogVersionHeader, code)[unique = true]; $assignmentHeader[unique = true]             ; qualifier                     ; $valueHeader               ; valuePosition[unique = true]',
                "                            ; $SYSTEM_VERSION:$PRODUCT_CODE                      ; $SYSTEM_VERSION:$CLASS:$SYSTEM_VERSION:tester; $SYSTEM/$VERSION/${CLASS}.tester; \"reference, ${tester.pk}\"; 0")
        and: 'outbound channel listens for Product created'
        changeDetector.createChannel('outboundProducts', IO, destination)
        changeDetector.createChangeStream 'outboundProducts', 'Product'
        and: 'product is disassociated from the classification class'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader=catalogVersion(catalog(id), version)',
                'UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]; products($catalogVersionHeader, code)[mode = remove]',
                "                          ; $CLASS             ; $SYSTEM_VERSION                     ; $SYSTEM_VERSION:$PRODUCT_CODE")

        when:
        cronJobService.performCronJob(cronJob, true)

        then: "update is sent"
        condition().eventually {
            assert testOutboundFacade.invocations() == 1
            assert testOutboundFacade.itemsFromInvocationsTo(destination, IO)
                    .collect({ it.itemtype }) == ['Product']
            assert testOutboundItemConsumer.invocations() == 1
        }
    }

    @Test
    def "sends update when product classification attribute is assigned"() {
        given: 'product is associated with the classification class'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader=catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]; products($catalogVersionHeader, code)',
                "                                 ; $CLASS             ; $SYSTEM_VERSION                     ; $SYSTEM_VERSION:$PRODUCT_CODE")
        and: 'Outbound sync channel listens for Product changes'
        changeDetector.createChannel('outboundProducts', IO, destination)
        changeDetector.createChangeStream 'outboundProducts', 'Product'
        and: 'product has classification attribute assigned'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass(catalogVersion(catalog(id), version), code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$assignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                '$valueHeader=value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                'INSERT_UPDATE ProductFeature; product($catalogVersionHeader, code)[unique = true]; $assignmentHeader[unique = true]             ; qualifier                     ; $valueHeader               ; valuePosition[unique = true]',
                "                            ; $SYSTEM_VERSION:$PRODUCT_CODE                      ; $SYSTEM_VERSION:$CLASS:$SYSTEM_VERSION:tester; $SYSTEM/$VERSION/${CLASS}.tester; \"reference, ${tester.pk}\"; 0")

        when:
        cronJobService.performCronJob(cronJob, true)

        then: 'update is sent'
        condition().eventually {
            assert testOutboundFacade.invocations() == 1
            assert testOutboundFacade.itemsFromInvocationsTo(destination, IO)
                    .collect({ it.itemtype }) == ['Product']
            assert testOutboundItemConsumer.invocations() == 1
        }
    }

    @Test
    def 'sends update when classification attributes is unassigned from the product'() {
        given: 'product is associated with the classification class'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader=catalogVersion(catalog(id), version)',
                'INSERT_UPDATE ClassificationClass; code[unique = true]; $catalogVersionHeader[unique = true]; products($catalogVersionHeader, code)',
                "                                 ; $CLASS             ; $SYSTEM_VERSION                     ; $SYSTEM_VERSION:$PRODUCT_CODE")
        and: 'product has classification attribute assigned'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass($catalogVersionHeader, code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$assignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                '$valueHeader=value[translator = de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator]',
                'INSERT_UPDATE ProductFeature; product($catalogVersionHeader, code)[unique = true]; $assignmentHeader[unique = true]             ; qualifier                     ; $valueHeader               ; valuePosition[unique = true]',
                "                            ; $SYSTEM_VERSION:$PRODUCT_CODE                      ; $SYSTEM_VERSION:$CLASS:$SYSTEM_VERSION:tester; $SYSTEM/$VERSION/${CLASS}.tester; \"reference, ${tester.pk}\"; 0")
        and: 'outbound sync channel listens for Product changes'
        changeDetector.createChannel('outboundProducts', IO, destination)
        changeDetector.createChangeStream 'outboundProducts', 'Product'
        and: 'the classification attribute is unassigned from the product'
        IntegrationTestUtil.importImpEx(
                '$catalogVersionHeader = catalogVersion(catalog(id), version)',
                '$systemVersionHeader = systemVersion(catalog(id), version)',
                '$classificationClassHeader = classificationClass($catalogVersionHeader, code)',
                '$classificationAttributeHeader = classificationAttribute($systemVersionHeader, code)',
                '$assignmentHeader=classificationAttributeAssignment($classificationClassHeader, $classificationAttributeHeader)',
                'REMOVE ProductFeature; product($catalogVersionHeader, code)[unique = true]; $assignmentHeader[unique = true]             ',
                "                     ; $SYSTEM_VERSION:$PRODUCT_CODE                      ; $SYSTEM_VERSION:$CLASS:$SYSTEM_VERSION:tester")

        when:
        cronJobService.performCronJob(cronJob, true)

        then: 'update is sent'
        condition().eventually {
            assert testOutboundFacade.invocations() == 1
            assert testOutboundFacade.itemsFromInvocationsTo(destination, IO)
                    .collect({ it.itemtype }) == ['Product']
            assert testOutboundItemConsumer.invocations() == 1
        }
    }

    def createEmployee() {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE Employee; uid[unique = true]; name',
                "                      ; $UID              ; Bob Tester")
        IntegrationTestUtil.findAny(EmployeeModel, { it.uid == UID }).orElse(null)
    }
}