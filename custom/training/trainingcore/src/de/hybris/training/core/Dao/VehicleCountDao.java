package de.hybris.training.core.Dao;

import de.hybris.training.core.model.VehicleBaseModel;

import java.util.List;

public interface VehicleCountDao {
    public List<VehicleBaseModel> findAllVehicleCount();
}
