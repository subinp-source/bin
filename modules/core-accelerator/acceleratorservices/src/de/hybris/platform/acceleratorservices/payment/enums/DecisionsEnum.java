/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.enums;

/**
 * This Enum represents the transaction results that may return from the PSP Service.
 */
public enum DecisionsEnum
{
	ACCEPT, //The request succeeded.

	REVIEW, //Decision Manager was triggered and you should review this order. A subscription or profile will not be created when an order is in a review state. You must manually create each one by using the Business Center.

	ERROR, //A system error occurred.

	REJECT, //One or more of the services was declined.

	CANCEL, //Customer cancelled the order.

	PARTIAL //The authorization was partially approved.
}
