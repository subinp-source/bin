package de.hybris.training.facades.populators;

import de.hybris.platform.commercefacades.DealerFolder.data.DealerBaseData;
import de.hybris.platform.commercefacades.product.data.DriverBaseData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.training.core.model.DealerBaseModel;
import de.hybris.training.core.model.DriverBaseModel;

public class DealerBaseDataPopulator implements Populator<DealerBaseModel, DealerBaseData> {
    @Override
    public void populate(DealerBaseModel dealerBaseModel, DealerBaseData driverBaseData) throws ConversionException {
        driverBaseData.setLocalizeName(dealerBaseModel.getLocalizeName());
        driverBaseData.setUniqueId(dealerBaseModel.getUniqueId());
        driverBaseData.setAddress(dealerBaseModel.getAddress());
    }


}
