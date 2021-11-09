/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentItemModule', ['cmsSmarteditServicesModule', 'translationServiceModule'])
    .controller('componentItemController', function(
        assetsService,
        componentSharedService,
        $translate
    ) {
        this.$onInit = function() {
            componentSharedService.isComponentShared(this.componentInfo).then(
                function(isSharedComponent) {
                    this.isSharedComponent = isSharedComponent;
                }.bind(this)
            );
        };

        this.getTemplateInfoForNonCloneableComponent = function() {
            var message = $translate.instant('se.cms.component.non.cloneable.tooltip', {
                componentName: this.componentInfo.name
            });
            return "<div class='se-popover--inner-content'>" + message + '</div>';
        };

        this.$onChanges = function(changesObj) {
            if (changesObj.cloneOnDrop) {
                this.componentDisabled = this.cloneOnDrop && !this.componentInfo.cloneable;
            }
        };
    })
    .component('componentItem', {
        templateUrl: 'componentItemTemplate.html',
        controller: 'componentItemController',
        bindings: {
            componentInfo: '<',
            cloneOnDrop: '<'
        }
    });
