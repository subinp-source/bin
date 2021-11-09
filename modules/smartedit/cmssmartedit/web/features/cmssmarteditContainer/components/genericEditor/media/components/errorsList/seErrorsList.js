/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seErrorsListModule', [])
    .controller('seErrorsListController', function() {
        this.getSubjectErrors = function() {
            return this.subject
                ? this.errors.filter(
                      function(error) {
                          return error.subject === this.subject;
                      }.bind(this)
                  )
                : this.errors;
        };
    })
    .directive('seErrorsList', function() {
        return {
            templateUrl: 'seErrorsListTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seErrorsListController',
            controllerAs: 'ctrl',
            bindToController: {
                errors: '<',
                subject: '<'
            }
        };
    });
