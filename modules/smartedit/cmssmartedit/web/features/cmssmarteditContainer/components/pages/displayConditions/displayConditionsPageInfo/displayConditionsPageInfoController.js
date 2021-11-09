/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('displayConditionsPageInfoControllerModule', ['translationServiceModule'])
    .controller('displayConditionsPageInfoController', function($translate) {
        this.displayConditionLabelI18nKey = 'se.cms.display.conditions.label';
        this.pageNameI18nKey = 'se.cms.pagelist.headerpagename';
        this.pageTypeI18nKey = 'se.cms.pagelist.headerpagetype';

        this.getPageDisplayConditionI18nKey = function() {
            return this.isPrimary
                ? 'se.cms.display.conditions.primary.id'
                : 'se.cms.display.conditions.variation.id';
        };

        this.getPageDisplayConditionDescriptionI18nKey = function() {
            return this.isPrimary
                ? 'se.cms.display.conditions.primary.description'
                : 'se.cms.display.conditions.variation.description';
        };

        this.$onChanges = function() {
            $translate(this.getPageDisplayConditionDescriptionI18nKey()).then(
                function(translation) {
                    this.helpTemplate = '<span>' + translation + '</span>';
                }.bind(this)
            );
        };
    });
