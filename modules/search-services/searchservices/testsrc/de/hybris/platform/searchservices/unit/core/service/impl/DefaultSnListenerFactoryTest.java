/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.unit.core.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.impl.DefaultSnListenerDefinition;
import de.hybris.platform.searchservices.core.service.impl.DefaultSnListenerFactory;
import de.hybris.platform.searchservices.search.service.SnSearchListener;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;


@UnitTest
public class DefaultSnListenerFactoryTest
{
	private static final String LISTENER1_NAME = "listener1";
	private static final String LISTENER2_NAME = "listener2";
	private static final String LISTENER3_NAME = "listener3";
	private static final String LISTENER4_NAME = "listener4";
	private static final String FAKE_LISTENER_NAME = "fakeListener";

	private static final int HIGH_PRIORITY = 200;
	private static final int DEFAULT_PRIORITY = 100;
	private static final int LOW_PRIORITY = 50;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private SnContext context;

	@Mock
	private AbstractSnSearchProviderConfiguration searchProviderConfiguration;

	@Mock
	private SnIndexConfiguration indexConfiguration;

	@Mock
	private SnIndexType indexType;

	@Mock
	private TestListener listener1;

	@Mock
	private TestListener listener2;

	@Mock
	private TestListener listener3;

	@Mock
	private TestListener listener4;

	@Mock
	private Object fakeListener;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		when(context.getIndexConfiguration()).thenReturn(indexConfiguration);
		when(context.getIndexType()).thenReturn(indexType);
		when(indexConfiguration.getSearchProviderConfiguration()).thenReturn(searchProviderConfiguration);

		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER1_NAME, TestListener.class))).thenReturn(Boolean.TRUE);
		when(applicationContext.getBean(LISTENER1_NAME, TestListener.class)).thenReturn(listener1);

		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER2_NAME, TestListener.class))).thenReturn(Boolean.TRUE);
		when(applicationContext.getBean(LISTENER2_NAME, TestListener.class)).thenReturn(listener2);

		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER3_NAME, TestListener.class))).thenReturn(Boolean.TRUE);
		when(applicationContext.getBean(LISTENER3_NAME, TestListener.class)).thenReturn(listener3);

		when(Boolean.valueOf(applicationContext.isTypeMatch(LISTENER4_NAME, TestListener.class))).thenReturn(Boolean.TRUE);
		when(applicationContext.getBean(LISTENER4_NAME, TestListener.class)).thenReturn(listener4);

		when(Boolean.valueOf(applicationContext.isTypeMatch(FAKE_LISTENER_NAME, TestListener.class))).thenReturn(Boolean.FALSE);
	}

	protected DefaultSnListenerFactory createListenerFactory()
	{
		final DefaultSnListenerFactory listenersFactory = new DefaultSnListenerFactory();
		listenersFactory.setSupportedTypes(Collections.<Class<?>> singletonList(TestListener.class));
		listenersFactory.setApplicationContext(applicationContext);

		listenersFactory.afterPropertiesSet();

		return listenersFactory;
	}

	@Test
	public void noListenerConfigured()
	{
		// given
		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<SnSearchListener> listeners = listenersFactory.getListeners(context, SnSearchListener.class);

		// then
		assertThat(listeners).isEmpty();
	}

	@Test
	public void noListenerOfExpectedTypeConfigured()
	{
		// given
		final DefaultSnListenerDefinition listenerDefinition = new DefaultSnListenerDefinition();
		listenerDefinition.setPriority(DEFAULT_PRIORITY);
		listenerDefinition.setListener(fakeListener);

		final Map<String, DefaultSnListenerDefinition> listenerDefinitions = new HashMap<String, DefaultSnListenerDefinition>();
		listenerDefinitions.put(FAKE_LISTENER_NAME, listenerDefinition);

		when(applicationContext.getBeansOfType(DefaultSnListenerDefinition.class)).thenReturn(listenerDefinitions);

		when(searchProviderConfiguration.getListeners()).thenReturn(List.of(FAKE_LISTENER_NAME));
		when(indexConfiguration.getListeners()).thenReturn(List.of(FAKE_LISTENER_NAME));
		when(indexType.getListeners()).thenReturn(List.of(FAKE_LISTENER_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).isEmpty();
	}

	@Test
	public void globalListenerConfigured()
	{
		// given
		final DefaultSnListenerDefinition listenerDefinition = new DefaultSnListenerDefinition();
		listenerDefinition.setPriority(DEFAULT_PRIORITY);
		listenerDefinition.setListener(listener1);

		final Map<String, DefaultSnListenerDefinition> listenerDefinitions = new HashMap<String, DefaultSnListenerDefinition>();
		listenerDefinitions.put(LISTENER1_NAME, listenerDefinition);

		when(applicationContext.getBeansOfType(DefaultSnListenerDefinition.class)).thenReturn(listenerDefinitions);

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener1);
	}

	@Test
	public void searchProviderConfigurationListenerConfigured()
	{
		// given
		when(searchProviderConfiguration.getListeners()).thenReturn(List.of(LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener1);
	}

	@Test
	public void indexConfigurationListenerConfigured()
	{
		// given
		when(indexConfiguration.getListeners()).thenReturn(List.of(LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener1);
	}

	@Test
	public void indexTypeListenerConfigured()
	{
		// given
		when(indexType.getListeners()).thenReturn(List.of(LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener1);
	}

	@Test
	public void globalListenersAreReturnedInCorrectOrder()
	{
		// given
		final DefaultSnListenerDefinition listenerDefinition1 = new DefaultSnListenerDefinition();
		listenerDefinition1.setPriority(LOW_PRIORITY);
		listenerDefinition1.setListener(listener1);

		final DefaultSnListenerDefinition listenerDefinition2 = new DefaultSnListenerDefinition();
		listenerDefinition2.setPriority(HIGH_PRIORITY);
		listenerDefinition2.setListener(listener2);

		final DefaultSnListenerDefinition listenerDefinition3 = new DefaultSnListenerDefinition();
		listenerDefinition3.setPriority(DEFAULT_PRIORITY);
		listenerDefinition3.setListener(listener3);

		final Map<String, DefaultSnListenerDefinition> listenerDefinitions = new HashMap<String, DefaultSnListenerDefinition>();
		listenerDefinitions.put(LISTENER1_NAME, listenerDefinition1);
		listenerDefinitions.put(LISTENER2_NAME, listenerDefinition2);
		listenerDefinitions.put(LISTENER3_NAME, listenerDefinition3);

		when(applicationContext.getBeansOfType(DefaultSnListenerDefinition.class)).thenReturn(listenerDefinitions);

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener2, listener3, listener1);
	}

	@Test
	public void searchProviderConfigurationListenersAreReturnedInCorrectOrder()
	{
		// given
		when(searchProviderConfiguration.getListeners()).thenReturn(List.of(LISTENER2_NAME, LISTENER3_NAME, LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener2, listener3, listener1);
	}

	@Test
	public void indexConfigurationListenersAreReturnedInCorrectOrder()
	{
		// given
		when(indexConfiguration.getListeners()).thenReturn(List.of(LISTENER2_NAME, LISTENER3_NAME, LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener2, listener3, listener1);
	}

	@Test
	public void indexTypeListenersAreReturnedInCorrectOrder()
	{
		// given
		when(indexType.getListeners()).thenReturn(List.of(LISTENER2_NAME, LISTENER3_NAME, LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener2, listener3, listener1);
	}

	@Test
	public void listenersAreReturnedInCorrectOrder()
	{
		// given
		final DefaultSnListenerDefinition listenerDefinition = new DefaultSnListenerDefinition();
		listenerDefinition.setPriority(DEFAULT_PRIORITY);
		listenerDefinition.setListener(listener4);

		final Map<String, DefaultSnListenerDefinition> listenerDefinitions = new HashMap<String, DefaultSnListenerDefinition>();
		listenerDefinitions.put(LISTENER4_NAME, listenerDefinition);

		when(applicationContext.getBeansOfType(DefaultSnListenerDefinition.class)).thenReturn(listenerDefinitions);

		when(searchProviderConfiguration.getListeners()).thenReturn(List.of(LISTENER2_NAME));
		when(indexConfiguration.getListeners()).thenReturn(List.of(LISTENER3_NAME));
		when(indexType.getListeners()).thenReturn(List.of(LISTENER1_NAME));

		final DefaultSnListenerFactory listenersFactory = createListenerFactory();

		// when
		final List<TestListener> listeners = listenersFactory.getListeners(context, TestListener.class);

		// then
		assertThat(listeners).containsExactly(listener4, listener2, listener3, listener1);
	}

	public interface TestListener
	{
		// No methods
	}
}
