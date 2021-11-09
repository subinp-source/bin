/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata

import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class ClassificationSystemVersionBuilder {

	static final String CLASSIFICATION_SYSTEM = classificationSystem()
	static final String VERSION = 'testClassificationSystemVersion'

	private String classificationSystem
	private String version
	private boolean active

	private static String classificationSystem() {
		ClassificationSystemBuilder.classificationSystem().build().getId()
	}

	static ClassificationSystemVersionBuilder classificationSystemVersion() {
		new ClassificationSystemVersionBuilder()
	}

	ClassificationSystemVersionBuilder withVersion(String version) {
		this.version = version
		this
	}

	ClassificationSystemVersionBuilder withClassificationSystem(ClassificationSystemModel classificationSystem) {
		withClassificationSystem(classificationSystem.getId())
		this
	}

	ClassificationSystemVersionBuilder withClassificationSystem(String classificationSystem) {
		this.classificationSystem = classificationSystem
		this
	}

	ClassificationSystemVersionBuilder withActive(boolean active) {
		this.active = active
		this
	}

	ClassificationSystemVersionModel build() {
		importImpEx(
			'INSERT_UPDATE ClassificationSystemVersion; catalog(id)[unique = true]; version[unique = true]    ; active',
			"; ${deriveClassificationSystem()} ; ${deriveVersion()} ; $active"
		)
		getModel()
	}

	private String deriveClassificationSystem() {
		classificationSystem ?: CLASSIFICATION_SYSTEM
	}

	private String deriveVersion() {
		version ?: VERSION
	}

	private ClassificationSystemVersionModel getModel() {
		IntegrationTestUtil.findAny(ClassificationSystemVersionModel, { it.version == deriveVersion() && it.catalog.id == deriveClassificationSystem() }).orElse(null)
	}
}
