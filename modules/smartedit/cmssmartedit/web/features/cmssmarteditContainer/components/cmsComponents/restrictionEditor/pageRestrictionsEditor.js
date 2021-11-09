/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('pageRestrictionsEditorModule', ['restrictionsServiceModule'])
    .controller('pageRestrictionsEditorController', function(
        restrictionTypesService,
        pageRestrictionsFacade,
        restrictionsService
    ) {
        this.restrictionsResult = function(onlyOneRestrictionMustApply, restrictions) {
            this.model.onlyOneRestrictionMustApply = onlyOneRestrictionMustApply;
            this.model.restrictions = restrictions;
        }.bind(this);

        this.getRestrictionTypes = function() {
            return pageRestrictionsFacade.getRestrictionTypesByPageType(this.model.typeCode);
        }.bind(this);

        this.getSupportedRestrictionTypes = function() {
            return restrictionsService.getSupportedRestrictionTypeCodes();
        };

        this.$onChanges = function(changesObj) {
            if (changesObj.model) {
                this.model.restrictions = changesObj.model.currentValue.restrictions;
                this.model.onlyOneRestrictionMustApply =
                    changesObj.model.currentValue.onlyOneRestrictionMustApply;
            }
        };
    })
    /**
     * @name pageRestrictionsEditorModule.directive:pageRestrictionsEditor
     * @scope
     * @restrict E
     * @element page-restrictions-editor
     *
     * @description
     * page wrapper for Restrictions on top of {@link restrictionsEditorModule.restrictionsEditor restrictionsEditor} page.
     *
     * @param {<Object} model The page model
     */
    .component('pageRestrictionsEditor', {
        templateUrl: 'pageRestrictionsEditorTemplate.html',
        controller: 'pageRestrictionsEditorController',
        controllerAs: 'pageRestrictionsEditorCtrl',
        bindings: {
            model: '<page',
            isEditable: '<'
        }
    });
