/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.odata2webservices.odata.builders.meta.payload


import static de.hybris.platform.integrationservices.util.JsonBuilder.json

class ClassificationAttributeAssignmentPayloadBuilder {

    private String attributeName
    private String classificationClass
    private String classificationSystem
    private String classificationVersion
    
    ClassificationAttributeAssignmentPayloadBuilder() {
    }

    static def classificationAttributeAssignment() {
        new ClassificationAttributeAssignmentPayloadBuilder()
    }

    def withAttributeName(String attributeName) {
        this.attributeName = attributeName
        this
    }

    def withClassificationClass(String classificationClass) {
        this.classificationClass = classificationClass
        this
    }

    def withClassificationSystem(String classificationSystem) {
        this.classificationSystem = classificationSystem
        this
    }

    def withClassificationVersion(String classificationVersion) {
        this.classificationVersion = classificationVersion
        this
    }
    
    def build() {
        json()
                .withField("classificationClass", classificationClass())
                .withField("classificationAttribute", classificationAttribute()).build()
    }

    private def classificationClass() {
        json()
                .withField("catalogVersion", classificationSystemVersion())
                .withField("code", classificationClass).build()
    }

    private def classificationAttribute() {
        json()
                .withField("systemVersion", classificationSystemVersion())
                .withField("code", attributeName).build()
    }

    private def classificationSystemVersion() {
        json()
                .withField("catalog", json().withId(classificationSystem))
                .withField("version", classificationVersion).build()
    }
}
