/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.config.zk.au;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.context.WebApplicationContext;
import org.zkoss.io.NullReader;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultCockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.jaxb.Config;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.jaxb.JAXBContextFactory;
import com.hybris.cockpitng.core.util.jaxb.SchemaValidationStatus;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.testing.util.LabelLookupFactory;
import com.hybris.cockpitng.util.CockpitSessionService;


@RunWith(MockitoJUnitRunner.class)
public class DefaultConfigUploadAuExtensionTest
{

	private static final String XML_CONTENT = "xml-content";
	public static final String PROPERTY_VALIDATE_COCKPIT_CONFIG_ORCHESTRATOR = "cockpitng.validate.cockpitConfig.orchestrator";
	public static final String ATTRIBUTE_COCKPIT_CONFIGURATION_FILTERED = "cockpitConfigurationFiltered";

	@Spy
	private DefaultConfigUploadAuExtension ext;

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private DefaultCockpitConfigurationService configurationService;
	@Mock
	private CockpitAdminService cockpitAdminService;
	@Mock
	private Unmarshaller unmarshaller;
	@Mock
	private CockpitProperties cockpitProperties;
	@Mock
	private CockpitSessionService sessionService;

	@Before
	public void setUp() throws JAXBException
	{
		final ServletContext servletContext = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(servletContext);
		final WebApplicationContext webApplicationContext = mock(WebApplicationContext.class);
		when(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE))
				.thenReturn(webApplicationContext);
		when(webApplicationContext.getBean("cockpitConfigurationService", DefaultCockpitConfigurationService.class))
				.thenReturn(configurationService);
		when(webApplicationContext.getBean("cockpitAdminService", CockpitAdminService.class)).thenReturn(cockpitAdminService);
		when(webApplicationContext.getBean("cockpitProperties", CockpitProperties.class)).thenReturn(cockpitProperties);
		when(webApplicationContext.getBean("cockpitSessionService", CockpitSessionService.class)).thenReturn(sessionService);
		final JAXBContextFactory jaxbContextFactory = mock(JAXBContextFactory.class);
		final JAXBContext jaxbContext = mock(JAXBContext.class);
		when(jaxbContextFactory.createContext(any())).thenReturn(jaxbContext);
		when(jaxbContext.createUnmarshaller()).thenReturn(unmarshaller);
		when(webApplicationContext.getBean("cockpitJAXBContextFactory", JAXBContextFactory.class)).thenReturn(jaxbContextFactory);
	}

	@Test
	public void handleConfigUploadShouldSaveConfigurationsWithWarnings() throws IOException
	{
		//given
		when(request.getReader()).thenReturn(new BufferedReader(new NullReader()));
		when(configurationService.validate(any())).thenReturn(SchemaValidationStatus.warning());

		//when
		ext.handleConfigUpload(request, response);

		//then
		verify(ext).storeConfig(any(), any(), any());
	}

	@Test
	public void handleConfigUploadShouldNotSaveConfigurationsWithErrors() throws IOException
	{
		//given
		when(request.getReader()).thenReturn(new BufferedReader(new NullReader()));
		when(configurationService.validate(any())).thenReturn(SchemaValidationStatus.error());

		//when
		ext.handleConfigUpload(request, response);

		//then
		verify(ext, never()).storeConfig(any(), any(), any());
	}

	@Test(expected = IllegalArgumentException.class)
	public void serviceShouldFailOnUnrecognisedPathInfo()
	{
		//when
		ext.service(request, response, "/unknownPath");
	}

	@Test
	public void serviceShouldDispatchToHandleUpload() throws IOException
	{
		//given
		when(request.getReader()).thenReturn(new BufferedReader(new NullReader()));
		when(configurationService.validate(any())).thenReturn(SchemaValidationStatus.success());

		//when
		ext.service(request, response, "/configUpload");

		//then
		verify(ext).handleConfigUpload(request, response);
	}

	@Test
	public void serviceShouldDispatchToHandleValidate() throws IOException
	{
		//given
		when(request.getReader()).thenReturn(new BufferedReader(new NullReader()));
		when(configurationService.validate(any())).thenReturn(SchemaValidationStatus.success());

		//when
		ext.service(request, response, "/configValidate");

		//then
		verify(ext).handleConfigValidate(request, response);
	}

	@Test
	public void handleConfigValidateShouldNotInteractOnTurnedOffValidation()
	{
		//given
		when(cockpitProperties.getBoolean(eq(PROPERTY_VALIDATE_COCKPIT_CONFIG_ORCHESTRATOR))).thenReturn(Boolean.FALSE);

		//when
		ext.handleConfigValidate(request, response);

		//then
		verifyZeroInteractions(response);
	}

	@Test
	public void handleConfigValidateShouldSetHeadersOnResponseOnValidationResult() throws IOException
	{
		//given
		when(cockpitProperties.getBoolean(eq(PROPERTY_VALIDATE_COCKPIT_CONFIG_ORCHESTRATOR))).thenReturn(Boolean.TRUE);
		when(request.getReader()).thenReturn(new BufferedReader(new NullReader()));
		final SchemaValidationStatus error = SchemaValidationStatus.error("Ops!");
		when(configurationService.validate(any())).thenReturn(error);
		CockpitTestUtil
				.mockLabelLookup(LabelLookupFactory.createLabelLookup().registerLabel("config.validation.error", "I failed"));

		//when
		ext.handleConfigValidate(request, response);

		//then
		verify(response).addHeader("validationStatus", "error");
		verify(response).addHeader("validationLabel", "I failed");
	}

	@Test
	public void getInputStreamShouldProvideStreamWithRequestData() throws IOException
	{
		//given
		when(request.getReader()).thenReturn(new BufferedReader(new StringReader("Test content")));

		//when
		final InputStream inputStream = ext.getInputStream(request);

		//then
		final StringWriter writer = new StringWriter();
		IOUtils.copy(new InputStreamReader(inputStream, Charset.defaultCharset()), writer);
		assertThat(writer.toString()).isEqualTo("Test content");
	}


	@Test
	public void handleConfigUploadShouldPassRequestData() throws IOException
	{
		//given
		final SchemaValidationStatus successStatus = SchemaValidationStatus.success();
		when(request.getReader()).thenReturn(new BufferedReader(new StringReader(XML_CONTENT)));
		when(sessionService.getAttribute(eq(ATTRIBUTE_COCKPIT_CONFIGURATION_FILTERED))).thenReturn(Boolean.FALSE);
		when(configurationService.validate(any())).thenReturn(successStatus);

		//when
		ext.handleConfigUpload(request, response);

		//then
		verify(ext).storeConfig(eq(XML_CONTENT), same(request), same(response));
		verify(configurationService).setConfigAsString(eq(XML_CONTENT));
		verify(cockpitAdminService).refreshCockpit();
	}

	@Test
	public void storeConfigShouldMergeConfigurationIfFiltered() throws JAXBException, CockpitConfigurationException
	{
		//given
		when(sessionService.getAttribute(eq(ATTRIBUTE_COCKPIT_CONFIGURATION_FILTERED))).thenReturn(Boolean.TRUE);
		final Config changes = mock(Config.class);
		final Config merged = mock(Config.class);
		when(configurationService.getChangesAsConfig(eq("a config"), same(unmarshaller))).thenReturn(changes);
		when(configurationService.loadRootConfiguration()).thenReturn(merged);

		//when
		ext.storeConfig("a config", request, response);

		//then
		verify(configurationService).storeRootConfig(merged);
		verify(cockpitAdminService).refreshCockpit();
	}

	@Test
	public void isConfigurationFilteredShouldReturnFalseOnNull()
	{
		//given
		when(sessionService.getAttribute(eq("showFilterOptions"))).thenReturn(null);

		//when
		final boolean filtered = ext.isConfigurationFiltered(request);

		//then
		assertThat(filtered).isFalse();
	}

	@Test
	public void isConfigurationFilteredShouldReturnTrueOnBooleanTrue()
	{
		//given
		when(sessionService.getAttribute(eq(ATTRIBUTE_COCKPIT_CONFIGURATION_FILTERED))).thenReturn(Boolean.TRUE);

		//when
		final boolean filtered = ext.isConfigurationFiltered(request);

		//then
		assertThat(filtered).isTrue();
	}

	@Test
	public void handleErrorShouldSet500ErrorCode()
	{
		//when
		ext.handleError(response, new Exception());

		//then
		verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	@Test
	public void shouldValidateCockpitConfigShouldReturnFalseOnFalseString()
	{
		//given
		when(cockpitProperties.getBoolean(eq(PROPERTY_VALIDATE_COCKPIT_CONFIG_ORCHESTRATOR))).thenReturn(Boolean.FALSE);

		//when
		final boolean validate = ext.shouldValidateCockpitConfig(request);

		//then
		assertThat(validate).isFalse();
	}

	@Test
	public void shouldValidateCockpitConfigShouldReturnTrueOnTrueString()
	{
		//given
		when(cockpitProperties.getBoolean(eq(PROPERTY_VALIDATE_COCKPIT_CONFIG_ORCHESTRATOR))).thenReturn(Boolean.TRUE);

		//when
		final boolean validate = ext.shouldValidateCockpitConfig(request);

		//then
		assertThat(validate).isTrue();
	}

	@Test
	public void shouldValidationBeCalledOnConfigurationUpload() throws IOException
	{
		//given
		final SchemaValidationStatus successStatus = SchemaValidationStatus.success();
		when(request.getReader()).thenReturn(new BufferedReader(new StringReader(XML_CONTENT)));
		when(configurationService.validate(any())).thenReturn(successStatus);

		//when
		ext.handleConfigUpload(request, response);

		//then
		verify(configurationService).validate(any());
	}
}
