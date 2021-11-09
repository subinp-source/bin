/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.handlers.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.personalizationservices.constants.PersonalizationservicesConstants;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.personalizationservices.voters.Voter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

import jersey.repackaged.com.google.common.collect.Lists;


@UnitTest
public class DefaultPersonalizationHandlerTest
{
	DefaultPersonalizationHandler personalizationHandler;

	Voter ignoreVoter, recalculateVoter, asyncVoter, loadVoter, updateVoter, multipleActionVoter;

	HttpServletRequest request;
	HttpServletResponse response;

	@Mock
	private UserModel currentUser;
	@Mock
	private Collection<CatalogVersionModel> sessionCatalogVersions;

	@Mock
	private CxRecalculationService recalculationService;
	@Mock
	private UserService userService;
	@Mock
	private CxService cxService;
	@Mock
	private CatalogVersionService catalogVersionService;
	@Mock
	private ModelService modelService;
	@Mock
	private SessionService sessionService;

	@Mock
	private CxConsentService cxConsentService;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		personalizationHandler = new DefaultPersonalizationHandler();
		personalizationHandler.setCxRecalculationService(recalculationService);
		personalizationHandler.setSessionService(sessionService);
		personalizationHandler.setCxConsentService(cxConsentService);

		ignoreVoter = mock(Voter.class);
		recalculateVoter = mock(Voter.class);
		loadVoter = mock(Voter.class);
		updateVoter = mock(Voter.class);
		asyncVoter = mock(Voter.class);
		multipleActionVoter = mock(Voter.class);


		given(userService.getCurrentUser()).willReturn(currentUser);
		given(catalogVersionService.getSessionCatalogVersions()).willReturn(sessionCatalogVersions);

		when(cxConsentService.userHasActiveConsent()).thenReturn(true);

		when(recalculateVoter.getVote(any(), any())).thenReturn(buildVote(false, RecalculateAction.RECALCULATE));
		when(asyncVoter.getVote(any(), any())).thenReturn(buildVote(false, RecalculateAction.ASYNC_PROCESS));
		when(loadVoter.getVote(any(), any())).thenReturn(buildVote(false, RecalculateAction.LOAD));
		when(updateVoter.getVote(any(), any())).thenReturn(buildVote(false, RecalculateAction.UPDATE));
		when(ignoreVoter.getVote(any(), any())).thenReturn(buildVote(false, RecalculateAction.IGNORE));

	}

	Vote buildVote(final boolean conclusive, final RecalculateAction... actions)
	{
		final Vote result = new Vote();
		result.setConclusive(conclusive);
		result.setRecalculateActions(Sets.newHashSet(actions));
		return result;
	}

	List<Voter> buildVoters(final Voter... voters)
	{
		return Lists.newArrayList(voters);
	}

	@Test
	public void testActivatePersonalization() throws Exception
	{
		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(sessionService, times(1)).setAttribute(PersonalizationservicesConstants.ACTIVE_PERSONALIZATION, Boolean.TRUE);
	}

	@Test
	public void testAsyncVote() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(asyncVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.ASYNC_PROCESS)));
	}

	@Test
	public void testAsyncVoteWithoutConsent() throws Exception
	{
		//given
		when(cxConsentService.userHasActiveConsent()).thenReturn(false);
		personalizationHandler.setVoters(buildVoters(asyncVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(List.of()));
	}

	@Test
	public void testIgnoreVote() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(ignoreVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(0)).recalculate(any());
	}

	@Test
	public void testRecalculateVote() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(recalculateVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.RECALCULATE)));
	}

	@Test
	public void testUpdateVote() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(updateVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.UPDATE)));
	}

	@Test
	public void testUpdateVoteWithoutConsent() throws Exception
	{
		//given
		when(cxConsentService.userHasActiveConsent()).thenReturn(false);
		personalizationHandler.setVoters(buildVoters(updateVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(List.of()));
	}

	@Test
	public void testLoadVote() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(loadVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.LOAD)));
	}

	@Test
	public void testVoteMerging() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(asyncVoter, recalculateVoter, updateVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate((List<RecalculateAction>) argThat(
				containsInAnyOrder(RecalculateAction.ASYNC_PROCESS, RecalculateAction.RECALCULATE, RecalculateAction.UPDATE)));
	}

	@Test
	public void testVoteMergingWithoutConsent() throws Exception
	{
		//given
		when(cxConsentService.userHasActiveConsent()).thenReturn(false);
		personalizationHandler.setVoters(buildVoters(asyncVoter, recalculateVoter, updateVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(List.of(RecalculateAction.RECALCULATE)));
	}

	@Test
	public void testMultipleVote() throws Exception
	{
		//given
		when(multipleActionVoter.getVote(any(), any()))
				.thenReturn(buildVote(true, RecalculateAction.RECALCULATE, RecalculateAction.UPDATE));
		personalizationHandler.setVoters(buildVoters(multipleActionVoter, asyncVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(0)).recalculate(
				eq(Arrays.asList(RecalculateAction.UPDATE, RecalculateAction.RECALCULATE, RecalculateAction.ASYNC_PROCESS)));
	}

	@Test
	public void testIgnoreVoteMerging() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(recalculateVoter, ignoreVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(0)).recalculate(any());
	}

	@Test
	public void testFinalVoteMerging() throws Exception
	{
		//given
		when(recalculateVoter.getVote(any(), any())).thenReturn(buildVote(true, RecalculateAction.RECALCULATE));
		personalizationHandler.setVoters(buildVoters(recalculateVoter, ignoreVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.RECALCULATE)));
	}

	@Test
	public void testFinalVoteOrder() throws Exception
	{
		//given
		when(recalculateVoter.getVote(any(), any())).thenReturn(buildVote(true, RecalculateAction.RECALCULATE));
		when(ignoreVoter.getVote(any(), any())).thenReturn(buildVote(true, RecalculateAction.IGNORE));
		personalizationHandler.setVoters(buildVoters(recalculateVoter, ignoreVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.RECALCULATE)));
	}


	@Test
	public void testActionListOptimization() throws Exception
	{
		//given
		personalizationHandler.setVoters(buildVoters(recalculateVoter, loadVoter));

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(recalculationService, times(1)).recalculate(eq(Collections.singletonList(RecalculateAction.RECALCULATE)));
	}
}
