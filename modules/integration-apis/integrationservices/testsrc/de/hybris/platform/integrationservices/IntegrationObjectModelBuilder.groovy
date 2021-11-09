/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices

import com.google.common.base.Preconditions
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil

class IntegrationObjectModelBuilder {
	private String code

	/**
	 * Get a new instance of the builder
	 *
	 * @return IntegrationObjectModelBuilder
	 */
	static IntegrationObjectModelBuilder integrationObject() {
		return new IntegrationObjectModelBuilder()
	}

	IntegrationObjectModelBuilder withCode(final String code) {
		this.code = code
		return this
	}

	/**
	 * Each time build() is called, a new instance of the the {@link de.hybris.platform.integrationservices.model.IntegrationObjectModel} is returned
	 * with the same properties that were set.
	 *
	 * @return IntegrationObjectModel
	 */
	IntegrationObjectModel build() throws ImpExException {
		Preconditions.checkArgument(this.code != null, "code cannot be null")
		return IntegrationObjectTestUtil.createIntegrationObject(code)
	}
}
