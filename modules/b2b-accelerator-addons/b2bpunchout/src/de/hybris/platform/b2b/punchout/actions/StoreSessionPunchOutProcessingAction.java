/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.actions;

import de.hybris.platform.b2b.punchout.services.PunchOutSessionService;

import org.cxml.CXML;
import org.springframework.beans.factory.annotation.Required;


/**
 * This implementation of {@link PunchOutProcessingAction} is meant to populate the PunchOut Setup Response.
 */
public class StoreSessionPunchOutProcessingAction implements PunchOutProcessingAction<CXML, CXML>
{

	private PunchOutSessionService punchOutSessionService;

	@Override
	public void process(final CXML input, final CXML output)
	{
		punchOutSessionService.saveCurrentPunchoutSession();
	}

	public PunchOutSessionService getPunchOutSessionService()
	{
		return punchOutSessionService;
	}

	@Required
	public void setPunchOutSessionService(final PunchOutSessionService punchOutSessionService)
	{
		this.punchOutSessionService = punchOutSessionService;
	}


}
