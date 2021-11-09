/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2webservices.update;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.config.InboundServicesConfiguration;
import de.hybris.platform.inboundservices.enums.AuthenticationType;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.odata2webservices.constants.Odata2webservicesConstants;
import de.hybris.platform.odata2webservices.enums.IntegrationType;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.slf4j.Logger;

@SystemSetup(extension = Odata2webservicesConstants.EXTENSIONNAME)
public class InboundChannelConfigurationSystemUpdater
{
	private static final Logger LOG = Log.getLogger(InboundChannelConfigurationSystemUpdater.class);
	private final ModelService modelService;
	private final FlexibleSearchService flexibleSearchService;
	private final InboundServicesConfiguration inboundServicesConfiguration;

	public InboundChannelConfigurationSystemUpdater(final ModelService modelService,
	                                                final FlexibleSearchService flexibleSearchService,
	                                                final InboundServicesConfiguration inboundServicesConfiguration)
	{
		this.modelService = modelService;
		this.flexibleSearchService = flexibleSearchService;
		this.inboundServicesConfiguration = inboundServicesConfiguration;
	}

	@SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
	public void associateIntegrationObjectWithInboundConfigChannel()
	{
		if (!inboundServicesConfiguration.isLegacySecurity())
		{
			final List<IntegrationObjectModel> nullICCIntegrationObjects = findIOsWithoutICCs();
			LOG.info("Associating {} Integration Objects of type INBOUND to InboundChannelConfiguration...",
					nullICCIntegrationObjects.size());

			nullICCIntegrationObjects.forEach(i -> {
				LOG.debug("Associating Integration Object: {}", i.getCode());
				modelService.save(createInboundChannelConfiguration(i));
			});

			LOG.info("Finished associating Integration Objects.");
		}
		else
		{
			LOG.info("Legacy security enabled, no Integration Objects will be associated to InboundChannelConfiguration.");
		}
	}

	private List<IntegrationObjectModel> findIOsWithoutICCs()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"SELECT {io." + IntegrationObjectModel.PK + "} " +
						"FROM {" + IntegrationObjectModel._TYPECODE + " AS io LEFT OUTER JOIN " +
						InboundChannelConfigurationModel._TYPECODE + " AS icc ON " +
						"{ icc:" + InboundChannelConfigurationModel.INTEGRATIONOBJECT + "} = {io." + ItemModel.PK + "}} " +
						"WHERE ({io." + IntegrationObjectModel.INTEGRATIONTYPE + "} = " +
						"({{ SELECT {" + ItemModel.PK + "} FROM {" + IntegrationType._TYPECODE + "} " +
						"WHERE {code} ='" + IntegrationType.INBOUND.getCode() + "'}}) " +
						"AND ({icc." + InboundChannelConfigurationModel.INTEGRATIONOBJECT + "} is null))");

		return flexibleSearchService.<IntegrationObjectModel>search(query).getResult();
	}

	private InboundChannelConfigurationModel createInboundChannelConfiguration(final IntegrationObjectModel io)
	{
		final InboundChannelConfigurationModel obj = new InboundChannelConfigurationModel();
		obj.setIntegrationObject(io);
		obj.setAuthenticationType(AuthenticationType.BASIC);

		return obj;
	}
}
