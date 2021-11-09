/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.daos;

import de.hybris.platform.b2b.punchout.jalo.PunchOutCredential;
import de.hybris.platform.b2b.punchout.model.PunchOutCredentialModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;


/**
 * DAO for entity {@link PunchOutCredential}.
 */
public interface PunchOutCredentialDao
{
	/**
	 * Get a {@link PunchOutCredentialModel} for a given domain-identity pair.
	 * 
	 * @param domain
	 *           The PunchOut domain used for the identity (e.g.: DUNS)
	 * @param identity
	 *           The identity value.
	 * @return The {@link PunchOutCredentialModel}, or null, if there is no matching pair.
	 * @throws AmbiguousIdentifierException
	 *            If there is more the one {@link PunchOutCredentialModel} for the given pair.
	 */
	PunchOutCredentialModel getPunchOutCredential(final String domain, final String identity) throws AmbiguousIdentifierException;
}
