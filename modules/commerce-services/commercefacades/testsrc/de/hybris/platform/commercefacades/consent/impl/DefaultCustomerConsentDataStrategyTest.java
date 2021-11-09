/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.consent.ConsentFacade;
import de.hybris.platform.commercefacades.consent.data.ConsentData;
import de.hybris.platform.commercefacades.consent.data.ConsentTemplateData;
import de.hybris.platform.commercefacades.consent.impl.DefaultCustomerConsentDataStrategy;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static de.hybris.platform.commercefacades.constants.CommerceFacadesConstants.USER_CONSENTS;
import static de.hybris.platform.testframework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@UnitTest
public class DefaultCustomerConsentDataStrategyTest
{
	private static final String WITHDRAWN = "WITHDRAWN";
	private static final String GIVEN = "GIVEN";
	public static final int EXPECTED_CONSENT_MAP_SIZE = 4;
	@Mock
	private SessionService sessionService;
	@Mock
	private ConsentFacade consentFacade;
	@InjectMocks
	private DefaultCustomerConsentDataStrategy defaultCustomerConsentDataStrategy;

	@Before
	public void setUp() throws IOException
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldPopulateCustomerConsentDataInSessionWhenUserLogsIn() throws Exception
	{
		//given
		final String template1 = "templateCode1", template2 = "templateCode2", template3 = "templateCode3", template4 = "templateCode4", //
				template5 = "templateCode5", template6 = "templateCode6";
		final ConsentData consentData = buildConsentData();
		final ConsentData consentDataWithWithdrawnDate = buildConsentDataWithWithdrawnDate();
		final List<ConsentTemplateData> consentTemplatesData = Arrays.asList(ConsentTemplateDataBuilder.aConsentTemplateData() //
						.withId(template1).withVersion(1).withExposed(true).build(), //
				ConsentTemplateDataBuilder.aConsentTemplateData() //
						.withId(template2).withVersion(1).withExposed(true).build(), //
				ConsentTemplateDataBuilder.aConsentTemplateData() //
						.withId(template3).withVersion(1).withConsentData(consentData).withExposed(true).build(), //
				ConsentTemplateDataBuilder.aConsentTemplateData() //
						.withId(template4).withVersion(1).withConsentData(consentData).withExposed(false).build(), //
				ConsentTemplateDataBuilder.aConsentTemplateData() //
						.withId(template5).withVersion(1).withConsentData(consentDataWithWithdrawnDate).withExposed(true).build(), //
				ConsentTemplateDataBuilder.aConsentTemplateData() //
						.withId(template6).withVersion(1).withConsentData(consentDataWithWithdrawnDate).withExposed(false).build());

		given(consentFacade.getConsentTemplatesWithConsents()).willReturn(consentTemplatesData);

		//when
		defaultCustomerConsentDataStrategy.populateCustomerConsentDataInSession();

		//then
		final ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verify(sessionService).setAttribute(Mockito.eq(USER_CONSENTS), captor.capture());
		final Map consentMap = captor.getValue();

		assertEquals(EXPECTED_CONSENT_MAP_SIZE, consentMap.size());
		assertEquals(WITHDRAWN, consentMap.get(template1));
		assertEquals(WITHDRAWN, consentMap.get(template2));
		assertEquals(GIVEN, consentMap.get(template3));
		assertEquals(WITHDRAWN, consentMap.get(template5));
	}

	private ConsentData buildConsentData()
	{
		final ConsentData consentData = new ConsentData();
		consentData.setConsentGivenDate(new Date());
		return consentData;
	}

	private ConsentData buildConsentDataWithWithdrawnDate()
	{
		final ConsentData consentData = new ConsentData();
		consentData.setConsentGivenDate(new Date());
		consentData.setConsentWithdrawnDate(new Date());
		return consentData;
	}

	private static class ConsentTemplateDataBuilder
	{
		private String id;

		private String name;

		private String description;

		private int version;

		private boolean exposed;

		private ConsentData consentData;

		private ConsentTemplateDataBuilder()
		{
		}

		public static ConsentTemplateDataBuilder aConsentTemplateData()
		{
			return new ConsentTemplateDataBuilder();
		}

		public ConsentTemplateDataBuilder withId(final String id)
		{
			this.id = id;
			return this;
		}

		public ConsentTemplateDataBuilder withName(final String name)
		{
			this.name = name;
			return this;
		}

		public ConsentTemplateDataBuilder withDescription(final String description)
		{
			this.description = description;
			return this;
		}

		public ConsentTemplateDataBuilder withVersion(final int version)
		{
			this.version = version;
			return this;
		}

		public ConsentTemplateDataBuilder withExposed(final boolean exposed)
		{
			this.exposed = exposed;
			return this;
		}

		public ConsentTemplateDataBuilder withConsentData(final ConsentData consentData)
		{
			this.consentData = consentData;
			return this;
		}

		public ConsentTemplateData build()
		{
			final ConsentTemplateData consentTemplateData = new ConsentTemplateData();
			consentTemplateData.setId(id);
			consentTemplateData.setName(name);
			consentTemplateData.setDescription(description);
			consentTemplateData.setVersion(Integer.valueOf(version));
			consentTemplateData.setExposed(exposed);
			consentTemplateData.setConsentData(consentData);
			return consentTemplateData;
		}
	}
}
