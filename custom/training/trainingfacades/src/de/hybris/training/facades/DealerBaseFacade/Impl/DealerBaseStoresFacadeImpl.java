package de.hybris.training.facades.DealerBaseFacade.Impl;

import de.hybris.platform.commercefacades.DealerFolder.data.DealerBaseData;
import de.hybris.platform.commercefacades.product.data.DriverBaseData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.training.core.model.DealerBaseModel;
import de.hybris.training.core.model.DriverBaseModel;
import de.hybris.training.core.services.DealerApiServices;
import de.hybris.training.core.services.HybrisApiServices;
import de.hybris.training.facades.DealerBaseFacade.DealerBaseStoresFacade;
import de.hybris.training.facades.DriverBaseFacade.DriverBaseStoresFacade;

import java.util.List;

public class DealerBaseStoresFacadeImpl implements DealerBaseStoresFacade {

    private DealerApiServices dealerApiServices;
    private Converter<DealerBaseModel, DealerBaseData> storeconverter;

    @Override
    public List<DealerBaseData> getAllDealerDetails() {
        final List<DealerBaseModel> dealerBaseDataModel =dealerApiServices.getAllDealerDetails();

        return Converters.convertAll(dealerBaseDataModel,getStoreconverter());
    }

    public DealerApiServices getDealerApiServices() {
        return dealerApiServices;
    }

    public void setDealerApiServices(DealerApiServices dealerApiServices) {
        this.dealerApiServices = dealerApiServices;
    }

    public Converter<DealerBaseModel, DealerBaseData> getStoreconverter() {
        return storeconverter;
    }

    public void setStoreconverter(Converter<DealerBaseModel, DealerBaseData> storeconverter) {
        this.storeconverter = storeconverter;
    }
}
