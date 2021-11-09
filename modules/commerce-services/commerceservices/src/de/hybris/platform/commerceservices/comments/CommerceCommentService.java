/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.comments;

import de.hybris.platform.commerceservices.service.data.CommerceCommentParameter;


/**
 * Commerce comment service.
 */
public interface CommerceCommentService
{

	/**
	 * Adds a comment to an item.
	 *
	 * @param parameter The comment parameter with comment data to be added.
	 */
	void addComment(final CommerceCommentParameter parameter);
}
