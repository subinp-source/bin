/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata


import de.hybris.platform.catalog.model.classification.ClassificationSystemModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class ClassificationSystemBuilder {

	static final String ID = 'testClassificationSystem'
	static final String NAME = ID

	private String id
	private String name

	static ClassificationSystemBuilder classificationSystem() {
		new ClassificationSystemBuilder()
	}

	ClassificationSystemBuilder withId(String id) {
		this.id = id
		this
	}

	ClassificationSystemBuilder withName(String name) {
		this.name = name
		this
	}

	ClassificationSystemModel build() {
		importImpEx(
				'INSERT_UPDATE ClassificationSystem; id[unique = true]         ; name[lang = en]',
				"; ${deriveId()} ; ${deriveName()}"
		)
		getModel()
	}

	private String deriveId() {
		id ?: ID
	}

	private String deriveName() {
		name ?: NAME
	}

	private ClassificationSystemModel getModel() {
		IntegrationTestUtil.findAny(ClassificationSystemModel, { it.id == deriveId() }).orElse(null)
	}
}
