/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.populators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.kymaintegrationservices.dto.ServiceRegistrationData;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class WebserviceSpecificationPopulatorIT extends ServicelayerTransactionalTest
{
    private static final String TEST_PROVIDER = "SAP Hybris";
    private static final String PROVIDER_PROP = "kymaintegrationservices.kyma-specification-provider";
    private static final String KYMA_DEFAULT_TARGET = "kymaDefaultTarget";
    private static final String TEST_DEPLOYMENT_END_POINT="deployment.end.point.test.scope";
    private static final String TEST_DEPLOYMENT_END_POINT_URL="https://some.url";

    private final WebserviceSpecificationPopulator populator = new WebserviceSpecificationPopulator();

    @javax.annotation.Resource(name = "destinationService")
    private DestinationService<AbstractDestinationModel> destinationService;

    @javax.annotation.Resource(name = "kymaExportJacksonObjectMapper")
    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/test/apiConfigurations.impex", "UTF-8");

        populator.setJacksonObjectMapper(objectMapper);

        Config.setParameter(PROVIDER_PROP, TEST_PROVIDER);
        Config.setParameter(TEST_DEPLOYMENT_END_POINT, TEST_DEPLOYMENT_END_POINT_URL);
    }

    @Test
    public void populateApiSpecification_NullChecks() throws IOException
    {
        final JsonNode asmSpec = objectMapper.readTree(asm_swagger);

        final List<ExposedDestinationModel> models = destinationService.getDestinationsByDestinationTargetId(KYMA_DEFAULT_TARGET).stream()
              .filter(ExposedDestinationModel.class::isInstance).map(ExposedDestinationModel.class::cast)
              .collect(Collectors.toList());
        assertFalse(CollectionUtils.isEmpty(models));

        final ExposedDestinationModel destinationModel = models.get(0);
        destinationModel.getEndpoint().setDescription(null);
        final ServiceRegistrationData serviceRegistrationData = new ServiceRegistrationData();

        assertTrue("Host should be empty and not null", asmSpec.get("host").textValue().isEmpty());
        assertTrue("basePath should be null", asmSpec.get("basePath").isNull());

        populator.populate(destinationModel, serviceRegistrationData);
        serviceRegistrationData.getApi().setSpec(asmSpec);

        // passed null from model
        assertTrue(null == serviceRegistrationData.getDescription());

        final String jsonString = objectMapper.writeValueAsString(serviceRegistrationData);
        assertTrue("Should be empty host in result", jsonString.contains("host"));
        assertFalse("Should be no null description in result", jsonString.contains("description"));
    }

    private String asm_swagger = "{\n"
          + "    \"swagger\": \"2.0\",\n"
          + "    \"info\": {\n"
          + "        \"version\": \"1.0.0\",\n"
          + "        \"title\": \"Assisted Service Module\"\n"
          + "    },\n"
          + "    \"host\": \"\",\n"
          + "    \"basePath\": null,\n"
          + "    \"tags\": [\n"
          + "        {\n"
          + "            \"name\": \"customers-controller\"\n"
          + "        },\n"
          + "        {\n"
          + "            \"name\": \"customer-lists-controller\"\n"
          + "        }\n"
          + "    ]\n"
          + "}\n";
}
