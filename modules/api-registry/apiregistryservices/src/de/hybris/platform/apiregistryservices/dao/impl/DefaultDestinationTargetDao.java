/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao.impl;

import de.hybris.platform.apiregistryservices.dao.DestinationTargetDao;
import de.hybris.platform.apiregistryservices.model.AbstractCredentialModel;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;


/**
 * Default implementation of {@link DestinationTargetDao}
 */
public class DefaultDestinationTargetDao extends AbstractItemDao implements DestinationTargetDao
{
	protected static final String CREDENTIAL_ID = "credentialId";
	protected static final String FIND_DESTINATIONTARGET_BY_CREDENTIAL =
			"SELECT {" + DestinationTargetModel.PK + "} FROM {" + DestinationTargetModel._TYPECODE + " AS dtm JOIN "
					+ AbstractDestinationModel._TYPECODE + " AS adm ON {dtm:" + DestinationTargetModel.PK + "}={adm:"
					+ AbstractDestinationModel.DESTINATIONTARGET + "} JOIN " + AbstractCredentialModel._TYPECODE + " AS acm ON {adm:"
					+ AbstractDestinationModel.CREDENTIAL + "}={acm:" + AbstractCredentialModel.PK + "}} WHERE {acm:"
					+ AbstractCredentialModel.ID + "}=?" + CREDENTIAL_ID;

	protected static final String ID = "uid";

	protected static final String FIND_DESTINATIONTARGET_BY_ID = "SELECT {" + DestinationTargetModel.PK + "} FROM {"
			+ DestinationTargetModel._TYPECODE + " } WHERE {" + DestinationTargetModel.ID + "}=?" + ID;


	@Override
	public DestinationTargetModel findDestinationTargetByCredentialId(final String credentialId)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_DESTINATIONTARGET_BY_CREDENTIAL);
		query.addQueryParameter(CREDENTIAL_ID, credentialId);

		final SearchResult<DestinationTargetModel> result = search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public DestinationTargetModel findDestinationTargetById(final String id)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_DESTINATIONTARGET_BY_ID);
		query.addQueryParameter(ID, id);

		return searchUnique(query);
	}
}
