package de.hybris.training.core.Dao;

import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.training.core.model.DriverBaseModel;

import java.util.List;

public interface HybrisApiDao extends Dao {

    List<DriverBaseModel> getDriverDetailsByName(String nameOfDriver);

}
