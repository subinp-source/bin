
package de.hybris.training.facades.DealerBaseFacade;

import de.hybris.platform.commercefacades.DealerFolder.data.DealerBaseData;
import de.hybris.platform.commercefacades.product.data.DriverBaseData;

import java.util.List;

public interface DealerBaseStoresFacade {

    public List<DealerBaseData> getAllDealerDetails();
}

