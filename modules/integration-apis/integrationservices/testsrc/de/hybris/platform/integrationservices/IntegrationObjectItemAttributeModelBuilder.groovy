/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices

import com.google.common.base.Preconditions
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.util.IntegrationTestUtil

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class IntegrationObjectItemAttributeModelBuilder {
	private String name
	private IntegrationObjectItemModel integrationObjectItem
	private IntegrationObjectItemModel returnIntegrationObjectItem
	private Boolean unique = false

	static IntegrationObjectItemAttributeModelBuilder integrationObjectItemAttribute() {
		return new IntegrationObjectItemAttributeModelBuilder()
	}

	IntegrationObjectItemAttributeModelBuilder withItem(final IntegrationObjectItemModel integrationObjectItem) {
		this.integrationObjectItem = integrationObjectItem
		return this
	}

	IntegrationObjectItemAttributeModelBuilder withName(final String name) {
		this.name = name
		return this
	}

	IntegrationObjectItemAttributeModelBuilder withReturnItem(final IntegrationObjectItemModel returnIntegrationObjectItem) {
		this.returnIntegrationObjectItem = returnIntegrationObjectItem
		return this
	}

	IntegrationObjectItemAttributeModelBuilder asUnique() {
		unique = true
		this
	}

	IntegrationObjectItemAttributeModel build() throws ImpExException {
		Preconditions.checkArgument(this.name != null, "name cannot be null")
		Preconditions.checkArgument(this.integrationObjectItem != null, "integrationObjectItem cannot be null")

		createIntegrationObjectItemAttribute(integrationObjectItem, name, unique)
	}

	private static IntegrationObjectItemAttributeModel createIntegrationObjectItemAttribute(final IntegrationObjectItemModel item, final String attributeName, final Boolean isUnique) throws ImpExException {
		def integrationItem = item.integrationObject.code + ":" + item.code
		def descriptor = item.code + ":" + attributeName
		importImpEx(
				'$integrationItem = integrationObjectItem(integrationObject(code), code)[unique = true]',
				'$attributeName = attributeName[unique = true]',
				'$attributeDescriptor = attributeDescriptor(enclosingType(code), qualifier)',
				'INSERT_UPDATE IntegrationObjectItemAttribute ; $integrationItem ; $attributeName ; $attributeDescriptor ; unique',
				"                                             ; $integrationItem ; $attributeName ; $descriptor          ; $isUnique"
		)
		findIntegrationObjectItemAttribute(item, attributeName)
	}

	private static IntegrationObjectItemAttributeModel findIntegrationObjectItemAttribute(final IntegrationObjectItemModel integrationObjectItem, final String attributeName) {
		return IntegrationTestUtil.findAny(IntegrationObjectItemAttributeModel, {
			it.integrationObjectItem == integrationObjectItem && it.attributeName == attributeName
		}).orElse(null)
	}
}
