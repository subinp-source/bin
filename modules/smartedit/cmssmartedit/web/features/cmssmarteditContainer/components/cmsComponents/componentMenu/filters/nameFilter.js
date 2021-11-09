/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('nameFilterModule', []).filter('nameFilter', function() {
    return function(components, criteria) {
        var filterResult = [];
        if (!criteria || criteria.length < 3) {
            return components;
        }

        criteria = criteria.toLowerCase();
        var criteriaList = criteria.split(' ');

        (components || []).forEach(function(component) {
            var match = true;
            var term = component.name.toLowerCase();

            criteriaList.forEach(function(item) {
                if (term.indexOf(item) === -1) {
                    match = false;
                    return false;
                }
            });

            if (match && filterResult.indexOf(component) === -1) {
                filterResult.push(component);
            }
        });
        return filterResult;
    };
});
