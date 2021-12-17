package de.hybris.training.facades.DriverBaseFacade;

import de.hybris.platform.commercefacades.product.data.DriverBaseData;

import java.util.List;

public interface DriverBaseStoresFacade {

    public List<DriverBaseData> getDriverBaseDetails(final String nameOfDriver);
    public List<DriverBaseData> getAllDriverDetails();

}
