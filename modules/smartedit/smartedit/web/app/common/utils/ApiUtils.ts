/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc service
 * @name functionsModule.service:ApiUtils
 *
 * @description Collection of utility methods for handling responses from backend calls
 *
 */
export class ApiUtils {
    /**
     * @ngdoc method
     * @name functionsModule.service:ApiUtils#getDataFromResponse
     * @methodOf functionsModule.service:ApiUtils
     *
     * @description
     * when provided with a response returned from a backend call, will filter the response
     * to retrieve the data of interest.
     *
     * @param {Object} response, response returned from a backend call.
     * @returns {Array} Returns the array from the response.
     */
    getDataFromResponse(response: any): any {
        const dataKey = Object.keys(response).filter(function(key) {
            return response[key] instanceof Array;
        })[0];

        return response[dataKey];
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:ApiUtils#getKeyHoldingDataFromResponse
     * @methodOf functionsModule.service:ApiUtils
     *
     * @description
     * when provided with a response returned from a backend call, will filter the response
     * to retrieve the key holding the data of interest.
     *
     * @param {Object} response, response returned from a backend call.
     * @returns {String} Returns the name of the key holding the array from the response.
     */
    getKeyHoldingDataFromResponse(response: any): string {
        const dataKey = Object.keys(response).filter(function(key) {
            return response[key] instanceof Array;
        })[0];

        return dataKey;
    }
}
export const apiUtils = new ApiUtils();
