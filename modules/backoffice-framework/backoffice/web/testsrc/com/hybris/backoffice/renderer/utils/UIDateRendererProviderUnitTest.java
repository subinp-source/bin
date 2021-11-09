/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.renderer.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Sessions;

import com.hybris.cockpitng.testing.util.CockpitTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class UIDateRendererProviderUnitTest {

    @Spy
    private UIDateRendererProvider provider;

    @Test
    public void getFormattedDateLabelShouldUseUsersTimeZone()
    {
        //given
        CockpitTestUtil.mockZkEnvironment();

        final TimeZone timeZone = TimeZone.getTimeZone("GMT-12");
        Sessions.getCurrent().setAttribute(Attributes.PREFERRED_TIME_ZONE, timeZone);
        final Date referenceDate = new Date();

        //when
        provider.getFormattedDateLabel(new Date(), referenceDate);

        //then

        final ArgumentCaptor<DateFormat> timeCaptor = ArgumentCaptor.forClass(DateFormat.class);
        final ArgumentCaptor<DateFormat> dateCaptor = ArgumentCaptor.forClass(DateFormat.class);


        verify(provider).getLabel(anyString(), timeCaptor.capture(), dateCaptor.capture(), eq(referenceDate));

        assertThat(timeCaptor.getValue().getTimeZone()).isSameAs(timeZone);
        assertThat(dateCaptor.getValue().getTimeZone()).isSameAs(timeZone);

    }

}