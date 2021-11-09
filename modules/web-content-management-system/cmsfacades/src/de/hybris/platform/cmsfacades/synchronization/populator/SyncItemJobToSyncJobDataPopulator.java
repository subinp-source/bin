/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.synchronization.populator;

import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cmsfacades.data.SyncJobData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Optional;

/**
 *
 * Simple class for populating additional job synchronization data in {@link SyncJobData} from {@link SyncItemJobModel}.
 *
 */
public class SyncItemJobToSyncJobDataPopulator implements Populator<Optional<SyncItemJobModel>, SyncJobData>
{

    @Override
    public void populate(final Optional<SyncItemJobModel> source, final SyncJobData target) throws ConversionException
    {
        source.ifPresent(src -> {
            target.setSourceCatalogVersion(src.getSourceVersion().getVersion());
            target.setTargetCatalogVersion(src.getTargetVersion().getVersion());
        });
    }
}
