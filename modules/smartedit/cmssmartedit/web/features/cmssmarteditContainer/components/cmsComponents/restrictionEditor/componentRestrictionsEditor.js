/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentRestrictionsEditorModule', ['restrictionsServiceModule'])
    .controller('componentRestrictionsEditorController', function(
        restrictionTypesService,
        restrictionsService
    ) {
        this.restrictionsResult = function(onlyOneRestrictionMustApply, restrictions) {
            this.model.onlyOneRestrictionMustApply = onlyOneRestrictionMustApply;
            this.model.restrictions = restrictions;
        }.bind(this);

        this.getRestrictionTypes = function() {
            return restrictionTypesService.getRestrictionTypes();
        };

        this.getSupportedRestrictionTypes = function() {
            return restrictionsService.getSupportedRestrictionTypeCodes();
        };

        this.$onInit = function() {
            if (this.model.restrictions === undefined) {
                this.editor.form.pristine.restrictions = [];
                this.editor.form.pristine.onlyOneRestrictionMustApply = false;
            }
        };

        this.$onChanges = function(changesObj) {
            // This is to align the initial state of the editor with the initial state of the model as the restriction editor sets it as the default state.
            if (changesObj.model) {
                this.model.restrictions = changesObj.model.currentValue.restrictions;
                this.model.onlyOneRestrictionMustApply =
                    changesObj.model.currentValue.onlyOneRestrictionMustApply;
            }
        };
    })
    /**
     * @name componentRestrictionsEditorModule.directive:componentRestrictionsEditor
     * @scope
     * @restrict E
     * @element component-restrictions-editor
     *
     * @description
     * Component wrapper for Restrictions on top of {@link restrictionsEditorModule.restrictionsEditor restrictionsEditor} component.
     *
     * @param {<Object} model The component model
     */
    .component('componentRestrictionsEditor', {
        templateUrl: 'componentRestrictionsEditorTemplate.html',
        controller: 'componentRestrictionsEditorController',
        controllerAs: 'componentRestrictionsEditorCtrl',
        bindings: {
            model: '<',
            editor: '=',
            isEditable: '<'
        }
    });
