/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


public interface TypedCriteria
{
	boolean containsAnySupportedType();

	Optional<Identity> getRootId();

	Map<SingleAttributeKey, Object> getRootUniqueParams();

	Optional<Identity> getEntityId();

	Set<Identity> getSupportedTypes();

	boolean onlyRootRequested();
}
