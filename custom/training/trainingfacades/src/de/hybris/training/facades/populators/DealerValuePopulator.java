package de.hybris.training.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

public class DealerValuePopulator extends SearchResultVariantProductPopulator {
    @Override
    public void populate(SearchResultValueData source, ProductData target) {
       /* super.populate(source, target);
        target.setDealer(this.getValue(source,"dealer"));
        target.setDriver(this.getValue(source,"driver").toString());
        String registrationNumber = this.getValue(source,"vehicleRegistrationNumber").toString();
        //target.setVehicleRegistrationNumber(source,registrationNumber);
        System.out.println("kooi");*/
        super.populate(source, target);
        final Object obj=this.getValue(source,"vehicleRegistrationNumber");
        if (obj !=null){


            final String registrationNumber=this.getValue(source,"vehicleRegistrationNumber").toString();
            target.setVehicleRegistrationNumber(registrationNumber);
            target.setDealer(this.getValue(source,"dealer"));
            target.setDriver(this.getValue(source,"driver").toString());
        }
        else{
            target.setVehicleRegistrationNumber(null);
            target.setDriver(null);
            target.setDealer(null);
        }



    }
}

