/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerCloseListener.CLOSE_CONFIRMATION_MESSAGE;
import static com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerCloseListener.CLOSE_CONFIRMATION_TITLE;
import static com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerCloseListener.VALUE_CHANGED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zul.impl.MessageboxDlg;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.impl.ListContainerCloseListener;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerCloseListenerTest
{
	@Mock
	ListContainerCloseListener mockedDelegate;

	@Spy
	@InjectMocks
	WorkflowDesignerCloseListener closeListener;

	@Test
	public void shouldResetChangedValueInModelWhenNewObjectIsHandled()
	{
		// given
		final Map<String, Boolean> model = new HashMap<>();

		// when
		closeListener.onNew(model);

		// then
		assertThat(model).contains(entry(VALUE_CHANGED, false));
	}

	@Test
	public void shouldSetChangedValueInModelWhenUpdatedObjectIsHandled()
	{
		// given
		final Map<String, Boolean> model = new HashMap<>();

		// when
		closeListener.onChange(model);

		// then
		assertThat(model).contains(entry(VALUE_CHANGED, true));
	}

	@Test
	public void shouldHandleOnCloseForUnchangedModelByDelegating()
	{
		// given
		final Event event = mock(Event.class);
		final WidgetInstance widgetInstance = mock(WidgetInstance.class);

		// when
		closeListener.onClose(event, widgetInstance);

		// then
		then(mockedDelegate).should().onClose(event, widgetInstance);
	}

	@Test
	public void shouldHandleOnCloseForChangedModel()
	{
		// given
		CockpitTestUtil.mockZkEnvironment();
		final Map<String, Boolean> model = Map.of(VALUE_CHANGED, true);

		final Event event = mock(Event.class);
		final WidgetInstance widgetInstance = mock(WidgetInstance.class);
		given(widgetInstance.getModel()).willReturn(model);

		given(closeListener.getLabel(CLOSE_CONFIRMATION_MESSAGE)).willReturn("confirmation message");
		given(closeListener.getLabel(CLOSE_CONFIRMATION_TITLE)).willReturn("confirmation title");

		final Desktop desktop = ExecutionsCtrl.getCurrent().getDesktop();
		final MessageboxDlg messageboxDlg = mock(MessageboxDlg.class);
		given(messageboxDlg.getDesktop()).willReturn(desktop);
		given(ExecutionsCtrl.getCurrent().createComponents(contains("messagebox.zul"), eq(null), eq(null),
				aMapWithElements(Map.entry("message", "confirmation message"), Map.entry("title", "confirmation title"))))
						.willReturn(new Component[]
						{ messageboxDlg });

		// when
		closeListener.onClose(event, widgetInstance);

		// then: window should not close
		then(event).should().stopPropagation();
	}

	@SafeVarargs
	private Map<?, ?> aMapWithElements(final Map.Entry<String, String>... entries)
	{
		return Matchers.argThat(new ArgumentMatcher<>()
		{
			@Override
			public boolean matches(final Object o)
			{
				final Map<String, String> actualMap = (Map<String, String>) o;
				return actualMap.entrySet().containsAll(Set.of(entries));
			}
		});
	}
}
