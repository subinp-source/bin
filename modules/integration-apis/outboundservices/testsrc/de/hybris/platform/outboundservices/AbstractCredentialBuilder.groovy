/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices

import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel

abstract class AbstractCredentialBuilder<BUILDER, CREDENTIAL extends AbstractCredentialModel> {

	String id
	String password

	BUILDER withId(String id) {
		this.id = id
		(BUILDER) this
	}

	BUILDER withPassword(String password) {
		this.password = password
		(BUILDER) this
	}

	abstract CREDENTIAL build();
}
