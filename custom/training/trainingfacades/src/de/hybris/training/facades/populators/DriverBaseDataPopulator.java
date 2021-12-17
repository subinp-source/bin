package de.hybris.training.facades.populators;

import de.hybris.platform.commercefacades.product.data.DriverBaseData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.training.core.model.DriverBaseModel;

public class DriverBaseDataPopulator implements Populator<DriverBaseModel, DriverBaseData> {


    @Override
    public void populate(DriverBaseModel driverBaseModel, DriverBaseData driverBaseData) throws ConversionException {
        driverBaseData.setNameOfDriver(driverBaseModel.getNameOfDriver());
        driverBaseData.setDriverId(driverBaseModel.getDriverId());
        driverBaseData.setExperience(driverBaseModel.getExperience());
    }
}
