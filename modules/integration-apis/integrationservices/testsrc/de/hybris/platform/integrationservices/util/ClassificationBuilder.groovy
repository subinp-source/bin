/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.impex.jalo.ImpExException
import org.apache.commons.lang.StringUtils

/**
 * A helper class for setting up classifications in test scenarios
 */
class ClassificationBuilder {
    private ClassContext classContext
    private Collection<Attribute> attributes

    private ClassificationBuilder() {
        classContext = new ClassContext()
        attributes = []
    }

    static ClassificationBuilder classification() {
        new ClassificationBuilder()
    }

    ClassificationBuilder withSystem(String system) {
        tap { classContext.classificationSystem = system }
    }

    ClassificationBuilder withVersion(String version) {
        tap { classContext.classificationVersion = version }
    }

    ClassificationBuilder withClassificationClass(String className) {
        tap { classContext.classificationClass = className }
    }

    ClassificationBuilder withAttribute(Attribute attribute) {
        tap { attributes.add attribute }
    }

    void setup() throws ImpExException {
        IntegrationTestUtil.importImpEx(
                'INSERT_UPDATE ClassificationSystem; id[unique = true]',
                "                                  ; $classContext.classificationSystem",
                'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]',
                "                                         ; $classContext.classificationSystem; $classContext.classificationVersion")
        if (StringUtils.isNotBlank(classContext.classificationClass)) {
            IntegrationTestUtil.importImpEx(
                    'INSERT_UPDATE ClassificationClass; code[unique = true] ; catalogVersion(catalog(id), version)[unique = true]',
                    "                                 ; $classContext.classificationClass; ${classContext.systemVersion()}")
        }
        if (!attributes.empty) {
            IntegrationTestUtil.importImpEx(Attribute.buildAttributesImpex(classContext, attributes))
            if (attributes.find {!it.enumValues.empty}) {
                IntegrationTestUtil.importImpEx(Attribute.buildAttributeValuesImpex(classContext, attributes))
            }
            IntegrationTestUtil.importImpEx(Attribute.buildAssignmentImpex(classContext, attributes))
        }
    }

    static void cleanup() {
        IntegrationTestUtil.removeAll ClassAttributeAssignmentModel
        IntegrationTestUtil.removeAll ClassificationAttributeModel
        IntegrationTestUtil.removeAll ClassificationAttributeUnitModel
        IntegrationTestUtil.removeAll ClassificationClassModel
        IntegrationTestUtil.removeAll ClassificationSystemModel
    }

    static Attribute attribute() {
        new Attribute()
    }

    static class Attribute {
        private String attributeName
        private String attributeType
        private String attributeReferencedType
        private boolean mandatory
        private boolean multiValue
        private boolean localized
        private List<String> enumValues = []
        private boolean range

        Attribute withName(String name) {
            tap { attributeName = name }
        }

        Attribute number() {
            attributeType('number')
        }

        Attribute string() {
            attributeType('string')
        }

        Attribute date() {
            attributeType('date')
        }

        Attribute references(String type) {
            attributeType('reference', type)
        }

        Attribute valueList(List<String> values) {
            attributeType('enum', '', values)
        }

        Attribute multiValue() {
            tap { multiValue = true }
        }

        Attribute mandatory() {
            tap { mandatory = true }
        }

        Attribute localized() {
            tap { localized = true }
        }

        Attribute range() {
            tap { range = true }
        }

        private Attribute attributeType(String type, String refType='', List values=[]) {
            tap {
                attributeType = type
                attributeReferencedType = refType
                enumValues = values
            }
        }

        private def flattenValues(ClassContext ctx) {
            enumValues.collect({ "$it:${ctx.systemVersion()}" }).join(',')
        }

        static String[] buildAttributeValuesImpex(ClassContext ctx, Collection<Attribute> attributes) {
            def impex = ['INSERT_UPDATE ClassificationAttributeValue; systemVersion(catalog(id), version)[unique = true]; code[unique = true]']
            impex.addAll attributes
                    .collectMany { it.enumValues }
                    .collect { "                                          ; ${ctx.systemVersion()}; $it" }
            impex.toArray()
        }

        static String[] buildAttributesImpex(ClassContext ctx, Collection<Attribute> attributes) {
            def impex = ['INSERT_UPDATE ClassificationAttribute; code[unique = true]; systemVersion(catalog(id), version)[unique = true]']
            impex.addAll attributes.collect { "                                     ; $it.attributeName ; ${ctx.systemVersion()}" }
            impex.toArray()
        }

        private static String[] buildAssignmentImpex(ClassContext ctx, Collection<Attribute> attributes) {
            def impex = [
                    '$class=classificationClass(catalogVersion(catalog(id), version), code)',
                    '$attribute=classificationAttribute(systemVersion(catalog(id), version), code)',
                    'INSERT_UPDATE ClassAttributeAssignment; $class[unique = true]; $attribute[unique = true]; attributeType(code); referenceType(code); mandatory; multiValued; localized; range; attributeValues(code, systemVersion(catalog(id), version))']
            impex.addAll attributes.collect {
                "                                      ; ${ctx.systemVersionClass()}; ${ctx.systemVersion()}:$it.attributeName; $it.attributeType; $it.attributeReferencedType; $it.mandatory; $it.multiValue; $it.localized; $it.range; ${it.flattenValues(ctx)}"
            }
            impex.toArray()
        }
    }

    private static class ClassContext {
        private static final def DEFAULT_SYSTEM = 'Classifications'
        private static final def DEFAULT_VERSION = 'Test'

        String classificationSystem = DEFAULT_SYSTEM
        String classificationVersion = DEFAULT_VERSION
        String classificationClass

        def systemVersion() {
            "$classificationSystem:$classificationVersion"
        }

        def systemVersionClass() {
            "${systemVersion()}:$classificationClass"
        }
    }
}

