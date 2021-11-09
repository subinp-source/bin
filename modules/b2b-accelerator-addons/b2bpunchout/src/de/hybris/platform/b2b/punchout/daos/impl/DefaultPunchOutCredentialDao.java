/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.daos.impl;

import de.hybris.platform.b2b.punchout.daos.PunchOutCredentialDao;
import de.hybris.platform.b2b.punchout.model.PunchOutCredentialModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation for {@link PunchOutCredentialDao}.
 */
public class DefaultPunchOutCredentialDao extends DefaultGenericDao<PunchOutCredentialModel> implements PunchOutCredentialDao
{
	public DefaultPunchOutCredentialDao()
	{
		super(PunchOutCredentialModel._TYPECODE);
	}

	@Override
	public PunchOutCredentialModel getPunchOutCredential(final String domain, final String identity)
			throws AmbiguousIdentifierException
	{
		final Map<String, String> params = new HashMap<>();
		params.put(PunchOutCredentialModel.DOMAIN, domain);
		params.put(PunchOutCredentialModel.IDENTITY, identity);
		final List<PunchOutCredentialModel> resList = find(params);
		if (resList.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + resList.size() + " PunchOut Credentials with domain '" + domain
					+ "' and indentity '" + identity + "'");
		}
		return resList.isEmpty() ? null : resList.get(0);
	}
}
