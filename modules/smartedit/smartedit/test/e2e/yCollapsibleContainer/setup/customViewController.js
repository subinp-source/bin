/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('customViewModule', [])

    .constant('PATH_TO_CUSTOM_VIEW', 'setup/customView.html')

    .controller('customViewController', function() {
        this.resetSetup = function() {
            // emptying configuration
            for (var key in this.configuration) {
                if (this.configuration.hasOwnProperty(key)) {
                    delete this.configuration[key];
                }
            }

            // applying all default configuration

            this.configuration = {
                expandedByDefault: false,
                iconAlignment: 'right',
                iconVisible: true
            };
        };

        // initialization
        this.configuration = {};
        this.resetSetup();
    });
