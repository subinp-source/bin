package de.hybris.platform.integrationbackofficetest.widgets.modals.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import de.hybris.platform.integrationbackoffice.widgets.handlers.IntegrationObjectClassificationClassWizardHandler;
import de.hybris.platform.integrationbackoffice.widgets.modals.utility.ClassificationAttributesWithoutLocalizationRenderer;

import org.junit.Test;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


public class ClassificationAttributesWithoutLocalizationRendererTest extends ClassificationAttributesWithoutLocalizationRenderer
{
	@Test
	public void shouldRenderUseFullQualifierCheckboxOnWizard()
	{
		// given
		CockpitTestUtil.mockZkEnvironment();
		final Div parent = new Div();
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);

		// when
		renderUseFullClassificationQualifierCheckbox(parent, wim);

		// then
		assertThat(parent.query(".yw-use-full-qualifier-container")).as("Use full qualifier contains is rendered").isNotNull();
		assertThat(parent.query(".yw-use-full-qualifier-container").query(".ye-switch-checkbox"))
				.as("Checkbox inside the container is rendered").isNotNull();
	}

	@Test
	public void shouldModelBeUpdatedWhenCheckboxIsSelected()
	{
		// given
		CockpitTestUtil.mockZkEnvironment();
		final Div parent = new Div();
		ViewType viewType = new ViewType();
		DataType dataType = mock(DataType.class);
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(wim.getModel()).willReturn(widgetModel);

		// when checkbox is rendered
		renderUseFullClassificationQualifierCheckbox(parent, wim);

		// and checkbox is clicked
		final Component checkbox = parent.query(".ye-switch-checkbox");
		CockpitTestUtil.simulateEvent(checkbox, new CheckEvent(Events.ON_CHECK, checkbox, true));

		// then
		then(widgetModel).should().setValue(IntegrationObjectClassificationClassWizardHandler.USE_FULL_QUALIFIER, true);
	}
}
