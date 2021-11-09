package de.hybris.training.backoffice.renders;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;


import de.hybris.training.core.model.DriverBaseModel;

import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

import java.util.logging.Logger;

public class TrainingRenderDriverId extends AbstractWidgetComponentRenderer<Listcell, ListColumn, DriverBaseModel> {

    static final private Logger LOG = Logger.getLogger(String.valueOf(TrainingRenderDriverId.class));

    @Override
    public void render(Listcell listcell, ListColumn listColumn, DriverBaseModel driverBaseModel, DataType dataType,
                        WidgetInstanceManager widgetInstanceManager) {
        LOG.info("inside AbstractWidgetComponentRenderer..");
        final Div div = new Div();
        final Label label = new Label();
        String driver_id= driverBaseModel.getDriverId();
        int i=0;
        while(i<driver_id.length() && driver_id.charAt(i)=='0'){
            i++;
        }
        StringBuilder sb =new StringBuilder(driver_id);
        sb.replace(0,i,"");
        driver_id=sb.toString();
        label.setValue(driver_id);
        div.appendChild(label);
        listcell.appendChild(div);
        fireComponentRendered(listcell,listColumn,driverBaseModel);

    }
}










