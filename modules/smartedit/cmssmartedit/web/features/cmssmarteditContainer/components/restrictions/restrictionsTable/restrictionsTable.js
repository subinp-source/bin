/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name restrictionsTableModule
 * @requires l10nModule
 * @requires restrictionsCriteriaServiceModule
 * @description
 * This module defines the {@link restrictionsTableModule.directive:restrictionsTable restrictionsTable} component
 */
angular
    .module('restrictionsTableModule', [
        'cmssmarteditContainerTemplates',
        'l10nModule',
        'restrictionsCriteriaServiceModule',
        'yLoDashModule'
    ])

    .controller('restrictionsTableController', function(restrictionsCriteriaService, lodash) {
        var REMOVE_RESTRICTION_KEY = 'se.cms.restrictions.item.remove';
        var EDIT_RESTRICTION_KEY = 'se.cms.restrictions.item.edit';
        var oldRestrictionsEditability = [];

        this.resetRestrictionCriteria = function() {
            if (!this.restrictions || this.restrictions.length < 2) {
                //default if none is provided or restrictions less than 2
                this.restrictionCriteria = this.criteriaOptions[0];
            }
        }.bind(this);

        this.removeRestriction = function(restriction) {
            var restrictionIndex = this.restrictions.indexOf(restriction);
            this.restrictions.splice(restrictionIndex, 1);
            this._removeUnnecessaryError(restrictionIndex);
            this._modifyErrorPositions(restrictionIndex);
        }.bind(this);

        this._removeUnnecessaryError = function(removedRestrictionIndex) {
            var errorIndex = this.errors.findIndex(function(error) {
                return error.position === removedRestrictionIndex;
            });

            if (errorIndex > -1) {
                this.errors.splice(errorIndex, 1);
            }
        }.bind(this);

        this._modifyErrorPositions = function(removedRestrictionIndex) {
            this.errors.forEach(function(error) {
                if (error.position >= removedRestrictionIndex) {
                    error.position = error.position - 1;
                }
            });
        }.bind(this);

        this.editRestriction = function(restriction) {
            this.onClickOnEdit(restriction);
        }.bind(this);

        this.isInError = function(index) {
            return (
                !!this.errors &&
                lodash.some(this.errors, function(error) {
                    return error.position === index;
                })
            );
        };

        /**
         * Returns the list of all default actions for a restriction.
         */
        this._getDefaultActions = function() {
            return [
                {
                    key: EDIT_RESTRICTION_KEY,
                    callback: this.editRestriction
                },
                {
                    key: REMOVE_RESTRICTION_KEY,
                    callback: this.removeRestriction,
                    customCss: 'se-dropdown-item--delete'
                }
            ];
        };

        /**
         * Returns actions for a restriction. It verifies whether the restirction is editable or not.
         * If the restirction is not editable only remove restriction action is returned.
         */
        this._getRestrictionActions = function(restriction) {
            var actions = this._getDefaultActions();
            if (!restriction.$$canBeEdited) {
                var index = actions.findIndex(function(action) {
                    return action.key === EDIT_RESTRICTION_KEY;
                });

                actions.splice(index, 1);
            }
            return actions;
        };

        /**
         * Provides the list of actions for each restriction.
         */
        this._provideActionsForRestrictions = function() {
            this.restrictions.forEach(
                function(restriction) {
                    restriction.$$actions = this._getRestrictionActions(restriction);
                }.bind(this)
            );
        };

        /**
         * Verifies whether the restrictions editability is changed.
         */
        this._restrictionsEditabilityChanged = function(restrictions) {
            var restrictionsEditability = this.getRestrictionsEditability(restrictions);
            return !lodash.isEqual(restrictionsEditability, oldRestrictionsEditability);
        };

        /**
         * Returns array of booleans where each element represents whether the restriction is editable or not.
         */
        this.getRestrictionsEditability = function(restrictions) {
            return restrictions.map(function(restriction) {
                return restriction.$$canBeEdited;
            });
        };

        this.$onInit = function() {
            oldRestrictionsEditability = this.getRestrictionsEditability(this.restrictions);
            this.defaultActions = this._getDefaultActions();
            this._provideActionsForRestrictions();
            this.criteriaOptions = restrictionsCriteriaService.getRestrictionCriteriaOptions();
            this.resetRestrictionCriteria();
        };

        this.$doCheck = function() {
            if (this._restrictionsEditabilityChanged(this.restrictions)) {
                this._provideActionsForRestrictions();
                oldRestrictionsEditability = this.getRestrictionsEditability(this.restrictions);
            }
            this.resetRestrictionCriteria();
        };
    })

    /**
     * @ngdoc directive
     * @name restrictionsTableModule.directive:restrictionsTable
     * @restrict E
     * @scope
     * @param {= String} customClass The name of the CSS class.
     * @param {= Boolean} editable States whether the restrictions table could be modified.
     * @param {< Function=} onClickOnEdit Triggers the custom on edit event.
     * @param {= Function} onSelect Triggers the custom on select event.
     * @param {= Object} restrictions The object of restrictions.
     * @param {= Object =} restrictionCriteria The object that contains information about criteria.
     * @param {< Array =} errors The list of errors.
     * @description
     * Directive that can render a list of restrictions and provides callback functions such as onSelect and onCriteriaSelected. *
     */
    .component('restrictionsTable', {
        templateUrl: 'restrictionsTableTemplate.html',
        controller: 'restrictionsTableController',
        controllerAs: '$ctrl',
        bindings: {
            customClass: '=',
            editable: '=',
            onClickOnEdit: '<?',
            onSelect: '=',
            restrictions: '=',
            restrictionCriteria: '=?',
            errors: '<?'
        }
    });
