/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.odata


import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class ClassificationAttributeUnitBuilder {

	static final String CODE = 'testClassificationAttributeUnit'
	static final String CLASSIFICATION_SYSTEM_VERSION = systemVersion()
	static final String UNIT_TYPE = 'testClassificationAttributeUnitType'
	static final String SYMBOL = 'testClassificationAttributeUnitSymbol'

	private String code
	private String systemVersion
	private String unitType
	private String symbol

	private static String systemVersion() {
		def model = ClassificationSystemVersionBuilder.classificationSystemVersion().build()
		systemVersion(model)
	}

	private static String systemVersion(ClassificationSystemVersionModel model) {
		"$model.catalog.id:$model.version"
	}

	static ClassificationAttributeUnitBuilder classificationAttributeUnit() {
		new ClassificationAttributeUnitBuilder()
	}

	ClassificationAttributeUnitBuilder withCode(String code) {
		this.code = code
		this
	}

	ClassificationAttributeUnitBuilder withSystemVersion(ClassificationSystemVersionModel model) {
		withSystemVersion(systemVersion(model))
		this
	}

	ClassificationAttributeUnitBuilder withSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion
		this
	}

	ClassificationAttributeUnitBuilder withUnitType(String unitType) {
		this.unitType = unitType
		this
	}

	ClassificationAttributeUnitBuilder withSymbol(String symbol) {
		this.symbol = symbol
		this
	}

	ClassificationAttributeUnitModel build() {
		importImpEx(
			'INSERT_UPDATE ClassificationAttributeUnit; code[unique = true]; unitType[unique = true]; systemVersion(catalog(id), version); symbol',
			"; ${deriveCode()}; ${deriveUnitType()}; ${deriveSystemVersion()}; ${deriveSymbol()}"
		)
		getModel()
	}

	private String deriveCode() {
		code ?: CODE
	}

	private String deriveSystemVersion() {
		systemVersion ?: CLASSIFICATION_SYSTEM_VERSION
	}

	private String deriveUnitType() {
		unitType ?: UNIT_TYPE
	}

	private String deriveSymbol() {
		symbol ?: SYMBOL
	}

	private ClassificationAttributeUnitModel getModel() {
		IntegrationTestUtil.findAny(ClassificationAttributeUnitModel, { it.code == deriveCode() && it.unitType == deriveUnitType() }).orElse(null)
	}
}
