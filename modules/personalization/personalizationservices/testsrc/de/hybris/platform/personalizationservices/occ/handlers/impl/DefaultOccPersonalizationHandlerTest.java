/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationservices.occ.handlers.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.config.CxConfigModel;
import de.hybris.platform.personalizationservices.occ.CxOccAttributesStrategy;
import de.hybris.platform.personalizationservices.occ.CxOccInterceptor;
import de.hybris.platform.personalizationservices.occ.voters.CxOccVoter;
import de.hybris.platform.personalizationservices.service.CxRecalculationService;
import de.hybris.platform.personalizationservices.voters.CxOccInterceptorVote;
import de.hybris.platform.personalizationservices.voters.Vote;
import de.hybris.platform.servicelayer.session.MockSessionService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSessionTokenService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.Validator;

import com.google.common.collect.Sets;

import jersey.repackaged.com.google.common.collect.Lists;


@UnitTest
public class DefaultOccPersonalizationHandlerTest
{
	@Mock
	private DefaultSessionTokenService tokenService;
	@Mock
	private CxConfigurationService cxConfigurationService;
	@Mock
	private CxRecalculationService cxRecalculationService;
	@Mock
	private CxOccAttributesStrategy cxOccAttributesStrategy;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private Validator personalizationIdValidator;

	private SessionService sessionService;
	@Mock
	private CxOccVoter cxOccRecalculateVoter;
	@Mock
	private CxOccInterceptor conclusiveYesVoteInterceptor, conclusiveNoVoteInterceptor, yesVoteInterceptor, noVoteInterceptor,
			nullVoteInterceptor;

	private DefaultOccPersonalizationHandler personalizationHandler;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		sessionService = new MockSessionService();

		personalizationHandler = new DefaultOccPersonalizationHandler();
		personalizationHandler.setCxConfigurationService(cxConfigurationService);
		personalizationHandler.setTokenService(tokenService);
		personalizationHandler.setCxRecalculationService(cxRecalculationService);
		personalizationHandler.setCxOccAttributesStrategy(cxOccAttributesStrategy);
		personalizationHandler.setPersonalizationIdValidator(personalizationIdValidator);
		personalizationHandler.setSessionService(sessionService);

		final List<CxOccInterceptor> interceptors = Collections.emptyList();
		personalizationHandler.setInterceptors(interceptors);

		when(conclusiveYesVoteInterceptor.shouldPersonalizeRequestVote(any())).thenReturn(buildInterceptorVote(true, true));
		when(conclusiveNoVoteInterceptor.shouldPersonalizeRequestVote(any())).thenReturn(buildInterceptorVote(true, false));
		when(yesVoteInterceptor.shouldPersonalizeRequestVote(any())).thenReturn(buildInterceptorVote(false, true));
		when(noVoteInterceptor.shouldPersonalizeRequestVote(any())).thenReturn(buildInterceptorVote(false, false));
		when(nullVoteInterceptor.shouldPersonalizeRequestVote(any())).thenReturn(null);

		when(cxOccRecalculateVoter.getVote(any())).thenReturn(buildVote(false, RecalculateAction.RECALCULATE));

		final CxConfigModel configModel = new CxConfigModel();
		configModel.setOccPersonalizationEnabled(true);

		doReturn(Optional.of(configModel)).when(cxConfigurationService).getConfiguration();

		when(cxOccAttributesStrategy.readPersonalizationId(any())).thenReturn(Optional.of(""));
	}

	@Test
	public void testHandlePersonalizationForEmptyInterceptorList() throws ServletException, IOException
	{
		//given
		final List<CxOccInterceptor> interceptors = Collections.emptyList();
		personalizationHandler.setInterceptors(interceptors);
		final List<CxOccVoter> cxOccVoters = Collections.singletonList(cxOccRecalculateVoter);
		personalizationHandler.setVoters(cxOccVoters);

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(cxRecalculationService, atMost(1))
				.recalculate(Lists.newArrayList(RecalculateAction.RECALCULATE, RecalculateAction.LOAD));
	}

	@Test
	public void testHandlePersonalizationWithNoVoteInterceptor() throws ServletException, IOException
	{
		//given
		final List<CxOccInterceptor> interceptors = Collections.singletonList(noVoteInterceptor);
		personalizationHandler.setInterceptors(interceptors);
		final List<CxOccVoter> cxOccVoters = Collections.singletonList(cxOccRecalculateVoter);
		personalizationHandler.setVoters(cxOccVoters);

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(cxRecalculationService, atMost(1)).recalculate(Collections.singletonList(RecalculateAction.LOAD));
	}

	@Test
	public void testHandlePersonalizationForYesVoteInterceptor() throws ServletException, IOException
	{
		//given
		final List<CxOccInterceptor> interceptors = Collections.singletonList(yesVoteInterceptor);
		personalizationHandler.setInterceptors(interceptors);
		final List<CxOccVoter> cxOccVoters = Collections.singletonList(cxOccRecalculateVoter);
		personalizationHandler.setVoters(cxOccVoters);

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(cxRecalculationService, atMost(1))
				.recalculate(Lists.newArrayList(RecalculateAction.RECALCULATE, RecalculateAction.LOAD));
	}

	@Test
	public void testHandlePersonalizationForNullVoteInterceptor() throws ServletException, IOException
	{
		//given
		final List<CxOccInterceptor> interceptors = Lists.newArrayList(yesVoteInterceptor, nullVoteInterceptor);
		personalizationHandler.setInterceptors(interceptors);
		final List<CxOccVoter> cxOccVoters = Collections.singletonList(cxOccRecalculateVoter);
		personalizationHandler.setVoters(cxOccVoters);

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(cxRecalculationService, atMost(1))
				.recalculate(Lists.newArrayList(RecalculateAction.RECALCULATE, RecalculateAction.LOAD));
	}

	@Test
	public void testHandlePersonalizationForConclusiveYesVoteInterceptor() throws ServletException, IOException
	{
		//given
		final List<CxOccInterceptor> interceptors = Lists.newArrayList(yesVoteInterceptor, noVoteInterceptor,
				conclusiveYesVoteInterceptor);
		personalizationHandler.setInterceptors(interceptors);
		final List<CxOccVoter> cxOccVoters = Collections.singletonList(cxOccRecalculateVoter);
		personalizationHandler.setVoters(cxOccVoters);

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(cxRecalculationService, atMost(1))
				.recalculate(Lists.newArrayList(RecalculateAction.RECALCULATE, RecalculateAction.LOAD));
	}

	@Test
	public void testHandlePersonalizationForConclusiveNoVoteInterceptor() throws ServletException, IOException
	{
		//given
		final List<CxOccInterceptor> interceptors = Lists.newArrayList(yesVoteInterceptor, conclusiveNoVoteInterceptor);
		personalizationHandler.setInterceptors(interceptors);
		final List<CxOccVoter> cxOccVoters = Collections.singletonList(cxOccRecalculateVoter);
		personalizationHandler.setVoters(cxOccVoters);

		//when
		personalizationHandler.handlePersonalization(request, response);

		//then
		verify(cxRecalculationService, atMost(1)).recalculate(Collections.singletonList(RecalculateAction.LOAD));
	}

	private Vote buildVote(final boolean conclusive, final RecalculateAction... actions)
	{
		final Vote result = new Vote();
		result.setConclusive(conclusive);
		result.setRecalculateActions(Sets.newHashSet(actions));
		return result;
	}

	private CxOccInterceptorVote buildInterceptorVote(final boolean conclusive, final boolean vote)
	{
		final CxOccInterceptorVote result = new CxOccInterceptorVote();
		result.setConclusive(conclusive);
		result.setVote(vote);
		return result;
	}
}
