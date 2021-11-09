/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.rest.clients;

public class ProfileResponse {

    private String response;

    public ProfileResponse(String response) {
        this.response = response;
    }

    public ProfileResponse() {
        //Default constructor
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
