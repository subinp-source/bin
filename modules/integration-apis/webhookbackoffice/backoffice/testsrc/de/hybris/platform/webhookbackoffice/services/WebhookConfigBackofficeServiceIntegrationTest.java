package de.hybris.platform.webhookbackoffice.services;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;

@IntegrationTest
public class WebhookConfigBackofficeServiceIntegrationTest extends ServicelayerTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private WebhookConfigBackofficeService webhookService;

	@Before
	public void setUp()
	{
		final GenericApplicationContext applicationContext = (GenericApplicationContext) Registry.getApplicationContext();
		final DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();

		final AbstractBeanDefinition validationDefinition = BeanDefinitionBuilder.rootBeanDefinition(
				WebhookConfigBackofficeService.class).getBeanDefinition();
		beanFactory.registerBeanDefinition("webhookConfigService", validationDefinition);
		webhookService = (WebhookConfigBackofficeService) Registry.getApplicationContext().getBean("webhookConfigService");
		webhookService.setModelService(modelService);
		webhookService.setFlexibleSearchService(flexibleSearchService);
	}

	@Test
	public void testGetConsumedDestinationByChannel() throws ImpExException
	{
		givenConsumedDestinations();

		List<ConsumedDestinationModel> consumedDestinationList = webhookService.getConsumedDestinationByChannel(
				DestinationChannel.DEFAULT);
		assertEquals(2, consumedDestinationList.size());

		consumedDestinationList = webhookService.getConsumedDestinationByChannel(DestinationChannel.WEBHOOKSERVICES);
		assertEquals(1, consumedDestinationList.size());

		cleanupConsumedDestinations();
	}

	private void givenConsumedDestinations() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE DestinationTarget;id[unique=true];destinationChannel(code)[default=DEFAULT];template",
				";TestDefaultDestinationTarget;;false",

				"INSERT_UPDATE DestinationTarget;id[unique=true];destinationChannel(code)[default=DEFAULT];template",
				";TestWebhookService;WEBHOOKSERVICES;false",

				"INSERT_UPDATE Endpoint; id[unique = true]; version[unique = true]; name        ; specUrl",
				"                      ; TestWebhookEndpoint     ; 1     ; local-hybris; https://specUrl",

				"INSERT_UPDATE BasicCredential; id[unique = true]; username; password",
				"                             ; TestBasicCredential        ; will ; MockPassword",

				"INSERT_UPDATE ConsumedDestination; id[unique = true] ; url              ; endpoint(id, version)         ; credential(id); destinationTarget(id)",
				"                                 ; TestConsumedDestinationWithDefault   ; https://localUrl ; TestWebhookEndpoint:1; TestBasicCredential     ; TestDefaultDestinationTarget",
				"                                 ; TestConsumedDestinationWithDefault2   ; https://youCantSeeMe ; TestWebhookEndpoint:1; TestBasicCredential     ; TestDefaultDestinationTarget",
				"                                 ; TestConsumedDestinationWithDefault3   ; https://youCantSeeMe ; TestWebhookEndpoint:1; TestBasicCredential     ; TestWebhookService"
		);
	}

	private void cleanupConsumedDestinations()
	{
		IntegrationTestUtil.remove(DestinationTargetModel.class,
				dt -> "TestDefaultDestinationTarget".equals(dt.getId()) || "TestWebhookService".equals(dt.getId()));
		IntegrationTestUtil.remove(EndpointModel.class, ep -> "TestWebhookEndpoint".equals(ep.getId()));
		IntegrationTestUtil.remove(BasicCredentialModel.class, bc -> "TestBasicCredential".equals(bc.getId()));
		IntegrationTestUtil.remove(ConsumedDestinationModel.class,
				cd -> "TestConsumedDestinationWithDefault".equals(cd.getId()) ||
						"TestConsumedDestinationWithDefault2".equals(cd.getId()) ||
						"TestConsumedDestinationWithDefault3".equals(cd.getId()));
	}

	@Test
	public void testGetActiveGroovyScripts() throws ImpExException
	{
		givenScripts();
		final var scripts = webhookService.getActiveGroovyScripts();
		assertThat(scripts).hasSize(1)
						   .hasOnlyOneElementSatisfying(s -> {
							   assertThat(s.getCode()).isEqualTo("activeScript");
							   assertThat(s.getActive()).isTrue();
							   assertThat(s.getScriptType()).isEqualTo(ScriptType.GROOVY);
						   });
		cleanupScripts();
	}

	private void givenScripts() throws ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE Script; code[unique = true]; scriptType(code); content",
				"                    ; activeScript       ; GROOVY          ; 'not active'",
				"                    ; activeScript       ; GROOVY          ; 'active'",
				"                    ; notGroovtScript    ; BEANSHELL       ; 'not returned because not groovy'"
		);
	}

	private void cleanupScripts()
	{
		IntegrationTestUtil.remove(ScriptModel.class,
				s -> "activeScript".equals(s.getCode()) ||
						"inactiveScript".equals(s.getCode()) ||
						"notGroovyScript".equals(s.getCode()));
	}
}
