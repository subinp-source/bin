/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservicesfeaturetests.ws;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;

public class InboundChannelConfigurationBuilder
{
	private String code;
	private AuthenticationType authType;

	public static InboundChannelConfigurationBuilder inboundChannelConfigurationBuilder()
	{
		return new InboundChannelConfigurationBuilder();
	}

	public InboundChannelConfigurationBuilder withCode(final String code)
	{
		this.code = code;
		return this;
	}

	public InboundChannelConfigurationBuilder withAuthType(final AuthenticationType authType)
	{
		this.authType = authType;
		return this;
	}

	public void build() throws ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE InboundChannelConfiguration; integrationObject(code)[unique = true]; authenticationType(code)",
			String.format("                           ; %s                                    ; %s                       ",
						this.code, this.authType));
	}
}
