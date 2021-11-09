/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name componentCloneOptionFormModule
 * @description
 * #componentCloneOptionFormModule
 *
 * The componentCloneOptionModule module contains the clone page selection options
 * for choosing between copying child components or referencing them.
 *
 */
angular
    .module('componentCloneOptionFormModule', ['yHelpModule'])

    .controller('componentCloneOptionFormController', function($translate) {
        this.CLONE_COMPONENTS_IN_CONTENT_SLOTS_OPTION = {
            REFERENCE_EXISTING: 'reference',
            CLONE: 'clone'
        };

        this.$onInit = function() {
            this.helpTemplate =
                '<span>' + $translate.instant('se.cms.clonepagewizard.options.tooltip') + '</span>';

            this.componentInSlotOption = this.CLONE_COMPONENTS_IN_CONTENT_SLOTS_OPTION.REFERENCE_EXISTING;
            this.onSelectionChange({
                $cloneOptionData: this.componentInSlotOption
            });
        };

        this.updateComponentInSlotOption = function(option) {
            this.componentInSlotOption = option;
            this.onSelectionChange({
                $cloneOptionData: this.componentInSlotOption
            });
        };
    })

    /**
     * @ngdoc directive
     * @name componentCloneOptionModule.directive:componentCloneOptionForm
     * @scope
     * @restrict E
     * @element component-clone-option-form
     *
     * @description
     * Component for selecting the clone page options for child components in content slots.
     * The options are clone existing child components or reference them
     *
     * @param {&Expression} onSelectionChange The parent function to call when a selection is made.
     */
    .component('componentCloneOptionForm', {
        controller: 'componentCloneOptionFormController',
        templateUrl: 'componentCloneOptionTemplate.html',
        bindings: {
            onSelectionChange: '&'
        }
    });
