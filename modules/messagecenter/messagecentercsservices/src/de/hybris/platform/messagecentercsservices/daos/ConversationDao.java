/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsservices.daos;

import de.hybris.platform.messagecentercsservices.model.ConversationModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;


/**
 * DAO to provide methods to query for conversation.
 */
public interface ConversationDao extends GenericDao<ConversationModel>
{

	/**
	 * Find Unassigned conversations for agent
	 *
	 * @return the list of ConversationModel
	 */
	List<ConversationModel> findUnassignedConversations();

}
