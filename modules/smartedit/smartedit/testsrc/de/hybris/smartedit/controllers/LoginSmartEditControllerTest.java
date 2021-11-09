/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static de.hybris.smartedit.controllers.Page.LOGIN_PAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LoginSmartEditControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private LoginSmartEditController loginSmartEditController;

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(loginSmartEditController).build();
    }

    @Test
    public void smart_edit_control_forwards_to_smart_edit_login_page() throws Exception {

       mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl(LOGIN_PAGE.getViewName()));
    }
}