/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices

import com.google.common.base.Preconditions
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil

class IntegrationObjectItemModelBuilder {
	private String code
	private String type
	private Boolean root = false
	private IntegrationObjectModel integrationObject
	private final Set<IntegrationObjectItemAttributeModelBuilder> attributes = new HashSet<>()

	static IntegrationObjectItemModelBuilder integrationObjectItem() {
		return new IntegrationObjectItemModelBuilder()
	}

	IntegrationObjectItemModelBuilder withCode(final String code) {
		this.code = code
		return this
	}

	IntegrationObjectItemModelBuilder withIntegrationObject(final IntegrationObjectModel integrationObject) {
		this.integrationObject = integrationObject
		return this
	}

	IntegrationObjectItemModelBuilder withType(final String type) {
		this.type = type
		this
	}

	IntegrationObjectItemModelBuilder asRoot() {
		this.root = true
		return this
	}

	IntegrationObjectItemModelBuilder withAttribute(final IntegrationObjectItemAttributeModelBuilder attr)
	{
		attributes.add(attr)
		return this
	}

	IntegrationObjectItemModel build() throws ImpExException {
		Preconditions.checkArgument(this.code != null, "code cannot be null")
		Preconditions.checkArgument(this.integrationObject != null, "integrationObject cannot be null")
		final IntegrationObjectItemModel item = IntegrationObjectTestUtil.createIntegrationObjectItem(integrationObject, code, root)
		for (IntegrationObjectItemAttributeModelBuilder attributeBuilder: attributes) {
			attributeBuilder.withItem(item).build()
		}
		item
	}
}
