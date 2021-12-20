package de.hybris.training.facades.DriverBaseFacade.Impl;

import de.hybris.platform.commercefacades.product.data.DriverBaseData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.training.core.model.DriverBaseModel;
import de.hybris.training.core.services.HybrisApiServices;
import de.hybris.training.facades.DriverBaseFacade.DriverBaseStoresFacade;

import java.util.List;

public class DriverBaseStoresFacadeImpl implements DriverBaseStoresFacade {


    private HybrisApiServices hybrisApiServices;
    private Converter<DriverBaseModel,DriverBaseData> storesconverter;


    @Override
    public List<DriverBaseData> getDriverBaseDetails(final String nameOfDriver) {

        final List<DriverBaseModel> driverBaseDataModel =hybrisApiServices.getDriverDetailsByName(nameOfDriver);



        return Converters.convertAll(driverBaseDataModel,getStoresconverter());
    }

    @Override
    public List<DriverBaseData> getAllDriverDetails() {
        final List<DriverBaseModel> driverBaseDataModel =hybrisApiServices.getAllDriverDetails();

        return Converters.convertAll(driverBaseDataModel,getStoresconverter());
    }

    @Override
    public void removeDriver(String nameOfDriver) {
        hybrisApiServices.removeDriverByName(nameOfDriver);
    }


    public HybrisApiServices getHybrisApiServices() {
        return hybrisApiServices;
    }

    public void setHybrisApiServices(HybrisApiServices hybrisApiServices) {
        this.hybrisApiServices = hybrisApiServices;
    }

    public Converter<DriverBaseModel, DriverBaseData> getStoresconverter() {
        return storesconverter;
    }

    public void setStoresconverter(Converter<DriverBaseModel, DriverBaseData> storesconverter) {
        this.storesconverter = storesconverter;
    }
}
