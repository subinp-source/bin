/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsservices.daos.impl;

import de.hybris.platform.messagecentercsservices.daos.ConversationDao;
import de.hybris.platform.messagecentercsservices.enums.ConversationStatus;
import de.hybris.platform.messagecentercsservices.model.ConversationModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.List;


/**
 * Default implementation of {@link ConversationDao}
 */
public class DefaultConversationDao extends DefaultGenericDao<ConversationModel> implements ConversationDao
{

	private static final String FIND_ASSIGNABLE_CONVERSATIONS = "SELECT {c:" + ConversationModel.PK + "} FROM {"
			+ ConversationModel._TYPECODE + " AS c JOIN " + ConversationStatus._TYPECODE + " AS s ON {c:" + ConversationModel.STATUS
			+ "} = {s:pk} AND {s:code} = '" + ConversationStatus.OPEN.getCode() + "'} WHERE {c:" + ConversationModel.AGENT
			+ "} IS NULL";

	public DefaultConversationDao()
	{
		super(ConversationModel._TYPECODE);
	}


	@Override
	public List<ConversationModel> findUnassignedConversations()
	{
		return getFlexibleSearchService().<ConversationModel> search(FIND_ASSIGNABLE_CONVERSATIONS).getResult();
	}

}
