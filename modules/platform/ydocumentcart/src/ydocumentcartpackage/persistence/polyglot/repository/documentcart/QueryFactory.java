/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.model.Identity;


public interface QueryFactory
{

	Query getQuery(Identity id);

	Query getQuery(Entity entity);

	Query getQuery(EntityCreation creation);

	Query getQuery(EntityModification modification);

	Query getQuery(Criteria criteria);

}
