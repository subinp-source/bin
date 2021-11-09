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
import de.hybris.platform.commerceservices.event.AnonymousConsentChangeEvent;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.personalizationyprofile.strategy.impl.DefaultCxProfileIdentifierStrategy;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CxCdsAnonymousConsentChangeEventListenerTest
{
	private static final String SAMPLE_TEMPLATE_ID = "template";

	private CxCdsAnonymousConsentChangeEventListener listener;

	@Mock
	private DefaultCxProfileIdentifierStrategy defaultCxProfileIdentifierStrategy;

	@Mock
	private UserService userService;

	private CustomerModel anonymousUser;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		listener = new CxCdsAnonymousConsentChangeEventListener();
		listener.setDefaultCxProfileIdentifierStrategy(defaultCxProfileIdentifierStrategy);
		listener.setUserService(userService);

		when(userService.getAnonymousUser()).thenReturn(anonymousUser);
	}

	@Test
	public void testNull()
	{
		//given
		final AnonymousConsentChangeEvent event = null;

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(0)).resetProfileIdentifier(any());
	}

	@Test
	public void testNotGivenConsent()
	{
		//given
		final AnonymousConsentChangeEvent event = getEvent(PROFILE_CONSENT, null);

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(1)).resetProfileIdentifier(anonymousUser);
	}

	@Test
	public void testWithdrawnConsent()
	{
		//given
		final AnonymousConsentChangeEvent event = getEvent(PROFILE_CONSENT, false);

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(1)).resetProfileIdentifier(anonymousUser);
	}

	@Test
	public void testGivenConsent()
	{
		//given
		final AnonymousConsentChangeEvent event = getEvent(PROFILE_CONSENT, true);

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(0)).resetProfileIdentifier(any());
	}

	@Test
	public void testDifferentConsentWithdrawn()
	{
		//given
		final AnonymousConsentChangeEvent event = getEvent(SAMPLE_TEMPLATE_ID, false);

		//when
		listener.onApplicationEvent(event);

		//then
		verify(defaultCxProfileIdentifierStrategy, times(0)).resetProfileIdentifier(any());
	}


	AnonymousConsentChangeEvent getEvent(final String templateId, final Boolean given)
	{
		if (given == null)
		{
			return new AnonymousConsentChangeEvent(templateId, null, null, java.util.Collections.emptyMap());
		}
		else if (given)
		{
			return new AnonymousConsentChangeEvent(templateId, "", "GIVEN", java.util.Collections.emptyMap());
		}
		else
		{
			return new AnonymousConsentChangeEvent(templateId, "GIVEN", "WITHDRAWN", java.util.Collections.emptyMap());

		}
	}
}
