/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.interceptor;

import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.List;

/**
 * Interceptor to validate no duplicate Integration API exposed in DestinationTarget {@link DestinationTargetModel}
 */
public class ExposedDestinationIntegrationAPIValidateInterceptor implements ValidateInterceptor<ExposedDestinationModel>
{
    private static final String ERROR_MESSAGE = "Integration API of InboundChannelConfiguration [%s] has already been exposed in this DestinationTarget [%s]";

    @Override
    public void onValidate(final ExposedDestinationModel exposedDestinationModel, final InterceptorContext interceptorContext) throws InterceptorException
    {
        final DestinationTargetModel currentDestinationTarget = exposedDestinationModel.getDestinationTarget();

        if(!isExposedDestinationCreated(exposedDestinationModel) && exposedDestinationModel.getInboundChannelConfiguration() != null)
        {
            final InboundChannelConfigurationModel inboundChannelConfiguration = exposedDestinationModel.getInboundChannelConfiguration();
            final List<ExposedDestinationModel> iCCexposedDestinationModelList = inboundChannelConfiguration.getExposedDestinations();

            if(isExposedInDestinationTarget(iCCexposedDestinationModelList, currentDestinationTarget))
            {
                throw new InterceptorException(String.format(ERROR_MESSAGE, inboundChannelConfiguration.getIntegrationObject().getCode(), currentDestinationTarget.getId()));
            }
        }
    }

    private boolean isExposedInDestinationTarget(final List<ExposedDestinationModel> exposedDestinationModelList, final DestinationTargetModel destinationTargetModel)
    {
        final String destinationTargetId = destinationTargetModel.getId();
        return exposedDestinationModelList.stream()
                        .anyMatch(exposedDestinationModel -> exposedDestinationModel.getDestinationTarget().getId().equals(destinationTargetId));
    }

    private boolean isExposedDestinationCreated(final ExposedDestinationModel exposedDestinationModel)
    {
        return exposedDestinationModel.getPk() != null;
    }
}
