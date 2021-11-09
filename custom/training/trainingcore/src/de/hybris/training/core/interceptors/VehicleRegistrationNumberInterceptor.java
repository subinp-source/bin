package de.hybris.training.core.interceptors;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.training.core.events.NewEventForRegistrationNumber;
import de.hybris.training.core.model.VehicleBaseModel;

public class VehicleRegistrationNumberInterceptor implements PrepareInterceptor<VehicleBaseModel> {

    @Override
    public void onPrepare(VehicleBaseModel vehicleBaseModel, InterceptorContext interceptorContext) throws InterceptorException {


        vehicleBaseModel.getVehicleRegistrationNumber();
        NewEventForRegistrationNumber newEventForRegistrationNumber= new NewEventForRegistrationNumber();
        newEventForRegistrationNumber.message();

    }
}
