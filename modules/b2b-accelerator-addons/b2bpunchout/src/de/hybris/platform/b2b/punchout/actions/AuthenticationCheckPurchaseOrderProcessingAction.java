/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.actions;

import de.hybris.platform.core.model.order.CartModel;

import org.cxml.CXML;


/**
 * Verifies the authentication for the given input {@link CXML}.
 */
public class AuthenticationCheckPurchaseOrderProcessingAction implements PunchOutProcessingAction<CXML, CartModel>
{

	private AuthenticationVerifier punchoutAuthenticationVerifier;

	@Override
	public void process(final CXML input, final CartModel output)
	{
		getPunchOutAuthenticationVerifier().verify(input);
	}

	public AuthenticationVerifier getPunchOutAuthenticationVerifier()
	{
		return punchoutAuthenticationVerifier;
	}

	public void setPunchOutAuthenticationVerifier(final AuthenticationVerifier punchoutAuthenticationVerifier)
	{
		this.punchoutAuthenticationVerifier = punchoutAuthenticationVerifier;
	}

}
