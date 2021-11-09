/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.constraints;

import static de.hybris.platform.apiregistryservices.utils.EventExportUtils.DELIMITER_PROP;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.enums.EventMappingType;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.model.events.EventPropertyConfigurationModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;


@IntegrationTest
public class EventMappingValidValidatorTest extends ServicelayerTransactionalTest
{
	private static final String DELIMITER = "\\.";
	private static final String BEAN = "testBean";

	private static final String KEY_REGEXP = "^[A-Za-z0-9]*$";
	private static final String VALUE_REGEXP = "^[A-Za-z0-9]+(?:.[A-Za-z0-9]+)*$";

	private EventMappingValidValidator validator;
	@Mock
	private EventMappingValid parameters;

	@Mock
	private ConstraintValidatorContext context;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		validator = new EventMappingValidValidator();
		when(parameters.keyFlags()).thenReturn(new Pattern.Flag[]
		{ Pattern.Flag.UNICODE_CASE });
		when(parameters.valueFlags()).thenReturn(new Pattern.Flag[]
		{ Pattern.Flag.UNICODE_CASE });
		when(parameters.keyRegexp()).thenReturn(KEY_REGEXP);
		when(parameters.valueRegexp()).thenReturn(VALUE_REGEXP);
		Config.setParameter(DELIMITER_PROP, DELIMITER);
	}

	public EventConfigurationModel getValidInactiveConfiguration()
	{
		final EventConfigurationModel configuration = new EventConfigurationModel();
		configuration.setMappingType(EventMappingType.GENERIC);
		configuration.setEventClass(SubmitOrderEvent.class.getCanonicalName());
		final List<EventPropertyConfigurationModel> list = new ArrayList<>();
		list.add(buildEventPCM("event.order.code", "orderCode"));
		list.add(buildEventPCM("event.order.totalPrice", "totalPrice"));
		configuration.setEventPropertyConfigurations(list);
		return configuration;
	}

	public EventConfigurationModel getValidActiveConfiguration()
	{
		final EventConfigurationModel configuration = getValidInactiveConfiguration();
		configuration.setExportFlag(true);
		return configuration;
	}

	public EventConfigurationModel getInactiveConfigurationWithInexistentProperty()
	{
		final EventConfigurationModel configuration = getValidInactiveConfiguration();
		configuration.getEventPropertyConfigurations().add(buildEventPCM("event.inexistentProperty", "inexistentProperty"));
		return configuration;
	}

	public EventConfigurationModel getActiveConfigurationWithInexistentProperty()
	{
		final EventConfigurationModel configuration = getInactiveConfigurationWithInexistentProperty();
		configuration.setExportFlag(true);
		return configuration;
	}

	@Test
	public void testInitialize()
	{
		validator.initialize(parameters);
	}

	@Test
	public void testIsValidWhenExportFlagIsFalseAndMappingInvalid()
	{
		final EventConfigurationModel configuration = getInactiveConfigurationWithInexistentProperty();
		validator.initialize(parameters);
		assertThat(validator.isValid(configuration, context)).isTrue();
	}

	@Test
	public void testIsInvalidWhenExportFlagIsTrueAndMappingInvalid()
	{
		final EventConfigurationModel configuration = getActiveConfigurationWithInexistentProperty();
		validator.initialize(parameters);
		assertThat(validator.isValid(configuration, context)).isFalse();
	}

	@Test
	public void testIsValidWhenExportFlagIsTrueAndMappingValid()
	{
		final EventConfigurationModel configuration = getValidActiveConfiguration();
		validator.initialize(parameters);
		assertThat(validator.isValid(configuration, context)).isTrue();
		configuration.getEventPropertyConfigurations().add(buildEventPCM("event.seSite", "site"));
		assertThat(validator.isValid(configuration, context)).isFalse();
		configuration.getEventPropertyConfigurations().add(buildEventPCM("event", "site"));
		assertThat(validator.isValid(configuration, context)).isFalse();
	}

	@Test
	public void testIsValidWhenExportFlagIsTrueAndMappingValidForPropertyConfiguration()
	{
		final EventConfigurationModel configuration = getValidActiveConfiguration();
		validator.initialize(parameters);
		final EventPropertyConfigurationModel eventPropertyConfigurationModel = configuration.getEventPropertyConfigurations()
				.get(0);
		eventPropertyConfigurationModel.setEventConfiguration(configuration);
		assertThat(validator.isValid(eventPropertyConfigurationModel, context)).isTrue();
	}

	@Test
	public void testIsValidWhenMappingIsEmptyOrNull()
	{
		final EventConfigurationModel configuration = new EventConfigurationModel();
		configuration.setExportFlag(true);
		configuration.setMappingType(EventMappingType.GENERIC);
		configuration.setEventClass(SubmitOrderEvent.class.getCanonicalName());
		validator.initialize(parameters);
		assertThat(validator.isValid(configuration, context)).isTrue();

		configuration.setEventPropertyConfigurations(new ArrayList<>());
		assertThat(validator.isValid(configuration, context)).isTrue();
	}

	@Test
	public void testBeanConfiguration()
	{
		final EventConfigurationModel configuration = new EventConfigurationModel();
		configuration.setMappingType(EventMappingType.GENERIC);
		configuration.setConverterBean(BEAN);
		configuration.setEventClass(SubmitOrderEvent.class.getCanonicalName());

		final Converter c = mock(Converter.class);

		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();
		beanFactory.registerSingleton(BEAN, c);
		assertTrue(validator.isValid(configuration, context));
	}

	@Test
	public void testConverterBeanExistAndHasCorrectType()
	{
		final String beanName = "eventMappingValidValidatorTestConverterBeanExistAndHasCorrectType";
		final EventConfigurationModel configuration = buildEventConfigWithConverterBean(beanName);

		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();
		beanFactory.registerSingleton(beanName, mock(Converter.class));

		assertTrue(validator.isValid(configuration, context));
	}

	@Test
	public void testConverterBeanWrongType()
	{
		final String beanName = "eventMappingValidValidatorTestConverterBeanWrongType";
		final EventConfigurationModel configuration = buildEventConfigWithConverterBean(beanName);

		final ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx.getBeanFactory();
		beanFactory.registerSingleton(beanName, mock(Object.class));

		assertFalse(validator.isValid(configuration, context));
	}

	@Test
	public void testConverterBeanNotFound()
	{
		final String beanName = "eventMappingValidValidatorTestConverterBeanNotFound";
		final EventConfigurationModel configuration = buildEventConfigWithConverterBean(beanName);

		assertFalse(validator.isValid(configuration, context));
	}

	private EventConfigurationModel buildEventConfigWithConverterBean(final String converterBeanName) {
		final EventConfigurationModel configuration = new EventConfigurationModel();
		configuration.setMappingType(EventMappingType.BEAN);
		configuration.setConverterBean(converterBeanName);
		configuration.setExportFlag(true);
		configuration.setEventClass(SubmitOrderEvent.class.getCanonicalName());
		return configuration;
	}

	protected EventPropertyConfigurationModel buildEventPCM(final String mapping, final String name)
	{
		final EventPropertyConfigurationModel eventPCM = new EventPropertyConfigurationModel();
		eventPCM.setPropertyMapping(mapping);
		eventPCM.setPropertyName(name);
		eventPCM.setType("string");
		eventPCM.setTitle("test");
		return eventPCM;
	}
}
