/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationyprofile.event;

import static com.hybris.yprofile.constants.ProfileservicesConstants.PROFILE_CONSENT;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.event.ConsentWithdrawnEvent;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.commerceservices.model.consent.ConsentTemplateModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.personalizationyprofile.strategy.impl.DefaultCxProfileIdentifierStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@UnitTest
public class CxCdsConsentWithdrawnEventListenerTest
{

	private CxCdsConsentWithdrawnEventListener listener;

	@Mock
	private DefaultCxProfileIdentifierStrategy defaultCxProfileIdentifierStrategy;

	private ConsentModel consentModel;

	@Mock
	private ConsentTemplateModel templateModel;

	@Mock
	private CustomerModel customer;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		listener = new CxCdsConsentWithdrawnEventListener();
		listener.setDefaultCxProfileIdentifierStrategy(defaultCxProfileIdentifierStrategy);

		consentModel = new ConsentModel();
		consentModel.setConsentTemplate(templateModel);
		consentModel.setCustomer(customer);
	}

	@Test
	public void testNull()
	{
		//given
		final ConsentWithdrawnEvent event = null;

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(0)).resetProfileIdentifier(any());
	}


	@Test
	public void testNoData()
	{
		//given
		final ConsentWithdrawnEvent event = new ConsentWithdrawnEvent();

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(0)).resetProfileIdentifier(any());
	}

	@Test
	public void testWithdrawnConsent()
	{
		//given
		final ConsentWithdrawnEvent event = getEvent();
		when(templateModel.getId()).thenReturn(PROFILE_CONSENT);

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(1)).resetProfileIdentifier(customer);
	}

	@Test
	public void testWithdrawnDifferentConsent()
	{
		//given
		final ConsentWithdrawnEvent event = getEvent();
		when(templateModel.getId()).thenReturn("anotherConsentTemplate");

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(0)).resetProfileIdentifier(any());
	}

	private ConsentWithdrawnEvent getEvent()
	{
		final ConsentWithdrawnEvent event = new ConsentWithdrawnEvent();
		event.setConsent(consentModel);
		return event;
	}
}
